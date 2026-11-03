// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.manoa;

import edu.wpi.first.wpilibj2.command.Command;

/* You should consider using the more terse Command factories API instead https://docs.wpilib.org/en/stable/docs/software/commandbased/organizing-command-based.html#defining-commands */
public class manoaCommand extends Command {
  int time = 0;
  public manoasubsystem Manoa = new manoasubsystem(); 
  
  /** Creates a new manoaCommand. */
  public manoaCommand(manoasubsystem manoa) {
    this.Manoa = manoa;
    addRequirements(manoa);
    // Use addRequirements() here to declare subsystem dependencies.
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    Manoa.setFunnelDutyCycle(manoaconstants.manoaDutyCycle);
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {}

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    Manoa.setFunnelDutyCycle(0);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    time ++;
    return time>=250;
  }
}
