/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.ShooterSubsystem;

public class DeveloperMode extends CommandBase {

  private ShooterSubsystem m_shooterSubsystem;
  
  private final Timer m_devModeCheck = new Timer();
  private final double k_devModeCheckTime = 5;

  /**
   * Creates a new DeveloperMode.
   */
  public DeveloperMode(ShooterSubsystem subsystem) {
    m_shooterSubsystem = subsystem;
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    m_devModeCheck.start();
    m_shooterSubsystem.setDeveloperMode(false);
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    if (m_devModeCheck.get() > k_devModeCheckTime) {
      m_shooterSubsystem.setDeveloperMode(true);
    }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    m_devModeCheck.stop();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
