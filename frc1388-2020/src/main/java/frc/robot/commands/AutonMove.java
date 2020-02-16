/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import frc.robot.subsystems.DriveTrain;
import frc.robot.RobotContainer;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.geometry.Pose2d;
import edu.wpi.first.wpilibj2.command.CommandBase;

public class AutonMove extends CommandBase {

  private DriveTrain m_driveTrain;
  private Pose2d m_targetPosi;
  private Timer timer = new Timer();
  private boolean hasMovePeriodPass = false;
  
  private final double kDRIVE = 0.5;
  private final double kROTATION = 0.5;
  private final double TIME_PERIOD_MOVE = 3.0;

  /**
   * Creates a new AutonMove.
   */
  public AutonMove( DriveTrain driveTrain) {
    m_driveTrain = driveTrain;

    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements( m_driveTrain);
    // m_targetPosi = new Pose2d(10, 10, m_driveTrain.getAngle());
    
  }


  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    timer.start();
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {

    if(timer.hasPeriodPassed(TIME_PERIOD_MOVE) && !hasMovePeriodPass){
      m_driveTrain.curvatureDrive( kDRIVE, kROTATION, true );
    }
    
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
