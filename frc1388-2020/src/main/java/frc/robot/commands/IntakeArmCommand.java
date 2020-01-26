/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.IntakeSubsystem;
import frc.robot.subsystems.IntakeSubsystem.Direction;

public class IntakeArmCommand extends CommandBase {
  private final IntakeSubsystem m_intakeSubsystem;
  private final Direction m_armDirection;
  /**
   * Creates a new IntakeArmCommand.
   * @param intakeSubsystem intake subsystem
   * @param shouldArmDeploy If true, deploy arm. If false, retract arm.
   */
  public IntakeArmCommand(IntakeSubsystem intakeSubsystem, boolean shouldArmDeploy) {
    // Use addRequirements() here to declare subsystem dependencies.
    m_intakeSubsystem = intakeSubsystem;

    if (shouldArmDeploy) {
      m_armDirection = Direction.kDown;
    } else {
      m_armDirection = Direction.kUp;
    }

    addRequirements(m_intakeSubsystem);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    m_intakeSubsystem.setIntakeArmMotor(m_armDirection);
  }
  

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    m_intakeSubsystem.setIntakeArmMotor(Direction.kStop);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return m_intakeSubsystem.getIntakeLimitSwitchTop() || m_intakeSubsystem.getIntakeLimitSwitchBottom();
  }
}
