package frc.robot;

import edu.wpi.first.wpilibj2.command.button.CommandPS5Controller;
import frc.robot.subsystems.Intake.Rollers.RollerCommands;
import frc.robot.subsystems.Intake.Rollers.RollerConstants;
import frc.robot.subsystems.Intake.Rollers.Rollers;

public class RobotContainer {
    public static CommandPS5Controller driveController;
    public static Rollers roller;
    public RobotContainer(){
        roller = new Rollers();
        driveController = new CommandPS5Controller(0);
        driveController.L2().whileTrue(new RollerCommands(roller));
    }
}