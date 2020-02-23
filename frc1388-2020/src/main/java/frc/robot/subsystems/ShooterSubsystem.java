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
  private int tickCount = 0;
  
  // Purpose unclear. Put here for convenience
  // private final double nominalPercentOutForward = 0;
  // private final double nominalPercentOutReverse = -0.1;
  // private final double peakPercentOutForward = 1;
  // private final double peakPercentOutReverse = -1
  
  private final int kPIDLoopIdx = 0;
  private final double kGains_Velocity_kF = 0.05;  // no load motor testing: .056=1000 .05=2000, .048=3000, .047=4000
  private final double kGains_Velocity_kP = 0.25;
  private final double kGains_Velocity_kI = 0;
  private final double kGains_Velocity_kD = 0;

  // Math variables needed to convert RPM to ticks per second/ticks per
  private final int sensorCyclesPerSecond = 10;   // sensor velocity period is 100 ms
  private final int secPerMin = 60;
  private final int countsPerRev = 2048;

  // Current RPM to use
  private double m_rpm = 0;
  // Shooter enabled flag
  private boolean m_enabled = false;

  // Developer mode should be removed after the shooter has been characterized.
  private boolean developerMode = true;   // Make sure this is set to false when not testing code!

  // Values for the array of presets used for competition
  private final double shooterRpmFromStartingLine = 1000;   // TODO: determine value wanted
  private final double shooterRpmFromNearTrench = 2000;     // TODO: determine value wanted
  private final double shooterRpmFromFarTrench = 3000;      // TODO: determine value wanted
 
  // Array of presets intended for use in competition
  private final double[] presetList = {   // make sure these values are in increasing order!
    shooterRpmFromStartingLine,
    shooterRpmFromNearTrench,
    shooterRpmFromFarTrench,
  };

  // TESTING: Array used for RPM testing, hence the "developer" part of the name
  private final double[] developerPresetList = {    // Temporary list for characterizing the shooter
    1000, 1100, 1200, 1300, 1400, 1500, 1600, 1700, 1800, 1900,
    2000, 2100, 2200, 2300, 2400, 2500, 2600, 2700, 2800, 2900,
    3000, 3100, 3200, 3300, 3400, 3500, 3600, 3700, 3800, 3900,
    4000, 4100, 4200, 4300, 4400, 4500, 4600, 4700, 4800, 4900,
    5000, 5100, 5200, 5300, 5400, 5500, 5600, 5700, 5800, 5900,
    6000, 6100, 6200, 6300, 6400, 6500, 6600, 6700, 6800, 6900,
  };

  // List of presets available to the operator
  private double[] m_rpmPresetList;

  // Index of the current preset
  private int m_presetIndex = 0;

  // =======================================
  // Constructor: ShooterSubsystem
  // =======================================

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

    // Used to determine if we are using 3 preset RPM values, or all 60 values
    if (developerMode) {
      m_rpmPresetList = developerPresetList;
      USBLogging.warning("SHOOTER DEVELOPMENT MODE!");
    }
    else {
      m_rpmPresetList = presetList;
    }

    m_rpm = m_rpmPresetList[m_presetIndex];

  } // End Constructor: ShooterSubsystem

  // =======================================
  // Public Methods: ShooterSubsystem
  // =======================================

  // Sets a flag used in periodic to set the motor to the wanted RPM
  public void startShooter() {
    m_enabled = true;
  }

  // Gives the wanted RPM to a periodic function for non-preset values
  public void setShooterRPM(double rpm) {
    m_rpm = rpm;
  }

  // Get the actual, current shooter RPM
  // @return double Current RPM of the shooter
  public double getShooterRPM() {

    double rawVelocity = m_shootMotor.getSelectedSensorVelocity();   // counts per cycle (100 ms)
    // calculate actual velocity in rpm based on raw velocity data
    double rpmActual = rawVelocity * sensorCyclesPerSecond * secPerMin / countsPerRev;
    return rpmActual;
  }

  // Gives the wanted RPM to a periodic function for preset values
  public void usePresetRPM() {
    m_rpm = m_rpmPresetList[m_presetIndex];
  }

  // Changes the RPM preset to the next value in the list
  public void presetRPMUp() {
    if(m_presetIndex < (m_rpmPresetList.length -1)) {
      m_presetIndex++;
    }
    usePresetRPM();
  }

  // Changes the RPM preset to the previous value in the list
  public void presetRPMDown() {
    if(m_presetIndex > 0) {
      m_presetIndex--;
    }
    usePresetRPM();
  }

  // Stops the shooter, RPM will be 0
  public void stopShooter() {
    m_enabled = false;
    m_shootMotor.set(0);
  }

  // Sets the feeder output speed
  public void setFeeder(double speed) {
    m_feedMotor.set(speed);
  }

  // Stops the feeder
  public void stopFeeder() {
    m_feedMotor.set(0);
  }


  @Override
  public void periodic() {
    tickCount++;
    if (tickCount >25 ) {
      USBLogging.printLog("Target = " + m_rpm + "||RPM = " + getShooterRPM());
      tickCount = 0;
    }


    // Flag used to determine if motor should be running,
    //runs motor at the wanted RPM
    if(m_enabled == true) {
      double speed = m_rpm * countsPerRev / sensorCyclesPerSecond / secPerMin;
      m_shootMotor.set(ControlMode.Velocity, speed);
    }

    // Show current RPM on the Dashboard
    // TODO: 

  }

} // End Class: ShooterSubsystem
