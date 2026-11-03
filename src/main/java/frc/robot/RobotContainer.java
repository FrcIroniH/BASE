package frc.robot;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import org.littletonrobotics.junction.Logger;
import org.littletonrobotics.junction.networktables.LoggedDashboardChooser;

import com.pathplanner.lib.path.PathPlannerPath;

import edu.wpi.first.wpilibj.Alert;
import edu.wpi.first.wpilibj.Alert.AlertType;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import edu.wpi.first.wpilibj2.command.button.CommandPS5Controller;
import edu.wpi.first.wpilibj2.command.sysid.SysIdRoutine.Direction;
import frc.robot.subsystems.DriveTrain.DriveBase;
import frc.robot.subsystems.Vision.Vision;

import frc.robot.subsystems.Intake.Rollers.RollerCommands;
import frc.robot.subsystems.Intake.Rollers.Rollers;
import frc.robot.subsystems.Intake.rackAndPinion.intakeFullExtension;
import frc.robot.subsystems.Intake.rackAndPinion.intakeFullRetraction;
import frc.robot.subsystems.Intake.rackAndPinion.rackAndPinionSubsystem;
import frc.robot.subsystems.Intake.transfer.transferConstants;
import frc.robot.subsystems.Intake.transfer.transferOn;
import frc.robot.subsystems.Intake.transfer.transferSubsystem;
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
    private final DriveBase driveBase = new DriveBase();
    private final hoodSubsystem hood = new hoodSubsystem(driveBase::getPose);
    private final flyWheelsubsystem flywheel = new flyWheelsubsystem(driveBase::getPose);    // for SysID:
    private final hoodSysID hoodSysId = new hoodSysID(hood); 
    private final flyWheelSysID flywheelSysId = new flyWheelSysID(flywheel);
    private final kickerSubsystem kicker = new kickerSubsystem();
    public static Alert brownoutBattery = new Alert("BROWNOUT", AlertType.kError);

    private static double tuningOpenHoodPrecent = 1;
    private static double tuningRPM = 0;
    double hoodAngle(boolean raise) {
        if (raise) {
            tuningOpenHoodPrecent -= 0.2;
        } else { 
            tuningOpenHoodPrecent += 0.2;

        }
        if (tuningOpenHoodPrecent < 0 ) {
            tuningOpenHoodPrecent = 0;
        }
        if (tuningOpenHoodPrecent > 1) {
            tuningOpenHoodPrecent = 1;
        }
        return tuningOpenHoodPrecent;
    }
    double flywheelRPM(boolean raise) {
        if (raise) {
            tuningRPM += 10;
        }
        else {
            tuningRPM -= 10;
        }
        if (tuningRPM < 0) {
            tuningRPM = 0;
        }
        return tuningRPM;
    }



    public RobotContainer(){
        driveController = new CommandPS5Controller(0);
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

        // for Tuning:
        driveController.L1().onTrue(new openHood(hood, hoodAngle(true)));
        driveController.L2().onTrue(new openHood(hood, hoodAngle(false)));
        driveController.R1().onTrue(new flyWheelCommand(flywheel, flywheelRPM(true)));
        driveController.R2().onTrue(new flyWheelCommand(flywheel, flywheelRPM(false)));
        driveController.cross().whileTrue(new kickerOn(kicker));
        Logger.recordOutput("Shooter/Flywheel/tuningRPM", tuningRPM);
        Logger.recordOutput("Shooter/Hood/tuningOpenHoodPrecent", tuningOpenHoodPrecent);


        


    }
}

