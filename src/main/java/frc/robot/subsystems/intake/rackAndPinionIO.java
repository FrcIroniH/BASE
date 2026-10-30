package frc.robot.subsystems.intake;

import org.littletonrobotics.junction.AutoLog;

public interface rackAndPinionIO {
    @AutoLog
    public class intakeInputs{
        double PinionRotation;
    }
    public void resetAngleEncoder();
    public void extendTo(double PinionRotation);
    public void update(intakeInputs inputs);
}
