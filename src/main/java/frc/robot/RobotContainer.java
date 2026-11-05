
package frc.robot;
<<<<<<< Updated upstream
import edu.wpi.first.wpilibj2.command.button.CommandPS5Controller;

public class RobotContainer {
    public static CommandPS5Controller driveController;

    public RobotContainer(){
        driveController = new CommandPS5Controller(0);
    }
=======
 
import static edu.wpi.first.units.Units.Second;
 
import java.util.function.Supplier;
 
import org.littletonrobotics.conduit.ConduitApi;
 
import com.pathplanner.lib.util.FlippingUtil;
 
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
 
public class RobotContainer2 {
    
    public static Alert controllerDisconnected = new Alert("Drive Controller Disconnect", AlertType.kError);
    public static Alert brownoutBattery = new Alert("BROWNOUT", AlertType.kError);
 
    public static CommandPS5Controller driveController;
 
    public static DriveBase driveBase;
    public static hoodSubsystem hood;
    public static flyWheelsubsystem flywheel;
    public static kickerSubsystem kicker;
    public static transferSubsystem transfer;
    public static rackAndPinionSubsystem rackAndPinion;
    public static Rollers rollers;
    public static Vision vision;
 
    public static Runnable fuelIncrementer;
    public static int fuelApproximation = 0;
 
    public static boolean intakeExtended = false;
 
    //#region Premade Commands
    public static Supplier<Command> intakeWithSlowDrive;
    public static Supplier<Command> extendIntake;
    public static Supplier<Command> retractIntake;
    public static Supplier<Command> toggleIntake;
    public static Supplier<Command> outtake;
    public static Supplier<Command> passSpinUp;
    public static Supplier<Command> hubSpinUp;
    public static Supplier<Command> feed;
    //#endregion
 
    public RobotContainer2() {
        driveController = new CommandPS5Controller(0);
 
        driveBase = new DriveBase();
        hood = new hoodSubsystem();
        flywheel = new flyWheelsubsystem();
        kicker = new kickerSubsystem();
        transfer = new transferSubsystem();
        rackAndPinion = new rackAndPinionSubsystem();
        rollers = new Rollers();
 
        vision = new Vision(driveBase::addVisionMeasurement, driveBase::getPose, driveBase::reset);
 
        configureCommands();
        configureDefaultCommands();
        configureMechanismBindings();
    }
 
    //#region Commands
    public static void configureCommands() {
        fuelIncrementer = () -> fuelApproximation += 3;
 
        intakeWithSlowDrive = () ->
            new RollerCommands(rollers)
                .alongWith(Commands.startEnd(DriveCommand::slowSpeed, DriveCommand::fullSpeed))
                .withName("Intake");
 
        extendIntake = () ->
            new intakeFullExtension(rackAndPinion)
                .beforeStarting(() -> intakeExtended = true)
                .withName("Extend Intake");
 
        retractIntake = () ->
            new intakeFullRetraction(rackAndPinion)
                .beforeStarting(() -> intakeExtended = false)
                .withName("Retract Intake");
 
        toggleIntake = () ->
            Commands.either(
                retractIntake.get(),
                extendIntake.get(),
                () -> intakeExtended
            ).withName("Toggle Intake");
 
        // Extends the intake if it's closed, reverses the rollers,
        // and reverses the transfer when outside the alliance zone.
        outtake = () ->
            Commands.parallel(
                Commands.either(Commands.none(), extendIntake.get(), () -> intakeExtended),
                new rollersReverse(rollers),
                Commands.either(new transferReverse(transfer), Commands.none(), () -> !inAllianceZone())
            ).withName("Outtake");
 
        // L2: spin up for a pass (average of hub and trench distance), aim at hub.
        // Hood closes when near the trench.
        passSpinUp = () ->
            Commands.parallel(
                new flyWheelCommand(flywheel, RobotContainer2::passDistance),
                new openHood(hood, () -> nearTrench() ? 0 : passDistance()),
                new AimDriving(driveBase, driveController, RobotContainer2::angleToHub)
            ).withName("Pass Spin Up");
 
        // R1: spin up and aim for a hub shot. Hood closes when near the trench.
        hubSpinUp = () ->
            Commands.parallel(
                new flyWheelCommand(flywheel, RobotContainer2::distanceFromHub),
                new openHood(hood, () -> nearTrench() ? 0 : distanceFromHub()),
                new AimDriving(driveBase, driveController, RobotContainer2::angleToHub)
            ).withName("Hub Spin Up");
 
        feed = () ->
            new transferOn(transfer)
                .alongWith(new kickerOn(kicker))
                .withName("Feed");
    }
 
    public static void configureDefaultCommands() {
        // Hood stays closed when nothing else uses it, so the robot always fits under the trench.
        // openHood must call addRequirements(hood) for this to work.
        hood.setDefaultCommand(new openHood(hood, () -> 0));
    }
    //#endregion
 
