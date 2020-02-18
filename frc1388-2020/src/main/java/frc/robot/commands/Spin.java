/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.ColorSpinner;

public class Spin extends CommandBase {
  private final ColorSpinner m_colorSpinner;
  private Direction m_direction;
  private final double m_spinnerSpeed = 0.5;  // TODO: Determine the desired spinner speed

  public enum Direction {
    kLeft,
    kRight
  }
  
  /**
   * Creates a new Spin.
   */
  public Spin(ColorSpinner colorSpinner, Direction direction) {
    m_colorSpinner = colorSpinner;
    m_direction = direction;
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(m_colorSpinner);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    if (m_direction == Direction.kLeft) {
      m_colorSpinner.spinMotor(-m_spinnerSpeed);
    } else if (m_direction == Direction.kRight) {
      m_colorSpinner.spinMotor(m_spinnerSpeed);
    }
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    m_colorSpinner.spinMotor(0);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
