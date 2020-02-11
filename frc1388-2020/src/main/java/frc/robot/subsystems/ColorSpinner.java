/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

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

  private final double m_armSpeed = .5;

  private static final Color kRedTarget = ColorMatch.makeColor(0.517, 0.343, 0.141);
  private static final Color kBlueTarget = ColorMatch.makeColor(0.123, 0.415, 0.461);
  private static final Color kGreenTarget = ColorMatch.makeColor(0.165, 0.576, 0.258);
  private static final Color kYellowTarget = ColorMatch.makeColor(0.318, 0.557, 0.125);

  private final ColorMatch colorMatch = new ColorMatch();

  public enum ColorWheel {
    UNKNOWN(ColorMatch.makeColor(0, 0, 0), "Unknown"), RED(kRedTarget, "Red"), YELLOW(kYellowTarget, "Yellow"),
    GREEN(kGreenTarget, "Green"), BLUE(kBlueTarget, "Blue");

    Color eColor;
    String eName;

    ColorWheel(final Color color, final String name) {
      eColor = color;
      eName = name;
    }

    public Color getColor() {
      return eColor;
    }

    public String getName() {
      return eName;
    }

  }

  // ======================================================
  // Constructors
  // ======================================================

  public ColorSpinner() {

    m_colorSensor = new ColorSensorV3(Constants.I2C_Port_ColorSensor);
    m_spinnerMotor = new WPI_VictorSPX(Constants.CANID_colorSpinnerMotor);
    m_armMotor = new WPI_VictorSPX(Constants.CANID_spinnerArmMotor);

    m_spinnerMotor.setInverted(true);

    colorMatch.addColorMatch(kRedTarget);
    colorMatch.addColorMatch(kGreenTarget);
    colorMatch.addColorMatch(kBlueTarget);
    colorMatch.addColorMatch(kYellowTarget);
  }

  // ======================================================
  // Color Sensor
  // ======================================================

  public ColorWheel checkColor() {
    final Color color = m_colorSensor.getColor();
    ColorWheel curColor = ColorWheel.UNKNOWN;
    USBLogging.debug("(R, G, B) = (" + color.red + ", " + color.green + ", " + color.blue + ")");
    final ColorMatchResult result = colorMatch.matchClosestColor(color);
    USBLogging.debug("R = " + result.color.red + "  G = " + result.color.green + "  B = " + result.color.blue
        + " confidence = " + result.confidence);

    curColor = setColor(result);
    return curColor;
  }

  private ColorWheel setColor(final ColorMatchResult result) {
    ColorWheel curColor;
    if (result.confidence <= .95) {
      curColor = ColorWheel.UNKNOWN;
    } else if (result.color.equals(kRedTarget)) {
      curColor = ColorWheel.RED;
    } else if (result.color.equals(kGreenTarget)) {
      curColor = ColorWheel.GREEN;
    } else if (result.color.equals(kBlueTarget)) {
      curColor = ColorWheel.BLUE;
    } else if (result.color.equals(kYellowTarget)) {
      curColor = ColorWheel.YELLOW;
    } else {
      curColor = ColorWheel.UNKNOWN;
    }
    return curColor;
}

  // ======================================================
  // Color Sensor print out
  // ======================================================

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }

  // ======================================================
  // Motor Spinner
  // ======================================================

  public void spinMotor(final double speed) {
      m_spinnerMotor.set(speed);
   }

  // ======================================================
  // Arm Motor
  // ======================================================

  public void raiseArm() {
    m_armMotor.set(m_armSpeed);
  }

  public void lowerArm() {

    m_armMotor.set(-m_armSpeed);
  }

  public void stopArm() {
    m_armMotor.set(0);
  }

}
