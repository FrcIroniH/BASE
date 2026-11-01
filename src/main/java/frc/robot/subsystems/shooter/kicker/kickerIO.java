package frc.robot.subsystems.shooter.kicker;

import org.littletonrobotics.junction.AutoLog;

public interface kickerIO {
    @AutoLog
    public class kickerIOInputs {
        double velocity;
    }
    void setDutyCycle(double dutyCycle);
    void update(kickerIOInputs inputs);
}
