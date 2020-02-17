/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;
import com.fasterxml.jackson.databind.util.RawValue;

/**
 * Add your docs here.
 */
public class ShooterSubsystem extends SubsystemBase {
  private final WPI_TalonFX m_shootMotor;
  private final WPI_VictorSPX m_feedMotor;

  private final int pidIdx = 0;
  private final int timeoutMs = 0;
  // Purpose unclear. Put here for convenience
  /*
  private final double nominalPercentOutForward = 0;
  private final double nominalPercentOutReverse = -0.1;
  private final double peakPercentOutForward = 1;
  private final double peakPercentOutReverse = -1
  */
  private final int kPIDLoopIdx = 0;
  private final double kGains_Velocity_kF = 0;
  private final double kGains_Velocity_kP = 0;
  private final double kGains_Velocity_kI = 0;
  private final double kGains_Velocity_kD = 0;

  // Math variables needed to convert RPM to ticks per second/ticks per
  private final int sensorCyclesPerSecond = 10;   // sensor velocity period is 100 ms
  private final int secPerMin = 60;
  private final int countsPerRev = 2048;


  public ShooterSubsystem() {
    m_shootMotor = new WPI_TalonFX(Constants.CANID_shootMotor);
    m_feedMotor = new WPI_VictorSPX(Constants.CANID_feedMotor);
  
    // Factory Default all hardware to prevent unexpected behaviour
    m_shootMotor.configFactoryDefault();
    m_shootMotor.setNeutralMode(NeutralMode.Coast);
    m_shootMotor.configSelectedFeedbackSensor(FeedbackDevice.IntegratedSensor, pidIdx, timeoutMs);

    m_shootMotor.setInverted(true);
    m_shootMotor.setSensorPhase(true);

    // Purpose unclear. Put here for convenience
    /*
    m_shootMotor.configNominalOutputForward(nominalPercentOutForward, timeoutMs);
    m_shootMotor.configNominalOutputReverse(nominalPercentOutReverse, timeoutMs);
    m_shootMotor.configPeakOutputForward(peakPercentOutForward, timeoutMs);
    m_shootMotor.configPeakOutputForward(peakPercentOutReverse, timeoutMs);
    */

    m_shootMotor.config_kF(kPIDLoopIdx, kGains_Velocity_kF, timeoutMs);
    m_shootMotor.config_kP(kPIDLoopIdx, kGains_Velocity_kP, timeoutMs);
    m_shootMotor.config_kI(kPIDLoopIdx, kGains_Velocity_kI, timeoutMs);
    m_shootMotor.config_kD(kPIDLoopIdx, kGains_Velocity_kD, timeoutMs);


  }

  public void setShooterRpm(double RPM) {
    double speed = RPM * countsPerRev / sensorCyclesPerSecond / secPerMin;

    m_shootMotor.set(ControlMode.Velocity, speed);
  }

  public void setFeedMotor(double speed) {
    m_feedMotor.set(speed);
  }

  public double getShooterRpm() {
    double rawVelocity = m_shootMotor.getSelectedSensorVelocity();   // counts per cycle (100 ms)

    double rpm = rawVelocity * sensorCyclesPerSecond * secPerMin / countsPerRev;
    return rpm;
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    // This variable placement ensures that the method is called every time periodic is run
    System.out.println("RPM = " + getShooterRpm() + "  power = " + m_shootMotor.get());
    setShooterRpm(500); // TODO: Remove testing code

  }
}
