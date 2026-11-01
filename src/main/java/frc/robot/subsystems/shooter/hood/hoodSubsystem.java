package frc.robot.subsystems.shooter.hood;

import java.util.function.Supplier;

import static edu.wpi.first.units.Units.Volts;

import org.littletonrobotics.junction.Logger;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.units.measure.Voltage;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.subsystems.shooter.flywheel.flyWheelConstants;

public class hoodSubsystem extends SubsystemBase {
    private final hoodIO io;
    private final hoodIOInputsAutoLogged inputs = new hoodIOInputsAutoLogged();
    private final Supplier<Pose2d> poseSupplier;
    private double targetPosition = 0;
    private double distanceToHub = 0;

    public hoodSubsystem(Supplier<Pose2d> poseSupplier) {
        this.poseSupplier = poseSupplier;
        io = new hoodIOReal();
        // assumes the hood is fully closed when the robot boots
        resetAngleEncoder();
    }
    public void resetAngleEncoder() {
        io.resetAngleEncoder();
    }
    public void open() {
        targetPosition = hoodConstants.MAX_ANGLE_MOVEMENT_IN_MOTOR_ROTATIONS * flyWheelConstants.getHoodPercent(distanceToHub);
        io.openToPosition(targetPosition);
    }
    /** true when the hood is within tolerance of its last commanded position */
    public boolean atAngle() {
        return Math.abs(inputs.hoodOpen - targetPosition) < hoodConstants.POSITION_TOLERANCE_IN_MOTOR_ROTATIONS;
    }
    /** used by SysId - drives the motor directly, the soft limits still stop it at the ends of travel */
    public void setVoltage(Voltage voltage) {
        io.setVoltage(voltage.in(Volts));
    }
    public boolean isNearTop() {
        return inputs.hoodOpen >= hoodConstants.MAX_ANGLE_MOVEMENT_IN_MOTOR_ROTATIONS - hoodConstants.SYSID_LIMIT_MARGIN_IN_MOTOR_ROTATIONS;
    }
    public boolean isNearBottom() {
        return inputs.hoodOpen <= hoodConstants.SYSID_LIMIT_MARGIN_IN_MOTOR_ROTATIONS;
    }
    @Override
    public void periodic() {
        io.update(inputs);
        Logger.processInputs("Shooter/Hood", inputs);

        distanceToHub = flyWheelConstants.distanceToHub(poseSupplier.get());
        Logger.recordOutput("Shooter/Hood/DistanceToHub", distanceToHub);
        Logger.recordOutput("Shooter/Hood/TargetPosition", targetPosition);
        Logger.recordOutput("Shooter/Hood/AtAngle", atAngle());
    }
}
