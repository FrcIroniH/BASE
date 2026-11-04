// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.Drive;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.button.CommandPS5Controller;
import frc.robot.Robot;
import frc.robot.subsystems.DriveTrain.DriveBase;
import frc.robot.subsystems.DriveTrain.DriveBaseConstants;
import frc.robot.subsystems.DriveTrain.SwerveModules.DevBotConstants;

import java.util.function.Supplier;

/* You should consider using the more terse Command factories API instead https://docs.wpilib.org/en/stable/docs/software/commandbased/organizing-command-based.html#defining-commands */
public class AimDriving extends Command {
    private final DriveBase driveBase;
    private final CommandPS5Controller controller;
    private static double MAX_FREE_WHEEL_SPEED;

    Supplier<Double> angleSupplier;
    PIDController thetaController = DriveBaseConstants.PathPlanner.THETA_PID.createPIDController();

    public AimDriving(DriveBase driveBase, CommandPS5Controller controller, Supplier<Double> angleSupplier) {
        this.driveBase = driveBase;
        this.controller = controller;
        this.angleSupplier = angleSupplier;
        addRequirements(this.driveBase);
        setName("AimDriving");

        thetaController.enableContinuousInput(-Math.PI, Math.PI);

        MAX_FREE_WHEEL_SPEED = DevBotConstants.MAX_WHEEL_LINEAR_VELOCITY;
    }

    @Override
    public void initialize() {
        driveBase.drive(new ChassisSpeeds());
        thetaController.reset();
    }

    public static double deadBand(double value) {
        return Math.abs(value) > 0.1 ? value : 0;
    }

    @Override
    public void execute() {
        thetaController.setSetpoint(angleSupplier.get());

        double R2Axis  = 1 - controller.getR2Axis();

        if(R2Axis <= 0.1) R2Axis = 0.1;

        //sets the value of the 3 vectors we need (accounting for drift)
        double leftX = -deadBand(controller.getLeftY());
        double leftY = -deadBand(controller.getLeftX());

        leftX *= R2Axis * MAX_FREE_WHEEL_SPEED;
        leftY *= R2Axis * MAX_FREE_WHEEL_SPEED;

        ChassisSpeeds fieldRelativeSpeeds = new ChassisSpeeds(leftX, leftY, thetaController.calculate(driveBase.getPose().getRotation().getRadians()));

        Rotation2d gyroAngle = driveBase.getRotation2d();
        if(Robot.isRedAlliance) gyroAngle = gyroAngle.plus(Rotation2d.fromDegrees(180));

        ChassisSpeeds robotRelativeSpeeds = ChassisSpeeds.fromFieldRelativeSpeeds(fieldRelativeSpeeds, gyroAngle);

        //drives the robot with the values
        driveBase.drive(robotRelativeSpeeds);
    }

    @Override
    public void end(boolean interrupted){
        driveBase.drive(new ChassisSpeeds());
    }
}
