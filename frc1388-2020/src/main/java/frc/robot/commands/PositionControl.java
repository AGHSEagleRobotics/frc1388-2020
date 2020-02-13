/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.USBLogging;
import frc.robot.subsystems.ColorSpinner;
import frc.robot.subsystems.ColorSpinner.ColorWheel;

public class PositionControl extends CommandBase {
  private final ColorSpinner m_colorSpinner;
  private ColorWheel m_desiredColor = ColorWheel.UNKNOWN;
  private String m_colorString = "";
  /**
   * Creates a new PositionControl.
   */
  public PositionControl( ColorSpinner colorSpinner) {
    // Use addRequirements() here to declare subsystem dependencies.
    m_colorSpinner = colorSpinner;
    addRequirements(m_colorSpinner);
  
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    m_colorString = DriverStation.getInstance().getGameSpecificMessage();
    if(m_colorString.length() > 0){
      switch(m_colorString.charAt(0)){
        case 'B' :
          m_desiredColor = ColorWheel.BLUE;
          USBLogging.info("Color is Blue on FMS");
          break;
        case 'G' :
          m_desiredColor = ColorWheel.GREEN;
          USBLogging.info("Color is Green on FMS");
          break;
        case 'R' :
          m_desiredColor = ColorWheel.RED;
          USBLogging.info("Color is Red on FMS");
          break;
        case 'Y' :
          m_desiredColor = ColorWheel.YELLOW;
          USBLogging.info("Color is Yellow on FMS");
          break;
        default :
          USBLogging.error("FMS gave invalid color");
          break;
      }

    } else {
      USBLogging.warning("No color has currently been selected by FMS");
    }
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    m_colorSpinner.spinMotor(0.1);

  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    if( m_desiredColor == ColorWheel.UNKNOWN ){
      return true;
    }
    return false;
  }
}
