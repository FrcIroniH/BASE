package frc.robot.subsystems.shooter.flywheel;

import java.util.function.Supplier;

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
    public static final double GEAR_RATIO = 3;
    public static final int CURRENT_LIMIT = 80;
    public static final int CURRENT_THRESHOLD = 100;
    public static final double KS = 0.1; 
    public static final double KV = 0.36;  
    public static final PIDFGains PID = new PIDFGains(0.05, 0, 0, KV);
    

    public static final InterpolatingDoubleTreeMap DISTANCE_TO_RPM = new InterpolatingDoubleTreeMap();
    static {
        DISTANCE_TO_RPM.put(1.8, 2500.0);
        DISTANCE_TO_RPM.put(2.3, 2630.0);
        DISTANCE_TO_RPM.put(2.8, 2790.0);
        DISTANCE_TO_RPM.put(3.3, 2990.0);
        DISTANCE_TO_RPM.put(3.8, 3240.0);
        DISTANCE_TO_RPM.put(4.3, 3500.0);
        DISTANCE_TO_RPM.put(4.8,  3630.0);
        DISTANCE_TO_RPM.put(5.3, 3700.0);
        DISTANCE_TO_RPM.put(5.8, 3870.0);
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

    public static Supplier<Double> distanceToHub(Supplier<Pose2d> poseSupplier) {
        return () -> {
            Pose2d robotPose = poseSupplier.get();
            Translation2d hub = getHubCenter();
            double dx = hub.getX() - robotPose.getX();
            double dy = hub.getY() - robotPose.getY();
            return Math.sqrt(dx * dx + dy * dy);
        };
    }
}

