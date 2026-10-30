package frc.robot.subsystems.intake;

import frc.util.MarinersController.MarinersController;
import frc.util.MarinersController.MarinersTalonFX;
import frc.util.MarinersController.MarinersSparkBase;
import frc.util.MarinersController.MarinersController.ControlMode;
import frc.util.MarinersController.MarinersSparkBase.MotorType;

public class intakeIOReal implements intakeIO {
    private final MarinersTalonFX pinionMotor;
    public intakeIOReal(){
        pinionMotor = new MarinersTalonFX("pinionMotor", intakeConstants.CONTROLLER_LOCATION, intakeConstants.motorID);
        pinionMotor.setcurrentlimit


    }
    void resetAngleEncoder() {

    }
    void extendTo(double PinionRotation) {

    }
    void extensionPower(double pinionPower) {

    }
    void update(intakeInputs inputs) {
        
    }

}
