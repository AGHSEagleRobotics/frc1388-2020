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
import frc.robot.commands.IntakeArmCommand;
import frc.robot.commands.IntakeShaftCommand;
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
  // The robot's subsystems and commands are defined here...
  private DriveTrain m_driveTrain; 
  private ADIS16470_IMU  m_gyro;
  private IntakeSubsystem m_intakeSubsystem = new IntakeSubsystem();
  private IntakeShaftCommand m_intakeShaftCommand = new IntakeShaftCommand(m_intakeSubsystem);
  private Rumble m_driveRumble = new Rumble(driveController);
  private Rumble m_opRumble = new Rumble(opController);
  private MagazineSubsystem m_magazineSubsystem = new MagazineSubsystem();

  private ColorSpinner m_colorSpinner = new ColorSpinner();
  /**
   * The container for the robot.  Contains subsystems, OI devices, and commands.
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
  //  new JoystickButton(driveController, XboxController.Button.kA.value).whenPressed(m_intakeShaftCommand);
    // TODO Determine correct timeout value.
  //  new JoystickButton(driveController, XboxController.Button.kB.value)
  //      .whenPressed(new IntakeArmCommand(m_intakeSubsystem, true).withTimeout(Double.POSITIVE_INFINITY));
  //  new JoystickButton(driveController, XboxController.Button.kX.value)
  //      .whenPressed(new IntakeArmCommand(m_intakeSubsystem, false).withTimeout(Double.POSITIVE_INFINITY));
    new JoystickButton(opController, XboxController.Button.kBumperRight.value)
        .whileHeld(() -> m_colorSpinner.spinMotor(-1) );

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
