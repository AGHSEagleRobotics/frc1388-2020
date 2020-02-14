
/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import java.lang.Math;

import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;

/**
 * Add your docs here.
 */
public class MagazineSubsystem extends SubsystemBase {
  // Put methods for controlling this subsystem
  // here. Call these from Commands.
  private boolean m_shooting = false;
  private boolean m_intake = false;
  private boolean m_eject = false;
  private final WPI_VictorSPX m_horizontalMagazineMotor;
  private final WPI_VictorSPX m_verticalMagazineMotor;

  AnalogInput ballSensor;
  private final double MIN_VOLTAGE = 0.00001;
  private final double MAX_DISTANCE = 35; // This is the maximum accurate distace the sensor can read in centimeters
  private final double MIN_DISTANCE = 4.5; // This is the minimum accurate distace the sensor can read in centimeters
  private final double DISTANCE_MULTIPLIER = 12.84;
  private final double VOLTAGE_EXPONENT = -0.9824;

  // TODO: In need of testing to define optimal distance
  final double BALL_PRESENT_DISTANCE = 10;

  public MagazineSubsystem() {
    ballSensor = new AnalogInput(Constants.AIN_ballSensor);
    m_horizontalMagazineMotor = new WPI_VictorSPX(Constants.CANID_horizontalMagazineMotor);
    m_verticalMagazineMotor = new WPI_VictorSPX(Constants.CANID_verticalMagazineMotor);
  }

  public void setMagazines(double speed) {
    m_horizontalMagazineMotor.set(speed);
    m_verticalMagazineMotor.set(speed);
  }

  private double getDistance() {
    double voltage = 0;
    double distance = 0;

    // Don't allow zero/negative values
    voltage = Math.max(ballSensor.getVoltage(), MIN_VOLTAGE);
    distance = DISTANCE_MULTIPLIER * Math.pow(voltage, VOLTAGE_EXPONENT);

    // Constrain output
    distance = Math.max(Math.min(distance, MAX_DISTANCE), MIN_DISTANCE);

    System.out.println("distance = " + distance + "   voltage = " + voltage);
    return distance;
  }

  public boolean ballIsPresent() {
    boolean ballPresent = false;
    if(getDistance() > BALL_PRESENT_DISTANCE){
      ballPresent = false;
      System.out.println("false");
    }
    if(getDistance() <BALL_PRESENT_DISTANCE){
      ballPresent = true;
      System.out.println("true");
    }
    return ballPresent;
  }

  public void startShooting() {
    m_shooting = true;
  }

  public void stopShooting() {
    m_shooting = false;
  }

  public void startIntakeMode() {
    m_intake = true;
  }

  public void stopIntakeMode() {
    m_intake = false;
  }

  public void startEjectMode() {
    m_eject = true;
  }

  public void stopEjectMode() {
    m_eject = false;
  }

  public boolean isMagazineFull() {
    return false;
    //TODO determine what this method should return.
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    if (m_shooting) {

    } else if (m_eject) {

    } else if (isMagazineFull()) {

    } else if (m_intake) {

    } else {

    }
  }
}
