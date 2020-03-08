/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import com.analog.adis16470.frc.ADIS16470_IMU;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.geometry.Rotation2d;
import frc.robot.CompDashBoard.EscapePlan;
import frc.robot.commands.AutonMove;
import frc.robot.commands.AutonShootMove;
import frc.robot.commands.DeployIntake;
import frc.robot.commands.Drive;
import frc.robot.commands.PositionControl;
import frc.robot.commands.RotationalControl;
import frc.robot.commands.SpinnerArm;
import frc.robot.commands.Eject;
import frc.robot.commands.IntakeDefault;
import frc.robot.commands.RetractIntake;
import frc.robot.commands.Climb;
import frc.robot.commands.MultiShot;
import frc.robot.commands.Trolley;
import frc.robot.commands.UnjamIntake;
import frc.robot.commands.DeveloperMode;

import frc.robot.subsystems.ClimberSubsystem;
import frc.robot.subsystems.DriveTrain;
import frc.robot.subsystems.IntakeSubsystem;
import frc.robot.subsystems.MagazineSubsystem;
import frc.robot.subsystems.Rumble;
import frc.robot.subsystems.TrolleySubsystem;
import frc.robot.subsystems.ShooterSubsystem;
import frc.robot.subsystems.ColorSpinner;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.button.Button;
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
  
  private static final double DEADBAND_NUM = 0.15;
  private final double k_intakeShaftRetractSpeed = -0.2;
  
  // The robot's subsystems and commands are defined here...
  private AutonMove m_autonMove;
  
  private CompDashBoard m_compDashboard;

  // Subsystems:
  private DriveTrain m_driveTrain; 
  private ColorSpinner m_colorSpinner;
  private ShooterSubsystem m_shooterSubsystem;
  private MagazineSubsystem m_magazineSubsystem;
  private IntakeSubsystem m_intakeSubsystem;
  private Rumble m_driveRumble = new Rumble(driveController);
  private Rumble m_opRumble = new Rumble(opController);
  private TrolleySubsystem m_trolleySubsystem;
  private ClimberSubsystem m_climberSubsystem;

  // Commands:
  private Trolley m_trolleyCommand;
  private Climb m_climbCommand;
  private RotationalControl m_rotationControlCmd;
  private PositionControl m_positionControlCmd;
  private SpinnerArm m_spinnerArmUp;
  private SpinnerArm m_spinnerArmDown;
  private MultiShot m_multiShot;
  private Eject m_eject;
  private DeployIntake m_deployIntake;
  private RetractIntake m_retractIntake;
  private DeveloperMode m_developerMode;
  private UnjamIntake m_unjamIntake;

  // components 
  public static XboxController driveController = new XboxController(Constants.USB_driveController);
  public static XboxController opController = new XboxController(Constants.USB_opController);
  private ADIS16470_IMU m_gyro;
  
  

  /**
   * The container for the robot.  Contains subsystems, OI devices, and commands.
   */
  public RobotContainer() {
    m_compDashboard = new CompDashBoard();

    m_gyro = new ADIS16470_IMU();
    m_gyro.calibrate();

    // construct subsystems
    m_driveTrain = new DriveTrain( ()-> Rotation2d.fromDegrees( m_gyro.getAngle() )  );
    m_shooterSubsystem = new ShooterSubsystem( m_compDashboard );
    m_magazineSubsystem = new MagazineSubsystem( m_compDashboard );
    m_intakeSubsystem = new IntakeSubsystem();
    m_colorSpinner = new ColorSpinner( m_compDashboard );
    m_trolleySubsystem = new TrolleySubsystem();
    m_climberSubsystem = new ClimberSubsystem();

    // construct commands
    m_multiShot = new MultiShot(m_shooterSubsystem, m_magazineSubsystem);
    m_eject = new Eject(m_intakeSubsystem, m_magazineSubsystem);
    m_deployIntake = new DeployIntake(m_intakeSubsystem, m_magazineSubsystem);
    m_retractIntake = new RetractIntake(m_intakeSubsystem, m_magazineSubsystem);
    m_unjamIntake = new UnjamIntake(m_intakeSubsystem);

    // m_autonMove = new AutonMove(
    //     m_driveTrain,                     // dependecy
    //     AutonMove.Mode.kDistanceDrive,    // drive mode
    //     1,                                // drive distance (feet)
    //     0.5,                              // drive speed (%)
    //     0,                                // rotation control
    //     false);                           // quick turn

    m_rotationControlCmd  = new RotationalControl(m_colorSpinner);
    m_positionControlCmd = new PositionControl(m_colorSpinner);
    m_spinnerArmUp = new SpinnerArm(m_colorSpinner, SpinnerArm.Direction.kUp);
    m_spinnerArmDown = new SpinnerArm(m_colorSpinner, SpinnerArm.Direction.kDown);
    m_trolleyCommand = new Trolley(m_trolleySubsystem);
    m_climbCommand = new Climb(m_climberSubsystem);
    m_developerMode = new DeveloperMode(m_shooterSubsystem);

    // register subsystems so the scheduler runs periodically
    CommandScheduler.getInstance().registerSubsystem(m_magazineSubsystem);
    CommandScheduler.getInstance().registerSubsystem(m_colorSpinner);
    CommandScheduler.getInstance().registerSubsystem(m_magazineSubsystem);
    
    // set default commands here
    m_driveTrain.setDefaultCommand(new Drive(m_driveTrain, m_driveRumble, m_compDashboard ) );
    m_intakeSubsystem.setDefaultCommand(new IntakeDefault(m_intakeSubsystem, m_magazineSubsystem, m_retractIntake));
    m_climberSubsystem.setDefaultCommand(m_climbCommand);
    m_trolleySubsystem.setDefaultCommand(m_trolleyCommand);
    // Configure the button bindings
    configureButtonBindings();

    CommandScheduler.getInstance()
        .onCommandInitialize(command -> USBLogging.printCommandStatus(command, "initialized"));

    CommandScheduler.getInstance().onCommandFinish(command -> USBLogging.printCommandStatus(command, "Finished"));

    CommandScheduler.getInstance().onCommandInterrupt(command -> USBLogging.printCommandStatus(command, "Interrupted"));

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

    // ========================================
    // Shooter
    // ========================================
    
        // Multi Shot
    new JoystickButton(driveController, XboxController.Button.kBumperRight.value)
        .whenHeld(m_multiShot);
    new JoystickButton(opController, XboxController.Button.kY.value)
       .whenPressed(() -> m_shooterSubsystem.presetRPMUp());
    new JoystickButton(opController, XboxController.Button.kX.value)
       .whenPressed(() -> m_shooterSubsystem.presetRPMDown());
    //     // Developer Mode
    // new Button(() -> driveController.getTriggerAxis(Hand.kLeft) > 0)
    //    .whenHeld(m_developerMode);
  
    // ========================================
    // Intake
    // ========================================

    // Deploy Intake Arm
    new JoystickButton(driveController, XboxController.Button.kA.value)
        .whenPressed(m_deployIntake);
    new JoystickButton(opController, XboxController.Button.kA.value)
        .whenPressed(m_deployIntake);
    // Retract Intake Arm
    new JoystickButton(driveController, XboxController.Button.kB.value)
        .whenPressed(m_retractIntake);
    new JoystickButton(opController, XboxController.Button.kB.value)
        .whenPressed(m_retractIntake);
    // Eject
    new JoystickButton(driveController, XboxController.Button.kStickRight.value)
        .whileHeld(m_eject);
    new JoystickButton(opController, XboxController.Button.kStickRight.value)
        .whileHeld(m_eject);
    // UnjamIntake
    new JoystickButton(opController, XboxController.Button.kStickLeft.value)
        .whenHeld(m_unjamIntake);
    
    // ========================================
    // Color Spinner
    // ========================================

    // Color Spinner Left
    new JoystickButton(opController, XboxController.Button.kBumperLeft.value)
        .whileHeld(() -> m_colorSpinner.spinMotor(-1.0), m_colorSpinner)
        .whenReleased(() -> m_colorSpinner.spinMotor(0), m_colorSpinner);

    // Color Spinner Right (op)
    new JoystickButton(opController, XboxController.Button.kBumperRight.value)
        .whileHeld(() -> m_colorSpinner.spinMotor(1.0), m_colorSpinner)
        .whenReleased(() -> m_colorSpinner.spinMotor(0), m_colorSpinner);

    // Color Spinner Arm Up (op)
    new POVButton( opController, Dpad.kUP.getAngle())
        .whenHeld(m_spinnerArmUp);
    // Color Spinner Arm Up (drive)    
    new POVButton( driveController, Dpad.kUP.getAngle())
        .whenHeld(m_spinnerArmUp);
        
        // Color Spinner Arm Down (op)
    new POVButton( opController, Dpad.kDown.getAngle())
        .whenHeld(m_spinnerArmDown);
    // Color Spinner Arm Down (drive)
    new POVButton( driveController, Dpad.kDown.getAngle())
        .whenHeld(m_spinnerArmDown);
    
    // toggle Rotational Control on/off
    new JoystickButton(driveController, XboxController.Button.kX.value)
        .toggleWhenPressed(m_rotationControlCmd);
    // toggle Positional Control on/off
    new JoystickButton(driveController, XboxController.Button.kY.value)
        .toggleWhenPressed(m_positionControlCmd);


    // ========================================
    // View Camera Toggle
    // ========================================

    // Toggle Camera Source (op)
    new JoystickButton(opController, XboxController.Button.kBack.value)
        .whenPressed( m_compDashboard::switchVideoSource );
    // Toggle Camera Source (drive)
    new JoystickButton(driveController, XboxController.Button.kBack.value)
        .whenPressed( m_compDashboard::switchVideoSource );
    // toggle limelight led mode (drive)
    new JoystickButton(driveController, XboxController.Button.kStart.value)
        .whenPressed( m_compDashboard::toggleLimelightLED );
    // toggle limelight led mode (op)
    new JoystickButton(opController, XboxController.Button.kStart.value)
        .whenPressed( m_compDashboard::toggleLimelightLED );


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
    if(input < DEADBAND_NUM && input > -DEADBAND_NUM ){
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
    return deadBand( opController.getX(Hand.kRight) );
  }

  public static double getOpLeftYAxis() {
    return deadBand(opController.getY(Hand.kLeft));
  }

  public static boolean getAButton() {
    return driveController.getAButton();
  }

  public static boolean getLeftStickButton() {
    return driveController.getStickButton(Hand.kLeft);
  }

  public static boolean isLeftOpTriggerPressed() {
    return opController.getTriggerAxis(Hand.kLeft) > 0.9;
  }

  public static boolean isRightOpTriggerPressed() {
    return opController.getTriggerAxis(Hand.kRight) > 0.9;
  }

  public static boolean getDriveLeftBumber(){
    return driveController.getBumper(Hand.kLeft);
  }

  public Trolley getTrolley(){
    return m_trolleyCommand;
  }

  public  Climb getClimb(){
    return m_climbCommand;
  }


  public Command getAutonCommand() {
    EscapePlan escapePlan = m_compDashboard.getSelectedEscapePlan();
    switch( m_compDashboard.getSelectedObjective() ){
      case MOVE:
      return new AutonMove(
        m_driveTrain,                     // dependecy
        AutonMove.Mode.kDistanceDrive,    // drive mode
        escapePlan.getDistance(),         // drive distance (inches)
        0.4,                              // drive speed (%)
        0,                                // rotation control
        false);                           // quick turn
      case SHOOT:
      return new MultiShot(m_shooterSubsystem, m_magazineSubsystem); // return m_multiShot.withTimeout( );
      case SHOOTMOVE:
      return new AutonShootMove(
        m_shooterSubsystem,
        m_magazineSubsystem,
        m_driveTrain,
        AutonMove.Mode.kDistanceDrive,    // drive mode
        escapePlan.getDistance()); // return m_shootMove;
      case NOTHING:
      return null;
      default:
      return null;
  }
  }

}

