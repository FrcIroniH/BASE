package frc.robot.subsystems.intake;

import edu.wpi.first.math.MathUtil;
import frc.util.MarinersController.MarinersController;
import frc.util.MarinersController.MarinersTalonFX;
import frc.util.MarinersController.MarinersSparkBase;
import frc.util.MarinersController.MarinersController.ControlMode;
import frc.util.MarinersController.MarinersSparkBase.MotorType;
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
    public void update(intakeInputs inputs) {
        inputs.PinionRotation = pinionMotor.getPosition();
    }

}
