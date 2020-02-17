/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import frc.robot.subsystems.DriveTrain;
import frc.robot.RobotContainer;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.CommandBase;

public class AutonMove extends CommandBase {
  private final double kDRIVE = 0.5;
  private final double kROTATION = 0.5;

  private final double TIME_PERIOD_MOVE = 3.0;
  
  private DriveTrain m_driveTrain;
  private Timer timer = new Timer();
  private boolean m_hasMovePeriodPass = false;
  private int m_distance = 0;
  private double m_distanceInFeet = 0.0;
  private double m_timeToRun = 0.0;
  private boolean m_timeMode = false;
  private boolean isInFeet = false;
  private double m_rotation = 0.0;
  

  /**
   * Creates a new AutonMove.
   */
  public AutonMove( DriveTrain driveTrain) {
    m_driveTrain = driveTrain;

    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements( m_driveTrain);
    // m_targetPois = new Pose2d(10, 10, m_driveTrain.getAngle());
    
  }


  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    timer.start();
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {

    if( m_timeMode ){
      if(timer.hasPeriodPassed(m_timeToRun) && !m_hasMovePeriodPass)
        m_driveTrain.curvatureDrive( kDRIVE, m_rotation, true );
    }else{
      if(isInFeet){
        if( m_driveTrain.leftEncoderDistance() < m_distance && m_driveTrain.rightEncoderDistance() < m_distance )
          m_driveTrain.curvatureDrive(kDRIVE, m_rotation, true);
      }else{
        if( m_driveTrain.leftEncoderDistance() < m_distanceInFeet && m_driveTrain.rightEncoderDistance() < m_distanceInFeet)
          m_driveTrain.curvatureDrive(kDRIVE, m_rotation, true);
      }
    }
  }
   
  

  public void setTimeRun( double timeForRunning ){
    m_timeMode = true;
    m_timeToRun = timeForRunning;
    m_rotation = 0.0;
  }

  public void setDistanceInUnit( int distance ){
    m_timeMode = false;
    m_distance = distance;
    m_rotation = 0.0;
  }

  public void setDistanceInFeet( double distance ){
    m_timeMode = false;
    m_distanceInFeet = distance;
    m_rotation = 0.0;
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    timer.stop();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    if(timer.getMatchTime() > 15.0 ){
      return true;
    }else{
      return false;
    }
  }
}
