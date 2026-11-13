package frc.robot.subsystems.Intake.rackAndPinion;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.subsystems.Intake.rackAndPinion.rackAndPinionConstants.rackAndPinionState;

import org.littletonrobotics.junction.AutoLog;

public class rackAndPinionSubsystem extends SubsystemBase {
    rackAndPinionIO io;
    rackAndPinionInputsAutoLogged rackinputs = new rackAndPinionInputsAutoLogged();
    
    public rackAndPinionSubsystem() {
        io = new rackAndPinionIOReal();
    }
    
    public void resetAngleEncoder(){
        io.resetAngleEncoder();
    }
    public void extend() {
        io.extendTo(rackAndPinionConstants.pinionRotation);
        rackAndPinionConstants.currentPinionState = rackAndPinionConstants.currentPinionState.OPEN;
    }
    public void extendTo(double position) {
        io.extendTo(position);
        rackAndPinionConstants.currentPinionState = rackAndPinionConstants.currentPinionState.HALF;
    }
    public void retract() {
        io.extendTo(0);
        rackAndPinionConstants.currentPinionState = rackAndPinionConstants.currentPinionState.CLOSE;

    }
    @Override
    public void periodic() {
        io.update(rackinputs);
        if (rackinputs.currentStalled) {
            io.stop();
        }
    }

    public boolean isCurrentStalled() {
        return rackinputs.currentStalled;
    }
    public boolean isJammed() {
        return rackinputs.currentStalled && !rackinputs.enchoderStalled;
    }
    public rackAndPinionConstants.rackAndPinionState rackAndPinionState() {
        return rackAndPinionConstants.currentPinionState;
    }


}