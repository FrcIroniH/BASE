package frc.robot.subsystems.shooter.flywheel;

import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.interpolation.InterpolatingDoubleTreeMap;
import edu.wpi.first.math.util.Units;
import frc.robot.Robot;
import frc.util.PIDFGains;
import frc.util.MarinersController.MarinersController.ControllerLocation;

public class flyWheelConstants {
    public static final ControllerLocation CONTROLLER_LOCATION = ControllerLocation.RIO;
    public static final Translation2d ROBOT_TO_SHOOTER = new Translation2d(0.0, 0.0);
    public static final int MOTOR_ID = 8;
    public static final int SEC_MOTOR_ID = 24;
    public static final boolean IS_INVERTED = false;
    public static final boolean IS_FOLLOWER_INVERTED_FROM_LEAD = true; 

    public static final double GEAR_RATIO = 3;
    public static final int CURRENT_LIMIT = 80;
    public static final int CURRENT_THRESHOLD = 100;
    public static final double KS = 0.1; 
    public static final double KV = 0.36;  
    public static final PIDFGains PID = new PIDFGains(0.05, 0, 0, KV);
    public static final double RPM_TOLERANCE = 100;
}