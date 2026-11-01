package frc.robot.subsystems.shooter.hood;

import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.math.geometry.Translation2d;
import frc.util.MarinersController.MarinersController.ControllerLocation;
import java.util.function.Supplier;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.interpolation.InterpolatingDoubleTreeMap;
import edu.wpi.first.wpilibj.DriverStation;
import frc.util.PIDFGains;

public class hoodConstants {
    public static final ControllerLocation CONTROLLER_LOCATION = ControllerLocation.RIO;
    public static final double KS = 0.1; 
    public static final double KV = 0.36;
    public static final PIDFGains PID = new PIDFGains(0.05, 0, 0, KV);
    public static final int motorID = 0;
    public static final int currentLimit = 100;
    public static final int currentThreshhold = 80;
    public static final boolean isInverted = false;
    public static final double maxVelocity = 0;
    public static final double maxAcceleration = 0;
    public static final boolean IS_INVERTED = false;
    public static final boolean IS_FOLLOWER_INVERTED_FROM_LEAD = true;
    public static final double MAX_ANGLE_MOVEMENT_IN_MOTOR_ROTATIONS = 6;
    public static final Translation2d ROBOT_TO_SHOOTER = new Translation2d(0.0, 0.0);
    public static final InterpolatingDoubleTreeMap DISTANCE_TO_ANGLE_PRECENT = new InterpolatingDoubleTreeMap();
    static {
        DISTANCE_TO_ANGLE_PRECENT.put(1.0, 0.0);
        DISTANCE_TO_ANGLE_PRECENT.put(5.8, 1.0);
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