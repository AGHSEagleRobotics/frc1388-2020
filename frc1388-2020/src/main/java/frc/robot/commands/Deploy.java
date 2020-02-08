/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.IntakeSubsystem;
import frc.robot.subsystems.MagazineSubsystem;
import frc.robot.subsystems.ShooterSubsystem;

public class Deploy extends CommandBase {
  private final IntakeSubsystem m_intakeSubsystem;
  private final ShooterSubsystem m_shooterSubsystem;
  private final MagazineSubsystem m_magazineSubsystem;
  /**
   * Creates a new Deploy.
   */
  public Deploy(IntakeSubsystem intakeSubsystem, ShooterSubsystem shooterSubsystem, MagazineSubsystem magazineSubsystem) {
    m_intakeSubsystem = intakeSubsystem;
    m_shooterSubsystem = shooterSubsystem;
    m_magazineSubsystem = magazineSubsystem;
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(m_intakeSubsystem);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    m_intakeSubsystem.setIntakeArmMotor(-1);
    m_intakeSubsystem.setIntakeShaftMotor(-1);
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() { 
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

  private boolean isArmDeployed() {
    return m_intakeSubsystem.getIntakeLimitSwitchBottom();
  }
}
