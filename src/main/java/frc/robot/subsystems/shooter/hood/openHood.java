// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.shooter.hood;

import java.util.function.DoubleSupplier;
import edu.wpi.first.wpilibj2.command.Command;

public class openHood extends Command {
  private final hoodSubsystem hood;
  // Store the supplier itself, not the double
  private final DoubleSupplier distanceToHubSupplier; 

  /** Creates a new openHood. */
  public openHood(hoodSubsystem hood, DoubleSupplier distanceToHub) {
    this.hood = hood;
    this.distanceToHubSupplier = distanceToHub; // Assign the supplier
    addRequirements(hood);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {}

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
      // Get the latest double value from the supplier every 20ms loop
      hood.open(distanceToHubSupplier.getAsDouble());
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {}

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}