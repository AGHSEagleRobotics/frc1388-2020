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
import frc.robot.subsystems.TrolleySubsystem;

public class Trolley extends CommandBase {
  private TrolleySubsystem m_trolleySubsystem;
  private double m_trolleySpeed = 0.0;
  private double m_climbingSpeed = 0.0;
  private Timer timer = new Timer();
  private Timer timerElasp = new Timer();
  private final double timeTillMotorReady = 3.0;
  private final double timeTillLockReady = 2.0;
  private boolean unlockedForTime = false;
  private boolean pendLocking = false;

  /**
   * Creates a new Trolley.
   */
  public Trolley( TrolleySubsystem trolleySubsystem ) {
    // Use addRequirements() here to declare subsystem dependencies.
    m_trolleySubsystem = trolleySubsystem;
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    m_trolleySpeed = RobotContainer.getOpRightXAxis();
    // if( !LockTrolleyGear.getTrolleySolenoidState()){
    //   m_trolleySubsystem.setTrolleyMotor(m_trolleySpeed);
    // }

    if(m_trolleySpeed != 0 ){
      m_trolleySubsystem.setTrolleyServoUnlock();
      timer.reset();
    }

    // if( !m_trolleySubsystem.gettrolleySolenoidState() ){
    //   m_trolleySubsystem.settrolleyMotor(m_climbingSpeed);
    // }

    if( timer.hasPeriodPassed(timeTillMotorReady) ){
      unlockedForTime = true;
    }

    if( !m_trolleySubsystem.getTrolleyServoState() && unlockedForTime ){
      m_trolleySubsystem.setTrolleyMotor(m_climbingSpeed);
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
      m_trolleySubsystem.setTrolleyServoLock();
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
