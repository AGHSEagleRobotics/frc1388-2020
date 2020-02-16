/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import java.util.function.Supplier;

import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;

import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.geometry.Pose2d;
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


  // Competition Bot motors
  // private final WPI_TalonFX m_leftFront;
  // private final WPI_TalonFX m_rightFront;
  // private final WPI_TalonFX m_leftBack;
  // private final WPI_TalonFX m_rightBack;
  // // Knightmare test motors // FIXME: do not check in with the following uncommented. test only!
  private final WPI_TalonSRX m_leftFront;
  private final WPI_TalonSRX m_rightFront;
  private final WPI_VictorSPX m_leftBack;
  private final WPI_VictorSPX m_rightBack;

  // need to instantiate the differenetail drive
  private final DifferentialDrive differentialDrive;

  private final DifferentialDriveOdometry m_odometry;
  private Rotation2d angle;
  private final Supplier<Rotation2d> m_angleSupplier;

  private final double K_CPR_TO_FT = 1 / 216; // don't know this value

  private Pose2d m_newPosition;

  private double leftEncoderDistance;
  private double rightEncoderDistance;

  public DriveTrain( Supplier<Rotation2d> angleSupplier ) {
    
    // Competition Robot motors
    // m_leftFront = new WPI_TalonFX( Constants.CANID_driveLF );
    // m_rightFront = new WPI_TalonFX( Constants.CANID_driveRF );
    // m_leftBack = new WPI_TalonFX( Constants.CANID_driveLB);
    // m_rightBack = new WPI_TalonFX( Constants.CANID_driveRB );
    // // knightmare test motors // FIXME: do not check in with the following uncommented. test only!
    m_leftFront = new WPI_TalonSRX( Constants.CANID_driveLF );
    m_rightFront = new WPI_TalonSRX( Constants.CANID_driveRF );
    m_leftBack = new WPI_VictorSPX( Constants.CANID_driveLB);
    m_rightBack = new WPI_VictorSPX( Constants.CANID_driveRB );
    
    followMode();
    // configFalconFX();
    
    differentialDrive = new DifferentialDrive(m_leftFront, m_rightFront);
    
    m_angleSupplier = angleSupplier;

    m_odometry = new DifferentialDriveOdometry(m_angleSupplier.get());
    angle = m_angleSupplier.get();

    // uncomment which nuetral mode desired
    // neutralCoast();
    neutralBrake();

    addChild("DifferentialDrive", differentialDrive);
    differentialDrive.setSafetyEnabled(true);
    differentialDrive.setExpiration(0.1);
    differentialDrive.setMaxOutput(1.0);
    differentialDrive.setDeadband( 0.2 );
  }

  public void neutralBrake() {
    m_leftFront.setNeutralMode( NeutralMode.Brake);
    m_rightFront.setNeutralMode( NeutralMode.Brake);
    m_leftBack.setNeutralMode( NeutralMode.Brake);
    m_rightBack.setNeutralMode( NeutralMode.Brake);
  }

  public void neutralCoast() {
    m_leftFront.setNeutralMode( NeutralMode.Coast);
    m_rightFront.setNeutralMode( NeutralMode.Coast);
    m_leftBack.setNeutralMode( NeutralMode.Coast);
    m_rightBack.setNeutralMode( NeutralMode.Coast);
  }

  // public void configFalconFX(){
  //   m_leftFront.configSelectedFeedbackSensor(FeedbackDevice.IntegratedSensor);
  //   m_rightFront.configSelectedFeedbackSensor(FeedbackDevice.IntegratedSensor);
  // }

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
    m_leftBack.follow( m_leftFront );
    m_rightBack.follow( m_rightFront );
  }
  public Pose2d getDriveOdemetry(){
    return m_odometry.getPoseMeters();  
  }
  public Pose2d compareTo(Pose2d pose2d){
    return m_odometry.getPoseMeters().relativeTo(pose2d);
  }
  public Rotation2d getAngle(){
    return m_angleSupplier.get();
  }
  
  // to be used in the future for uses like checking the gyro
  @Override
  public void periodic() {
    // refer to getSelectedSensorPosition() and configSelectedFeedbackSensor (FeedbackDevice feedbackDevice)
    leftEncoderDistance = m_leftFront.getSelectedSensorPosition();
    rightEncoderDistance = m_rightFront.getSelectedSensorPosition();
    angle =  m_angleSupplier.get();
    m_odometry.update( angle, leftEncoderDistance, rightEncoderDistance );

  }
}
