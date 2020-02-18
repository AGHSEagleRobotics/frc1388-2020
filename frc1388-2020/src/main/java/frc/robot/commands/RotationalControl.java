/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.USBLogging;
import frc.robot.subsystems.ColorSpinner;
import frc.robot.subsystems.ColorSpinner.ColorWheel;

public class RotationalControl extends CommandBase {

  private final static int ROTATION_STOP_COUNT = 28;
  private final double m_spinSpeed = 0.5;
  private int m_changeColorCounter = 0;
  private ColorWheel m_prevColor = ColorWheel.UNKNOWN;
  private final ColorSpinner m_colorSpinner;
  private final double m_spinnerArmDown = -0.1;
  /**
   * Creates a new RotationalControl.
   */

  public RotationalControl( ColorSpinner colorSpinner ) {
    m_colorSpinner = colorSpinner;
    addRequirements( m_colorSpinner );
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    m_colorSpinner.spinMotor(m_spinSpeed);
    m_colorSpinner.setArmMotor(m_spinnerArmDown);
    ColorWheel curColor = m_colorSpinner.checkColor();

    if (curColor != m_prevColor && curColor != ColorWheel.UNKNOWN) {
      // Don't increase counter until the color is known.
      if (m_prevColor != ColorWheel.UNKNOWN) {
        m_changeColorCounter++;
      }
      m_prevColor = curColor;
      USBLogging.info("Color Counter: " + m_changeColorCounter + "\n" + curColor.getName());
    }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    resetColorCounter();
    m_colorSpinner.spinMotor(0);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return (m_changeColorCounter >= ROTATION_STOP_COUNT);
  }

  
  private void resetColorCounter(){
    m_changeColorCounter = 0;
  }
}
