/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import frc.robot.subsystems.ShooterSubsystem;

import edu.wpi.first.wpilibj.Timer;

import edu.wpi.first.wpilibj2.command.CommandBase;

public class MultiShot extends CommandBase {

  private ShooterSubsystem m_shooterSubsystem;

  private final Timer m_spinUpTimer = new Timer();
  private final double k_shooterSpinUpTime = 3; // Arbitrary time required for shooter to spin up
  private double m_rpm = 0;

  /**
   * Creates a new MultiShot.
   */
  public MultiShot(ShooterSubsystem subsystem) {
    m_shooterSubsystem = subsystem;
    addRequirements(m_shooterSubsystem);
    m_rpm = -1; // Invalid value used to differentiate constructors
  }

  public MultiShot(ShooterSubsystem subsystem, double rpm) {
    m_shooterSubsystem = subsystem;
    addRequirements(m_shooterSubsystem);
    m_rpm = rpm;
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    if (m_rpm > 0) {
      m_shooterSubsystem.setShooterRPM(m_rpm);
    }
    else {
      m_shooterSubsystem.usePresetRPM();
    }
    m_shooterSubsystem.startShooter();
    m_spinUpTimer.start();
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    if (m_spinUpTimer.hasPeriodPassed(k_shooterSpinUpTime) == true) {
      m_shooterSubsystem.setFeeder(1);
      m_spinUpTimer.stop();
    }

  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    m_shooterSubsystem.stopShooter();
    m_shooterSubsystem.stopFeeder();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
