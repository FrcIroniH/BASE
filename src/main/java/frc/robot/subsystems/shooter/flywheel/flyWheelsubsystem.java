package frc.robot.subsystems.shooter.flywheel;

import java.util.function.Supplier;


import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import static edu.wpi.first.units.Units.Volts;
import org.littletonrobotics.junction.Logger;
import edu.wpi.first.units.measure.Voltage;

public class flyWheelsubsystem extends SubsystemBase {
    private final flyWheelIO io;
    private final flyWheelInputsAutoLogged inputs = new flyWheelInputsAutoLogged();
    private final Supplier<Pose2d> poseSupplier;
    private double targetRPM = 0;
    private double distanceToHub = 0;

    public flyWheelsubsystem(Supplier<Pose2d> poseSupplier) {
        this.poseSupplier = poseSupplier;
        io = new flyWheelIOReal();
    }
    public void setRPMFromDistance() {
        targetRPM = flyWheelConstants.getRPM(distanceToHub);
        io.setRPM(targetRPM);
    }

    public void stop() {
        targetRPM = 0;
        io.setVoltage(0);
    }

    /** true when the flywheel is spinning and within tolerance of its target */
    public boolean atSpeed() {
        return targetRPM > 0 && Math.abs(inputs.currentRPM - targetRPM) < flyWheelConstants.RPM_TOLERANCE;
    }

    @Override
    public void periodic() {
        io.update(inputs);
        Logger.processInputs("Shooter/Flywheel", inputs);

        distanceToHub = flyWheelConstants.distanceToHub(poseSupplier.get());
        Logger.recordOutput("Shooter/Flywheel/DistanceToHub", distanceToHub);
        Logger.recordOutput("Shooter/Flywheel/TargetRPM", targetRPM);
        Logger.recordOutput("Shooter/Flywheel/AtSpeed", atSpeed());
        Logger.recordOutput("Shooter/Flywheel/OutOfTableRange",
            distanceToHub < flyWheelConstants.MIN_TABLE_DISTANCE || distanceToHub > flyWheelConstants.MAX_TABLE_DISTANCE);
    }
    public void setVoltage(Voltage voltage) {
        targetRPM = 0;
        io.setVoltage(voltage.in(Volts));
    }
}
