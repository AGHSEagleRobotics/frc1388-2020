/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import java.util.Map;

import com.analog.adis16470.frc.ADIS16470_IMU;

import edu.wpi.cscore.HttpCamera;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.cscore.VideoSink;
import edu.wpi.cscore.VideoSource;
import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.networktables.NetworkTableInstance;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.geometry.Rotation2d;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.ComplexWidget;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import frc.robot.commands.AutonMove;
import frc.robot.commands.DeployIntake;
import frc.robot.commands.Drive;
import frc.robot.commands.Eject;
import frc.robot.commands.RetractIntake;
import frc.robot.subsystems.DriveTrain;
import frc.robot.subsystems.IntakeSubsystem;
import frc.robot.subsystems.MagazineSubsystem;
import frc.robot.subsystems.Rumble;
import frc.robot.subsystems.ColorSpinner;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;

/**
 * This class is where the bulk of the robot should be declared. Since
 * Command-based is a "declarative" paradigm, very little robot logic should
 * actually be handled in the {@link Robot} periodic methods (other than the
 * scheduler calls). Instead, the structure of the robot (including subsystems,
 * commands, and button mappings) should be declared here.
 */
public class RobotContainer {
  private final int visionProcessPipeline = 0;
  private final int visionDrivePipeline = 1;

  // The robot's subsystems and commands are defined here...

  // Commands:
  private Eject m_eject;
  private DeployIntake m_deployIntake;
  private RetractIntake m_retractIntake;
  private AutonMove m_autonMove;
  
  // Subsystems:
  private DriveTrain m_driveTrain; 
  private IntakeSubsystem m_intakeSubsystem = new IntakeSubsystem();
  private MagazineSubsystem m_magazineSubsystem = new MagazineSubsystem();
  private ColorSpinner m_colorSpinner = new ColorSpinner();
  private Rumble m_driveRumble = new Rumble(driveController);
  private Rumble m_opRumble = new Rumble(opController);
  
  // components 
  private ADIS16470_IMU m_gyro;
  private UsbCamera m_cameraIntake;
  private UsbCamera m_cameraClimber;
  private HttpCamera m_limeLight;
  private int m_currVideoSourceIndex = 0;
  private VideoSink m_videoSink;
  private VideoSource[] m_videoSources;
  private ComplexWidget complexWidget;

  /**
   * The container for the robot.  Contains subsystems, OI devices, and commands.
   */
  public RobotContainer() {

    m_gyro = new ADIS16470_IMU();
    m_gyro.calibrate();

    m_cameraIntake = CameraServer.getInstance().startAutomaticCapture(Constants.USB_cameraIntake);
    // m_cameraClimber = CameraServer.getInstance().startAutomaticCapture( Constants.USB_cameraClimber);

    m_cameraIntake.setConnectVerbose(0);

    m_limeLight = new HttpCamera("limelight", "http://limelight.local:5800/stream.mjpg");
    
    m_videoSources = new VideoSource[]{
      m_limeLight, 
      m_cameraIntake
    };

    m_videoSink = CameraServer.getInstance().getServer();

    m_videoSink.setSource(m_cameraIntake);

    ShuffleboardTab shuffleboard = Shuffleboard.getTab("SmartDashboard");

    complexWidget = shuffleboard
      .add(m_videoSink.getSource())
      .withWidget(BuiltInWidgets.kCameraStream)
      .withProperties(Map.of("Show Crosshair", true, "Show Controls", false));

    // sets the pipeline of the limelight
    NetworkTableInstance.getDefault().getTable("limelight").getEntry("pipeline").setNumber(visionDrivePipeline);
    // NetworkTableInstance.getDefault().getTable("limelight").getEntry("pipeline").setNumber(visionProcessPipeline);
    NetworkTableInstance.getDefault().getTable("limelight").getEntry("camMode").setNumber(1);

    m_driveTrain = new DriveTrain( ()-> Rotation2d.fromDegrees( m_gyro.getAngle() )  );

    m_eject = new Eject(m_intakeSubsystem, m_magazineSubsystem);
    m_deployIntake = new DeployIntake(m_intakeSubsystem, m_magazineSubsystem);
    m_retractIntake = new RetractIntake(m_intakeSubsystem, m_magazineSubsystem);
    m_autonMove = new AutonMove(m_driveTrain);

    // set default commands here
    m_driveTrain.setDefaultCommand(new Drive(m_driveTrain, m_driveRumble ) );
    CommandScheduler.getInstance().registerSubsystem(m_magazineSubsystem);

    // Configure the button bindings
    configureButtonBindings();

    CommandScheduler.getInstance()
        .onCommandInitialize(command -> USBLogging.printCommandStatus(command, "initialized"));

    CommandScheduler.getInstance().onCommandFinish(
      command -> USBLogging.printCommandStatus(command, "FINISHeD"));

    CommandScheduler.getInstance().onCommandInterrupt(
      command -> USBLogging.printCommandStatus(command, "Interrupted"));

  }

  public double getGyroAngle(){
    return m_gyro.getAngle();
  }

  /**
   * Use this method to define your button->command mappings. Buttons can be
   * created by instantiating a {@link GenericHID} or one of its subclasses
   * ({@link edu.wpi.first.wpilibj.Joystick} or {@link XboxController}), and then
   * passing it to a {@link edu.wpi.first.wpilibj2.command.button.JoystickButton}.
   */
  private void configureButtonBindings() {
    new JoystickButton(driveController, XboxController.Button.kA.value)
        .whenPressed(m_deployIntake);
    new JoystickButton(driveController, XboxController.Button.kB.value)
        .whenPressed(m_retractIntake);
    new JoystickButton(opController, XboxController.Button.kA.value)
        .whenPressed(m_deployIntake);
    new JoystickButton(opController, XboxController.Button.kB.value)
        .whenPressed(m_retractIntake);

    new JoystickButton(opController, XboxController.Button.kY.value)
        .whileHeld(m_eject)
        .whenReleased(() -> m_magazineSubsystem.stopEjectMode());

    new JoystickButton(opController, XboxController.Button.kBumperRight.value)
        .whileHeld(() -> m_colorSpinner.spinMotor(-1) );
    new JoystickButton(opController, XboxController.Button.kBack.value)
        .whenPressed(this::switchVideoSource );
    new JoystickButton(driveController, XboxController.Button.kBack.value)
        .whenPressed(this::switchVideoSource );
  }


  public static XboxController driveController = new XboxController(Constants.USB_driveController);
  public static XboxController opController = new XboxController(Constants.USB_opController);

  public static double getDriveRightXAxis() {
    return driveController.getX(Hand.kRight);
  }

  public static double getDriveLeftYAxis() {
    return driveController.getY(Hand.kLeft);
  }

  public static boolean getAButton() {
    return driveController.getAButton();
  }

  public static boolean getLeftStickButton() {
    return driveController.getStickButton(Hand.kLeft);
  }

  public static boolean getLeftBumper() {
    return opController.getBumper(Hand.kLeft);
  }


  // Cam stuff lol

  private void switchVideoSource() {
    m_currVideoSourceIndex = (m_currVideoSourceIndex+1) % m_videoSources.length;
    m_videoSink.setSource( m_videoSources[m_currVideoSourceIndex] );
  }

  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  public Command getAutonomousCommand() {
    // An Drive will run in autonomous 
    return null; //m_autoCommand; // for the time being no Autonomous Command
  }
 


}
