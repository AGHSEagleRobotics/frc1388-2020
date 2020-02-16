/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class IntakeSubsystem extends SubsystemBase {

   // Instance fields of the shaft motor and the arm motor.
  private final WPI_VictorSPX m_intakeShaftMotor;
  private final WPI_TalonSRX m_intakeArmMotor;
  
  private final DigitalInput m_intakeLimitSwitchTop;
  private final DigitalInput m_intakeLimitSwitchBottom;

  public IntakeSubsystem() {
    m_intakeShaftMotor = new WPI_VictorSPX(Constants.CANID_intakeShaftMotor); // forward is positive, reverse is negative
    m_intakeArmMotor = new WPI_TalonSRX(Constants.CANID_intakeArmMotor);  // positive is up, negative is down
    m_intakeLimitSwitchTop = new DigitalInput(Constants.DIO_intakeArmTop);
    m_intakeLimitSwitchBottom = new DigitalInput(Constants.DIO_intakeArmBottom);
  }

  public void setIntakeShaftMotor(double speed){
    m_intakeShaftMotor.set(speed);
  }

  public void setIntakeArmMotor(double speed) {
    if (speed < 0 && getIntakeLimitSwitchBottom()) {
      speed = 0;
    } else if (speed > 0 && getIntakeLimitSwitchTop()) {
      speed = 0;
    }
    m_intakeArmMotor.set(speed);
  }

  // Limit switches on the top and bottom of the intake arm

  /**
   * This gets the intake arm limit switch
   * 
   * @return true when pressed, false when not pressed
   */
  public boolean getIntakeLimitSwitchTop() {
    return m_intakeLimitSwitchTop.get();
  }

  /**
   * This gets the intake arm limit switch
   * 
   * @return true when pressed, false when not pressed
   */
  public boolean getIntakeLimitSwitchBottom() {
    return m_intakeLimitSwitchBottom.get();
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
