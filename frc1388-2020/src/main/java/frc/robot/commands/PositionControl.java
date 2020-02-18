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

public class PositionControl extends CommandBase {
  private final ColorSpinner m_colorSpinner;
  private ColorWheel m_desiredColor = ColorWheel.UNKNOWN;
  private final double m_spinSpeed = 0.5;
  private final double m_spinnerArmDown = -0.1;

  /**
   * Creates a new PositionControl.
   */
  public PositionControl( ColorSpinner colorSpinner) {
    m_colorSpinner = colorSpinner;
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(m_colorSpinner);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    m_desiredColor = ColorWheel.fromGameMessage();
    USBLogging.info("Desired Color is " + m_desiredColor.getName());
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    // Operate the Spinner Wheel
    m_colorSpinner.spinMotor(m_spinSpeed);   
    // Make sure the Wheel on the color panel
    m_colorSpinner.setArmMotor(m_spinnerArmDown);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    m_colorSpinner.spinMotor(0);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return m_desiredColor == ColorWheel.UNKNOWN  || 
            m_desiredColor == m_colorSpinner.checkColor();
  }
}
