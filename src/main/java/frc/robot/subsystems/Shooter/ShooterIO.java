package frc.robot.subsystems.Shooter;

import org.littletonrobotics.junction.AutoLog;

public interface ShooterIO {
    @AutoLog
    class ShooterInputs{


    }

    void setDutyCycle(double desired_dutyCycle);
    void setVelocity(double desired_Velocity);
    void setPoint(double desired_Angle);

    double getVelocity();
    double getAcceleration();
    double getPoint();

    void shooterVision();

    void update(ShooterInputs inputs);

}
