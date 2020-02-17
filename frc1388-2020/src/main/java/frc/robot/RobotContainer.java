/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import java.util.Map;
import java.util.function.BooleanSupplier;

import com.analog.adis16470.frc.ADIS16470_IMU;

import edu.wpi.cscore.HttpCamera;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.cscore.VideoSink;
import edu.wpi.cscore.VideoSource;
import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.geometry.Rotation2d;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInLayouts;

import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.ComplexWidget;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardLayout;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj.shuffleboard.WidgetType;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import frc.robot.commands.AutonMove;
import frc.robot.commands.AutonMoveShoot;
import frc.robot.commands.AutonShoot;
import frc.robot.commands.DeployIntake;
import frc.robot.commands.Drive;
import frc.robot.commands.Eject;
import frc.robot.commands.RetractIntake;
import frc.robot.commands.LockRackAndPinion;
import frc.robot.commands.LockTrolleyGear;
import frc.robot.commands.Trolley;
import frc.robot.commands.Climb;

import frc.robot.subsystems.ClimberSubsystem;
import frc.robot.subsystems.DriveTrain;
import frc.robot.subsystems.IntakeSubsystem;
import frc.robot.subsystems.MagazineSubsystem;
import frc.robot.subsystems.Rumble;
import frc.robot.subsystems.TrolleySubsystem;
import frc.robot.subsystems.ColorSpinner;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import edu.wpi.first.wpilibj2.command.button.POVButton;

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
  private final int camHeight = 5;
  private final int camWidth = 4;
  private final int colorSpinnerGridHeight = 10;
  private final int colorSpinnerGridWidth = 8;
  
  private static final double DEADBAND_NUM = 0.15;

  // The robot's subsystems and commands are defined here...
  
  // Commands:
  private Eject m_eject;
  private DeployIntake m_deployIntake;
  private RetractIntake m_retractIntake;
  private AutonMove m_autonMove;
  private AutonShoot m_autonShoot;
  private AutonMoveShoot m_autonMoveShoot;
  
  // Subsystems:
  private DriveTrain m_driveTrain; 
  private IntakeSubsystem m_intakeSubsystem = new IntakeSubsystem();
  private MagazineSubsystem m_magazineSubsystem = new MagazineSubsystem();
  private ColorSpinner m_colorSpinner = new ColorSpinner();
  private Rumble m_driveRumble = new Rumble(driveController);
  private Rumble m_opRumble = new Rumble(opController);
  private TrolleySubsystem m_trolleySubsystem = new TrolleySubsystem();
  private ClimberSubsystem m_climberSubsystem = new ClimberSubsystem();
  private Trolley m_trolleyCommand = new Trolley(m_trolleySubsystem);
  private Climb m_climbCommand = new Climb(m_climberSubsystem);
  
  // components 
  public static XboxController driveController = new XboxController(Constants.USB_driveController);
  public static XboxController opController = new XboxController(Constants.USB_opController);
  private ADIS16470_IMU m_gyro;
  // cameras
  private UsbCamera m_cameraIntake;
  private UsbCamera m_cameraClimber;
  private HttpCamera m_limeLight;
  private int m_currVideoSourceIndex = 0;
  private VideoSink m_videoSink;
  private VideoSource[] m_videoSources;

  // shuffleboard
  private ShuffleboardTab shuffleboard;
  private ComplexWidget complexWidgetCam;
  private ComplexWidget complexWidgetAuton;
  private SendableChooser<Command> autonChooser = new SendableChooser<>();
  private NetworkTableEntry MaxCapacityBox;
  private ShuffleboardLayout colorSpinnerGrid;
  private BooleanSupplier blueColorSupplier;
  private BooleanSupplier redColorSupplier;
  private BooleanSupplier yellowColorSupplier;
  private BooleanSupplier greenColorSupplier;
  /**
   * The container for the robot.  Contains subsystems, OI devices, and commands.
   */
  public RobotContainer() {

    m_gyro = new ADIS16470_IMU();
    m_gyro.calibrate();

    m_driveTrain = new DriveTrain( ()-> Rotation2d.fromDegrees( m_gyro.getAngle() )  );

    m_eject = new Eject(m_intakeSubsystem, m_magazineSubsystem);
    m_deployIntake = new DeployIntake(m_intakeSubsystem, m_magazineSubsystem);
    m_retractIntake = new RetractIntake(m_intakeSubsystem, m_magazineSubsystem);
    m_autonMove = new AutonMove(m_driveTrain);

    // set default commands here
    m_driveTrain.setDefaultCommand(new Drive(m_driveTrain, m_driveRumble ) );
    CommandScheduler.getInstance().registerSubsystem(m_magazineSubsystem);

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

    shuffleboard = Shuffleboard.getTab("Competition");

    complexWidgetCam = shuffleboard
      .add(m_videoSink.getSource())
      .withWidget(BuiltInWidgets.kCameraStream)
      .withSize(camHeight, camWidth)
      .withProperties(Map.of("Show Crosshair", true, "Show Controls", false, "Title", "Camera"));

    autonChooser.addOption("Nothing", null );
    autonChooser.addOption("Move", m_autonMove );
    autonChooser.addOption("Shoot", m_autonShoot );
    autonChooser.addOption("MoveShoot", m_autonMoveShoot );
    // autonChooser.addOption("FiveShoot", "FiveShoot" );
    // autonChooser.addOption("EightShoot", "EightShoot" );

    complexWidgetAuton = shuffleboard
      .add(autonChooser)
      .withWidget(BuiltInWidgets.kComboBoxChooser);

    MaxCapacityBox = shuffleboard
      .add("MaxCapacity", false)
      .withWidget(BuiltInWidgets.kBooleanBox)
      .withProperties(Map.of("colorWhenTrue", "green", "colorWhenFalse", "grey" ))
      .getEntry();

    colorSpinnerGrid = shuffleboard
      .getLayout("Color Spinner", BuiltInLayouts.kGrid)
      .withSize( colorSpinnerGridWidth, colorSpinnerGridHeight );

    // TODO get the suppliers to work
    colorSpinnerGrid
      .addBoolean("Blue", blueColorSupplier )
      .withWidget(BuiltInWidgets.kBooleanBox);
    colorSpinnerGrid
      .addBoolean("Red", redColorSupplier)
      .withWidget(BuiltInWidgets.kBooleanBox);
    colorSpinnerGrid
      .addBoolean("Green", greenColorSupplier)
      .withWidget(BuiltInWidgets.kBooleanBox);
    colorSpinnerGrid
      .addBoolean("Yellow", yellowColorSupplier)
      .withWidget(BuiltInWidgets.kBooleanBox);

    // sets the pipeline of the limelight
    NetworkTableInstance.getDefault().getTable("limelight").getEntry("pipeline").setNumber(visionDrivePipeline);
    // NetworkTableInstance.getDefault().getTable("limelight").getEntry("pipeline").setNumber(visionProcessPipeline);
    NetworkTableInstance.getDefault().getTable("limelight").getEntry("camMode").setNumber(1);

    m_driveTrain = new DriveTrain( ()-> Rotation2d.fromDegrees( m_gyro.getAngle() )  );

    m_eject = new Eject(m_intakeSubsystem, m_magazineSubsystem);
    m_deployIntake = new DeployIntake(m_intakeSubsystem, m_magazineSubsystem);
    m_retractIntake = new RetractIntake(m_intakeSubsystem, m_magazineSubsystem);

    // set default commands here
    m_driveTrain.setDefaultCommand(new Drive(m_driveTrain, m_driveRumble ) );
    CommandScheduler.getInstance().registerSubsystem(m_magazineSubsystem);
    // m_climberSubsystem.setDefaultCommand(m_climbCommand);
    // m_trolleySubsystem.setDefaultCommand(m_trolleyCommand);
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

    new JoystickButton(opController, XboxController.Button.kX.value).whenPressed( new LockRackAndPinion() );
    // have a similar approach as the aboves yet using the dpad directional 
    new POVButton( opController, Dpad.kLeft.getAngle() ).whenPressed( new LockTrolleyGear() );
    new JoystickButton(opController, XboxController.Button.kBumperRight.value)
    .whileHeld(() -> m_colorSpinner.spinMotor(-1) );
    new JoystickButton(opController, XboxController.Button.kBack.value).whenPressed(this::switchVideoSource );
    new JoystickButton(driveController, XboxController.Button.kBack.value).whenPressed(this::switchVideoSource );
  }
  public static enum Dpad{
    kUP(0),
    kUpRight(45),
    kRight(90),
    kDownRight(135),
    kDown(180),
    kDownLeft(225),
    kLeft(270),
    kUpLeft(315),
    none(-1);

    private int angle;

    Dpad( int angle ){
      this.angle = angle;
    }

    public int getAngle(){
      return angle;
    }
  }
  
  private static double deadBand( double input ){
    if(input < DEADBAND_NUM && input < DEADBAND_NUM ){
      return 0.0;
    }else{
      if( input > 0 ){
        return ( ( (1 + DEADBAND_NUM ) * input ) - DEADBAND_NUM);
      }else{
        return ( ( (1 + DEADBAND_NUM ) * input ) + DEADBAND_NUM);
      }
    }
  }

  public static double getDriveRightXAxis() {
    return driveController.getX(Hand.kRight);
  }

  public static double getDriveLeftYAxis() {
    return driveController.getY(Hand.kLeft);
  }

  public static double getOpRightXAxis() {
    return deadBand( driveController.getX(Hand.kRight) );
  }

  public static double getOpLeftYAxis() {
    return deadBand(driveController.getY(Hand.kLeft));
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

  public Trolley getTrolley(){
    return m_trolleyCommand;
  }

  public  Climb getClimb(){
    return m_climbCommand;
  }


  // Cam stuff lol

  private void switchVideoSource() {
    m_currVideoSourceIndex = (m_currVideoSourceIndex + 1) % m_videoSources.length;
    m_videoSink.setSource( m_videoSources[m_currVideoSourceIndex] );
  }

  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  public Command getAutonomousCommand() {
    // An Drive will run in autonomous 
    return autonChooser.getSelected();
  }
  

}
