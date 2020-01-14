/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.revrobotics.ColorMatch;
//import com.revrobotics.ColorMatchResult;
import com.revrobotics.ColorSensorV3;

import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.util.Color;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class ColorSpinner extends SubsystemBase {
  
  //======================================================
  // Instance Variables
  //======================================================

  private final ColorSensorV3 colorSensor;

  //private SpeedController spinnerMotor;

  // private String c1;

  public enum CheckColor{
      RED, GREEN, BLUE, YELLOW, UNKNOWN;
  }
  private ColorMatch colorMatch = new ColorMatch();

  //======================================================
  // Constructors
  //======================================================


  public ColorSpinner(ColorSensorV3 sensor, SpeedController motor) {
      colorSensor = sensor;
      //spinnerMotor = motor;
  }
  public ColorSpinner(ColorSensorV3 sensor) {
    colorSensor = sensor;
    colorMatch.addColorMatch(Color.kRed);
    colorMatch.addColorMatch(Color.kGreen);
    colorMatch.addColorMatch(Color.kBlue);    
    colorMatch.addColorMatch(Color.kYellow);
  }

  //======================================================
  // Color Sensor Checking
  //======================================================

  public String checkColor() {
    String c1;
    Color color = colorSensor.getColor();
    System.out.println( "R = " + color.red + "  G = " + color.green + "  B = " + color.blue );
    //ColorMatchResult result = colorMatch.matchClosestColor(color);
    //System.out.println( result.color);
    if ( color.equals( Color.kRed ) ) {
        c1 = "Red";
     }
      else if ( color.equals( Color.kGreen ) ) {
        c1 = "Green";
      }
      else if ( color.equals( Color.kBlue ) ) {
       c1 = "Blue";
     }
     else if ( color.equals( Color.kYellow ) ) {
       c1 = "Yellow";
     }
     else {
       c1 = "Uknown";
     }
      return c1;
  }



  

  //======================================================
  // Motor Spinner ( Unknown data )
  //======================================================
 
 
 
  
  //end cheat
  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    System.out.println( checkColor() );
   // checkColor();
  }
}

