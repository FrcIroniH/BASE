package frc.robot.subsystems.Intake.transfer;

import frc.util.MarinersController.MarinersController.ControllerLocation;
import frc.util.MarinersController.MarinersSparkBase.MotorType;

public class transferConstants {
    public static final MotorType MOTOR_TYPE = MotorType.SPARK_FLEX;
    public static final ControllerLocation CONTROLLER_LOCATION = ControllerLocation.MOTOR;
    public static final int MOTOR_ID = 0;
    public static final boolean isBrushless = true;
    public static final double DUTY_CYCLE = 0.8;
    public static final boolean isInverted = false;
    public static final int CURRENT_LIMIT = 100;
    public static final int CURRENT_THRESHOLD = 80;
    public static final ControllerLocation FUNNEL_CONTROLLER_LOCATION = ControllerLocation.MOTOR;
    public final static int FUNNEL_MOTOR_ID = 0;
    public static final Boolean isFunnelInverted = false;
    public static final int funnel_CURRENT_LIMIT = 100;
    public static final int funnel_CURRENT_THRESHOLD = 80;
}
