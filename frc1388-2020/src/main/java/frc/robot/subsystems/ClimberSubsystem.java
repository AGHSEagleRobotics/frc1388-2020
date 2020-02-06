/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class ClimberSubsystem extends SubsystemBase {
  private WPI_TalonSRX climberMotor = new WPI_TalonSRX(Constants.CANID_climbMotor);

  /**
   * Creates a new ClimberSubsystem.
   */
  public ClimberSubsystem() {
    setBrakeMode();
  }

  public void setBrakeMode(){
    climberMotor.setNeutralMode(NeutralMode.Brake);
  }

  public void setClimberMotor(double speed){
    climberMotor.set(speed);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
