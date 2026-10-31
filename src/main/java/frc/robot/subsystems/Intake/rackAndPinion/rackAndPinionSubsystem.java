package frc.robot.subsystems.Intake.rackAndPinion;

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
    }
}