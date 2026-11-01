package frc.robot.subsystems.shooter.hood;

import frc.util.MarinersController.MarinersTalonFX;
import frc.util.MarinersController.MarinersController.ControlMode;

public class hoodIOReal implements hoodIO {
    private final MarinersTalonFX hoodMotor;
    public hoodIOReal() {
        hoodMotor = new MarinersTalonFX("Shooter/Hood Motor", hoodConstants.CONTROLLER_LOCATION, hoodConstants.MOTOR_ID);
        hoodMotor.setCurrentLimits(hoodConstants.CURRENT_LIMIT, hoodConstants.CURRENT_THRESHOLD);
        hoodMotor.setMotorInverted(hoodConstants.IS_INVERTED);
        hoodMotor.setMotorIdleMode(true);
        hoodMotor.setPIDF(hoodConstants.PID);
        hoodMotor.enableSoftLimits(0, hoodConstants.MAX_ANGLE_MOVEMENT_IN_MOTOR_ROTATIONS);
    }
    @Override
    public void resetAngleEncoder() {
        hoodMotor.resetMotorEncoder();
    }
    @Override
    public void openToPosition(double openPosition) {
        hoodMotor.setReference(openPosition, ControlMode.Position);
    }
    @Override
    public void update(hoodIOInputs inputs) {
        inputs.hoodOpen = hoodMotor.getPosition();
        inputs.hoodVelocity = hoodMotor.getVelocity();
    }
    @Override
    public void setVoltage(double volts) {
        hoodMotor.setVoltage(volts);
    }
}
