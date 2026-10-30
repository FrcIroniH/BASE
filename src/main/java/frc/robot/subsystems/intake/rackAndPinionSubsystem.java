package frc.robot.subsystems.intake;

import org.littletonrobotics.junction.AutoLog;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class rackAndPinionSubsystem extends SubsystemBase {
    rackAndPinionIO io;
    rackAndPinionInputsAutologged inputs = new rackAndPinionIpnputAutologged();
    
    public rackAndPinionSubsystem() {
        io = new rackAndPinionIOReal();
    }
    public void resetAngleEncoder(){
        io.resetAngleEncoder();
    }
    public void extend() {
        io.extendTo();
    }
    @Override
    public void periodic() {
        io.update(inputs);
    }
}
