package frc.robot.subsystems.Shooter;

import org.littletonrobotics.junction.AutoLog;

public interface ShooterIO {
    @AutoLog
    class ShooterInputs{


    }
    //not good
    void setDutyCycle(double desired_dutyCycle); //needs a rework probably
    void setVelocity(double desired_Velocity); //needs a rework probably
    void setPoint(double desired_Angle); //done

    double getVelocity();//done
    double getAcceleration();
    double getPoint();

    void shooterVision();

    void update(ShooterInputs inputs);

}
