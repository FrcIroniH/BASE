package frc.robot;
import edu.wpi.first.wpilibj2.command.button.CommandPS5Controller;
import frc.robot.subsystems.shooter.hood.hoodSubsystem;
import frc.robot.subsystems.shooter.hood.openHood;

public class RobotContainer {
    public static CommandPS5Controller driveController;
    private final hoodSubsystem hood = new hoodSubsystem(null);

    public RobotContainer(){
        driveController = new CommandPS5Controller(0);
        driveController.L2().whileTrue(new openHood(hood, null));
    }
}