package frc.robot.subsystems.manoa;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class manoasubsystem extends SubsystemBase{

    public final manoaIO io;

    public manoasubsystem() {
        io = new manoaIOReal();
    }
    public void setFunnelDutyCycle(double dutyCycle){
        io.manoaDutyCycle(dutyCycle);
    }




}
