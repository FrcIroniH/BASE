package frc.robot.subsystems.Intake.rackAndPinion;

import org.littletonrobotics.junction.AutoLog;

public interface rackAndPinionIO {
    @AutoLog
    public class rackAndPinionInputs {
        double PinionRotation;
    }
    public void resetAngleEncoder();
    public void extendTo(double PinionRotation);
    public void update(rackAndPinionInputs inputs);
}