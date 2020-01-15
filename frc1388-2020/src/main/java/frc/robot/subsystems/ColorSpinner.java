/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.revrobotics.ColorMatch;
import com.revrobotics.ColorMatchResult;
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
    // color address for sensor w/o the LED light
    // private final Color kRedTarget = ColorMatch.makeColor( 0.641845703125, 0.29248046875, 0.0654296875);
    // private final Color kGreenTarget = ColorMatch.makeColor(0.256, 0.587, 0.156);
    // private final Color kBlueTarget = ColorMatch.makeColor(0.194, 0.484, 0.329);
    // private final Color kYellowTarget = ColorMatch.makeColor(0.411, 0.516, 0.072);
// color address for sensor with the LED light
    private final Color kRedTarget = ColorMatch.makeColor(0.532, 0.341, 0.126);    
    private final Color kBlueTarget = ColorMatch.makeColor(0.157, 0.452, 0.389);
    private final Color kGreenTarget = ColorMatch.makeColor(0.212, 0.577, 0.210);
    private final Color kYellowTarget = ColorMatch.makeColor(0.329, 0.556, 0.072);

  // private String c1;

  private final ColorMatch colorMatch = new ColorMatch();

  //======================================================
  // Constructors
  //======================================================

  public ColorSpinner(final ColorSensorV3 sensor, final SpeedController motor) {
      colorSensor = sensor;
      //spinnerMotor = motor;
  }
  public ColorSpinner(final ColorSensorV3 sensor) {
    colorSensor = sensor;
  
    colorMatch.addColorMatch(kRedTarget);
    colorMatch.addColorMatch(kGreenTarget);
    colorMatch.addColorMatch(kBlueTarget);    
    colorMatch.addColorMatch(kYellowTarget);
  }

  //======================================================
  // Color Sensor Checking
  //======================================================

  public String checkColor() {
    String c1;
    final Color color = colorSensor.getColor(); 
    System.out.println( "R = " + color.red + "  G = " + color.green + "  B = " + color.blue );
    final ColorMatchResult result = colorMatch.matchClosestColor(color);
    System.out.println( "R = " + result.color.red 
                    + "  G = " + result.color.green 
                    + "  B = " + result.color.blue 
                    + " confidence = " + result.confidence);
    if ( result.color.equals( kRedTarget ) ) {
        c1 = "Red";
     }
      else if ( result.color.equals( kGreenTarget ) ) {
        c1 = "Green";
      }
      else if ( result.color.equals( kBlueTarget ) ) {
       c1 = "Blue";
     }
     else if ( result.color.equals( kYellowTarget ) ) {
       c1 = "Yellow";
     }
     else {
       c1 = "Unknown";
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

