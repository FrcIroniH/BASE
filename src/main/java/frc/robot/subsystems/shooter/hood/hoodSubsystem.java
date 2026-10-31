package frc.robot.subsystems.shooter.hood;

import java.util.function.Supplier;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class hoodSubsystem extends SubsystemBase {
    hoodIO io;
    hoodIOInputsAutoLogged inputs = new hoodIOInputsAutoLogged();

    public hoodSubsystem(Supplier<Pose2d> poseSupplier) {
        io = new hoodIOReal(poseSupplier);
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