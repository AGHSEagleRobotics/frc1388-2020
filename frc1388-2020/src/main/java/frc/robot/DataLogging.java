/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import java.time.LocalTime;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;

/**
 * Add your docs here.
 */
public class DataLogging {
    private static final String tableName = "DataLog";

    private static NetworkTable m_table = NetworkTableInstance.getDefault().getTable(tableName);
    
    /**
     * Log a number to the DataLog.
     * @param key       Name of the data
     * @param value     The data
     * @return          True if the operation succeeded
     */
    public static boolean logNumber(String key, Number value) {
        Shuffleboard.startRecording();      // Make sure Shuffleboard is recording
        logTimestamp();
        return m_table.getEntry(key).setNumber(value);
    }
    
    /**
     * Log a boolean to the DataLog.
     * @param key       Name of the data
     * @param value     The data
     * @return          True if the operation succeeded
     */
    public static boolean logBoolean(String key, boolean value) {
        Shuffleboard.startRecording();      // Make sure Shuffleboard is recording
        logTimestamp();
        return m_table.getEntry(key).setBoolean(value);
    }
    
    /**
     * Log a string to the DataLog.
     * @param key       Name of the data
     * @param value     The data
     * @return          True if the operation succeeded
     */
    public static boolean logString(String key, String value) {
        Shuffleboard.startRecording();      // Make sure Shuffleboard is recording
        logTimestamp();
        return m_table.getEntry(key).setString(value);
    }

    private static void logTimestamp() {
        m_table.getEntry("Timestamp").setString(LocalTime.now().toString());
    }
}
