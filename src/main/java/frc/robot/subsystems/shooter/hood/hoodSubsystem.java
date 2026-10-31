package frc.robot.subsystems.shooter.hood;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class hoodSubsystem extends SubsystemBase {
    hoodIO io;
    hoodIOInputsAutoLogged inputs = new hoodIOInputsAutoLogged();

    public hoodSubsystem() {
        io = new hoodIOReal();
    }
    public void resetAngleEncoder() {
        io.resetAngleEncoder();
    }
    public void open(double Position) {
        io.openToPposition(Position);
    }
    public void close() {
        io.openToPposition(0);
    }
    @Override
    public void periodic() {
        io.update(inputs);
    }
}