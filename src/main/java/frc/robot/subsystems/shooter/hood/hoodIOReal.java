package frc.robot.subsystems.shooter.hood;

import frc.util.MarinersController.MarinersTalonFX;
import frc.util.MarinersController.MarinersController.ControlMode;
import edu.wpi.first.math.geometry.Pose2d;

import java.util.function.Supplier;

import edu.wpi.first.math.trajectory.TrapezoidProfile.Constraints;

public class hoodIOReal implements hoodIO {
    private final MarinersTalonFX hoodMotor;
    private final Supplier<Double> distanceSupplier;
    public hoodIOReal(Supplier<Pose2d> poseSupplier) {
        distanceSupplier = hoodConstants.distanceToHub(poseSupplier);
        hoodMotor = new MarinersTalonFX("hoodMotor", hoodConstants.CONTROLLER_LOCATION, hoodConstants.motorID);
        hoodMotor.setCurrentLimits(hoodConstants.currentLimit, hoodConstants.currentThreshhold);
        hoodMotor.setProfile(new Constraints(hoodConstants.maxVelocity, hoodConstants.maxAcceleration));
        hoodMotor.setMotorInverted(hoodConstants.isInverted);
    }
    public void resetAngleEncoder() {
        hoodMotor.resetMotorEncoder();
    }
    public void openToPposition(double openPositon) {
        hoodMotor.setReference(openPositon, ControlMode.Position);
    }
    public void update(hoodIOInputs inputs) {
        inputs.hoodOpen = hoodMotor.getPosition();
        inputs.distanceToHub = distanceSupplier.get();
    }
}