package frc.robot.subsystems.Intake.Rollers;

import frc.util.MarinersController.MarinersController.ControllerLocation;

/** Add your docs here. */
public class RollerConstants {
    public static final ControllerLocation CONTROLLER_LOCATION = ControllerLocation.MOTOR;
    public static final int MOTOR_ID = 8;
    public static final int SEC_MOTOR_ID = 24;
    public static final boolean IS_INVERTED = false;
    public static final boolean IS_SEC_INVERTED = false; // we need to check it but for now its inverted
    public static final double GEAR_RATIO = 3;
    public static final double DUTY_CYCLE = 0.3;

    public static final int CURRENT_LIMIT = 80;
    public static final int CURRENT_THRESHOLD = 100;
    public static final double maxVelocity = 5;
    public static final double acceleration = 2;

}
