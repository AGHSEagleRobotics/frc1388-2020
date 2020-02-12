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

public class DeployIntake extends CommandBase {
  private final IntakeSubsystem m_intakeSubsystem;
  private final MagazineSubsystem m_magazineSubsystem;
  
  private final double k_intakeArmMotorDown = -1;
  private final double k_intakeShaftMotorForward = 1;
  private final double k_deployIntakeTimeout = 1;
  /**
   * Creates a new Deploy.
   */
  public DeployIntake(IntakeSubsystem intakeSubsystem, MagazineSubsystem magazineSubsystem) {
    m_intakeSubsystem = intakeSubsystem;
    m_magazineSubsystem = magazineSubsystem;

    // Timeout used to stop the intake arm from going down too far in case the limit switch fails to stop the arm
    withTimeout(k_deployIntakeTimeout);

    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(m_intakeSubsystem);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    m_intakeSubsystem.setIntakeArmMotor(k_intakeArmMotorDown);
    m_intakeSubsystem.setIntakeShaftMotor(k_intakeShaftMotorForward);

    m_magazineSubsystem.startIntakeMode();
  }

  // Called every time scheduler the runs while the command is scheduled.
  @Override
  public void execute() { 
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    m_intakeSubsystem.setIntakeArmMotor(0);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return m_intakeSubsystem.getIntakeLimitSwitchBottom();
  }
}
