/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;

/**
 * Add your docs here.
 */
public class ShooterSubsystem extends SubsystemBase {
  // Put methods for controlling this subsystem
  // here. Call these from Commands.


    private WPI_TalonFX m_shootMotor;
    private WPI_VictorSPX m_feedMotor;

    public ShooterSubsystem() {
      m_shootMotor = new WPI_TalonFX(Constants.CANID_shootMotor);
      m_feedMotor = new WPI_VictorSPX(Constants.CANID_feedMotor);
    }

    public void setShootMotor( double speed) {
      m_shootMotor.set( speed );
    }

    public void setFeedMotor( double speed ) {
      m_feedMotor.set( speed );
    }
  }
