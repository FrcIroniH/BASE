package frc.robot.subsystems.intake;

import org.littletonrobotics.junction.AutoLog;

public interface intakeIO {
    @AutoLog
    public class intakeInputs{
        double PinionRotation;
    }
    void resetAngleEncoder();
    void extendTo(double PinionRotation);
    void extensionPower(double pinionPower);
    void update(intakeInputs inputs);
}
