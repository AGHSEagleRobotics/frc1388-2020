/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.DriveTrain;

public class AutonMoveShoot extends CommandBase {
  private DriveTrain m_driveTrain;
  private Timer timer = new Timer();
  private boolean hasMovePeriodPass = false;
  
  private final double kDRIVE = 0.5;
  private final double kROTATION = 0.5;
  private final double TIME_PERIOD_MOVE = 3.0;


  /**
   * Creates a new AutonMoveShoot.
   */
  public AutonMoveShoot( DriveTrain driveTrain) {
    // Use addRequirements() here to declare subsystem dependencies.

  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
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
