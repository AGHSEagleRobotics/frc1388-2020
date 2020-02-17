/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class ClimberSubsystem extends SubsystemBase {
  private WPI_VictorSPX m_climberMotor = new WPI_VictorSPX(Constants.CANID_climbMotor);

  /**
   * Creates a new ClimberSubsystem.
   */
  public ClimberSubsystem() {
    setBrakeMode();
    configClimberMotor();
  }

  public void setBrakeMode(){
    m_climberMotor.setNeutralMode(NeutralMode.Brake);
  }

  public void setClimberMotor(double speed){
    m_climberMotor.set(speed);
  }

  private void configClimberMotor(){
    m_climberMotor.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
