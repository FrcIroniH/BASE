package frc.robot;
import edu.wpi.first.wpilibj2.command.button.CommandPS5Controller;
<<<<<<< Updated upstream

public class RobotContainer {
    public static CommandPS5Controller driveController;

    public RobotContainer(){
        driveController = new CommandPS5Controller(0);
=======
import edu.wpi.first.wpilibj2.command.sysid.SysIdRoutine.Direction;
import frc.robot.subsystems.DriveTrain.DriveBase;
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
    private final flyWheelsubsystem flywheel = new flyWheelsubsystem(driveBase::getPose);
    private final kickerSubsystem kicker = new kickerSubsystem();
    private final hoodSysID hoodSysId = new hoodSysID(hood);
    private final flyWheelSysID flywheelSysId = new flyWheelSysID(flywheel);

    public RobotContainer(){
        driveController = new CommandPS5Controller(0);
        driveController.L2().whileTrue(new openHood(hood));
        driveController.R2().whileTrue(new flyWheelCommand(flywheel));
        driveController.R1().whileTrue(new kickerOn(kicker));

        driveController.cross().whileTrue(
            hoodSysId.quasistatic(Direction.kForward).alongWith(flywheelSysId.quasistatic(Direction.kForward)));
        driveController.circle().whileTrue(
            hoodSysId.quasistatic(Direction.kReverse).alongWith(flywheelSysId.quasistatic(Direction.kReverse)));
        driveController.square().whileTrue(
            hoodSysId.dynamic(Direction.kForward).alongWith(flywheelSysId.dynamic(Direction.kForward)));
        driveController.triangle().whileTrue(
            hoodSysId.dynamic(Direction.kReverse).alongWith(flywheelSysId.dynamic(Direction.kReverse)));
>>>>>>> Stashed changes
    }
}
