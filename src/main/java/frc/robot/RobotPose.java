package frc.robot;

import static edu.wpi.first.units.Units.Amp;

import java.util.ArrayList;
import java.util.function.Supplier;

import edu.wpi.first.math.estimator.PoseEstimator;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.kinematics.SwerveModulePosition;

public class RobotPose {

    private static RobotPose RobotPose;
    private ArrayList<Camera> Cameras;
    private Supplier<SwerveModulePosition[]> SwervePositions;
    private PoseEstimator Estimator;

    private RobotPose(ArrayList<Camera> cameras, Supplier<SwerveModulePosition[]> swervePositions){
        Cameras = cameras;
        SwervePositions = swervePositions;
        Estimator = new PoseEstimator<>(null, null, null, null)
    }

    public void Init(ArrayList<Camera> cameras, Supplier<SwerveModulePosition[]> swervePositions){
        if(RobotPose != null) return;
        RobotPose = new RobotPose(cameras, swervePositions);
    }

    public RobotPose getInstance(){
        return RobotPose;
    }

    public Pose2d getPose(){
        return null;
    }


}
