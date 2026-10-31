package frc.robot.subsystems.Shooter;

import org.littletonrobotics.junction.Logger;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.sysid.SysIdRoutine;
import edu.wpi.first.wpilibj2.command.sysid.SysIdRoutine.Config;
import edu.wpi.first.wpilibj2.command.sysid.SysIdRoutine.Direction;
import edu.wpi.first.wpilibj2.command.sysid.SysIdRoutine.Mechanism;

public class flyWheelSysID {
    private final SysIdRoutine routine;

    public flyWheelSysID(flyWheelsubsystem flywheel) {
        routine = new SysIdRoutine(
            new Config(
                null, null, null,   // defaults: 1 V/s ramp, 7 V step, 10 s timeout
                (state) -> Logger.recordOutput("Shooter/SYSID/State", state.toString())
            ),
            new Mechanism(
                flywheel::setVoltage,
                null,               // AdvantageKit + MarinersController already log the data
                flywheel
            )
            
        );
    }

    public Command quasistatic(Direction direction) {
        return routine.quasistatic(direction);
    }

    public Command dynamic(Direction direction) {
        return routine.dynamic(direction);
    }
}