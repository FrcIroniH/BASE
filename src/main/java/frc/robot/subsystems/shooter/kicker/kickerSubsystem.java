package frc.robot.subsystems.shooter.kicker;

import org.littletonrobotics.junction.Logger;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class kickerSubsystem extends SubsystemBase {
    private final kickerIO io;
    private final kickerIOInputsAutoLogged inputs = new kickerIOInputsAutoLogged();
    public kickerSubsystem() {
        io = new kickerIOReal();
    }
    public void setDutyCycle(double dutyCycle) {
        io.setDutyCycle(dutyCycle);
    }
    public void stop() {
        io.setDutyCycle(0);
    }
    @Override
    public void periodic() {
        io.update(inputs);
        Logger.processInputs("Shooter/Kicker", inputs);
    }
}
