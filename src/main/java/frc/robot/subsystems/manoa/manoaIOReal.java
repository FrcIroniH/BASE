package frc.robot.subsystems.manoa;

import frc.util.MarinersController.MarinersSparkBase;
import frc.util.MarinersController.MarinersTalonFX;
import frc.util.MarinersController.MarinersController.ControllerLocation;

public class manoaIOReal implements manoaIO {

  private  MarinersTalonFX manoaMotor;

    public manoaIOReal(){
        manoaMotor = configuremanoaMotor();

    }
    private MarinersTalonFX configuremanoaMotor(){
        manoaMotor = new MarinersTalonFX("manoa", manoaconstants.CONTROLLER_LOCATION , manoaconstants.manoaID);
        manoaMotor.setMotorInverted(manoaconstants.manoaIsInverted);
        manoaMotor.setCurrentLimits(manoaconstants.currentlimitMAX, manoaconstants.currentlimitMIN);
        return manoaMotor;
    }

 public void manoaDutyCycle(double manoaDutyCycle){
    manoaMotor.setDutyCycle(manoaDutyCycle);
 }

}