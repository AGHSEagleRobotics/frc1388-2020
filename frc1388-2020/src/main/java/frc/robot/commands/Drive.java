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

public class Drive extends CommandBase {

  private boolean precisionMode = false;
  private boolean lastAButton = false;
  private DriveTrain m_subsystem;
  
  /**
   * Creates a new DriveCommand.
   */
  public Drive( DriveTrain subsystem ) {
    m_subsystem = subsystem;
    addRequirements(m_subsystem);
    // Use addRequirements() here to declare subsystem dependencies.
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    double leftYAxis = RobotContainer.getDriveLeftYAxis();
    double rightXAxis = RobotContainer.getDriveRightXAxis();
    boolean AButton = RobotContainer.getAButton();

    if(AButton && !lastAButton) {
      precisionMode = !precisionMode;
    }
    lastAButton = AButton;

    leftYAxis *= 0.7;
    rightXAxis *= -0.7;

    m_subsystem.curvatureDrive( leftYAxis, rightXAxis, precisionMode);
    
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
