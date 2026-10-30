
import edu.wpi.first.wpilibj2.command.button.CommandPS5Controller;

public class RobotContainer {
    public static CommandPS5Controller driveController;

    public RobotContainer(){
        driveController = new CommandPS5Controller(0);
    }
}