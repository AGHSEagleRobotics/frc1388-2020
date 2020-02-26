/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.USBLogging;
import frc.robot.subsystems.IntakeSubsystem;
import frc.robot.subsystems.MagazineSubsystem;

public class RetractIntake extends CommandBase {
  private final IntakeSubsystem m_intakeSubsystem;
  private final MagazineSubsystem m_magazineSubsystem;
  
  private final double k_intakeArmMotorUp = 0.2;
  private final Timer m_retractIntakeTimer = new Timer();
  private final double k_retractIntakeTimeout = 2;
  private final double k_intakeShaftRetractSpeed = 0;  //speed when arm fully retracted

  // Stall Detection
  private final double k_stallAmps = 10.0;
  private final double k_stallTime = 2.0;
  private final Timer m_stallTimer = new Timer();

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
    
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(m_intakeSubsystem);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    m_intakeSubsystem.setIntakeArmMotor(k_intakeArmMotorUp);
    m_intakeSubsystem.setIntakeShaftMotor(k_intakeShaftRetractSpeed);
    m_retractIntakeTimer.start();

    m_magazineSubsystem.stopIntakeMode(); //for magazine behavior
  }

  // Called every time scheduler the runs while the command is scheduled.
  @Override
  public void execute() { 
    // System.out.println("Retracting");
    double intakeArmCurrent = Math.abs(m_intakeSubsystem.getIntakeArmCurrent());
    // System.out.println("Current = " + intakeArmCurrent);
    if (intakeArmCurrent > k_stallAmps && m_stallTimer.get() == 0) {
      m_stallTimer.start();
    //  System.out.println("Starting timer");
    } else if (intakeArmCurrent < k_stallAmps) {
      m_stallTimer.stop();
    //  System.out.println("Stopping timer");
      m_stallTimer.reset();
    }
    // System.out.println("Timer = " + m_stallTimer.get());
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    m_intakeSubsystem.setIntakeArmMotor(0);
    m_intakeSubsystem.setIntakeShaftMotor(0);

    System.out.println("Retracted");
    
    m_retractIntakeTimer.stop();
    m_retractIntakeTimer.reset();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return m_retractIntakeTimer.hasPeriodPassed(k_retractIntakeTimeout) ||
           m_stallTimer.hasPeriodPassed(k_stallTime);
  }
}
