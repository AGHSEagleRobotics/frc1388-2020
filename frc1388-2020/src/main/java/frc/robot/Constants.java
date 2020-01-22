/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

/**
 * The Constants class provides a convenient place for teams to hold robot-wide numerical or boolean
 * constants.  This class should not be used for any other purpose.  All constants should be
 * declared globally (i.e. public static).  Do not put anything functional in this class.
 *
 * <p>It is advised to statically import this class (or one of its inner classes) wherever the
 * constants are needed, to reduce verbosity.
 */
public final class Constants {

    /**
     * 
     * CAN IDs for (Not Known)
     * 
     */

     public static final int driveLFCANID = 4;
     public static final int driveRFCANID = 1;
     public static final int driveLBCANID = 3;
     public static final int driveRBCANID = 2; // don't know any of the CAN IDs

     public static final int driveControllerInput = 0;
     public static final int opControllerInput = 1; // op Controller not specifically assigned

     public static final int gyro = 0; // not known

}
