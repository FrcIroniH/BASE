package frc.robot;
import edu.wpi.first.wpilibj2.command.button.CommandPS5Controller;
import frc.robot.subsystems.manoa.manoaCommand;
import frc.robot.subsystems.manoa.manoasubsystem;

public class RobotContainer {
    public static CommandPS5Controller driveController;

    public final manoasubsystem manoa = new manoasubsystem();

    public RobotContainer(){
        driveController = new CommandPS5Controller(0);
        driveController.povRight().onTrue(new manoaCommand(manoa));
    }
}