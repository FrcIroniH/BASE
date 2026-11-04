// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.shooter.hood;

import java.util.function.DoubleSupplier;

import edu.wpi.first.wpilibj2.command.Command;

/**
 * Holds the hood at whatever percent the supplier gives, read EVERY loop.
 * Tuning:  new openHood(hood, () -> tuningHoodPercent)
 * Match:   new openHood(hood, () -> flyWheelConstants.getHoodPercent(distanceToHub))
 */
public class openHood extends Command {
  private final hoodSubsystem hood;
  private final DoubleSupplier openPercent;

  public openHood(hoodSubsystem hood, DoubleSupplier openPercent) {
    this.hood = hood;
    this.openPercent = openPercent;
    addRequirements(hood);
  }

  @Override
  public void execute() {
    hood.open(openPercent.getAsDouble());
  }

  @Override
  public boolean isFinished() {
    return false;
  }
}
