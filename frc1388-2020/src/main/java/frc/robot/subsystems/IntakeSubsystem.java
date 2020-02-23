/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class IntakeSubsystem extends SubsystemBase {

   // Instance fields of the shaft motor and the arm motor.
  private final WPI_VictorSPX m_intakeShaftMotor;
  private final WPI_TalonSRX m_intakeArmMotor;

  public IntakeSubsystem() {
    m_intakeShaftMotor = new WPI_VictorSPX(Constants.CANID_intakeShaftMotor); // forward is positive, reverse is negative
    m_intakeArmMotor = new WPI_TalonSRX(Constants.CANID_intakeArmMotor);  // positive is up, negative is down
  }

  public void setIntakeShaftMotor(double speed){
    m_intakeShaftMotor.set(speed);
  }

  public void setIntakeArmMotor(double speed) {
    m_intakeArmMotor.set(speed);
  }

  public double getIntakeArmCurrent() {
    return m_intakeArmMotor.getStatorCurrent();
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
