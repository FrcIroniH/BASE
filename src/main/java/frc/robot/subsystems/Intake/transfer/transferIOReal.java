package frc.robot.subsystems.Intake.transfer;

import frc.util.MarinersController.MarinersSparkBase;
import frc.util.MarinersController.MarinersTalonFX;

public class transferIOReal implements transferIO {
    private final MarinersSparkBase transferMotor;
    private final MarinersTalonFX funnelMotor;
    public transferIOReal() {
        transferMotor = new MarinersSparkBase("transferMotor", transferConstants.CONTROLLER_LOCATION, transferConstants.MOTOR_ID, transferConstants.isBrushless, transferConstants.MOTOR_TYPE);
        transferMotor.setMotorInverted(transferConstants.isInverted);
        transferMotor.setMotorIdleMode(false);
        transferMotor.setCurrentLimits(transferConstants.CURRENT_LIMIT, transferConstants.CURRENT_THRESHOLD);

        funnelMotor = new MarinersTalonFX("funnelMotor", transferConstants.FUNNEL_CONTROLLER_LOCATION, transferConstants.FUNNEL_MOTOR_ID);
        funnelMotor.setMotorInverted(transferConstants.isFunnelInverted);
        funnelMotor.setMotorIdleMode(false);
        funnelMotor.setCurrentLimits(transferConstants.funnel_CURRENT_LIMIT, transferConstants.funnel_CURRENT_THRESHOLD);
    }
    @Override
    public void setDutyCycle(double dutyCycle) {
        transferMotor.setDutyCycle(dutyCycle);
        funnelMotor.setDutyCycle(dutyCycle);
    }
}