    //#region Bindings
    public static void configureMechanismBindings() {
        driveController.L1().whileTrue(intakeWithSlowDrive.get());
 
        driveController.L2().whileTrue(passSpinUp.get());
 
        driveController.R1().whileTrue(hubSpinUp.get());

        driveController.R1().and(RobotContainer2::readyToShoot).whileTrue(feed.get());
 
        driveController.triangle().whileTrue(outtake.get());
 
        driveController.square().onTrue(toggleIntake.get());
 
        // Auto-retract if the extended intake stalls.
        // TODO: make sure isStalled() does NOT fire at the normal end-of-travel stop,
        // or the intake will retract right after extending.
        new Trigger(() -> intakeExtended && rackAndPinion.isStalled())
            .debounce(0.25)
            .onTrue(retractIntake.get());
    }
 
    public static void configureDriveBindings() {
        new Trigger(RobotState::isTeleop).and(RobotState::isEnabled).whileTrue(
            new StartEndCommand(() -> driveBase.setDefaultCommand(new DriveCommand(driveBase, driveController)),
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
    }
    //#endregion
 
    //#region Shooting Helpers
    public static boolean readyToShoot() {
        return Math.abs(angleToHub()) < Constants.SHOOTING_ANGLE_TOLERANCE && hood.atAngle()  && flywheel.atSpeed();
    }

    private static Pose2d hubPose() {
        Pose2d hub = Constants.FieldConstants.HUB_POSITION;

        if (Robot.isRedAlliance) {
            hub = FlippingUtil.flipFieldPose(hub);
        }
        return hub;
    }
 
    public static double distanceFromHub() {
        return driveBase.getPose().getTranslation().getDistance(hubPose().getTranslation());
    }
 
    public static double angleToHub() {
        return driveBase.getAngleToPose(hubPose());
    }
 
    public static double passDistance() {
        return (distanceFromHub() + getDistanceToNearestTrench()) / 2;
    }
 
    public static double getShootingAngle() {
        if (inAllianceZone()) {
            return angleToHub();
        } else {
            return (getAngleToNearestTrench() + angleToHub()) / 2;
        }
    }

    public static boolean inAllianceZone() {
        if (Robot.isRedAlliance) return driveBase.getPose().getX() > Constants.FieldConstants.RED_ALLIANCE_ZONE_X;
        else return driveBase.getPose().getX() < Constants.FieldConstants.BLUE_ALLIANCE_ZONE_X;
    }
    //#endregion
 
    //#region Trench Helpers
    public static boolean nearTrench() {
        return getDistanceToNearestTrench() <= Constants.FieldConstants.DISTANCE_FROM_TRENCH_TO_CLOSE_HOOD;
    }
 
    public static Pose2d NearestTrench() {
        Pose2d upTrench;
        Pose2d downTrench;

        if (Robot.isRedAlliance) {
            upTrench = Constants.TrenchLocations.upLeftTrench;
            downTrench = Constants.TrenchLocations.downLeftTrench;
        } else {
            upTrench = Constants.TrenchLocations.upRightTrench;
            downTrench = Constants.TrenchLocations.downRightTrench;
        }
        Translation2d robot = driveBase.getPose().getTranslation();
        double distanceToUp = robot.getDistance(upTrench.getTranslation());
        double distanceToDown = robot.getDistance(downTrench.getTranslation());
        if (distanceToUp < distanceToDown) {
            return upTrench;
        } else {
            return downTrench;
        }
    }

 
    public static double getDistanceToNearestTrench() {
        return driveBase.getPose().getTranslation().getDistance(NearestTrench().getTranslation());
    }
 
    public static double getAngleToNearestTrench() {
        return driveBase.getAngleToPose(NearestTrench());
    }
    //#endregion
 
    //#region Periodic Updates
    public static void pollAlerts() {
        controllerDisconnected.set(!driveController.isConnected());
        brownoutBattery.set(RobotController.isBrownedOut());
    }
 
    public static void updateElastic() {
        Alliance alliance = Robot.isRedAlliance ? Alliance.Red : Alliance.Blue;
 
        SmartDashboard.putNumber("Battery Voltage", RobotController.getBatteryVoltage());
        SmartDashboard.putNumber("Robot Velocity", driveBase.getVelocity());
        SmartDashboard.putNumber("Match Time", Timer.getMatchTime());
        SmartDashboard.putNumber("PDH Voltage", ConduitApi.getInstance().getPDPVoltage());
 
        SmartDashboard.putBoolean("Is Hub Active", HubTracker.isActive(alliance));
        SmartDashboard.putNumber("Time left in Shift",
            Math.floor(HubTracker.timeRemainingInCurrentShift().orElseGet(() -> Second.zero()).in(Second) * 100) / 100);
        SmartDashboard.putString("Current Shift",
            HubTracker.getCurrentShift().orElseGet(() -> HubTracker.Shift.AUTO).toString());
 
        SmartDashboard.putNumber("Fuel Approximation", fuelApproximation);
        SmartDashboard.putNumber("Distance to Hub", distanceFromHub());
        SmartDashboard.putBoolean("In Alliance Zone", inAllianceZone());
        SmartDashboard.putBoolean("Near Trench", nearTrench());
        SmartDashboard.putBoolean("Ready To Shoot", readyToShoot());
    }
    //#endregion
>>>>>>> Stashed changes
}