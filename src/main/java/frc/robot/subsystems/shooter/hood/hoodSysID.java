package frc.robot.subsystems.shooter.hood;

import static edu.wpi.first.units.Units.Seconds;
import static edu.wpi.first.units.Units.Volts;

import org.littletonrobotics.junction.Logger;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.sysid.SysIdRoutine;
import edu.wpi.first.wpilibj2.command.sysid.SysIdRoutine.Config;
import edu.wpi.first.wpilibj2.command.sysid.SysIdRoutine.Direction;
import edu.wpi.first.wpilibj2.command.sysid.SysIdRoutine.Mechanism;

public class hoodSysID {
    private final SysIdRoutine routine;
    private final hoodSubsystem hood;

    public hoodSysID(hoodSubsystem hood) {
        this.hood = hood;
        routine = new SysIdRoutine(
            new Config(
                Volts.of(hoodConstants.SYSID_RAMP_VOLTS_PER_SECOND).per(Seconds),
                Volts.of(hoodConstants.SYSID_STEP_VOLTS),
                Seconds.of(hoodConstants.SYSID_TIMEOUT_SECONDS),
                (state) -> Logger.recordOutput("Shooter/Hood/SYSID/State", state.toString())
            ),
            new Mechanism(
                hood::setVoltage,
                null,               
                hood
            )
        );
    }

    public Command quasistatic(Direction direction) {
        return routine.quasistatic(direction).until(() -> isAtEnd(direction));
    }

    public Command dynamic(Direction direction) {
        return routine.dynamic(direction).until(() -> isAtEnd(direction));
    }

    private boolean isAtEnd(Direction direction) {
        if (direction == Direction.kForward) {
            return hood.isNearTop();
        } else {
            return hood.isNearBottom();
        }
    }
}
