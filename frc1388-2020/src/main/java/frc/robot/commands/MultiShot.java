/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import frc.robot.subsystems.MagazineSubsystem;
import frc.robot.subsystems.ShooterSubsystem;

import edu.wpi.first.wpilibj.Timer;

import edu.wpi.first.wpilibj2.command.CommandBase;

public class MultiShot extends CommandBase {

  private ShooterSubsystem m_shooterSubsystem;
  private MagazineSubsystem m_magazineSubsystem;

  private final Timer m_spinUpTimer = new Timer();
  private final double k_shooterSpinUpTime = 3; // Arbitrary time required for shooter to spin up
  private double m_rpm = 0;

  /**
   * Creates a new MultiShot.
   */
  public MultiShot(ShooterSubsystem subsystem, MagazineSubsystem magazineSubsystem) {
    m_shooterSubsystem = subsystem;
    m_magazineSubsystem = magazineSubsystem;
    addRequirements(m_shooterSubsystem);
    m_rpm = -1; // Invalid value used to differentiate constructors
  }

  // Second constructor intended for use by autonomous commands
  public MultiShot(ShooterSubsystem shooterSubsystem, MagazineSubsystem magazineSubsystem, double rpm) {
    m_shooterSubsystem = shooterSubsystem;
    m_magazineSubsystem = magazineSubsystem;
    addRequirements(m_shooterSubsystem);
    m_rpm = rpm;
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    // Decides whether to use a preset RPM or not
    if (m_rpm > 0) {
      m_shooterSubsystem.setShooterRPM(m_rpm);
    }
    else {
      m_shooterSubsystem.usePresetRPM();
    }
    //Starts the shooter, and a timer to let it get up to speed
    m_magazineSubsystem.startShooting();
    m_shooterSubsystem.startShooter();
    m_spinUpTimer.start();
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    // Once the time needed for the shooter to get to the RPM is reached,
    //balls will be fed by the feeder into the shooter, timer is stopped
    if (m_spinUpTimer.hasPeriodPassed(k_shooterSpinUpTime) == true) {
      m_shooterSubsystem.setFeeder(1);
      m_spinUpTimer.stop();
    }

  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    // Stops the shooter and feeder once bumper is released
    m_magazineSubsystem.stopShooting();
    m_shooterSubsystem.stopShooter();
    m_shooterSubsystem.stopFeeder();
  }
  

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {

    return false;
  }
}
