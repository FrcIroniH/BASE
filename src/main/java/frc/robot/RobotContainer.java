
package frc.robot;
 
import static edu.wpi.first.units.Units.RPM;
import static edu.wpi.first.units.Units.Second;
 
import java.util.function.Supplier;
 
import org.littletonrobotics.conduit.ConduitApi;
 
import com.pathplanner.lib.util.FlippingUtil;

import edu.wpi.first.hal.SimDevice.Direction;
import edu.wpi.first.math.filter.Debouncer;
import edu.wpi.first.math.filter.Debouncer.DebounceType;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.wpilibj.Alert;
import edu.wpi.first.wpilibj.Alert.AlertType;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj.RobotController;
import edu.wpi.first.wpilibj.RobotState;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.StartEndCommand;
import edu.wpi.first.wpilibj2.command.button.CommandPS5Controller;
import edu.wpi.first.wpilibj2.command.button.POVButton;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.commands.Drive.AimDriving;
import frc.robot.commands.Drive.DriveCommand;
import frc.robot.commands.Drive.MinorAdjust;
import frc.robot.commands.Drive.MinorAdjust.AdjustmentDirection;
import frc.robot.subsystems.DriveTrain.DriveBase;
import frc.robot.subsystems.Vision.Vision;
import frc.robot.subsystems.Intake.Rollers.RollerCommands;
import frc.robot.subsystems.Intake.Rollers.Rollers;
import frc.robot.subsystems.Intake.Rollers.rollersReverse;
import frc.robot.subsystems.Intake.rackAndPinion.intakeFullExtension;
import frc.robot.subsystems.Intake.rackAndPinion.intakeFullRetraction;
import frc.robot.subsystems.Intake.rackAndPinion.rackAndPinionSubsystem;
import frc.robot.subsystems.Intake.transfer.transferOn;
import frc.robot.subsystems.Intake.transfer.transferReverse;
import frc.robot.subsystems.Intake.transfer.transferSubsystem;
import frc.robot.subsystems.shooter.flywheel.flyWheelCommand;
import frc.robot.subsystems.shooter.flywheel.flyWheelsubsystem;
import frc.robot.subsystems.shooter.hood.hoodSubsystem;
import frc.robot.subsystems.shooter.hood.openHood;
import frc.robot.subsystems.shooter.kicker.kickerOn;
import frc.robot.subsystems.shooter.kicker.kickerSubsystem;
import frc.util.HubTracker;
import java.util.function.BooleanSupplier;
import java.util.function.DoubleSupplier;
 
public class RobotContainer {
    
    public static Alert controllerDisconnected = new Alert("Drive Controller Disconnect", AlertType.kError);
    public static Alert brownoutBattery = new Alert("BROWNOUT", AlertType.kError);
 
    public static CommandPS5Controller driveController;
 
    public static hoodSubsystem hood;
    public static transferSubsystem transfer;
    public static kickerSubsystem kicker;
    public static flyWheelsubsystem flywheel;
    private static DoubleSupplier flywheelRPM;

    //#region Premade Commands
    public static Supplier<Command> shoot;
    public static Supplier<Command> feed;
    private static DoubleSupplier HoodPrecent;
    private static double hoodPrecent = 0;
    private static double flywheelrPM = 0;
    //#endregion
 
    public RobotContainer() {
        driveController = new CommandPS5Controller(0);
 
        hood = new hoodSubsystem();
        flywheel = new flyWheelsubsystem();
  
        configureCommands();
        configureDefaultCommands();
        configureMechanismBindings();
    }
 
    //#region Commands
    public static void configureCommands() { 
        feed = () ->
            new transferOn(transfer)
                .alongWith(new kickerOn(kicker))
                .withName("Feed");
        }
        
 
    public static void configureDefaultCommands() {
        hood.setDefaultCommand(new openHood(hood, HoodPrecent));
        flywheel.setDefaultCommand(new flyWheelCommand(flywheel, flywheelRPM));
    }
    //#endregion
 
    //#region Bindings
    public static void configureMechanismBindings() {
        driveController.PS().and(RobotContainer::readyToShoot).whileTrue(feed.get());
        driveController.cross().whileTrue(
            hoodSysId.quasistatic(Direction.kForward).alongWith(flywheelSysId.quasistatic(Direction.kForward)));
        driveController.circle().whileTrue(
            hoodSysId.quasistatic(Direction.kReverse).alongWith(flywheelSysId.quasistatic(Direction.kReverse)));
        driveController.square().whileTrue(
            hoodSysId.dynamic(Direction.kForward).alongWith(flywheelSysId.dynamic(Direction.kForward)));
        driveController.triangle().whileTrue(
            hoodSysId.dynamic(Direction.kReverse).alongWith(flywheelSysId.dynamic(Direction.kReverse)));
    }
    //#endregion
    


    public void ChangeRPM (boolean raise) {
        if (raise) {
            flywheelrPM += 10;
        }
        else {
            flywheelrPM -=10;
        }
        flywheelRPM = () -> flywheelrPM;
    }
    public void ChangeHoodPrecent(boolean raise) {
        if (raise) {
            hoodPrecent+=0.02;
        }
        else {hoodPrecent -= 0.02;}
        if (hoodPrecent > 1) {
            hoodPrecent = 1;
        }
        if (hoodPrecent < 0) {
            hoodPrecent = 0;
        }
        HoodPrecent = () -> hoodPrecent;
    }
    //#region Periodic Updates
    public static void pollAlerts() {
        controllerDisconnected.set(!driveController.isConnected());
        brownoutBattery.set(RobotController.isBrownedOut());
    }
    public static void updateElastic() {
        SmartDashboard.putNumber("RPM", flywheelrPM);
        SmartDashboard.putNumber("hood open precent", hoodPrecent);
    }
    //#endregion
    private static final Debouncer readyToShootDebouncer = new Debouncer(0.1, DebounceType.kRising);
    public static boolean readyToShoot() {
        return readyToShootDebouncer.calculate(hood.atAngle() && flywheel.atSpeed());
    }
}
