package frc.robot.subsystems.Shooter;

import java.util.function.Supplier;

import edu.wpi.first.math.geometry.Pose2d;
import frc.util.MarinersController.MarinersTalonFX;
import frc.util.MarinersController.MarinersController.ControlMode;

public class flyWheelIOReal implements flyWheelIO {
    private final MarinersTalonFX leadFlyWheel;
    private final MarinersTalonFX followFlyWheel;
    private final Supplier<Double> distanceSupplier;

    public flyWheelIOReal(Supplier<Pose2d> poseSupplier) {
        distanceSupplier = flyWheelConstants.distanceToHub(poseSupplier);
        leadFlyWheel = new MarinersTalonFX("Shooter/Lead flywheel Motor", flyWheelConstants.CONTROLLER_LOCATION, flyWheelConstants.MOTOR_ID);
        leadFlyWheel.setCurrentLimits(flyWheelConstants.CURRENT_LIMIT, flyWheelConstants.CURRENT_THRESHOLD);
        leadFlyWheel.setMotorInverted(flyWheelConstants.IS_INVERTED);
        leadFlyWheel.getMeasurements().setGearRatio(flyWheelConstants.GEAR_RATIO);
        leadFlyWheel.setPIDF(flyWheelConstants.PID);
        leadFlyWheel.setStaticFeedForward(flyWheelConstants.KS);
        followFlyWheel = new MarinersTalonFX("Shooter/Follow flywheel Motor", flyWheelConstants.CONTROLLER_LOCATION, flyWheelConstants.SEC_MOTOR_ID);
        followFlyWheel.setCurrentLimits(flyWheelConstants.CURRENT_LIMIT, flyWheelConstants.CURRENT_THRESHOLD);
        followFlyWheel.setMotorAsFollower(leadFlyWheel, flyWheelConstants.IS_FOLLOWER_INVERTED_FROM_LEAD);
        followFlyWheel.getMeasurements().setGearRatio(flyWheelConstants.GEAR_RATIO);
        followFlyWheel.setPIDF(flyWheelConstants.PID);
        followFlyWheel.setStaticFeedForward(flyWheelConstants.KS);
    }

    @Override
    public void setRPM(double RPM) {
        leadFlyWheel.setReference(RPM / 60.0, ControlMode.Velocity);
    }

    @Override
    public void update(flyWheelInputs inputs) {
        inputs.currentRPM = leadFlyWheel.getVelocity() * 60.0;
        inputs.distanceToHub = distanceSupplier.get();
    }
    @Override
    public void setVoltage(double volts) {
        leadFlyWheel.setVoltage(volts);
    }
}

