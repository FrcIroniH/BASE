package frc.robot.subsystems.shooter.kicker;

import static edu.wpi.first.units.Units.Meters;
import edu.wpi.first.math.interpolation.InterpolatingDoubleTreeMap;
import edu.wpi.first.units.measure.Distance;
import frc.util.MarinersController.MarinersController.ControllerLocation;
import frc.util.MarinersController.MarinersSparkBase.MotorType;

public class kickerConstants {
    public static final ControllerLocation CONTROLLER_LOCATION = ControllerLocation.MOTOR;
    public static final MotorType MOTOR_TYPE = MotorType.SPARK_FLEX;
    public static final double GEAR_RATIO = 1;
    public static final int LEAD_MOTOR_CURRENT_LIMIT = 35;
    public static final int LEAD_MOTOR_CURRENT_THRESHOLD = 60;
    public static final int LEAD_MOTOR_ID = 16;
    public static final int FOLLOW_MOTOR_ID = 14;
    public static final boolean LEAD_MOTOR_IS_INVERTED = true;
    public static final boolean FOLLOW_MOTOR_INVERTED_FROM_LEAD = false;
    public static final double KICKER_EJECT_SPEED = -0.8;
}