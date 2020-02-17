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
import frc.robot.commands.Drive;
import frc.robot.commands.PositionControl;
import frc.robot.commands.RotationalControl;
import frc.robot.subsystems.DriveTrain;
import frc.robot.subsystems.IntakeSubsystem;
import frc.robot.subsystems.Rumble;
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
  // The robot's subsystems and commands are defined here...
  private DriveTrain m_driveTrain;
  private ADIS16470_IMU m_gyro;
  // private Command m_autoCommand = new Command();
  private IntakeSubsystem m_intakeSubsystem = new IntakeSubsystem();
  private Rumble m_driveRumble = new Rumble(driveController);
  private Rumble m_opRumble = new Rumble(opController);

  private ColorSpinner m_colorSpinner = new ColorSpinner();
  private RotationalControl m_rotationControlCmd = new RotationalControl(m_colorSpinner);
  private PositionControl m_positionControlCmd = new PositionControl(m_colorSpinner);
  /**
   * The container for the robot. Contains subsystems, OI devices, and commands.
   */
  public RobotContainer() {
    m_gyro = new ADIS16470_IMU();
    m_gyro.calibrate();
    m_driveTrain = new DriveTrain( ()-> Rotation2d.fromDegrees( m_gyro.getAngle() )  );

    // set default commands here
    m_driveTrain.setDefaultCommand(new Drive(m_driveTrain, m_driveRumble ) );
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
    // new Joystick(driveController,
    // XboxController.Button.kA.value).whenPressed(intakeShaftCommandName);
    // new Joystick(driveController,
    // XboxController.Button.kB.value).whenPressed(intakeDownArmCommandName.withTimeout(double));
    // new Joystick(driveController,
    // XboxController.Button.kX.value).whenPressed(intakeUpArmCommandName.withTimeout(double));
    
    // Color Spinner Left
    new JoystickButton(opController, XboxController.Button.kBumperLeft.value)
        .whileHeld(() -> m_colorSpinner.spinMotor(-.1), m_colorSpinner)
        .whenReleased(() -> m_colorSpinner.spinMotor(0), m_colorSpinner);

    // Color Spinner Right
    new JoystickButton(opController, XboxController.Button.kBumperRight.value)
        .whileHeld(() -> m_colorSpinner.spinMotor(.1), m_colorSpinner)
        .whenReleased(() -> m_colorSpinner.spinMotor(0), m_colorSpinner);

    // Color Spinner Arm Up (op)
    new POVButton( opController, Dpad.kUP.getAngle() )
        .whileHeld(() -> m_colorSpinner.raiseArm(), m_colorSpinner)
        .whenReleased(() -> m_colorSpinner.stopArm(), m_colorSpinner);

    // Color Spinner Arm Down (op)
    new POVButton( opController, Dpad.kDown.getAngle() )
        .whileHeld(() -> m_colorSpinner.lowerArm(), m_colorSpinner)
        .whenReleased(() -> m_colorSpinner.stopArm(), m_colorSpinner);
    
    // Color Spinner Arm Up (drive)    
    new POVButton( driveController, Dpad.kUP.getAngle() )
        .whileHeld(() -> m_colorSpinner.raiseArm(), m_colorSpinner)
        .whenReleased(() -> m_colorSpinner.stopArm(), m_colorSpinner);

    // Color Spinner Arm Down (drive)
    new POVButton( driveController, Dpad.kDown.getAngle() )
        .whileHeld(() -> m_colorSpinner.lowerArm(), m_colorSpinner)
        .whenReleased(() -> m_colorSpinner.stopArm(), m_colorSpinner);
    
    // toggle Rotational Control on/off
    new JoystickButton(opController, XboxController.Button.kX.value)
        .toggleWhenPressed(m_rotationControlCmd);
  
    // toggle Positional Control on/off
    new JoystickButton(opController, XboxController.Button.kY.value)
        .toggleWhenPressed(m_positionControlCmd);

  }

  public static XboxController driveController = new XboxController(Constants.USB_driveController);
  public static XboxController opController = new XboxController(Constants.USB_opController);
  
  public enum Dpad{
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

  public static boolean isLeftOpTriggerPressed() {
    return opController.getTriggerAxis(Hand.kLeft) > 0.9;
  }

  public static boolean isRightOpTriggerPressed() {
    return opController.getTriggerAxis(Hand.kRight) > 0.9;
  }

  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  public Command getAutonomousCommand() {
    // An Drive will run in autonomous
    return null; // m_autoCommand; // for the time being no Autonomous Command

    /*
     * Victor speed control, work on robo arm motor,
     */

  }
}
