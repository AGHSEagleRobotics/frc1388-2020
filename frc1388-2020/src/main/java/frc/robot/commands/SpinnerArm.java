/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.ColorSpinner;

public class SpinnerArm extends CommandBase {
  private final ColorSpinner m_colorSpinner;
  private final double k_stallAmps = 5.0;
  private final double k_stallTime = 1.0;
  private final Timer m_stallTimer = new Timer();
  private Direction m_direction;
  private final double m_spinnerArmUp = 0.6;
  private final double m_spinnerArmDown = -0.5;

  public enum Direction {
    kUp,
    kDown
  }

  /**
   * Creates a new SpinnerUp.
   */
  public SpinnerArm(ColorSpinner colorSpinner, Direction direction) {
    m_colorSpinner = colorSpinner;
    m_direction = direction;
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(m_colorSpinner);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    if (m_direction == Direction.kUp) {
      m_colorSpinner.setArmMotor(m_spinnerArmUp);
    } else if (m_direction == Direction.kDown) {
      m_colorSpinner.setArmMotor(m_spinnerArmDown);
    }
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    double spinnerArmCurrent = Math.abs(m_colorSpinner.getSpinnerArmCurrent());
    if (spinnerArmCurrent > k_stallAmps && m_stallTimer.get() == 0) {
      m_stallTimer.start();
    } else if (spinnerArmCurrent < k_stallAmps) {
      m_stallTimer.stop();
      m_stallTimer.reset();
    }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    m_colorSpinner.stopArm();

    m_stallTimer.stop();
    m_stallTimer.reset();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return m_stallTimer.hasPeriodPassed(k_stallTime);
  }
}
