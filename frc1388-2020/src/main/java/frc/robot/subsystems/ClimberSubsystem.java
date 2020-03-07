/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.InvertType;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.USBLogging;


public class ClimberSubsystem extends SubsystemBase {
  private WPI_VictorSPX m_climberMotor = new WPI_VictorSPX(Constants.CANID_climbMotor);
  private final double SERVO_LOCK = 90.0;
  private final double SERVO_UNLOCK = 0.0;
  private final double ENDGAME_START = 30.0;
  
  private Relay m_solenoid = new Relay(Constants.RELAY_climbSolenoid);
  private boolean m_solenoidState = false;
  private boolean m_servoState = false;
  private Servo m_servo = new Servo(Constants.PWM_climbServo);
  private boolean m_climberEnabled = false;

  /**
   * Creates a new ClimberSubsystem.
   */
  public ClimberSubsystem() {
    setBrakeMode();
    m_climberMotor.setInverted(InvertType.InvertMotorOutput);
  }

  public void setBrakeMode(){
    m_climberMotor.setNeutralMode(NeutralMode.Brake);
  }

  public void setClimberMotor(double speed){
    // USBLogging.debug("changing the speed: " + speed);
    if( m_climberEnabled ){
      m_climberMotor.set(speed);
    }
  }

  // public boolean getClimberSolenoidState(){
  //   return m_solenoidState;
  // }

  
  // public void ClimberUnlock(){
  //   m_solenoid.set(Relay.Value.kOff);
  //   m_solenoidState = false;
  // }

  // public void ClimberLock(){
  // m_solenoid.set(Relay.Value.kOn);
  // m_solenoidState = true;
  // }

  // public void climberSolenoid( Relay.Value value){
  //  m_solenoid.set(value);
  //  if( Relay.Value.kOff == value){
  //    m_solenoidState = false;
  //  }else if( Relay.Value.kOn == value ){
  //    m_solenoidState = true;
  //  }
  // }

  public boolean getClimberServoState() {
    return m_servoState;
  }
  public void setClimberServoLock(){
    m_servo.set(SERVO_LOCK);
    m_servoState = true;
  }

  public void setClimberServoUnlock(){
    m_servo.set(SERVO_UNLOCK);
    m_servoState = false;
  }

  public void setClimberServo( double value){
    m_servo.set(value);
    if( value > SERVO_UNLOCK ){
      m_servoState = true;
    }else{
      m_servoState = false;
    }
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    boolean autonomous = DriverStation.getInstance().isAutonomous();
    double matchTime = DriverStation.getInstance().getMatchTime();
    if( matchTime <= ENDGAME_START && !autonomous && matchTime > 0){
      m_climberEnabled = true;
    }
    // System.out.println(matchTime);
  }
}
