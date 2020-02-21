/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import com.revrobotics.ColorMatch;
import com.revrobotics.ColorMatchResult;
import com.revrobotics.ColorSensorV3;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.util.Color;
import frc.robot.Constants;
import frc.robot.USBLogging;

public class ColorSpinner extends SubsystemBase {

  // ======================================================
  // Instance Variables
  // ======================================================

  private final ColorSensorV3 m_colorSensor;

  private final WPI_VictorSPX m_spinnerMotor;
  private final WPI_TalonSRX m_spinnerArmMotor;

  private final double m_armSpeed = 0.5;

  private static final Color kRedTarget = ColorMatch.makeColor(0.517, 0.343, 0.141);
  private static final Color kBlueTarget = ColorMatch.makeColor(0.123, 0.415, 0.461);
  private static final Color kGreenTarget = ColorMatch.makeColor(0.165, 0.576, 0.258);
  private static final Color kYellowTarget = ColorMatch.makeColor(0.318, 0.557, 0.125);

  private final ColorMatch colorMatch = new ColorMatch();

  private ColorWheel m_color;
  private Color m_tempColor;

  public enum ColorWheel {
    UNKNOWN(ColorMatch.makeColor(0, 0, 0), "Unknown"),
    RED(kRedTarget, "Red"),
    YELLOW(kYellowTarget, "Yellow"),
    GREEN(kGreenTarget, "Green"),
    BLUE(kBlueTarget, "Blue");

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

    public static ColorWheel fromGameMessage() {
      String gameData = DriverStation.getInstance().getGameSpecificMessage();
      if (gameData.length() > 0) {
        /* We are converting the color from the desired to what our color sensor should see
        *  when the field is on the desired color.
        */
        switch (gameData.charAt(0)) {
          case 'B':
            return RED;
          case 'G':
            return YELLOW;
          case 'R':
            return BLUE;
          case 'Y':
            return GREEN;
        }
      }
      return UNKNOWN;
    }

  }

  // ======================================================
  // Constructors
  // ======================================================

  public ColorSpinner() {

    m_colorSensor = new ColorSensorV3(Constants.I2C_Port_ColorSensor);
    m_spinnerMotor = new WPI_VictorSPX(Constants.CANID_colorSpinnerMotor);
    m_spinnerArmMotor = new WPI_TalonSRX(Constants.CANID_spinnerArmMotor);

    m_spinnerMotor.setInverted(true);
    m_spinnerArmMotor.setInverted(true);

    colorMatch.addColorMatch(kRedTarget);
    colorMatch.addColorMatch(kGreenTarget);
    colorMatch.addColorMatch(kBlueTarget);
    colorMatch.addColorMatch(kYellowTarget);
  }

  // ======================================================
  // Color Sensor
  // ======================================================

  public ColorWheel checkColor() {
    return m_color;
  }

  
  public void internalCheckColor(){
    m_tempColor = m_colorSensor.getColor();
    USBLogging.debug("(R, G, B) = (" + m_tempColor.red + ", " + m_tempColor.green + ", " + m_tempColor.blue + ")");
    final ColorMatchResult result = colorMatch.matchClosestColor(m_tempColor);
    // USBLogging.debug("R = " + result.color.red + "  G = " + result.color.green + "  B = " + result.color.blue
    //     + " confidence = " + result.confidence);

    m_color = setColor(result);
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
  // Motor Spinner
  // ======================================================

  public void spinMotor(final double speed) {
    m_spinnerMotor.set(speed);
  }

  // ======================================================
  // Arm Motor
  // ======================================================

  public void setArmMotor(double speed) {
    m_spinnerArmMotor.set(speed);
  }

  public void stopArm() {
    m_spinnerArmMotor.set(0);
  }

  public double getSpinnerArmCurrent() {
    return m_spinnerArmMotor.getStatorCurrent();
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    internalCheckColor();
  }
}
