package frc.robot;

import java.util.ArrayList;

import edu.wpi.first.math.Matrix;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Transform3d;

public class Camera {
    private String Name;
    private Transform3d Location;

    public record VisionFrame(
            Pose2d pose,
            double timestamp,
            Matrix<N3, N1> stdDev) {
    }

    public Camera(String name, Transform3d location) {
        Name = name;
        Location = location;
    }

    public ArrayList<VisionFrame> getPose() {
        return null;
    }
}
