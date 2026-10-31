package frc.robot;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.button.CommandPS5Controller;
import frc.robot.subsystems.Intake.Rollers.RollerCommands;
import frc.robot.subsystems.Intake.Rollers.RollerConstants;
import frc.robot.subsystems.Intake.Rollers.Rollers;
import frc.robot.subsystems.Intake.rackAndPinion.intakeFullRetraction;
import frc.robot.subsystems.Intake.rackAndPinion.intakeFullExtension;

import frc.robot.subsystems.Intake.rackAndPinion.rackAndPinionSubsystem;

public class RobotContainer {
    public static CommandPS5Controller driveController;
    public static Rollers roller;
    public static rackAndPinionSubsystem rackAndPinion;
    public RobotContainer(){
        roller = new Rollers();
        rackAndPinion = new rackAndPinionSubsystem();
        driveController = new CommandPS5Controller(0);
        driveController.L2().whileTrue(new RollerCommands(roller));
        driveController.L2().onTrue(new intakeFullExtension(rackAndPinion));
        driveController.L1().onTrue(new intakeFullRetraction(rackAndPinion));
    }
}