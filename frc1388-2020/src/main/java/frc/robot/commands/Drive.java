/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.RobotContainer;
import frc.robot.subsystems.DriveTrain;
import frc.robot.subsystems.Rumble;

public class Drive extends CommandBase {

  private boolean m_precisionMode = false;
  private boolean m_lastLeftStickButton = false; 
  private DriveTrain m_subsystem;
  private Rumble m_driveRumble;

  /**
   * Creates a new DriveCommand.
   */
  public Drive( DriveTrain subsystem, Rumble rumble ) {
    m_subsystem = subsystem;
    m_driveRumble = rumble;
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(m_subsystem);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {

    // retrives the nessesary inputs from the Controller
    double leftYAxis = RobotContainer.getDriveLeftYAxis();
    double rightXAxis = RobotContainer.getDriveRightXAxis();
    boolean leftStickButton = RobotContainer.getLeftStickButton();

    // checks to see if the button has been pressed and then flags the precision mode
    if(leftStickButton && !m_lastLeftStickButton) {
      m_precisionMode = !m_precisionMode;
      if(m_precisionMode ){
        m_driveRumble.rumblePulse(Rumble.RumbleSide.RIGHT);
      }else{
        m_driveRumble.rumblePulse(Rumble.RumbleSide.LEFT);
      }
    }
    m_lastLeftStickButton = leftStickButton;
    
    // the deadband is placed in the subsystem
    // adds the curvature differential drive and allows the precision mode to be toggled
    m_subsystem.curvatureDrive( leftYAxis, -rightXAxis, m_precisionMode);

  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
