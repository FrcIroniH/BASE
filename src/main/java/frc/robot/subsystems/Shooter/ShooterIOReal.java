package frc.robot.subsystems.Shooter;

import org.littletonrobotics.junction.AutoLog;

import frc.util.MarinersController.MarinersTalonFX;
import frc.util.MarinersController.MarinersController.ControlMode;
import frc.util.MarinersController.MarinersController.ControllerLocation;

public class ShooterIOReal implements ShooterIO {
  MarinersTalonFX leadMotor;
  MarinersTalonFX followMotor;
  MarinersTalonFX hoodMotor;
  public ShooterIOReal(){
    leadMotor = new MarinersTalonFX("shooter_Motor", ControllerLocation.RIO, 123);
    followMotor = new MarinersTalonFX("shooter_Follow_Motor",    ControllerLocation.RIO, 1234);
    followMotor.setMotorAsFollower(leadMotor, true);
    hoodMotor = new MarinersTalonFX("hood_motor", ControllerLocation.RIO, 12345);
  }
  @Override
  public void setDutyCycle(double desired_dutyCycle){
    leadMotor.setDutyCycle(ShooterConstants.desired_dutyCycle);

  }

  @Override
  public double getVelocity(){
    return leadMotor.getVelocity();
  }

  @Override
  public void setVelocity(double desired_Velocity){
    leadMotor.setDutyCycle(desired_Velocity);

  }

  @Override
  public void setPoint(double desired_Angle){
    hoodMotor.setReference(desired_Angle / 360 , ControlMode.Position);
    

  }

  void update(ShooterInputs inputs){
    @AutoLog
    
  }



}
