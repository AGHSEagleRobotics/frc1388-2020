/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.geometry.Rotation2d;
import edu.wpi.first.wpilibj.kinematics.DifferentialDriveOdometry;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

/**
 * Add your docs here.
 */
public class DriveTrain extends SubsystemBase {
  // Put methods for controlling this subsystem
  // here. Call these from Commands.

  private WPI_TalonSRX leftFront;
  private WPI_TalonSRX rightFront;
  private WPI_TalonSRX leftBack;
  private WPI_TalonSRX rightBack;

  private DifferentialDrive differentialDrive;

  private ADXRS450_Gyro m_gyro;
  private DifferentialDriveOdometry m_odometry;
  private Rotation2d angle;

  private Object leftEncoder;
  private Object rightEncoder;

  public DriveTrain() {
    m_gyro = new ADXRS450_Gyro();
    m_odometry = new DifferentialDriveOdometry(angle);

    leftFront = new WPI_TalonSRX( Constants.driveLFCANID);
    rightFront = new WPI_TalonSRX( Constants.driveRFCANID);
    leftBack = new WPI_TalonSRX( Constants.driveLBCANID);
    rightBack = new WPI_TalonSRX( Constants.driveRBCANID);

    leftEncoder = new Object( );
    rightEncoder = new Object( );


    // neutralCoast();
    neutralBrake();
    
    followMode();

    addChild("DifferentialDrive", differentialDrive);
    differentialDrive.setSafetyEnabled(true);
    differentialDrive.setExpiration(0.1);
    differentialDrive.setMaxOutput(1.0);
    differentialDrive.setDeadband( 0.2 );
  }

  public void neutralBrake() {
    leftFront.setNeutralMode( NeutralMode.Brake);
    rightFront.setNeutralMode( NeutralMode.Brake);
    leftBack.setNeutralMode( NeutralMode.Brake);
    rightBack.setNeutralMode( NeutralMode.Brake);
  }

  public void neutralCoast() {
    leftFront.setNeutralMode( NeutralMode.Coast);
    rightFront.setNeutralMode( NeutralMode.Coast);
    leftBack.setNeutralMode( NeutralMode.Coast);
    rightBack.setNeutralMode( NeutralMode.Coast);
  }

  // Creates Options for drive method
  public void arcadeDrive( double speed, double rotation ) {
    differentialDrive.arcadeDrive( speed, rotation);
  }
  public void curvatureDrive( double speed, double rotation, boolean isQuickTurn ){
    differentialDrive.curvatureDrive( speed, rotation, isQuickTurn);
  }
  public void tankDrive( double leftSpeed, double rightSpeed ) {
    differentialDrive.tankDrive(leftSpeed, rightSpeed);
  }

  // establishes the follow method in which the sides of 
  // motor controllers would follow the other
  
  public void followMode() {
    leftBack.follow( leftFront );
    rightBack.follow( rightFront );
  }

  // to be used in the future for uses like checking the gyro
  @Override
  public void periodic() {
    double leftEncoderDistance = (double) leftEncoder;
    double rightEncoderDistance = (double) rightEncoder;
    angle = Rotation2d.fromDegrees( m_gyro.getAngle() );
    m_odometry.update( angle, leftEncoderDistance, rightEncoderDistance );
    
  }
}
