// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.Intake.Rollers;

import org.littletonrobotics.junction.Logger;

import edu.wpi.first.wpilibj.Alert;
import edu.wpi.first.wpilibj.Alert.AlertType;
import edu.wpi.first.wpilibj.RobotState;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Rollers extends SubsystemBase {
  RollersIO io;
  /** Creates a new Rollers. */
  public Rollers() {
    io = new RollersIOReal();
  }

  public void setDutyCycle(double dutyCycle)
  {
    io.setDutyCycle(dutyCycle);
  }
  public void stopMotor(){
    io.setDutyCycle(0);
  }
}