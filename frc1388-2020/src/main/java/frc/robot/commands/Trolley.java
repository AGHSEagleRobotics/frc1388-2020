/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.RobotContainer;
import frc.robot.subsystems.TrolleySubsystem;

public class Trolley extends CommandBase {
  private TrolleySubsystem m_trolleySubsystem;
  private double m_trolleySpeed = 0.0;
  // private Timer timer = new Timer();
  // private Timer timerElasp = new Timer();
  // private final double timeTillMotorReady = 0.2;
  // private final double timeTillLockReady = 2.0;
  // private boolean unlockedForTime = false;
  // private boolean pendLocking = false;

  /**
   * Creates a new Trolley.
   */
  public Trolley( TrolleySubsystem trolleySubsystem ) {
    // Use addRequirements() here to declare subsystem dependencies.
    m_trolleySubsystem = trolleySubsystem;
    addRequirements(m_trolleySubsystem);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    // m_trolleySubsystem.setTrolleyServoLock();
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    m_trolleySpeed = RobotContainer.getOpRightXAxis();
    m_trolleySubsystem.setTrolleyMotor(m_trolleySpeed); 

    // // if( !LockTrolleyGear.getTrolleySolenoidState()){
    // //   m_trolleySubsystem.setTrolleyMotor(m_trolleySpeed);
    // // }

    // // unlocking the trolley servo adn beginning to start the timer for motor ready to move
    // if(m_trolleySpeed != 0.0 ){
    //   pendLocking = false;
    //   m_trolleySubsystem.setTrolleyServoUnlock();
    //   if( !unlockedForTime && timer.get() == 0.0){
    //     timer.start();
    //   }
    // }

    // if( !m_trolleySubsystem.gettrolleySolenoidState() ){
    //   m_trolleySubsystem.settrolleyMotor(m_climbingSpeed);
    // }

    // // checking if time has passed for the motor to be ready to move
    // if( timer.hasPeriodPassed(timeTillMotorReady) && !unlockedForTime ){
    //   unlockedForTime = true;
    //   timer.stop();
    //   timer.reset();
    // }


    // // setting the power of the motor of the climber
    // if( unlockedForTime &&  !m_trolleySubsystem.getTrolleyServoState() ){
    //     m_trolleySubsystem.setTrolleyMotor(m_climbingSpeed);
    // }

    // setting the power of the motor of the climber
  


  //   // 
  //   if( m_climbingSpeed == 0.0 && !pendLocking ){
  //     pendLocking = true;
  //     if(timerElasp.get() > 0.0 ){
  //       timerElasp.reset();
  //     }else{
  //       timerElasp.start();
  //     }
  //   }

  //   // locking if no input has been given in a certain amount of time
  //   if(pendLocking && timerElasp.hasPeriodPassed(timeTillLockReady)){
  //     m_trolleySubsystem.setTrolleyServoLock();
  //     unlockedForTime = false;
  //   }

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
