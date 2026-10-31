package frc.robot.subsystems.Shooter.Shooter;

import org.littletonrobotics.junction.AutoLog;

public interface flyWheelIO {
    @AutoLog
    class flyWheelInputs {
        double RPM;
    }
    public void setRPM(double RPM);
    public void update(flyWheelInputs inputs);
} 