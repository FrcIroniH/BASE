package frc.robot;
import java.util.HashMap;

import org.littletonrobotics.junction.networktables.LoggedDashboardChooser;

import com.pathplanner.lib.path.PathPlannerPath;

import edu.wpi.first.wpilibj.Alert;
import edu.wpi.first.wpilibj.Alert.AlertType;
import edu.wpi.first.wpilibj.RobotState;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.StartEndCommand;
import edu.wpi.first.wpilibj2.command.button.CommandPS5Controller;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import edu.wpi.first.wpilibj2.command.sysid.SysIdRoutine.Direction;
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
import frc.robot.subsystems.Intake.rackAndPinion.rackAndPinionConstants;
import frc.robot.subsystems.Intake.rackAndPinion.rackAndPinionSubsystem;
import frc.robot.subsystems.Intake.rackAndPinion.rackAndPinionIO.rackAndPinionInputs;
import frc.robot.subsystems.Intake.transfer.transferConstants;
import frc.robot.subsystems.Intake.transfer.transferOn;
import frc.robot.subsystems.Intake.transfer.transferReverse;
import frc.robot.subsystems.Intake.transfer.transferSubsystem;
import frc.robot.subsystems.shooter.flywheel.flyWheelCommand;
import frc.robot.subsystems.shooter.flywheel.flyWheelConstants;
import frc.robot.subsystems.shooter.flywheel.flyWheelSysID;
import frc.robot.subsystems.shooter.flywheel.flyWheelsubsystem;
import frc.robot.subsystems.shooter.hood.hoodSubsystem;
import frc.robot.subsystems.shooter.hood.hoodSysID;
import frc.robot.subsystems.shooter.hood.openHood;
import frc.robot.subsystems.shooter.kicker.kickerOn;
import frc.robot.subsystems.shooter.kicker.kickerSubsystem;

public class RobotContainer {
    public static CommandPS5Controller driveController;
    private static final DriveBase driveBase = new DriveBase();
    private final hoodSubsystem hood = new hoodSubsystem(driveBase::getPose);
    private final flyWheelsubsystem flywheel = new flyWheelsubsystem(driveBase::getPose);
    private final kickerSubsystem kicker = new kickerSubsystem();
    private final transferSubsystem transfer = new transferSubsystem();
    private final rackAndPinionSubsystem rackAndPinion = new rackAndPinionSubsystem();
    private final Rollers rollers = new Rollers();
    // for SysID:
    // private final hoodSysID hoodSysId = new hoodSysID(hood); 
    // private final flyWheelSysID flywheelSysId = new flyWheelSysID(flywheel);
    public static Alert brownoutBattery = new Alert("BROWNOUT", AlertType.kError);
    // public static Constants.TrenchLocations trenchLocations = new Constants.TrenchLocations(); // need to create TrenchLocations()
    public static Vision vision;
    public static LoggedDashboardChooser<Command> autoChooser;
    public static LoggedDashboardChooser<String> sideChooser;
    public static HashMap<String, Command> mirroredAutoMap = new HashMap<>();
    public static PathPlannerPath upLeftTrenchPath;
    public static PathPlannerPath upRightTrenchPath;
    public static Runnable fuelIncrementer;
    public static int fuelApproximation = 0;
        
