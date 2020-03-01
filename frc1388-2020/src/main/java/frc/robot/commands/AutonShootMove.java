/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.subsystems.DriveTrain;
import frc.robot.subsystems.MagazineSubsystem;
import frc.robot.subsystems.ShooterSubsystem;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/latest/docs/software/commandbased/convenience-features.html
public class AutonShootMove extends SequentialCommandGroup {
  private final double AUTON_SHOOT_RPM = 5400;
  /**
   * Creates a new AutonShootMove.
   */
  public AutonShootMove( ShooterSubsystem shooter, MagazineSubsystem magazineSubsystem, DriveTrain driveTrain ) {
    
    Command multiShot = new MultiShot( shooter, magazineSubsystem, AUTON_SHOOT_RPM).withTimeout(5);
    AutonMove autonMove = new AutonMove(
        driveTrain,                       // dependecy
        AutonMove.Mode.kDistanceDrive,    // drive mode
        72,                                // drive distance (inches)
        0.4,                              // drive speed (%)
        0,                                // rotation control
        false);                           // quick turn
    addCommands(multiShot, autonMove);
  }
}
