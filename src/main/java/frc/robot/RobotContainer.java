package frc.robot;
import static edu.wpi.first.units.Units.Meters;
import static edu.wpi.first.units.Units.Second;

import java.lang.System.Logger;
import java.util.HashMap;

import org.littletonrobotics.conduit.ConduitApi;
import org.littletonrobotics.junction.networktables.LoggedDashboardChooser;

import com.pathplanner.lib.path.PathPlannerPath;
import com.pathplanner.lib.util.FlippingUtil;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.units.measure.Distance;
import edu.wpi.first.wpilibj.Alert;
import edu.wpi.first.wpilibj.RobotBase;
import edu.wpi.first.wpilibj.RobotController;
import edu.wpi.first.wpilibj.Alert.AlertType;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj.RobotState;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.StartEndCommand;
import edu.wpi.first.wpilibj2.command.button.CommandPS5Controller;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import edu.wpi.first.wpilibj2.command.sysid.SysIdRoutine.Direction;
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
import frc.util.HubTracker;

public class RobotContainer {
    public static Alert controllerDisconnected = new Alert("Drive Controller Disconnect", AlertType.kError);
    public static Alert brownoutBattery = new Alert("BROWNOUT", AlertType.kError);
    public static void pollAlerts(){
        controllerDisconnected.set(!driveController.isConnected());
        brownoutBattery.set(RobotController.isBrownedOut());
    }

    public static CommandPS5Controller driveController;

    private static final DriveBase driveBase = new DriveBase();
    private final hoodSubsystem hood = new hoodSubsystem();
    private final flyWheelsubsystem flywheel = new flyWheelsubsystem();
    private final kickerSubsystem kicker = new kickerSubsystem();
    private final transferSubsystem transfer = new transferSubsystem();
    private final rackAndPinionSubsystem rackAndPinion = new rackAndPinionSubsystem();
    private final Rollers rollers = new Rollers();

    public static Vision vision;

    public static Runnable fuelIncrementer;
    public static int fuelApproximation = 0;
        
