package frc.robot.subsystems.shooter.hood;

import java.util.function.Supplier;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.subsystems.shooter.flywheel.flywheelConstants;

public class hoodSubsystem extends SubsystemBase {
    hoodIO io;
    hoodIOInputsAutoLogged inputs = new hoodIOInputsAutoLogged();

    public hoodSubsystem(Supplier<Pose2d> poseSupplier) {
        io = new hoodIOReal(poseSupplier);
        resetAngleEncoder();
    }
    public void resetAngleEncoder() {
        io.resetAngleEncoder();
    }
    public void open(Supplier<Pose2d> poseSupplier) {
        io.openToPposition(hoodConstants.MAX_ANGLE_MOVEMENT_IN_MOTOR_ROTATIONS * hoodConstants.DISTANCE_TO_ANGLE_PRECENT.get(hoodConstants.distanceToHub(poseSupplier)));
    }
    @Override
    public void periodic() {
        io.update(inputs);
    }
}