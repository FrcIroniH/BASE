package frc.robot.subsystems.shooter.flywheel;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.interpolation.InterpolatingDoubleTreeMap;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import frc.util.PIDFGains;
import frc.util.MarinersController.MarinersController.ControllerLocation;

public class flyWheelConstants {
    public static final ControllerLocation CONTROLLER_LOCATION = ControllerLocation.RIO;
    public static final Translation2d ROBOT_TO_SHOOTER = new Translation2d(0.0, 0.0);
    public static final int MOTOR_ID = 8;
    public static final int SEC_MOTOR_ID = 24;
    public static final boolean IS_INVERTED = false;
    public static final boolean IS_FOLLOWER_INVERTED_FROM_LEAD = true; 
    // TODO: verify on the robot - the top of SHOT_TABLE needs ~193 motor rps at a 3:1 reduction,
    // which is above a Kraken/Falcon's free speed. Either this is a 1:3 step-up or the table/KV units are off.
    public static final double GEAR_RATIO = 3;
    public static final int CURRENT_LIMIT = 80;
    public static final int CURRENT_THRESHOLD = 100;
    public static final double KS = 0.1; 
    public static final double KV = 0.36;  
    public static final PIDFGains PID = new PIDFGains(0.05, 0, 0, KV);
    public static final double RPM_TOLERANCE = 100;

    // each row is one shot: { distance to hub (m), hood open (0 = closed, 1 = fully open), flywheel RPM }
    // tune the hood and the RPM of a row together, never one column on its own.
    // starting hood values are the old straight line (1.0 m closed -> 5.8 m fully open)
    private static final double[][] SHOT_TABLE = {
        {1.8, 0.167, 2500},
        {2.3, 0.271, 2630},
        {2.8, 0.375, 2790},
        {3.3, 0.479, 2990},
        {3.8, 0.583, 3240},
        {4.3, 0.688, 3500},
        {4.8, 0.792, 3630},
        {5.3, 0.896, 3700},
        {5.8, 1.0,   3870},
    };
    public static final double MIN_TABLE_DISTANCE = 1.8;
    public static final double MAX_TABLE_DISTANCE = 5.8;

    // both maps are filled from the same rows, so the hood and the RPM always stay a matching pair
    private static final InterpolatingDoubleTreeMap DISTANCE_TO_HOOD_PERCENT = new InterpolatingDoubleTreeMap();
    private static final InterpolatingDoubleTreeMap DISTANCE_TO_RPM = new InterpolatingDoubleTreeMap();
    static {
        for (int i = 0; i < SHOT_TABLE.length; i++) {
            DISTANCE_TO_HOOD_PERCENT.put(SHOT_TABLE[i][0], SHOT_TABLE[i][1]);
            DISTANCE_TO_RPM.put(SHOT_TABLE[i][0], SHOT_TABLE[i][2]);
        }
    }

    public static double getHoodPercent(double distanceToHub) {
        return DISTANCE_TO_HOOD_PERCENT.get(distanceToHub);
    }

    public static double getRPM(double distanceToHub) {
        return DISTANCE_TO_RPM.get(distanceToHub);
    }

    public static final Translation2d BLUE_HUB_CENTER = new Translation2d(Units.inchesToMeters(182.107), Units.inchesToMeters(158.844));
    public static final Translation2d RED_HUB_CENTER = new Translation2d(Units.inchesToMeters(469.115), Units.inchesToMeters(158.844));

    public static Translation2d getHubCenter() {
        boolean isRed = DriverStation.getAlliance().orElse(Alliance.Blue) == Alliance.Red;
        if (isRed) {
            return RED_HUB_CENTER;
        } else {
            return BLUE_HUB_CENTER;
        }
    }

    /** distance from the robot center to the alliance hub, shared by the flywheel and the hood */
    public static double distanceToHub(Pose2d robotPose) {
        Translation2d hub = getHubCenter();
        double dx = hub.getX() - robotPose.getX();
        double dy = hub.getY() - robotPose.getY();
        return Math.sqrt(dx * dx + dy * dy);
    }
}
