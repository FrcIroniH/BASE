
package frc.robot;
 
import static edu.wpi.first.units.Units.Second;
 
import java.util.function.Supplier;
 
import org.littletonrobotics.conduit.ConduitApi;
import org.littletonrobotics.junction.Logger;
import org.littletonrobotics.junction.networktables.LoggedDashboardChooser;
import org.json.simple.parser.ParseException;
import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.commands.PathPlannerAuto;
import com.pathplanner.lib.path.GoalEndState;
import com.pathplanner.lib.path.PathConstraints;
import com.pathplanner.lib.path.PathPlannerPath;
import com.pathplanner.lib.util.FileVersionException;
import com.pathplanner.lib.util.FlippingUtil;
import java.io.IOException;
import edu.wpi.first.math.filter.Debouncer;
import edu.wpi.first.math.filter.Debouncer.DebounceType;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.wpilibj.Alert;
import edu.wpi.first.wpilibj.Alert.AlertType;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj.RobotController;
import edu.wpi.first.wpilibj.RobotState;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.InstantCommand;
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.function.BooleanSupplier;
 
public class RobotContainer {
    
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
    
     public static PathPlannerPath upLeftTrenchPath;
    public static PathPlannerPath upRightTrenchPath;
 
    public static boolean intakeExtended = false;

    public static LoggedDashboardChooser<Command> autoChooser;
    public static LoggedDashboardChooser<String> sideChooser;
    public static HashMap<String, Command> mirroredAutoMap = new HashMap<>();
 
    //#region Premade Commands
    public static Supplier<Command> intakeWithSlowDrive;
    public static Supplier<Command> extendIntake;
    public static Supplier<Command> retractIntake;
    public static Supplier<Command> toggleIntake;
    public static Supplier<Command> outtake;
    public static Supplier<Command> pass;
    public static Supplier<Command> shoot;
    public static Supplier<Command> feed;
    //#endregion
 
    public RobotContainer() {
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
        configureDriveBindings();
        configureChoosers();
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
 
        toggleIntake = () -> Commands.runOnce(() -> {
        if (intakeExtended) {
            CommandScheduler.getInstance().schedule(retractIntake.get());
        } else {
            CommandScheduler.getInstance().schedule(extendIntake.get());
        }
        });
 
        outtake = () ->
            Commands.parallel(
                Commands.either(Commands.none(), extendIntake.get(), () -> intakeExtended),
                new rollersReverse(rollers),
                Commands.either(new transferReverse(transfer), Commands.none(), () -> !inAllianceZone())
            ).withName("Outtake");
        pass = () ->
            Commands.parallel(
                new flyWheelCommand(flywheel, RobotContainer::passDistance),
                new openHood(hood, () -> nearTrench() ? 0 : passDistance()),
                new AimDriving(driveBase, driveController, RobotContainer::getShootingAngle)
            ).withName("Pass");
        shoot = () ->
            Commands.parallel(
                new flyWheelCommand(flywheel, RobotContainer::distanceFromHub),
                new openHood(hood, () -> nearTrench() ? 0 : distanceFromHub()),
                new AimDriving(driveBase, driveController, RobotContainer::getShootingAngle)
            ).withName("Shoot");
 
        feed = () ->
            new transferOn(transfer)
                .alongWith(new kickerOn(kicker))
                .withName("Feed");
        }
 
    public static void configureDefaultCommands() {
        hood.setDefaultCommand(new openHood(hood, () -> 0));
    }
    //#endregion
 
