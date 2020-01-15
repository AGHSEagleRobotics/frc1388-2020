/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.drive.DifferentialDrive;
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

  public DriveTrain() {
    leftFront = new WPI_TalonSRX( Constants.driveLFCANID);
    rightFront = new WPI_TalonSRX( Constants.driveRFCANID);
    leftBack = new WPI_TalonSRX( Constants.driveLBCANID);
    rightBack = new WPI_TalonSRX( Constants.driveRBCANID);

    // neutralCoast();
    neutralBrake();
    
    followMode();

    addChild("DifferentialDrive", differentialDrive);
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

  }
}
