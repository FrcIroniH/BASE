package frc.robot.subsystems.Intake.rackAndPinion;

import org.littletonrobotics.junction.AutoLog;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class rackAndPinionSubsystem extends SubsystemBase {
    rackAndPinionIO io;
    rackAndPinionInputsAutoLogged inputs = new rackAndPinionInputsAutoLogged();
    
    public rackAndPinionSubsystem() {
        io = new rackAndPinionIOReal();
    }
    
    public void resetAngleEncoder(){
        io.resetAngleEncoder();
    }
    public void extend() {
        io.extendTo(rackAndPinionConstants.pinionRotation);
    }
    public void retract() {
        io.extendTo(0);
    }
    @Override
    public void periodic() {
        io.update(inputs);
        if (inputs.stalled) {
            io.stop();
        }
    }

    public boolean isStalled() {
        return inputs.stalled;
    }
}