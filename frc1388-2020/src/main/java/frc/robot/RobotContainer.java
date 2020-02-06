/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import com.analog.adis16448.frc.ADIS16448_IMU;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.geometry.Rotation2d;
import frc.robot.commands.Climb;
import frc.robot.commands.Drive;
import frc.robot.commands.IntakeArmCommand;
import frc.robot.commands.IntakeShaftCommand;
import frc.robot.commands.LockRackAndPinion;
import frc.robot.commands.LockTrolleyGear;
import frc.robot.commands.Trolley;
import frc.robot.subsystems.ClimberSubsystem;
import frc.robot.subsystems.DriveTrain;
import frc.robot.subsystems.IntakeSubsystem;
import frc.robot.subsystems.Rumble;
import frc.robot.subsystems.TrolleySubsystem;
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
  private static final double DEADBAND_NUM = 0.15;

  // The robot's subsystems and commands are defined here...
  // private Command m_autoCommand = new Command();
  private DriveTrain m_driveTrain; 
  private ADIS16448_IMU m_gyro;
  private IntakeSubsystem m_intakeSubsystem = new IntakeSubsystem();
  private IntakeShaftCommand m_intakeShaftCommand = new IntakeShaftCommand(m_intakeSubsystem);
  private IntakeArmCommand m_intakeArmCommand = new IntakeArmCommand(m_intakeSubsystem);
  private Rumble m_driveRumble = new Rumble(driveController);
  private Rumble m_opRumble = new Rumble(opController);
  private TrolleySubsystem m_trolleySubsystem = new TrolleySubsystem();
  private ClimberSubsystem m_climberSubsystem = new ClimberSubsystem();
  private Trolley m_trolleyCommand = new Trolley(m_trolleySubsystem);
  private Climb m_climbCommand = new Climb(m_climberSubsystem);

  /**
   * The container for the robot.  Contains subsystems, OI devices, and commands.
   */
  public RobotContainer() {
    m_gyro = new ADIS16448_IMU();

    m_driveTrain = new DriveTrain( ()-> Rotation2d.fromDegrees( m_gyro.getAngle() )  );



    // set default commands here
    m_driveTrain.setDefaultCommand(new Drive(m_driveTrain, m_driveRumble ) );
    m_climberSubsystem.setDefaultCommand(m_climbCommand);
    m_trolleySubsystem.setDefaultCommand(m_trolleyCommand);
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
    new JoystickButton(opController, XboxController.Button.kX.value).whenPressed( new LockRackAndPinion() );
    // have a similar approach as the aboves yet using the dpad directional 
    new POVButton( opController, Dpad.kLeft.getAngle() ).whenPressed( new LockTrolleyGear() );
  }

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



  public static XboxController driveController = new XboxController(Constants.USB_driveController);
  public static XboxController opController = new XboxController(Constants.USB_opController);

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

  public Rumble getDriveRumble(){
    return m_driveRumble;
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
