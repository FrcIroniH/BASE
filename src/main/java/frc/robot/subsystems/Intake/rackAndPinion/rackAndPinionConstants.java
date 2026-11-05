package frc.robot.subsystems.Intake.rackAndPinion;

import edu.wpi.first.math.filter.Debouncer;
import edu.wpi.first.math.filter.Debouncer.DebounceType;
import frc.util.MarinersController.MarinersTalonFX;
import frc.util.MarinersController.MarinersController.ControllerLocation;
public class rackAndPinionConstants {
    public static final ControllerLocation CONTROLLER_LOCATION = ControllerLocation.MOTOR;
    public static final int motorID = 0;
    public static final int currentLimit = 100;
    public static final int currentThreshhold = 80;
    public static final double MAX_PINION_POWER = 1;
    public static final double pinionRotation = 6;
    public static final double STALL_CURRENT = 60;
    public static final double STALL_VELOCITY = 0.05;
    public static final double STALL_TIME = 0.15;
    public static boolean isExtended = false;


    private static final Debouncer stallCurrentDebouncer = new Debouncer(STALL_TIME, DebounceType.kRising);
    private static final Debouncer stallEnchoderDebouncer = new Debouncer(STALL_TIME, DebounceType.kRising);

    public static double getCurrent(MarinersTalonFX motor) {
        return Math.abs(motor.getMotor().getStatorCurrent().getValueAsDouble());
    }
    public static double getEnchoder(MarinersTalonFX motor) {
        return Math.abs(motor.getPosition)
    }
    public static boolean highCurrent(double current) {
        boolean highCurrent = current > STALL_CURRENT;
        return highCurrent;

    }
    public static boolean isCurrentStalled(double Current) {
        return stallCurrentDebouncer.calculate(highCurrent(Current));
    }
    public static boolean isEnchoderStalled(double enchoderReading) {

    }
}