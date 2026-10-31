package frc.robot.subsystems.shooter.kicker;

import frc.util.MarinersController.MarinersSparkBase;

public class kickerIOReal implements kickerIO {
    private static MarinersSparkBase leadMotor;
    private static MarinersSparkBase followMotor;
    public kickerIOReal() {
        leadMotor = new MarinersSparkBase("Kicker/Back Motor", kickerConstants.CONTROLLER_LOCATION, kickerConstants.LEAD_MOTOR_ID, true, kickerConstants.MOTOR_TYPE);
        leadMotor.setMotorInverted(kickerConstants.LEAD_MOTOR_IS_INVERTED);
        leadMotor.setMotorIdleMode(false);
        leadMotor.setCurrentLimits(kickerConstants.LEAD_MOTOR_CURRENT_LIMIT, kickerConstants.LEAD_MOTOR_CURRENT_THRESHOLD);

        followMotor = new MarinersSparkBase("Kicker/Front Motor", kickerConstants.CONTROLLER_LOCATION, kickerConstants.FOLLOW_MOTOR_ID, true, kickerConstants.MOTOR_TYPE);
        followMotor.setMotorInverted(kickerConstants.LEAD_MOTOR_IS_INVERTED);
        followMotor.setMotorIdleMode(false);
        followMotor.setMotorAsFollower(leadMotor, kickerConstants.FOLLOW_MOTOR_INVERTED_FROM_LEAD);
    }

    public void setDutyCycle(double dutyCycle){
        leadMotor.setDutyCycle(dutyCycle);
        followMotor.setDutyCycle(dutyCycle);
    }
}