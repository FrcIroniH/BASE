package frc.robot;

import org.littletonrobotics.junction.Logger;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.button.CommandPS5Controller;
import edu.wpi.first.wpilibj2.command.sysid.SysIdRoutine.Direction;
import frc.robot.subsystems.shooter.flywheel.flyWheelCommand;
import frc.robot.subsystems.shooter.flywheel.flyWheelSysID;
import frc.robot.subsystems.shooter.flywheel.flyWheelsubsystem;
import frc.robot.subsystems.shooter.hood.hoodSubsystem;
import frc.robot.subsystems.shooter.hood.hoodSysID;
import frc.robot.subsystems.shooter.hood.openHood;
import frc.robot.subsystems.shooter.kicker.kickerOn;
import frc.robot.subsystems.shooter.kicker.kickerSubsystem;


public class RobotContainer {

    public static CommandPS5Controller driveController;

    private final hoodSubsystem hood = new hoodSubsystem();
    private final flyWheelsubsystem flywheel = new flyWheelsubsystem();
    private final kickerSubsystem kicker = new kickerSubsystem();
    // for SysID:
    private final hoodSysID hoodSysId = new hoodSysID(hood);
    private final flyWheelSysID flywheelSysId = new flyWheelSysID(flywheel);

    // ---------------- Shooter tuning (no vision / no distanceToHub) ----------------
    // Put the robot at a measured distance, type it in "Tuning/Distance (m)",
    // then try speeds + hood angles until it scores and press "Save shot".
    private static final String DISTANCE_KEY = "Tuning/Distance (m)";
    private static final String RPM_KEY = "Tuning/RPM";
    private static final String HOOD_KEY = "Tuning/Hood Percent (0-1)";
    private static final double RPM_STEP = 50;
    private static final double HOOD_STEP = 0.05;
    private static final double MAX_RPM = 6000;

    private double tuningRPM() {
        return SmartDashboard.getNumber(RPM_KEY, 0);
    }

    private double tuningHoodPercent() {
        return SmartDashboard.getNumber(HOOD_KEY, 0);
    }

    private void nudgeRPM(double delta) {
        SmartDashboard.putNumber(RPM_KEY, MathUtil.clamp(tuningRPM() + delta, 0, MAX_RPM));
    }

    private void nudgeHood(double delta) {
        SmartDashboard.putNumber(HOOD_KEY, MathUtil.clamp(tuningHoodPercent() + delta, 0, 1));
    }

    /** Prints a ready-to-paste SHOT_TABLE row: {distance, hoodPercent, rpm}, */
    private void saveShot() {
        String row = String.format("{%.2f, %.3f, %.0f},",
            SmartDashboard.getNumber(DISTANCE_KEY, 0), tuningHoodPercent(), tuningRPM());
        System.out.println("[SHOT TABLE] " + row);
        Logger.recordOutput("Tuning/LastSavedShot", row);
        SmartDashboard.putString("Tuning/Last Saved Shot", row);
    }

    public RobotContainer() {
        driveController = new CommandPS5Controller(0);

        SmartDashboard.putNumber(DISTANCE_KEY, 0);
        SmartDashboard.putNumber(RPM_KEY, 0);
        SmartDashboard.putNumber(HOOD_KEY, 0);
        SmartDashboard.putData("Tuning/Save shot", new InstantCommand(this::saveShot).ignoringDisable(true));

        // for SysID:
        // driveController.cross().whileTrue(
        //     hoodSysId.quasistatic(Direction.kForward).alongWith(flywheelSysId.quasistatic(Direction.kForward)));
        // driveController.circle().whileTrue(
        //     hoodSysId.quasistatic(Direction.kReverse).alongWith(flywheelSysId.quasistatic(Direction.kReverse)));
        // driveController.square().whileTrue(
        //     hoodSysId.dynamic(Direction.kForward).alongWith(flywheelSysId.dynamic(Direction.kForward)));
        // driveController.triangle().whileTrue(
        //     hoodSysId.dynamic(Direction.kReverse).alongWith(flywheelSysId.dynamic(Direction.kReverse)));

        // for Tuning:
        // Hood always follows the dashboard value (change it live, it moves right away).
        hood.setDefaultCommand(new openHood(hood, this::tuningHoodPercent));

        // Nudge values from the controller (they also update on the dashboard; you can type them there too).
        driveController.L1().onTrue(new InstantCommand(() -> nudgeHood(+HOOD_STEP)).ignoringDisable(true));
        driveController.L2().onTrue(new InstantCommand(() -> nudgeHood(-HOOD_STEP)).ignoringDisable(true));
        driveController.R1().onTrue(new InstantCommand(() -> nudgeRPM(+RPM_STEP)).ignoringDisable(true));
        driveController.R2().onTrue(new InstantCommand(() -> nudgeRPM(-RPM_STEP)).ignoringDisable(true));

        // Triangle: toggle the flywheel on/off at the current tuning RPM (follows changes live).
        driveController.triangle().toggleOnTrue(new flyWheelCommand(flywheel, this::tuningRPM));
        // Cross (hold): feed the ball.
        driveController.cross().whileTrue(new kickerOn(kicker));
        // Square: save this distance/hood/RPM as a table row.
        driveController.square().onTrue(new InstantCommand(this::saveShot).ignoringDisable(true));

        // Match use later (once vision/pose works), e.g.:
        //   DoubleSupplier dist = () -> flyWheelConstants.distanceToHub(drive.getPose());
        //   new flyWheelCommand(flywheel, () -> flyWheelConstants.getRPM(dist.getAsDouble()));
        //   new openHood(hood, () -> flyWheelConstants.getHoodPercent(dist.getAsDouble()));
    }
}
