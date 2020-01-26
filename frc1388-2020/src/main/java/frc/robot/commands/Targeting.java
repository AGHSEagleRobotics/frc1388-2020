/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.RobotContainer;
import frc.robot.subsystems.DriveTrain;
import edu.wpi.first.networktables.*;

public class Targeting extends CommandBase {
  private DriveTrain m_subsystem;


  /************* Constants ***************/

  // constant of rotation to be implemented for scaling
  private final double K_ROTATION = 1.0;  // TODO unknown value

  // constant of drive speed proportion for scaling
  private final double K_DRIVE = 1.0; // TODO unknown value

  // area of the target we want
  // really I'm hesitant to put this because we could do the math to have the 
  // shooter change its velocity to match the distance and arch required to score
  private final double TARGET_AREA = 8.7; // TODO unknown value

  // saw some examples of a max speed on the targeting function some pros some cons
  private final double MAX_SPEED = 0.7; // TODO undecided value

  // seeking turn rate
  private final double TURN_ROTATION = 0.3; // TODO undecided value

  // constant for minimum valid target amount
  private final double MIN_VALID_TARGET = 0.1; // TODO undecided value

  // height of the mount in units //TODO decide units
  private final double MOUNT_HEIGHT = 12.0; // TODO find out the height

  // angle of the mount in units // TODO decide units
  private final double MOUNT_ANGLE = 0.0; // TODO find out angle of mount

  // height of the target // TODO decide units
  private final double TARGET_HEIGHT = 6.0; // TODO find out height of target

  // Maximum distance to shoot the ball
  private final double MAX_DISTANCE = 12.0;

  // Minimum distance to shoot the ball
  private final double MIN_DISTANCE = 3.0;

  /************* Changing Variables  ***************/


  // variable to hold the drive Speed
  private double driveSpeed = 0.0;

  // variable to hold the rotation
  private double driveRotation = 0.0;

  // valid target is whether the target has a target and is from 0 to 1 
  // aka tv
  private double validTarget = 0.0;

  // converts valid target to a boolean but also serves as a flag
  private boolean hasValidTarget = false;

  // flag for if the robot is a certain distance away from target and within the tolerance
  private boolean distanceFlag = false;

  // flag for if the robot is a certain z axis distance/horizantial offset from target and within the tolerance
  private boolean angleFlag = false;

  // flag for the previous target validity
  private double previousValidTarget = 0.0;

  // horizontal offset from crosshair to target
  // ranging from -29.8 to 29.8 degrees 
  // aka tx
  private double horizontalOffset = 0.0; 

  // vertical offset from the crosshair to the target
  // ranging from  -24.85 to 24.85 degrees 
  // aka ty
  // not needed for drive train but for shooter is crucial
  private double verticalOffset = 0.0;

  // the target area recieved and is ranged from 0-100 % of the image
  // aka ta
  private double area = 0.0;

  // distance from the target base // TODO decide units
  private double distanceFromTarget = 0.0;

  /* notatble that there is also a skew of image return and many others
    the, in my opinion poorly documented, 
    documentation is here (http://docs.limelightvision.io/en/latest/networktables_api.html)
  */

  // flag for targeting to be running or not
  private boolean targetingOn = false;

  private int ctr = 0;
  /**
   * Creates a new Targeting.
   */
  public Targeting( DriveTrain subsystem ) {
    m_subsystem = subsystem;
    addRequirements(subsystem);
    // Use addRequirements() here to declare subsystem dependencies.
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    updateLimeLight();
    distanceMath();

    if( ctr%200 == 0 ){
      System.out.println( "Vlaid Target" + validTarget );
      System.out.println( "Horizontal Offset" + horizontalOffset );
      System.out.println( "vertical Offset" + verticalOffset );
      System.out.println( "area" + area );
      System.out.println( "Distance Flag" + distanceFlag );
      System.out.println( "angle flag" + angleFlag );
    }
    ctr++;
    // area where we can add values to shuffle board as the if will exit the execute
    
    // retreives the full drive function button cue
    boolean fullDriveFunction = RobotContainer.getXButton(); // TODO change this to a toggle function
    // checks to see if targeting is enabled if not then will exit execute but not close the command
    targetingOn = RobotContainer.getBButton(); // TODO change this to toggle function
    if( !targetingOn ){
      return;
    }

    // if no target is seen it will turn in place till target found
    if( !hasValidTarget ){

      // if a valid target is seen then will turn towards the suspected target
      // else then will spin in a clockwise direction
      if( validTarget < MIN_VALID_TARGET ) {
        m_subsystem.arcadeDrive( TURN_ROTATION, 0.0 );
      }else{
        double seekRotation;
        if( horizontalOffset < 0.0 ) {
          seekRotation = -TURN_ROTATION;
        }else {
          seekRotation = TURN_ROTATION;
        }

        if(previousValidTarget > validTarget){
          seekRotation *= -1;
        }
        
        previousValidTarget = validTarget;
        m_subsystem.arcadeDrive( seekRotation, 0.0 );
      }
    }

    // this checks to make sure that a change in position is needed
    // not sold on the idea but thought it added to reenforcing the tolerances
    if( !distanceFlag || !angleFlag ){

      // if statement checking whether to turn or to drive or full drive function
      // I personally think is advantageous because it will be a beeline and have faster momentum towards the target
      if( fullDriveFunction ){
        m_subsystem.arcadeDrive( driveSpeed, driveRotation);
      }else if( !angleFlag ){
        m_subsystem.arcadeDrive( 0.0, driveRotation );
      }else{
        m_subsystem.arcadeDrive( driveSpeed, 0.0);
      }
      
    }

  }

  private void distanceMath(){
    // place to put math for computing distance from the target so that the distance flag can be triggered 
    // also a good place for calculating velocity to change for the shooter
    distanceFromTarget = (TARGET_HEIGHT - MOUNT_HEIGHT ) / Math.tan( MOUNT_ANGLE + verticalOffset );
    distanceFlag = distanceFromTarget < MAX_DISTANCE || distanceFromTarget > MIN_DISTANCE;

    // to calculate the positition editting the following formula is needed
    // other factors like air resistance, mass and degree angle of launch, gravity, Spin
    // position = initial position + initial velocity * time + 1/2 * acceleration * (time)^2
    // velocity = ((distanceFromTarget) + .5 * acceleration * ( time * time )
  }

  private void updateLimeLight() {
    validTarget = NetworkTableInstance.getDefault().getTable("limelight").getEntry("tv").getDouble(0);
    horizontalOffset = NetworkTableInstance.getDefault().getTable("limelight").getEntry("tx").getDouble(0);
    verticalOffset = NetworkTableInstance.getDefault().getTable("limelight").getEntry("ty").getDouble(0);
    area = NetworkTableInstance.getDefault().getTable("limelight").getEntry("ta").getDouble(0);
    
    if( validTarget < 1.0 ){
      hasValidTarget = false;
      driveSpeed = 0.0;
      driveRotation = 0.0;
      return;
    }
    hasValidTarget = true;

    // TODO decide the tolerance of the horizontal offset
    angleFlag =  horizontalOffset > .05 || horizontalOffset < .05;

    driveRotation = horizontalOffset* K_ROTATION;

    driveSpeed = (TARGET_AREA - area )*K_DRIVE;

    if( driveSpeed > MAX_SPEED){
      driveSpeed = MAX_SPEED;
    }
    
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
