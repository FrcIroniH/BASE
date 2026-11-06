package frc.robot.subsystems.shooter.flywheel;

import org.littletonrobotics.junction.AutoLog;

public interface flyWheelIO {
    @AutoLog
    class flyWheelInputs {
        double currentRPM;
    }
    void setRPM(double RPM);
    void update(flyWheelInputs inputs);
    void setVoltage(double volts);
}