    public RobotContainer(){
        driveController = new CommandPS5Controller(0);
        vision = new Vision(driveBase::addVisionMeasurement, driveBase::getPose, driveBase::reset);
        driveController.R1().whileTrue(new transferOn(transfer));
        driveController.L1().whileTrue(new RollerCommands(rollers).alongWith(Commands.startEnd(DriveCommand::slowSpeed, DriveCommand::fullSpeed)));
        driveController.R1().whileTrue(new flyWheelCommand(flywheel, () -> distanceFromHub()).alongWith(new AimDriving(driveBase, driveController, () -> angleToHub())));
        driveController.L2().whileTrue(new flyWheelCommand(flywheel, () -> ((distanceFromHub() + getDistanceToNearestTrench()) / 2)).alongWith(new AimDriving(driveBase, driveController, () -> angleToHub())));
        
        if  (getDistanceToNearestTrench() > Constants.FieldConstants.DISTANCE_FROM_TRENCH_TO_CLOSE_HOOD) {
            driveController.R1().whileTrue(new openHood(hood, () -> distanceFromHub()));
            driveController.L2().whileTrue(new openHood(hood, () -> (distanceFromHub() + getDistanceToNearestTrench()) / 2));
        } else {
            Commands.runOnce(() -> {
                new openHood(hood, () -> 0);
            })
            ;
        }
        if (Math.abs(getShootingAngle()) < Constants.SHOOTING_ANGLE_TOLERANCE && (hood.atAngle() && flywheel.atSpeed())) {
            Commands.runOnce(() -> {
                new transferOn(transfer);
                new kickerOn(kicker);
            })
            ;
        } else {
            driveController.triangle().whileTrue(new intakeFullExtension(rackAndPinion));
            driveController.triangle().whileTrue(new transferReverse(transfer));
        }
        driveController.triangle().whileTrue(new rollersReverse(rollers));
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
        if (rackAndPinionConstants.isExtended && rackAndPinion.isStalled()) {
            Commands.runOnce(() -> {
                new intakeFullRetraction(rackAndPinion);
                rackAndPinionConstants.isExtended = false;
            })
            ;
        }
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
    }
    public static boolean inAllianceZone(){
        if (Robot.isRedAlliance) return driveBase.getPose().getX() > Constants.FieldConstants.RED_ALLIANCE_ZONE_X;
        else return driveBase.getPose().getX() < Constants.FieldConstants.BLUE_ALLIANCE_ZONE_X;
    }
    public static double distanceFromHub(){
        Pose2d hubLocation = Constants.FieldConstants.HUB_POSITION;
        if(Robot.isRedAlliance){
            hubLocation = FlippingUtil.flipFieldPose(hubLocation);
        }
        return driveBase.getPose().getTranslation().getDistance(hubLocation.getTranslation());
    }
    public static double angleToHub(){
        Pose2d hubLocation = Constants.FieldConstants.HUB_POSITION;
        if(Robot.isRedAlliance) hubLocation = FlippingUtil.flipFieldPose(hubLocation);
        return driveBase.getAngleToPose(hubLocation);
    }
    public static double getShootingAngle(){
        return inAllianceZone() ? angleToHub() : (getAngleToNearestTrench() + angleToHub()) / 2;
    }
    public static Pose2d NearestTrench() {
        Pose2d UpRightTrench = Constants.TrenchLocations.upRightTrench;
        Pose2d DownRightTrench = Constants.TrenchLocations.downRightTrench;
        Pose2d UpLeftTrench = Constants.TrenchLocations.upLeftTrench;
        Pose2d DownRLeftTrench = Constants.TrenchLocations.downLeftTrench;
                if (Robot.isRedAlliance) {
            double distancetoUpTrench = driveBase.getPose().getTranslation().getDistance(UpLeftTrench.getTranslation());
            double distancetoDownTrench = driveBase.getPose().getTranslation().getDistance(DownRLeftTrench.getTranslation());
            if (distancetoDownTrench > distancetoUpTrench ) {
                return UpLeftTrench;
            } else {
                return DownRLeftTrench;
            }
        } 
        else {
            double distancetoUpTrench = driveBase.getPose().getTranslation().getDistance(UpRightTrench.getTranslation());
            double distancetoDownTrench = driveBase.getPose().getTranslation().getDistance(DownRightTrench.getTranslation());
            if (distancetoDownTrench > distancetoUpTrench ) {
                return UpRightTrench;
            } else {
                return DownRightTrench;
            }
        }
    }
    public static double getDistanceToNearestTrench() {
        return Math.abs(driveBase.getPose().getTranslation().getDistance(NearestTrench().getTranslation()));
    }
    public static double getAngleToNearestTrench(){
        return driveBase.getAngleToPose(NearestTrench());
    }
    public static void updateElastic(){
        SmartDashboard.putNumber("Battery Voltage", RobotController.getBatteryVoltage());
        SmartDashboard.putNumber("Robot Velocity", driveBase.getVelocity());
        SmartDashboard.putNumber("Match Time", Timer.getMatchTime());
        SmartDashboard.putNumber("PDH Voltage", ConduitApi.getInstance().getPDPVoltage());
        SmartDashboard.putBoolean("Is Hub Active", HubTracker.isActive(DriverStation.getAlliance().orElseGet(() -> Alliance.Red)));
        SmartDashboard.putNumber("Time left in Shift", Math.floor(HubTracker.timeRemainingInCurrentShift().orElseGet(() -> Second.zero()).in(Second) * 100) / 100);
        SmartDashboard.putString("Current Shift", HubTracker.getCurrentShift().orElseGet(() -> HubTracker.Shift.AUTO).toString());
        SmartDashboard.putNumber("Fuel Approximation", fuelApproximation);
        SmartDashboard.putString("Distance to Hub", String.format("%.2f", distanceFromHub()));
    }
}   