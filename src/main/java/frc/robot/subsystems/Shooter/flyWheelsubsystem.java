package frc.robot.subsystems.Shooter;

import java.util.function.Supplier;


import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import static edu.wpi.first.units.Units.Volts;
import org.littletonrobotics.junction.Logger;
import edu.wpi.first.units.measure.Voltage;

public class flyWheelsubsystem extends SubsystemBase {
    private final flyWheelIO io;
    private final flyWheelInputsAutoLogged inputs = new flyWheelInputsAutoLogged();

    public flyWheelsubsystem(Supplier<Pose2d> poseSupplier) {
        io = new flyWheelIOReal(poseSupplier);
    }
    public void setRPMFromDistance() {
        io.setRPM(flyWheelConstants.DISTANCE_TO_RPM.get(inputs.distanceToHub));
    }

    public void stop() {
        io.setVoltage(0);
    }

    @Override
    public void periodic() {
        io.update(inputs);
        Logger.processInputs("Shooter", inputs);
    }
    public void setVoltage(Voltage voltage) {
        io.setVoltage(voltage.in(Volts));
    }
}