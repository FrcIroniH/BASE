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
    public static final double STALL_POSITION_CHANGE = 0.002; 
    public static final double STALL_TIME = 0.15;
    public static boolean isExtended = false;
    public static final double ShootTime = 5;

    private static final Debouncer stallCurrentDebouncer = new Debouncer(STALL_TIME, DebounceType.kRising);
    private static final Debouncer stallEnchoderDebouncer = new Debouncer(STALL_TIME, DebounceType.kRising);

    private static double lastEnchoderPosition = 0;

    enum rackAndPinionState {
        OPEN, 
        HALF,
        CLOSE
    }
    public static double getCurrent(MarinersTalonFX motor) {
        return Math.abs(motor.getMotor().getStatorCurrent().getValueAsDouble());
    }

    public static double getEnchoder(MarinersTalonFX motor) {
        return motor.getPosition();
    }

    public static boolean highCurrent(double current) {
        return current > STALL_CURRENT;
    }

    public static boolean isCurrentStalled(double current) {
        return stallCurrentDebouncer.calculate(highCurrent(current));
    }

    public static boolean isEnchoderStalled(double enchoderPosition) {
        double change = Math.abs(enchoderPosition - lastEnchoderPosition);
        lastEnchoderPosition = enchoderPosition;
        return stallEnchoderDebouncer.calculate(change < STALL_POSITION_CHANGE);
    }
}