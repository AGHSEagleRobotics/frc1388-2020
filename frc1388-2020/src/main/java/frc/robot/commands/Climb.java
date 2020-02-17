/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.RobotContainer;
import frc.robot.subsystems.ClimberSubsystem;

public class Climb extends CommandBase {
  private ClimberSubsystem m_climberSubsystem;
  private double m_climbingSpeed = 0.0;
  private Timer timer = new Timer();
  private Timer timerElasp = new Timer();
  private final double timeTillMotorReady = 3.0;
  private final double timeTillLockReady = 2.0;
  private boolean unlockedForTime = false;
  private boolean pendLocking = false;

  /**
   * Creates a new Climb.
   */
  public Climb( ClimberSubsystem climberSubsystem) {
    // Use addRequirements() here to declare subsystem dependencies.
    m_climberSubsystem = climberSubsystem;
    addRequirements(m_climberSubsystem );
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    timer.start();
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    m_climbingSpeed = RobotContainer.getOpLeftYAxis();
    
    if(m_climbingSpeed != 0 && !unlockedForTime){
      m_climberSubsystem.setClimberServoUnlock();
      timer.reset();
      pendLocking = false;
    }

    // if( !m_climberSubsystem.getClimberSolenoidState() ){
    //   m_climberSubsystem.setClimberMotor(m_climbingSpeed);
    // }

    if( timer.hasPeriodPassed(timeTillMotorReady) && !unlockedForTime ){
      unlockedForTime = true;
    }

    if( !m_climberSubsystem.getClimberServoState() && unlockedForTime ){
      m_climberSubsystem.setClimberMotor(m_climbingSpeed);
    }

    if( m_climbingSpeed == 0.0 && !pendLocking ){
      pendLocking = true;
      if(timerElasp.get() > 0.0 ){
        timerElasp.reset();
      }else{
        timerElasp.start();
      }
    }

    if(pendLocking && timerElasp.hasPeriodPassed(timeTillLockReady)){
      m_climberSubsystem.setClimberServoLock();
      unlockedForTime = false;
    }

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
