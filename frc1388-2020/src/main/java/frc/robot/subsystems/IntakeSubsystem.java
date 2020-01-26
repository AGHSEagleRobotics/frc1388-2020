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
  public enum Direction {
    kUp,
    kDown,
    kStop
  }

   // Instance fields of the shaft motor and the arm motor.
  private final WPI_TalonSRX m_intakeShaftMotor;
  private final WPI_VictorSPX m_intakeArmMotor;
  
  private final DigitalInput m_intakeLimitSwitchTop;
  private final DigitalInput m_intakeLimitSwitchBottom;

  // Variables for the motor speeds. TODO The variable values can change based on the 
  // direction in which negative and positive values spin the motors and what speed
  // is required to efficiently run the mechanisms.

  private static final double intakeShaftSpeed = 1;
  private static final double intakeDownArmSpeed = 1; 
  private static final double intakeUpArmSpeed = -1;

  public IntakeSubsystem() {
    m_intakeShaftMotor = new WPI_TalonSRX(Constants.CANID_intakeShaftMotor);
    m_intakeArmMotor = new WPI_VictorSPX(Constants.CANID_intakeArmMotor);
    m_intakeLimitSwitchTop = new DigitalInput(Constants.DIO_intakeArmTop);
    m_intakeLimitSwitchBottom = new DigitalInput(Constants.DIO_intakeArmBottom);
  }

  public void setIntakeShaftMotor(double speed){
    m_intakeShaftMotor.set(speed);
  }
// TODO The else statement could be changed to the intakeShaftSpeed if people want
// to raise the arm to stop ball intake when the robot has reached maximum capacity
// (5 balls) and keep the shaft moving.

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
 // TODO The speed of the intake arm motor and the time it takes for the 
 // arm to lower itself to the correct angle
 // are interdependent and need to be changed so that the intake arm lowers
 // the correct amount.

  public void setIntakeArmMotor(Direction dir) {
    switch (dir) {
      case kUp:
        m_intakeArmMotor.set(intakeUpArmSpeed);
        break;
      case kDown:
        m_intakeArmMotor.set(intakeDownArmSpeed);
        break;
      case kStop:
        m_intakeArmMotor.set(0);
        break;
    }
  }

  public boolean getIntakeLimitSwitchTop() {
    return m_intakeLimitSwitchTop.get();
  }

  public boolean getIntakeLimitSwitchBottom() {
    return m_intakeLimitSwitchBottom.get();
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
