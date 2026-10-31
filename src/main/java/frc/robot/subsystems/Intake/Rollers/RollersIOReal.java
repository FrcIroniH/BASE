// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.Intake.Rollers;

import edu.wpi.first.math.trajectory.TrapezoidProfile;
import frc.util.MarinersController.MarinersTalonFX;
import edu.wpi.first.math.trajectory.TrapezoidProfile.Constraints;
import edu.wpi.first.units.Measure;

/** Add your docs here. */
public class RollersIOReal implements RollersIO {
    private final MarinersTalonFX FirstMotor;
    private final MarinersTalonFX SecendMotor;

    public RollersIOReal(){
        FirstMotor = new MarinersTalonFX("Intake/First Rollers Motor", RollerConstants.CONTROLLER_LOCATION,
                                    RollerConstants.MOTOR_ID);

        FirstMotor.setCurrentLimits(RollerConstants.CURRENT_LIMIT, RollerConstants.CURRENT_THRESHOLD);
        FirstMotor.setMotorInverted(RollerConstants.IS_INVERTED);
        FirstMotor.setProfile(new Constraints(RollerConstants.maxVelocity, RollerConstants.acceleration));
        FirstMotor.getMeasurements().setGearRatio(RollerConstants.GEAR_RATIO);

        SecendMotor = new MarinersTalonFX("Intake/Secend Rollers Motor", RollerConstants.CONTROLLER_LOCATION,
                                    RollerConstants.SEC_MOTOR_ID);

        SecendMotor.setCurrentLimits(RollerConstants.CURRENT_LIMIT, RollerConstants.CURRENT_THRESHOLD);
        SecendMotor.setMotorInverted(RollerConstants.IS_SEC_INVERTED);
        SecendMotor.setProfile(new Constraints(RollerConstants.maxVelocity, RollerConstants.acceleration));
        SecendMotor.getMeasurements().setGearRatio(RollerConstants.GEAR_RATIO);

    }


        public void setDutyCycle(double dutyCycle){
            FirstMotor.setDutyCycle(dutyCycle);
            SecendMotor.setDutyCycle(dutyCycle);
        }
    }
