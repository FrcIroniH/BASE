package frc.robot.subsystems.Intake.rackAndPinion;

import frc.util.MarinersController.MarinersTalonFX;
import frc.util.MarinersController.MarinersController.ControlMode;
import edu.wpi.first.math.trajectory.TrapezoidProfile.Constraints;

public class rackAndPinionIOReal implements rackAndPinionIO {
    private final MarinersTalonFX pinionMotor;
    public rackAndPinionIOReal(){
        pinionMotor = new MarinersTalonFX("pinionMotor", rackAndPinionConstants.CONTROLLER_LOCATION, rackAndPinionConstants.motorID);
        pinionMotor.setCurrentLimits(rackAndPinionConstants.currentLimit, rackAndPinionConstants.currentThreshhold);
        pinionMotor.setProfile(new Constraints(0.405, 0.2));

    }
    public void resetAngleEncoder() {
        pinionMotor.resetMotorEncoder();
    }
    public void extendTo(double PinionRotation) {
        pinionMotor.setReference(PinionRotation, ControlMode.Position);
        
        
    }
    public void update(rackAndPinionInputs inputs) {
        inputs.PinionRotation = pinionMotor.getPosition();
    }
}