    public RobotContainer(){
        driveController = new CommandPS5Controller(0);
        vision = new Vision(driveBase::addVisionMeasurement, driveBase::getPose, driveBase::reset);
        // driveController.L2().whileTrue(new openHood(hood)); vision needed
        // driveController.L2().whileTrue(new flyWheelCommand(flywheel));
        driveController.R1().whileTrue(new kickerOn(kicker));
        driveController.R1().whileTrue(new transferOn(transfer));
        driveController.L1().whileTrue(new RollerCommands(rollers));

        driveController.square().onTrue(
            Commands.runOnce(() -> {

            if (!rackAndPinionConstants.isExtended) {
                new intakeFullExtension(rackAndPinion);
            } else {
                new intakeFullRetraction(rackAndPinion);
            }
            rackAndPinionConstants.isExtended = !rackAndPinionConstants.isExtended;
            })
        );

        driveController.triangle().whileTrue(new intakeFullExtension(rackAndPinion));
        driveController.triangle().whileTrue(new rollersReverse(rollers));
        driveController.triangle().whileTrue(new transferReverse(transfer));
        if (rackAndPinionConstants.isExtended && rackAndPinion.isStalled()) {
            Commands.runOnce(() -> {
                new intakeFullRetraction(rackAndPinion);
                rackAndPinionConstants.isExtended = false;
            })
            ;
        }
    
        // for SysID:
        // driveController.L2().whileTrue(new openHood(hood));
        // driveController.R2().whileTrue(new flyWheelCommand(flywheel));
        // driveController.R1().whileTrue(new kickerOn(kicker));

        // driveController.cross().whileTrue(
        //     hoodSysId.quasistatic(Direction.kForward).alongWith(flywheelSysId.quasistatic(Direction.kForward)));
        // driveController.circle().whileTrue(
        //     hoodSysId.quasistatic(Direction.kReverse).alongWith(flywheelSysId.quasistatic(Direction.kReverse)));
        // driveController.square().whileTrue(
        //     hoodSysId.dynamic(Direction.kForward).alongWith(flywheelSysId.dynamic(Direction.kForward)));
        // driveController.triangle().whileTrue(
        //     hoodSysId.dynamic(Direction.kReverse).alongWith(flywheelSysId.dynamic(Direction.kReverse)));

        //R1 TO SHOOT, R2 BRAKES, L1 ROLLERCOMMAND,TRIANGLE DUMP FUEL FROM INTAKE, PS BUTTON GYRO RESET, SQUARE TO EXTED/RETRACT INTAKE, L2 TO PASS
        //ADD OPTION TO SHOOT WITHOUT VISION FROM A SET PLACE(MAYBE WITH GLOBAL MAP)

    }
        public static void configureDriveBindings(){
        new Trigger(RobotState::isTeleop).and(RobotState::isEnabled).whileTrue(
            new StartEndCommand(() ->driveBase.setDefaultCommand(new DriveCommand(driveBase, driveController)),
            driveBase::removeDefaultCommand)
            .ignoringDisable(true));

        driveController.PS().onTrue(driveBase.resetOnlyDirection());

        driveController.povRight().whileTrue(new MinorAdjust(driveBase, AdjustmentDirection.RIGHT));
        driveController.povLeft().whileTrue(new MinorAdjust(driveBase, AdjustmentDirection.LEFT));
        driveController.povUp().whileTrue(new MinorAdjust(driveBase, AdjustmentDirection.FORWARD));
        driveController.povDown().whileTrue(new MinorAdjust(driveBase, AdjustmentDirection.BACKWARDS));
        driveController.povUpLeft().whileTrue(new MinorAdjust(driveBase, AdjustmentDirection.FRONT_LEFT));
        driveController.povUpRight().whileTrue(new MinorAdjust(driveBase, AdjustmentDirection.FRONT_RIGHT));
        driveController.povDownLeft().whileTrue(new MinorAdjust(driveBase, AdjustmentDirection.BACK_LEFT));
        driveController.povDownRight().whileTrue(new MinorAdjust(driveBase, AdjustmentDirection.BACK_RIGHT));
        

        // driveController.R1().whileTrue(
        //     Commands.parallel(
        //         new AimDriving(driveBase, driveController, RobotContainer::getShootingAngle),
        //         preShooting.get().andThen(shootWithBump.get())
        //     ).withName("Full Shooting")
        // );
        // driveController.cross().whileTrue(preShooting.get().andThen(shootCommandWithNoVision.get()));

        // driveController.circle().whileTrue(preShooting.get().andThen(shootCommandWithNoVision2.get()));

        // driveController.create().onTrue(new InstantCommand(() -> withAutoEject = !withAutoEject));
        // driveController.options().onTrue(swapIntakeStateClosed.get());

        // driveController.L1().whileTrue(feeder.intakeCommand().alongWith(Commands.startEnd(DriveCommand::slowSpeed, DriveCommand::fullSpeed)));
        // driveController.square().whileTrue(feeder.passEjectCommand());
    }
}