
package frc.robot;
 
import static edu.wpi.first.units.Units.RPM;
import static edu.wpi.first.units.Units.Second;
 
import java.util.function.Supplier;
 
import org.littletonrobotics.conduit.ConduitApi;
 
import com.pathplanner.lib.util.FlippingUtil;

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
    public static flyWheelsubsystem flywheel;
    private static double flywheelRPM = 0;

    //#region Premade Commands
    public static Supplier<Command> shoot;
    //#endregion
 
    public RobotContainer() {
        driveController = new CommandPS5Controller(0);
 
        hood = new hoodSubsystem();
        flywheel = new flyWheelsubsystem();
  
        configureCommands();
        configureDefaultCommands();
        configureMechanismBindings();
        configureDriveBindings();
    }
 
    //#region Commands
    public static void configureCommands() { 
        shoot = () ->
            Commands.parallel(
                new flyWheelCommand(flywheel, flywheelRPM),
                new openHood(hood, () -> nearTrench() ? 0 : distanceFromHub())
            ).withName("Shoot");
 
    public static void configureDefaultCommands() {
        hood.setDefaultCommand(new openHood(hood, null));
        flywheel.setDefaultCommand(new flyWheelCommand(flywheel, () -> flywheelRPM));
    }
    //#endregion
 
    //#region Bindings
    public static void configureMechanismBindings() {
 
        driveController.cross().whileTrue(shoot.get());
    }
    //#endregion


 
    //#region Periodic Updates
    public static void pollAlerts() {
        controllerDisconnected.set(!driveController.isConnected());
        brownoutBattery.set(RobotController.isBrownedOut());
    }
    public static void updateElastic() {
        SmartDashboard.putNumber("RPM", flywheelRPM);
    }
    //#endregion
}