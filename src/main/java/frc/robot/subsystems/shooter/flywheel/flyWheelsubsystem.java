package frc.robot.subsystems.shooter.flywheel;

import static edu.wpi.first.units.Units.Volts;

import org.littletonrobotics.junction.Logger;

import edu.wpi.first.units.measure.Voltage;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

/**
 * The flywheel only knows how to spin at an RPM.
 * WHERE that RPM comes from (tuning dashboard / shot table + vision) is decided by the command,
 * so the flywheel works without vision or distanceToHub.
 */
public class flyWheelsubsystem extends SubsystemBase {
    private final flyWheelIO io;
    private final flyWheelInputsAutoLogged inputs = new flyWheelInputsAutoLogged();
    private double targetRPM = 0;

    public flyWheelsubsystem() {
        io = new flyWheelIOReal();
    }

    public void setRPM(double rpm) {
        targetRPM = Math.max(0, rpm);
        io.setRPM(targetRPM);
    }

    public void stop() {
        targetRPM = 0;
        io.setVoltage(0);
    }

    public boolean atSpeed() {
        return targetRPM > 0 && Math.abs(inputs.currentRPM - targetRPM) < flyWheelConstants.RPM_TOLERANCE;
    }

    public double getRPM() {
        return inputs.currentRPM;
    }

    public void setVoltage(Voltage voltage) {
        targetRPM = 0;
        io.setVoltage(voltage.in(Volts));
    }

    @Override
    public void periodic() {
        io.update(inputs);
        Logger.processInputs("Shooter/Flywheel", inputs);
        Logger.recordOutput("Shooter/Flywheel/TargetRPM", targetRPM);
        Logger.recordOutput("Shooter/Flywheel/AtSpeed", atSpeed());
    }
}
