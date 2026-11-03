// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.Intake.Rollers;

import frc.util.MarinersController.MarinersTalonFX;

import static edu.wpi.first.units.Units.Second;

import edu.wpi.first.math.trajectory.TrapezoidProfile.Constraints;

/** Add your docs here. */
public class RollersIOReal implements RollersIO {
    private final MarinersTalonFX FirstMotor;
    private final MarinersTalonFX SecondMotor;

    public RollersIOReal(){
        FirstMotor = new MarinersTalonFX("Intake/First Rollers Motor", RollerConstants.CONTROLLER_LOCATION,
                                    RollerConstants.MOTOR_ID);

        FirstMotor.setCurrentLimits(RollerConstants.CURRENT_LIMIT, RollerConstants.CURRENT_THRESHOLD);
        FirstMotor.setMotorInverted(RollerConstants.IS_INVERTED);
        FirstMotor.setProfile(new Constraints(RollerConstants.maxVelocity, RollerConstants.acceleration));
        FirstMotor.getMeasurements().setGearRatio(RollerConstants.GEAR_RATIO);
        FirstMotor.setMotorIdleMode(false);

        SecondMotor = new MarinersTalonFX("Intake/Secend Rollers Motor", RollerConstants.CONTROLLER_LOCATION,
                                    RollerConstants.SEC_MOTOR_ID);

        SecondMotor.setCurrentLimits(RollerConstants.CURRENT_LIMIT, RollerConstants.CURRENT_THRESHOLD);
        SecondMotor.setMotorInverted(RollerConstants.IS_SEC_INVERTED);
        SecondMotor.setProfile(new Constraints(RollerConstants.maxVelocity, RollerConstants.acceleration));
        SecondMotor.getMeasurements().setGearRatio(RollerConstants.GEAR_RATIO);
        SecondMotor.setMotorIdleMode(false);

    }


        public void setDutyCycle(double dutyCycle){
            FirstMotor.setDutyCycle(dutyCycle);
            SecondMotor.setDutyCycle(dutyCycle);
        }
    }
