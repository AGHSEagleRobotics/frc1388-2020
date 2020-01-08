/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.revrobotics.ColorSensorV3;

import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.util.Color;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class ColorSpinner extends SubsystemBase {
  
  private final ColorSensorV3 colorSensor;

  private final SpeedController spinnerMotor;

  /**
   * Creates a new ColorSpinner.
   */
  public ColorSpinner(ColorSensorV3 sensor, SpeedController motor) {
      // colorSensor = new ColorSensorV3(0);
      colorSensor = sensor;
      spinnerMotor = motor;
  }

  public Color readColor() {
    return colorSensor.getColor();
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
