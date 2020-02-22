/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.USBLogging;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;


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
  private final double kGains_Velocity_kF = 0.05;  // no load motor testing: .056=1000 .05=2000, .048=3000, .047=4000
  private final double kGains_Velocity_kP = 0.0;
  private final double kGains_Velocity_kI = 0;
  private final double kGains_Velocity_kD = 0;

  // Math variables needed to convert RPM to ticks per second/ticks per
  private final int sensorCyclesPerSecond = 10;   // sensor velocity period is 100 ms
  private final int secPerMin = 60;
  private final int countsPerRev = 2048;

  private double m_rpm = 0;


  // Developer mode should be removed after the shooter has been characterized.
  private boolean developerMode = true;   // Make sure this is set to false when not testing code!

  private final double shooterRpmFromStartingLine = 1000;   // TODO: determine value wanted
  private final double shooterRpmFromNearTrench = 2000;     // TODO: determine value wanted
  private final double shooterRpmFromFarTrench = 3000;      // TODO: determine value wanted

  private final double[] presetList = {   // make sure these values are in increasing order!
    shooterRpmFromStartingLine,
    shooterRpmFromNearTrench,
    shooterRpmFromFarTrench,
  };

  private final double[] developerPresetList = {    // Temporary list for characterizing the shooter
    3000, 3100, 3200, 3300, 3400, 3500, 3600, 3700, 3800, 3900,
    4000, 4100, 4200, 4300, 4400, 4500, 4600, 4700, 4800, 4900,
  };

  /** List of presets available to the operator */
  private double[] m_rpmPresetList;

  /** Index of the current preset */
  private int m_presetIndex = 0;

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

    if (developerMode) {
      m_rpmPresetList = developerPresetList;
      USBLogging.warning("SHOOTER DEVELOPMENT MODE!");
    }
    else {
      m_rpmPresetList = presetList;
    }

    m_rpm = m_rpmPresetList[m_presetIndex];

  }

  public void startShooter() {
    double speed = m_rpm * countsPerRev / sensorCyclesPerSecond / secPerMin;
    m_shootMotor.set(ControlMode.Velocity, speed);
  }

  public void setShooterRPM(double rpm) {
    m_rpm = rpm;
  }

  /**
   * Get the actual, current shooter RPM
   * @return double Current RPM of the shooter
   */

  public double getShooterRPM() {
    double rawVelocity = m_shootMotor.getSelectedSensorVelocity();   // counts per cycle (100 ms)

    double rpmActual = rawVelocity * sensorCyclesPerSecond * secPerMin / countsPerRev;
    return rpmActual;
  }

  public void usePresetRPM() {
    m_rpm = m_rpmPresetList[m_presetIndex];
  }

  public void presetRPMUp() {
    if(m_presetIndex < (m_rpmPresetList.length -1)) {
      m_presetIndex++;
    }
    usePresetRPM();
  }

  public void presetRPMDown() {
    if(m_presetIndex > 0) {
      m_presetIndex--;
    }
    usePresetRPM();
  }

  public void stopShooter() {
    m_shootMotor.set(0);
  }

  public void setFeeder(double speed) {
    m_feedMotor.set(speed);
  }

  public void stopFeeder() {
    m_feedMotor.set(0);
  }


  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    // This variable placement ensures that the method is called every time periodic is run
    System.out.println("RPM = " + getShooterRPM());
  }
}
