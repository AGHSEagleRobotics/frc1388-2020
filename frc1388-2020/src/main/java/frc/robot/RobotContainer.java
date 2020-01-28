/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import com.analog.adis16448.frc.ADIS16448_IMU;

import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;
import com.revrobotics.ColorSensorV3;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.geometry.Rotation2d;
import frc.robot.commands.Drive;
import frc.robot.commands.IntakeArmCommand;
import frc.robot.commands.IntakeShaftCommand;
import frc.robot.subsystems.DriveTrain;
import frc.robot.subsystems.IntakeSubsystem;
import frc.robot.subsystems.ColorSpinner;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;

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
  private ADIS16448_IMU m_gyro;
  // private Command m_autoCommand = new Command();
  private IntakeSubsystem m_intakeSubsystem = new IntakeSubsystem();
  private IntakeShaftCommand m_intakeShaftCommand = new IntakeShaftCommand(m_intakeSubsystem);
  private IntakeArmCommand m_intakeArmCommand = new IntakeArmCommand(m_intakeSubsystem);

  private final ColorSensorV3 m_colorSensor = new ColorSensorV3(Constants.I2C_Port_ColorSensor);

  // FIXME figure out what motors are on here
  private final WPI_VictorSPX m_spinnermotor = new WPI_VictorSPX(2) ;
 
  private final WPI_VictorSPX m_armMotor = new WPI_VictorSPX(0);

  private final ColorSpinner m_colorSpinner = new ColorSpinner(m_colorSensor, m_spinnermotor, m_armMotor);
  /**
   * The container for the robot.  Contains subsystems, OI devices, and commands.
   */
  public RobotContainer() {
    m_gyro = new ADIS16448_IMU();

    m_driveTrain = new DriveTrain( ()-> Rotation2d.fromDegrees( m_gyro.getAngle() )  );

    // set default commands here
    m_driveTrain.setDefaultCommand(new Drive(m_driveTrain));
    // Configure the button bindings
    configureButtonBindings();

    CommandScheduler.getInstance()
        .onCommandInitialize(command -> USBLogging.printCommandStatus(command, "initialized"));

    CommandScheduler.getInstance().onCommandFinish(
      command -> USBLogging.printCommandStatus(command, "FINISHeD"));

    CommandScheduler.getInstance().onCommandInterrupt(
      command -> USBLogging.printCommandStatus(command, "Interrupted"));

  }

  /**
   * Use this method to define your button->command mappings. Buttons can be
   * created by instantiating a {@link GenericHID} or one of its subclasses
   * ({@link edu.wpi.first.wpilibj.Joystick} or {@link XboxController}), and then
   * passing it to a {@link edu.wpi.first.wpilibj2.command.button.JoystickButton}.
   */
  private void configureButtonBindings() {
    //new Joystick(driveController, XboxController.Button.kA.value).whenPressed(intakeShaftCommandName);
    //new Joystick(driveController, XboxController.Button.kB.value).whenPressed(intakeDownArmCommandName.withTimeout(double));
    //new Joystick(driveController, XboxController.Button.kX.value).whenPressed(intakeUpArmCommandName.withTimeout(double));

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
