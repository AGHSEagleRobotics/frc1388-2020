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

public class RetractIntake extends CommandBase {
  private final IntakeSubsystem m_intakeSubsystem;
  private final MagazineSubsystem m_magazineSubsystem;
  
  private final double k_intakeArmMotorUp = 0.2;
  private final double k_retractIntakeTimeout = 1;
  private final double k_intakeShaftUnjamSpeed = -0.5;    //speed when arm is retracting
  private final double k_intakeShaftRetractSpeed = -0.2;  //speed when arm fully retracted

  // TODO The speed of the intake arm motor and the time it takes for the
  // arm to lower itself to the correct angle
  // are interdependent and need to be changed so that the intake arm lowers
  // the correct amount.
  /**
   * Creates a new Deploy.
   */
  public RetractIntake(IntakeSubsystem intakeSubsystem, MagazineSubsystem magazineSubsystem) {
    m_intakeSubsystem = intakeSubsystem;
    m_magazineSubsystem = magazineSubsystem;

    // Timeout used to stop the intake arm from going down too far in case the limit switch fails to stop the arm
    withTimeout(k_retractIntakeTimeout);

    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(m_intakeSubsystem);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    m_intakeSubsystem.setIntakeArmMotor(k_intakeArmMotorUp);
    m_intakeSubsystem.setIntakeShaftMotor(k_intakeShaftUnjamSpeed);

    m_magazineSubsystem.stopIntakeMode(); //for magazine behavior
  }

  // Called every time scheduler the runs while the command is scheduled.
  @Override
  public void execute() { 
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    m_intakeSubsystem.setIntakeArmMotor(0);
    m_intakeSubsystem.setIntakeShaftMotor(k_intakeShaftRetractSpeed);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return m_intakeSubsystem.getIntakeLimitSwitchTop();
  }
}
