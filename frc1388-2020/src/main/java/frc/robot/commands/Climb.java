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
import frc.robot.USBLogging;
import frc.robot.subsystems.ClimberSubsystem;

public class Climb extends CommandBase {
  private ClimberSubsystem m_climberSubsystem;
  private double m_climbingSpeed = 0.0;
  private Timer timer = new Timer();
  private Timer timerElasp = new Timer();
  private final double timeTillMotorReady = 0.2;
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
    m_climberSubsystem.setClimberServoLock();
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {

    m_climbingSpeed = RobotContainer.getOpLeftYAxis();
    m_climberSubsystem.setClimberMotor(m_climbingSpeed);

    // if( m_climbingSpeed != 0.0 ){
    //   pendLocking = false;
    //   m_climberSubsystem.setClimberServoUnlock();
    //   if( !unlockedForTime && timer.get() == 0.0 ){
    //     timer.start();
    //   }
    // }

    // // if( !m_climberSubsystem.getClimberSolenoidState() ){
    // //   m_climberSubsystem.setClimberMotor(m_climbingSpeed);
    // // }

    // if( timer.hasPeriodPassed(timeTillMotorReady) && !unlockedForTime ){
    //   unlockedForTime = true;
    //   timer.stop();
    //   timer.reset();
    // }

    // if( unlockedForTime ){
    //   if( !m_climberSubsystem.getClimberServoState() ){
    //     m_climberSubsystem.setClimberMotor(m_climbingSpeed);
    //   }else if( m_climbingSpeed < 0.0 ){
    //     m_climberSubsystem.setClimberMotor(m_climbingSpeed);
    //   }
    // }

    // if( m_climbingSpeed == 0.0 && !pendLocking ){
    //   pendLocking = true;
    //   timerElasp.start();
    // }

    // if(pendLocking && timerElasp.hasPeriodPassed(timeTillLockReady)){
    //   m_climberSubsystem.setClimberServoLock();
    //   unlockedForTime = false;
    //   timerElasp.stop();
    //   timerElasp.reset();
    // }

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
