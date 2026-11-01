package frc.robot.subsystems.shooter.kicker;

import frc.util.MarinersController.MarinersSparkBase;

public class kickerIOReal implements kickerIO {
    private final MarinersSparkBase leadMotor;
    private final MarinersSparkBase followMotor;
    public kickerIOReal() {
        leadMotor = new MarinersSparkBase("Kicker/Back Motor", kickerConstants.CONTROLLER_LOCATION, kickerConstants.LEAD_MOTOR_ID, true, kickerConstants.MOTOR_TYPE);
        leadMotor.setMotorInverted(kickerConstants.LEAD_MOTOR_IS_INVERTED);
        leadMotor.setMotorIdleMode(false);
        leadMotor.setCurrentLimits(kickerConstants.LEAD_MOTOR_CURRENT_LIMIT, kickerConstants.LEAD_MOTOR_CURRENT_THRESHOLD);

        // the follower's direction comes from FOLLOW_MOTOR_INVERTED_FROM_LEAD, so it is never commanded directly
        followMotor = new MarinersSparkBase("Kicker/Front Motor", kickerConstants.CONTROLLER_LOCATION, kickerConstants.FOLLOW_MOTOR_ID, true, kickerConstants.MOTOR_TYPE);
        followMotor.setMotorIdleMode(false);
        followMotor.setCurrentLimits(kickerConstants.LEAD_MOTOR_CURRENT_LIMIT, kickerConstants.LEAD_MOTOR_CURRENT_THRESHOLD);
        followMotor.setMotorAsFollower(leadMotor, kickerConstants.FOLLOW_MOTOR_INVERTED_FROM_LEAD);
    }

    @Override
    public void setDutyCycle(double dutyCycle){
        leadMotor.setDutyCycle(dutyCycle);
    }

    @Override
    public void update(kickerIOInputs inputs) {
        inputs.velocity = leadMotor.getVelocity();
    }
}
