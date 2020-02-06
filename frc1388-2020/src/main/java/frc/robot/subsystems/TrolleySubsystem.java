/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class TrolleySubsystem extends SubsystemBase {
  // Yes it is named the trolley. Any pizza cutter or ski lift is unacceptable
  private WPI_VictorSPX trolleyMotor = new WPI_VictorSPX(Constants.CANID_trolleyMotor);

  /**
   * Creates a new Trolley.
   */
  public TrolleySubsystem() {
    setBrakeMode();
  }

  public void setBrakeMode(){
    trolleyMotor.setNeutralMode(NeutralMode.Brake);
  }

  public void setTrolleyMotor(double speed){
    trolleyMotor.set(speed);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
