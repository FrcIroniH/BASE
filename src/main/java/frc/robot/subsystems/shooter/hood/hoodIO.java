package frc.robot.subsystems.shooter.hood;

import org.littletonrobotics.junction.AutoLog;

public interface hoodIO {
    @AutoLog
    public class hoodIOInputs {
        double hoodOpen;
        double distanceToHub;
    }
    void resetAngleEncoder();
    void openToPposition(double openPositon);
    void update(hoodIOInputs inputs);
}
