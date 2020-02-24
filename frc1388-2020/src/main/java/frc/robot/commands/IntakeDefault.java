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

public class IntakeDefault extends CommandBase {
  private final MagazineSubsystem m_magazineSubsystem;
  private final RetractIntake m_retractIntake;
  /**
   * Creates a new IntakeDefault.
   */
  public IntakeDefault(IntakeSubsystem intakeSubsystem, MagazineSubsystem magazineSubsystem, RetractIntake retractIntake) {
    m_magazineSubsystem = magazineSubsystem;
    m_retractIntake = retractIntake;
    
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(intakeSubsystem);
  }
  
  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
  }
  
  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    if (m_magazineSubsystem.isMagazineFull() && m_magazineSubsystem.isIntakeMode()) {
      m_retractIntake.schedule();
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
