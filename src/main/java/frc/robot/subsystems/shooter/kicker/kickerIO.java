package frc.robot.subsystems.shooter.kicker;

import org.littletonrobotics.junction.AutoLog;

public interface kickerIO {
    @AutoLog
    public class kickerIOInputs {
        
    }
    void setDutyCycle(double dutyCycle);
}
