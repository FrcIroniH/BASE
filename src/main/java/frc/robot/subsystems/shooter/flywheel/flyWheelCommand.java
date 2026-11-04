// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.shooter.flywheel;

import java.util.function.DoubleSupplier;

import edu.wpi.first.wpilibj2.command.Command;

/**
 * Spins the flywheel at whatever RPM the supplier gives, read EVERY loop. Stops when the command ends.
 * Tuning:  new flyWheelCommand(flywheel, () -> tuningRPM)
 * Match:   new flyWheelCommand(flywheel, () -> flyWheelConstants.getRPM(distanceToHub))
 */
public class flyWheelCommand extends Command {
  private final flyWheelsubsystem flywheel;
  private final DoubleSupplier rpm;

  public flyWheelCommand(flyWheelsubsystem flywheel, DoubleSupplier rpm) {
    this.flywheel = flywheel;
    this.rpm = rpm;
    addRequirements(flywheel);
  }

  @Override
  public void execute() {
    flywheel.setRPM(rpm.getAsDouble());
  }

  @Override
  public void end(boolean interrupted) {
    flywheel.stop();
  }

  @Override
  public boolean isFinished() {
    return false;
  }
}
