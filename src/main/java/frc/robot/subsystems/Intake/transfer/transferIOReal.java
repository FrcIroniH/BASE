package frc.robot.subsystems.Intake.transfer;

import frc.util.MarinersController.MarinersSparkBase;

public class transferIOReal implements transferIO {
    private final MarinersSparkBase transferMotor;
    public transferIOReal() {
        transferMotor = new MarinersSparkBase("transferMotor", transferConstants.CONTROLLER_LOCATION, transferConstants.MOTOR_ID, transferConstants.isBrushless, transferConstants.MOTOR_TYPE);
        transferMotor.setMotorInverted(transferConstants.isInverted);
        transferMotor.setMotorIdleMode(false);
        transferMotor.setCurrentLimits(transferConstants.CURRENT_LIMIT, transferConstants.CURRENT_THRESHOLD);
    }
    @Override
    public void setDutyCycle(double dutyCycle) {
        transferMotor.setDutyCycle(dutyCycle);
    }
}