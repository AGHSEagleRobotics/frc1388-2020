/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;


import com.ctre.phoenix.motorcontrol.InvertType;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;
import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;


public class TrolleySubsystem extends SubsystemBase {
  // Yes it is named the trolley. Any pizza cutter or ski lift is unacceptable
  private WPI_VictorSPX trolleyMotor = new WPI_VictorSPX(Constants.CANID_trolleyMotor);
  // private final double SERVO_LOCK = 90.0;
  // private final double SERVO_UNLOCK = 0.0;
  
  // private Relay m_solenoid = new Relay(Constants.RELAY_trolleySolenoid);
  // private boolean m_solenoidState = false;
  // private boolean m_servoState = false;
  // private Servo m_servo = new Servo(Constants.DIO_trolleyServo);

  /**
   * Creates a new Trolley.
   */
  public TrolleySubsystem() {
    setBrakeMode();
    trolleyMotor.setInverted(InvertType.InvertMotorOutput);
  }

  public void setBrakeMode(){
    trolleyMotor.setNeutralMode(NeutralMode.Brake);
  }

  public void setTrolleyMotor(double speed){
    trolleyMotor.set(speed);
  }

  // public boolean getTrolleySolenoidState(){
  //   return m_solenoidState;
  // }

  // public boolean getTrolleyServoState(){
  //   return m_servoState;
  // }

  // public void trolleyUnlock(){
  //   m_solenoid.set(Relay.Value.kOff);
  //   m_solenoidState = false;
  // }

  // public void trolleyLock(){
  //   m_solenoid.set(Relay.Value.kOn);
  //   m_solenoidState = true;
  // }

  // public void trolleySolenoid( Relay.Value value){
  //   m_solenoid.set(value);
  //   if( Relay.Value.kOff == value){
  //     m_solenoidState = false;
  //   }else if( Relay.Value.kOn == value ){
  //     m_solenoidState = true;
  //   }
  // }

  // public void setTrolleyServoLock(){
  //   m_servo.set(SERVO_LOCK);
  //   m_servoState = true;
  // }

  // public void setTrolleyServoUnlock(){
  //   m_servo.set(SERVO_UNLOCK);
  //   m_servoState = false;
  // }

  // public void setTrolleyServo( double value){
  //   m_servo.set(value);
  //   if( value > SERVO_UNLOCK ){
  //     m_servoState = true;
  //   }else{
  //     m_servoState = false;
  //   }
  // }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
