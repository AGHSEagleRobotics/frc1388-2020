
/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.CompDashBoard;
import frc.robot.Constants;
import frc.robot.USBLogging;

import java.lang.Math;

import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;

/**
 * Add your docs here.
 */
public class MagazineSubsystem extends SubsystemBase {
  // Put methods for controlling this subsystem
  // here. Call these from Commands.
  private CompDashBoard m_dashboard;

  // Method Fields
  private final WPI_VictorSPX m_magazineMotor;
  
  // Magazine Status Variables
  private final int BALL_STOPPED_VALUE = 5; // TODO change int value after testing
  private int m_ballPresentCounter = 0;

  private boolean m_shooting = false;
  private boolean m_intake = false;
  private boolean m_eject = false;
  private boolean m_magazineIsFull = false;
  
  // Periodic Variables
  private final double k_magazineShootSpeed = 0.5;
  private final double k_magazineEjectSpeed = -0.5;
  private final double k_magazineIntakeSpeed = 0.2;

  // Infrared Proximity Sensor Fields
  private final AnalogInput m_ballSensor;
  
  private final double MIN_VOLTAGE = 0.00001;
  private final double MAX_DISTANCE = 35; // This is the maximum accurate distace the sensor can read in inches
  private final double MIN_DISTANCE = 4.5; // This is the minimum accurate distace the sensor can read in inches
  private final double DISTANCE_MULTIPLIER = 12.84;
  private final double VOLTAGE_EXPONENT = -0.9824;

  // TODO: In need of testing to define optimal distance
  final double BALL_PRESENT_DISTANCE = 10;

  public MagazineSubsystem( CompDashBoard compDashBoard) {
    m_ballSensor = new AnalogInput(Constants.AIN_ballSensor);
    m_magazineMotor = new WPI_VictorSPX(Constants.CANID_magazineMotor);
    m_dashboard = compDashBoard;
  }

  // TODO change number of motors for magazine; number of motors for magazine is TBD
  public void setMagazine(double speed) {
    m_magazineMotor.set(speed);
  }

  private double getDistance() {
    double voltage = 0;
    double distance = 0;

    // Don't allow zero/negative values
    voltage = Math.max(m_ballSensor.getVoltage(), MIN_VOLTAGE);
    distance = DISTANCE_MULTIPLIER * Math.pow(voltage, VOLTAGE_EXPONENT);

    // Constrain output
    distance = Math.max(Math.min(distance, MAX_DISTANCE), MIN_DISTANCE);

    // System.out.println("distance = " + distance + "   voltage = " + voltage);
    return distance;
  }

  public boolean ballIsPresent() {
    // boolean ballPresent = false;
    // if (getDistance() > BALL_PRESENT_DISTANCE){
    // ballPresent = false;
    // }
    // if (getDistance() < BALL_PRESENT_DISTANCE){
    // ballPresent = true;
    // }
    return getDistance() < BALL_PRESENT_DISTANCE;
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
    // boolean magazineIsFull = false;
    if (ballIsPresent()) {
      // increase ball counter by 1
      m_ballPresentCounter++;
    } else {
      // reset ball present counter to zero
      m_ballPresentCounter = 0;
    }
    // if (m_ballPresentCounter >= BALL_STOPPED_VALUE) {
    // magazineIsFull = true;
    // } else {
    // magazineIsFull = false;
    // }
    // return magazineIsFull;
    return m_ballPresentCounter >= BALL_STOPPED_VALUE;
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    // This variable placement ensures that the method is called every time periodic
    // is run
    m_magazineIsFull = isMagazineFull();
    if (m_shooting) {
      m_magazineMotor.set(k_magazineShootSpeed);
    } else if (m_eject) {
      m_magazineMotor.set(k_magazineEjectSpeed);
      // System.out.println("Ejecting");
    } else if (m_magazineIsFull) {
      m_magazineMotor.set(0);
      // System.out.println("Magazine is full");
    } else if (m_intake) {
      m_magazineMotor.set(k_magazineIntakeSpeed);
    } else { // Run the motors at default speed
      m_magazineMotor.set(0);
    }

    m_dashboard.setMaxCapacity(m_magazineIsFull);
  }
}
