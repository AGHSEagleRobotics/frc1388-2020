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
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

/**
 * Add your docs here.
 */
public class ShooterSystem extends SubsystemBase {
  // Put methods for controlling this subsystem
  // here. Call these from Commands.


    private WPI_TalonFX shootMotor;
    private WPI_TalonSRX feedMotor;

    public ShooterSystem() {
        shootMotor = new WPI_TalonFX(Constants.CANID_shootMotor);
        feedMotor = new WPI_TalonSRX(Constants.CANID_feedMotor);

    }
  }
