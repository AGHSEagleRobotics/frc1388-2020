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

public class Eject extends CommandBase {
  private final IntakeSubsystem m_intakeSubsystem;
  private final MagazineSubsystem m_magazineSubsystem;

  private final double k_intakeArmMotorUp = 0.2;
  private final double k_intakeShaftUnjamSpeed = -0.5;  //speed when arm is retracting
  private final double k_intakeShaftEjectDefaultSpeed = -0.2;

  /**
   * Creates a new EmergencyReverse.
   */
  public Eject(IntakeSubsystem intakeSubsystem, MagazineSubsystem magazineSubsystem) {
    m_intakeSubsystem = intakeSubsystem;
    m_magazineSubsystem = magazineSubsystem;

    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(m_intakeSubsystem);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    m_intakeSubsystem.setIntakeArmMotor(k_intakeArmMotorUp);
    m_intakeSubsystem.setIntakeShaftMotor(k_intakeShaftUnjamSpeed);

    m_magazineSubsystem.startEjectMode();
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    if (m_intakeSubsystem.getIntakeLimitSwitchTop()) {
      m_intakeSubsystem.setIntakeArmMotor(0);
    }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    m_magazineSubsystem.stopEjectMode();
    m_intakeSubsystem.setIntakeShaftMotor(k_intakeShaftEjectDefaultSpeed);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
