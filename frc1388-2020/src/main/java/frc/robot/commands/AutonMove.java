/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import frc.robot.subsystems.DriveTrain;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.controller.PIDController;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpiutil.math.MathUtil;

public class AutonMove extends CommandBase {

  private final DriveTrain m_driveTrain;

  private final Mode m_mode;
  private final double m_cutoff;
  private final double m_speed;
  private final double m_rotation;
  private final boolean m_isQuickTurn;

  private final Timer m_timer = new Timer();
  private final double P_VALUE = 0.03;
  // FIXME We need to determine the proper constants
  private final PIDController m_pidController = new PIDController(P_VALUE, 0, 0);

  public enum Mode {

    kTimeDrive, kDistanceDrive

  }

  /**
   * Creates a new AutonMove.
   * 
   * @param driveTrain drive train subsystem
   * @param mode       stop mode
   * @param cutoff     timed mode cutoff = seconds, distance mode cutoff = inches
   */
  public AutonMove(DriveTrain driveTrain, Mode mode, double cutoff, double speed, double rotation,
      boolean isQuickTurn) {
    m_driveTrain = driveTrain;
    m_mode = mode;
    m_cutoff = cutoff;
    m_rotation = rotation;
    m_isQuickTurn = isQuickTurn;

    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(m_driveTrain);

    if(m_mode == Mode.kDistanceDrive){
      m_speed = Math.abs(speed);
    } else {
      m_speed = speed;
    }

    m_pidController.setTolerance(0.5);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    m_driveTrain.resetLeftEncoder();
    m_timer.start();
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {

    double speed;
    if( m_mode == Mode.kDistanceDrive){
      speed = m_pidController.calculate(m_driveTrain.getLeftEncoderDistance(), m_cutoff);
      speed = MathUtil.clamp(speed, -m_speed, m_speed);
    } else {
      speed = m_speed;
    }

    m_driveTrain.curvatureDrive(speed, m_rotation, m_isQuickTurn);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    m_timer.stop();
    m_timer.reset();
    m_driveTrain.curvatureDrive(0, 0, false);
    m_pidController.reset();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {

    switch (m_mode) {
    case kTimeDrive:
      return m_timer.hasPeriodPassed(m_cutoff);
    case kDistanceDrive:
      return m_pidController.atSetpoint();
    default:
      return Timer.getMatchTime() > 15.0;
    }
  }
}
