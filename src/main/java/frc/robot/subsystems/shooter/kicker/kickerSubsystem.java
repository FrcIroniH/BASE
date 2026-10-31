package frc.robot.subsystems.shooter.kicker;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class kickerSubsystem extends SubsystemBase {
    kickerIO io;
    public kickerSubsystem() {
        io = new kickerIOReal();
    }
    public void SetDutyCycle(double dutyCycle) {
        io.setDutyCycle(dutyCycle);
    }
    public void stop() {
        io.setDutyCycle(0);
    }
}