    //#region Bindings
    public static void configureMechanismBindings() {
        driveController.L1().whileTrue(intakeWithSlowDrive.get());
 
        driveController.L2().whileTrue(pass.get());
 
        driveController.R1().whileTrue(shoot.get());

        driveController.R1().and(RobotContainer::readyToShoot).whileTrue(feed.get());
 
        driveController.triangle().whileTrue(outtake.get());
 
        driveController.square().onTrue(toggleIntake.get());
 
        new Trigger(() -> rackAndPinion.isJammed() && intakeExtended)
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
    ////#region Choosers
    public static void configureChoosers(){
        List<String> namesOfAutos = AutoBuilder.getAllAutoNames();
        List<PathPlannerAuto> listOfAutos = new ArrayList<>();

        autoChooser = new LoggedDashboardChooser<>("chooser");
        for (String autoName : namesOfAutos) {
            PathPlannerAuto auto = new PathPlannerAuto(autoName);
           listOfAutos.add(auto);
        }

        listOfAutos.forEach(auto -> autoChooser.addOption(auto.getName(), auto));

        autoChooser.addDefaultOption("Do Nothing", new InstantCommand());
        SmartDashboard.putData("chooser", autoChooser.getSendableChooser());

        new Trigger(RobotState::isEnabled).and(RobotState::isTeleop).onTrue(new InstantCommand(() -> Robot.clearObjectPoseField("AutoPath")).ignoringDisable(true));
        new Trigger(RobotState::isDisabled).and(checkForPathChoiceUpdate).onTrue(new InstantCommand(() -> updateFieldFromAuto(autoChooser.get().getName())).ignoringDisable(true));

        List<PathPlannerAuto> mirroredListOfAutos = new ArrayList<>();
        for (String autoName : namesOfAutos) {
            PathPlannerAuto auto = new PathPlannerAuto(autoName, true);
            mirroredListOfAutos.add(auto);
         }
        for (int i = 0; i < namesOfAutos.size(); i++){
            mirroredAutoMap.put(namesOfAutos.get(i), mirroredListOfAutos.get(i));
        }

        sideChooser = new LoggedDashboardChooser<>("sideChooser");

        sideChooser.addDefaultOption("right", "right");
        sideChooser.addOption("left", "left");

        SmartDashboard.putData("sideChooser",sideChooser.getSendableChooser());
    }

    private static void updateFieldFromAuto(String autoName) {
        List<Pose2d> poses = new ArrayList<>();

        try {
            PathPlannerAuto.getPathGroupFromAutoFile(autoName).forEach(path -> {
                poses.addAll(path.getPathPoses());
            });
        } catch (IOException | ParseException e) {
            DriverStation.reportError("Error loading auto path", e.getStackTrace());
        }

        Robot.setTrajectoryField("AutoPath", poses);
    }

    private static final BooleanSupplier checkForPathChoiceUpdate = new BooleanSupplier() {
        private String lastAutoName = "InstantCommand";

        @Override
        public boolean getAsBoolean() {
            if (autoChooser.get() == null) return false;

            String currentAutoName = autoChooser.get().getName();

            try {
                return !Objects.equals(lastAutoName, currentAutoName);
            } finally {
                lastAutoName = currentAutoName;
            }

        }
    };

    public static Command getAuto(){
        return sideChooser.get().equals("right") ?
            autoChooser.get() : mirroredAutoMap.get(autoChooser.get().getName());
    }
    //#endregion
        public static void configureTrenchPaths(){
        try{
            upLeftTrenchPath = PathPlannerPath.fromPathFile("UpLeft");
            upRightTrenchPath = PathPlannerPath.fromPathFile("UpRight");
        }
        catch (IOException | ParseException | FileVersionException e){
            DriverStation.reportError("Error loading trench path", e.getStackTrace());
            upLeftTrenchPath = PathPlannerPath.fromPathPoints(new ArrayList<>(), PathConstraints.unlimitedConstraints(12.0), new GoalEndState(0, Rotation2d.kZero));
            upRightTrenchPath = PathPlannerPath.fromPathPoints(new ArrayList<>(), PathConstraints.unlimitedConstraints(12.0), new GoalEndState(0, Rotation2d.kZero));
        }
    }

    public static Command passTrench(){
        Pose2d pose = driveBase.getPose();
        PathPlannerPath targetPath;

        if(Robot.isRedAlliance) pose = FlippingUtil.flipFieldPose(driveBase.getPose());
        Logger.recordOutput("Trench/Flipped Pose", pose);

        PathPlannerPath path = pose.getX() <= 4.5 ? upLeftTrenchPath : upRightTrenchPath;
        targetPath = pose.getY() >= 4 ? path : path.mirrorPath();
        Logger.recordOutput("Trench/Trench Path", targetPath.getPathPoses().toArray(new Pose2d[0]));

        PathPlannerPath flippedTargetPath = Robot.isRedAlliance ? targetPath.flipPath() : targetPath;
        Logger.recordOutput("Trench/Flipped Trench Path", flippedTargetPath.getPathPoses().toArray(new Pose2d[0]));

        return driveBase.pathFindToPathAndFollow(targetPath);
   }
 
    //#region Shooting Helpers
    private static final Debouncer readyToShootDebouncer = new Debouncer(0.1, DebounceType.kRising);
    public static boolean readyToShoot() {
        return readyToShootDebouncer.calculate(Math.abs(angleToHub()) < Constants.SHOOTING_ANGLE_TOLERANCE && hood.atAngle()  && flywheel.atSpeed());
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
}