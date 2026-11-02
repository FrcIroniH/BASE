package frc.robot.subsystems.Intake.transfer;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class transferSubsystem extends SubsystemBase {
    private final transferIO io;
    public transferSubsystem() {
        io = new transferIOReal();
    }
    public void setDutyCycle(double dutyCycle) {
        io.setDutyCycle(dutyCycle);
    }
    public void stop() {
        io.setDutyCycle(0);
    }
}