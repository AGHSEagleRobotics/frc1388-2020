/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;
import com.revrobotics.ColorMatch;
import com.revrobotics.ColorMatchResult;
import com.revrobotics.ColorSensorV3;
import edu.wpi.first.wpilibj.util.Color;
import frc.robot.Constants;
import frc.robot.USBLogging;

public class ColorSpinner extends SubsystemBase {

  // ======================================================
  // Instance Variables
  // ======================================================

  private final ColorSensorV3 m_colorSensor;
  private final WPI_VictorSPX m_spinnerMotor;
  private final WPI_VictorSPX m_armMotor;  

  private boolean m_spinnerArmDown = false;

  private final Color kRedTarget = ColorMatch.makeColor(0.532, 0.341, 0.126);
  private final Color kBlueTarget = ColorMatch.makeColor(0.157, 0.452, 0.389);
  private final Color kGreenTarget = ColorMatch.makeColor(0.212, 0.577, 0.210);
  private final Color kYellowTarget = ColorMatch.makeColor(0.329, 0.556, 0.072);

  private final ColorMatch colorMatch = new ColorMatch();
  // ======================================================
  // Constructors
  // ======================================================

  public ColorSpinner() {
    m_colorSensor = new ColorSensorV3(Constants.I2C_Port_ColorSensor);
    m_spinnerMotor = new WPI_VictorSPX(Constants.CANID_colorSpinnerMotor);
    m_armMotor = new WPI_VictorSPX(Constants.CANID_spinnerArmMotor);

    neutralBrake();

    colorMatch.addColorMatch(kRedTarget);
    colorMatch.addColorMatch(kGreenTarget);
    colorMatch.addColorMatch(kBlueTarget);
    colorMatch.addColorMatch(kYellowTarget);
  }

  // ======================================================
  // Color Sensor Checking
  // ======================================================

  public String checkColor() {
    String c1;
    final Color color = m_colorSensor.getColor();
    USBLogging.debug( "R = " + color.red + "  G = " + color.green + "  B = " + color.blue );
    final ColorMatchResult result = colorMatch.matchClosestColor(color);
    USBLogging.debug( "R = " + result.color.red + "  G = " + result.color.green + "  B = " + result.color.blue
      + " confidence = " + result.confidence );
    if (result.confidence <= .95) {
      c1 = "Uknown";
    } else if (result.color.equals(kRedTarget)) {
      c1 = "Red";
    } else if (result.color.equals(kGreenTarget)) {
      c1 = "Green";
    } else if (result.color.equals(kBlueTarget)) {
      c1 = "Blue";
    } else if (result.color.equals(kYellowTarget)) {
      c1 = "Yellow";
    } else {
      c1 = "Unknown";
    }
    return c1;
}

  // ======================================================
  // Color Sensor print out
  // ======================================================

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    System.out.println(checkColor());
  }
 
  // ======================================================
  // Motor Spinner
  // ======================================================

  public void spinMotor( final double speed ) {
    m_spinnerMotor.set(speed);
  }

  private void neutralBrake() {
    m_spinnerMotor.setNeutralMode( NeutralMode.Brake);
  }

  // ======================================================
  // Arm Motor
  // ======================================================

  
  public void engageArm()
  {
    if( !m_spinnerArmDown )
    {
      //TODO: engage the arm
      m_spinnerArmDown = true;
    }
  }

  public void disengageArm()
  {
    if( m_spinnerArmDown )
    {
    //TODO: disengage the arm
    m_spinnerArmDown = false;
    }
  }


}
