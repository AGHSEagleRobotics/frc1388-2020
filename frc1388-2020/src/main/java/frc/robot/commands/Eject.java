/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.IntakeSubsystem;
import frc.robot.subsystems.MagazineSubsystem;

public class Eject extends CommandBase {
  private final IntakeSubsystem m_intakeSubsystem;
  private final MagazineSubsystem m_magazineSubsystem;

  private final double k_intakeArmMotorUp = 0.6;
  private final double k_intakeShaftEjectSpeed = -0.5;
  // private final double k_intakeShaftEjectDefaultSpeed = -0.2;

  private final Timer m_deployIntakeTimer = new Timer();
  private final double k_deployIntakeTimeout = 2;

  // Stall Detection
  private final double k_stallAmps = 10.0;
  private final double k_stallTime = 2.0;
  private final Timer m_stallTimer = new Timer();

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
    // System.out.println("Intake arm going up");
    m_intakeSubsystem.setIntakeShaftMotor(k_intakeShaftEjectSpeed);

    m_magazineSubsystem.startEjectMode();
    m_magazineSubsystem.stopIntakeMode();
    m_magazineSubsystem.stopShooting();

    m_deployIntakeTimer.start();
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
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

   if (m_stallTimer.get() >= k_stallTime) {
    m_intakeSubsystem.setIntakeArmMotor(0);
   }
    
   if (m_deployIntakeTimer.get() >= k_deployIntakeTimeout){
     m_intakeSubsystem.setIntakeArmMotor(0);
     // System.out.println("Intake Arm Up");
     // System.out.println("Intake arm retracted");
   }
  }
      
  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    m_magazineSubsystem.stopEjectMode();
    m_intakeSubsystem.setIntakeShaftMotor(0);
    m_intakeSubsystem.setIntakeArmMotor(0);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
