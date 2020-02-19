/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;  
import frc.robot.USBLogging.Level;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the TimedRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {

  private Command m_autonomousCommand;
  private Timer timer = new Timer();
  private boolean climberOn = false;

  private RobotContainer m_robotContainer;

  /**
   * This function is run when the robot is first started up and should be used
   * for any initialization code.
   */
  @Override
  public void robotInit() {
    // Start up USB logging
    USBLogging.openLog();
    USBLogging.setLogLevel(Level.DEBUG);


    // print software version - use printLog so this always, always gets printed
    USBLogging.printLog(
        "Git version: " + BuildInfo.GIT_VERSION + " (branch: " + BuildInfo.GIT_BRANCH + BuildInfo.GIT_STATUS + ")");
    USBLogging.printLog("Built: " + BuildInfo.BUILD_DATE + "  " + BuildInfo.BUILD_TIME);

    USBLogging.info("Robot.robotInit()");
    
    // Instantiate our RobotContainer. This will perform all our button bindings,
    // and put our
    // autonomous chooser on the dashboard.
    m_robotContainer = new RobotContainer();

  }

  /**
   * This function is called every robot packet, no matter the mode. Use this for
   * items like diagnostics that you want ran during disabled, autonomous,
   * teleoperated and test.
   *
   * <p>
   * This runs after the mode specific periodic functions, but before LiveWindow
   * and SmartDashboard integrated updating.
   */
  @Override
  public void robotPeriodic() {
    // Runs the Scheduler. This is responsible for polling buttons, adding
    // newly-scheduled
    // commands, running already-scheduled commands, removing finished or
    // interrupted commands,
    // and running subsystem periodic() methods. This must be called from the
    // robot's periodic
    // block in order for anything in the Command-based framework to work.
    CommandScheduler.getInstance().run();

  
  }

  /**
   * This function is called once each time the robot enters Disabled mode.
   */
  @Override
  public void disabledInit() {
    USBLogging.info("########  Robot disabled");

    // turns of limelight ledmode
    NetworkTableInstance.getDefault().getTable("limelight").getEntry("ledMode").setNumber(1);
  }

 
  @Override
  public void disabledPeriodic() {
  }

  /**
   * This autonomous runs the autonomous command selected by your
   * {@link RobotContainer} class.
   */
  @Override
  public void autonomousInit() {
    USBLogging.info("########  Autonomous enabled");

    // Get match info from FMS
    final DriverStation driverStation = DriverStation.getInstance();
    if (driverStation.isFMSAttached()) {
      String fmsInfo = "FMS info: ";
      fmsInfo += " " + driverStation.getEventName();
      fmsInfo += " " + driverStation.getMatchType();
      fmsInfo += " match " + driverStation.getMatchNumber();
      fmsInfo += " replay " + driverStation.getReplayNumber();
      fmsInfo += ";  " + driverStation.getAlliance() + " alliance";
      fmsInfo += ",  Driver Station " + driverStation.getLocation();
      USBLogging.info(fmsInfo);
    } else {
      USBLogging.info("FMS not connected");
    }

    m_autonomousCommand = m_robotContainer.getAutonomousCommand();

    // schedule the autonomous command (example)
    if (m_autonomousCommand != null) {
      m_autonomousCommand.schedule();
    }
  }

  /**
   * This function is called periodically during autonomous.
   */
  @Override
  public void autonomousPeriodic() {
  }

  @Override
  public void teleopInit() {
    USBLogging.info("########  Teleop enabled");

    // This makes sure that the autonomous stops running when
    // teleop starts running. If you want the autonomous to
    // continue until interrupted by another command, remove
    // this line or comment it out.
    if (m_autonomousCommand != null) {
      m_autonomousCommand.cancel();
    }

  }


  /**
   * This function is called periodically during operator control.
   */
  @Override
  public void teleopPeriodic() {
  }

  @Override
  public void testInit() {
    USBLogging.info("########  Test enabled");

    // Cancels all running commands at the start of test mode.
    CommandScheduler.getInstance().cancelAll();
  }

  /**
   * This function is called periodically during test mode.
   */
  @Override
  public void testPeriodic() {
    USBLogging.info("Angle " + m_robotContainer.getGyroAngle());
  }
}
