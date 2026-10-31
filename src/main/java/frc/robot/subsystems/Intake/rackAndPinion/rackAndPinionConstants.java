package frc.robot.subsystems.Intake.rackAndPinion;

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
}