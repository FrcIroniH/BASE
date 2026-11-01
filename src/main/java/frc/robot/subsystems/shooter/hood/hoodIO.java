package frc.robot.subsystems.shooter.hood;

import org.littletonrobotics.junction.AutoLog;

public interface hoodIO {
    @AutoLog
    public class hoodIOInputs {
        double hoodOpen;
        double hoodVelocity;
    }
    void resetAngleEncoder();
    void openToPosition(double openPosition);
    void update(hoodIOInputs inputs);
    void setVoltage(double volts);
}
