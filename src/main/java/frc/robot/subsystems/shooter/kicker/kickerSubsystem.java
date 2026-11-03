package frc.robot.subsystems.shooter.kicker;

import java.util.function.DoubleSupplier;

import org.littletonrobotics.junction.Logger;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class kickerSubsystem extends SubsystemBase {
    private final kickerIO io;
    private final kickerIOInputsAutoLogged inputs = new kickerIOInputsAutoLogged();
    private double angleToHub = 0;
    public kickerSubsystem() {
        io = new kickerIOReal();
    }
    public double getAngleToHub() {
        return angleToHub;
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
        Logger.recordOutput("Shooter/Kicker/AngleToHub", angleToHub);
    }
}
