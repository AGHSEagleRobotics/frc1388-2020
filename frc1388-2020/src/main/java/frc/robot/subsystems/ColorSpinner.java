/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import javax.lang.model.util.ElementScanner6;

import com.revrobotics.ColorSensorV3;

import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.util.Color;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class ColorSpinner extends SubsystemBase {
  
  private final ColorSensorV3 colorSensor;

  private final SpeedController spinnerMotor;

  public enum CheckColor{
      RED, GREEN, BLUE, YELLOW;
  }


  /**
   * Creates a new ColorSpinner.
   */
  public ColorSpinner(ColorSensorV3 sensor, SpeedController motor) {
      colorSensor = sensor;
      spinnerMotor = motor;
  }

  public CheckColor checkColor() {
    Color color = colorSensor.getColor();
    CheckColor c1;
      if ( color.equals( Color.kRed ) ) {
        c1 = CheckColor.RED;
     }
      else if ( color.equals( Color.kGreen ) ) {
        c1 = CheckColor.GREEN;
      }
      else if ( color.equals( Color.kBlue ) ) {
       c1 = CheckColor.BLUE;
     }
     else {
       c1 = CheckColor.YELLOW;
     }
      return c1;
  }

    // public boolean isColor (Color colorToCheck) {

       // get the current color from the color sensor

       // check if current color equals the color to Check and return true or false

  } 

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
