package frc.robot.subsystems.shooter.hood;

import frc.util.MarinersController.MarinersController.ControllerLocation;
import frc.util.PIDFGains;

public class hoodConstants {
    public static final ControllerLocation CONTROLLER_LOCATION = ControllerLocation.RIO;
    public static final PIDFGains PID = new PIDFGains(0.05, 0, 0);
    public static final int MOTOR_ID = 0;
    public static final int CURRENT_LIMIT = 80;
    public static final int CURRENT_THRESHOLD = 100;
    public static final boolean IS_INVERTED = false;
    public static final double MAX_ANGLE_MOVEMENT_IN_MOTOR_ROTATIONS = 6;
    public static final double POSITION_TOLERANCE_IN_MOTOR_ROTATIONS = 0.1;

    public static final double SYSID_RAMP_VOLTS_PER_SECOND = 0.5;
    public static final double SYSID_STEP_VOLTS = 2;
    public static final double SYSID_TIMEOUT_SECONDS = 4;
    public static final double SYSID_LIMIT_MARGIN_IN_MOTOR_ROTATIONS = 0.5;

}
