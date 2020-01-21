/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class IntakeSubsystem extends SubsystemBase {
  /**
   * Creates a new IntakeSubsystem.
   */
  private final WPI_TalonFX m_intakeShaftMotor;
  private final WPI_TalonFX m_intakeArmMotor;
  
  private static final double intakeShaftSpeed = 1;
  private static final double intakeArmSpeed = 1;
  
  public IntakeSubsystem( WPI_TalonFX intakeShaftMotor, WPI_TalonFX intakeArmMotor) {
    m_intakeShaftMotor = intakeShaftMotor;
    m_intakeArmMotor = intakeArmMotor;
  }

  public void setIntakeShaftMotor(double speed){
    m_intakeShaftMotor.set(speed);
  }

  public void setIntakeShaftMotor(boolean isOn) {
    if (isOn){
      m_intakeShaftMotor.set(intakeShaftSpeed);
    } else{
      m_intakeShaftMotor.set(0);
    }
  }

  public void setIntakeArmMotor(double speed) {
    m_intakeArmMotor.set(speed);
  }
 // The speed of the intake arm motor, the time it takes for the 
 // arm to lower itself to the correct angle, and when the command times out
 // are interdependent and need to be changed so that the intake arm lowers
 // to the correct angle.

  public void setIntakeArmMotor(boolean isOn) {
    if (isOn){
      m_intakeArmMotor.set(intakeArmSpeed);
    } else{
      m_intakeArmMotor.set(0);
    }
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
