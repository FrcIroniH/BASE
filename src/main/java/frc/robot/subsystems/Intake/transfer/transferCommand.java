// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.Intake.transfer;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.shooter.flywheel.flyWheelConstants;

/* You should consider using the more terse Command factories API instead https://docs.wpilib.org/en/stable/docs/software/commandbased/organizing-command-based.html#defining-commands */
public class transferCommand extends Command {
  public static transferSubsystem Transfer;
  /** Creates a new transferCommand. */
  public transferCommand(transferSubsystem transfer) {
    Transfer = transfer;
    addRequirements(transfer);

    // Use addRequirements() here to declare subsystem dependencies.
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {}

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    Transfer.setDutyCycle(transferConstants.DUTY_CYCLE);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    Transfer.stop();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
