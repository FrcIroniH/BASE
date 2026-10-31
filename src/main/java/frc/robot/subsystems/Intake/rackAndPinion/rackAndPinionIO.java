package frc.robot.subsystems.Intake.rackAndPinion;

import org.littletonrobotics.junction.AutoLog;

public interface rackAndPinionIO {
    @AutoLog
    public class rackAndPinionInputs {
        double PinionRotation;
        double current;
        boolean stalled;
    }
    void resetAngleEncoder();
    void extendTo(double PinionRotation);
    void update(rackAndPinionInputs inputs);
    void stop();
}