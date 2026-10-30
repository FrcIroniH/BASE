package frc.robot.subsystems.Shooter;

import frc.util.MarinersController.MarinersTalonFX;

public class ShooterIOReal implements ShooterIO {
  MarinersTalonFX leadMotor;
  MarinersTalonFX followMotor;
  MarinersTalonFX hoodMotor;
  public ShooterIOReal(){
    leadMotor = new MarinersTalonFX("shooter_Motor", null, 123);
    followMotor = new MarinersTalonFX("shooter_Follow_Motor",    null, 1234);
    followMotor.setMotorAsFollower(leadMotor, true);
    hoodMotor = new MarinersTalonFX("hood_motor", null, 12345);
  }
  @Override
  public void setDutyCycle(double desired_dutyCycle){
    leadMotor.setDutyCycle(ShooterConstants.desired_dutyCycle);

  }

  @Override
  public double getVelocity(){
    leadMotor.getVelocity();
  }

  @Override
  public void setPoint(double desired_Angle){
    

  }



}
