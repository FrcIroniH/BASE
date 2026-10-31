package frc.robot.subsystems.Intake.Rollers;

import org.littletonrobotics.junction.AutoLog;

/** Add your docs here. */
public interface RollersIO {
    @AutoLog
    class RollersInputs {

    }
    public void setDutyCycle(double dutyCycle);
}