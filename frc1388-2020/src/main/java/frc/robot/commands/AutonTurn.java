/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj.controller.PIDController;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpiutil.math.MathUtil;
import frc.robot.subsystems.DriveTrain;

public class AutonTurn extends CommandBase {
 
  private double m_autonTurnSpeed;
  private double m_autonAngle;
  //private double m_gyroTargetAngle;
  private final DriveTrain m_driveTrain;
  // private final double k_angleTolerance = 5.0;

  private final PIDController m_pidController = new PIDController(0.25, 0, 0);
  /**
   * Creates a new AutonTurn.
   */
  public AutonTurn( DriveTrain driveTrain, double autonAngle, double autonTurnSpeed ) {
    // Use addRequirements() here to declare subsystem dependencies.
    m_autonTurnSpeed = autonTurnSpeed;
    m_autonAngle = autonAngle;
    m_driveTrain = driveTrain;

    m_autonTurnSpeed = Math.abs(autonTurnSpeed);

    m_pidController.setTolerance(5);

    m_pidController.enableContinuousInput(-180, 180);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
  //   double currAngle = m_driveTrain.getAngle().getDegrees();

  //   m_gyroTargetAngle = currAngle + optimalTurnAngle(currAngle, m_autonAngle);
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    
    double speed = m_pidController.calculate(m_driveTrain.getAngle().getDegrees(), m_autonAngle);
    speed = MathUtil.clamp(speed, -m_autonTurnSpeed, m_autonTurnSpeed);
    
    m_driveTrain.curvatureDrive(0, speed, true);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    m_driveTrain.curvatureDrive(0, 0, false);
    m_pidController.reset();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {

    return m_pidController.atSetpoint();
  }
  
/**
 * Calculate shortest turning angle
 * @param currentAngle
 * @param desiredAngle
 * @return -180 -> 179
 */
  // private double optimalTurnAngle( double currentAngle, double desiredAngle) {
  //   return ((normalizeAngle(desiredAngle) - normalizeAngle(currentAngle)) + 540 ) % 360 - 180;
  // }

  // private double normalizeAngle( double angle ) {
  //   return angle % 360;
    // Because java mod is dumb
  // }


}
