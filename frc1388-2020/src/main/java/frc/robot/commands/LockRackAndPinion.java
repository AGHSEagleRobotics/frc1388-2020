/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Constants;

public class LockRackAndPinion extends CommandBase {
  private Solenoid m_solenoid = new Solenoid(Constants.RELAY_climbSolenoid);
  private static boolean m_solenoidState = false;

  /**
   * Creates a new LockRackAndPinion.
   */
  public LockRackAndPinion() {
    // Use addRequirements() here to declare subsystem dependencies.
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    // changing the state of the solenoid for the rack and pinion and then setting the solenoid to the switched state
    m_solenoidState = !m_solenoidState;
    m_solenoid.set(m_solenoidState);
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
  }

  public static boolean getRPSolenoidState(){
    return m_solenoidState;
  }

  public static void RPUnlock(){
    m_solenoidState = false;
  }

  public static void RPLock(){
    m_solenoidState = true;
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return true;
  }
}
