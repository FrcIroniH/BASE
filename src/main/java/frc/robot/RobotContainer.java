package frc.robot;

import edu.wpi.first.wpilibj2.command.button.CommandPS5Controller;
import edu.wpi.first.wpilibj2.command.sysid.SysIdRoutine.Direction;
import frc.robot.commands.Drive.DriveCommand;
import frc.robot.subsystems.DriveTrain.DriveBase;
import frc.robot.subsystems.Shooter.flyWheelCommand;
import frc.robot.subsystems.Shooter.flyWheelSysID;
import frc.robot.subsystems.Shooter.flyWheelsubsystem;
import frc.robot.subsystems.Vision.Vision;

public class RobotContainer {
    public static CommandPS5Controller driveController;

    public static DriveBase driveBase;
    public static Vision vision;
    public static flyWheelsubsystem flywheel;
    public static flyWheelSysID flywheelSysID;

    public RobotContainer() {
        driveController = new CommandPS5Controller(0);

        driveBase = new DriveBase();
        vision = new Vision(driveBase::addVisionMeasurement, driveBase::getPose, driveBase::reset);
        flywheel = new flyWheelsubsystem(driveBase::getPose);
        flywheelSysID = new flyWheelSysID(flywheel);

        driveBase.setDefaultCommand(new DriveCommand(driveBase, driveController));

        configureBindings();
    }

    private void configureBindings() {
        // hold R2 to spin the flywheel for the current distance
        driveController.R2().whileTrue(new flyWheelCommand(flywheel));

        // for sysid
        driveController.cross().whileTrue(flywheelSysID.quasistatic(Direction.kForward));
        driveController.circle().whileTrue(flywheelSysID.quasistatic(Direction.kReverse));
        driveController.square().whileTrue(flywheelSysID.dynamic(Direction.kForward));
        driveController.triangle().whileTrue(flywheelSysID.dynamic(Direction.kReverse));
    }
}