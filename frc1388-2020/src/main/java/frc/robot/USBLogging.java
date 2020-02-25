/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import java.io.*;
import java.time.*;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;

public class USBLogging {
    private static PrintStream m_logStream = null;
    private static FileOutputStream m_OutStream;
    private static LocalDate m_today = LocalDate.now();

    private static final String logPath = "/u/RobotLogs";
    private static final String logPrefix = "RobotLog_";
    private static final String logSuffix = "txt";
    private static final String className = USBLogging.class.getSimpleName();

    enum Level {
        FATAL, ERROR, OFF, WARNING, INFO, DEBUG
    }

    private static Level logLevel = Level.OFF;
    private static boolean levelIsSet = false;

    /**
     * Open a log file, if possible, to be used by logging statements.
     * <p>
     * This method will return a reference to the opened stream, although it isn't
     * generally needed outside this class. However, null will be returned if no
     * file was opened, which may be useful.
     * 
     * @return A stream to the file opened, or null if no file was opened.
     */
    public static PrintStream openLog() {
        String fName = "";
        // Make sure the log directory exists
        File fLogPath = new File(logPath);
        if (!fLogPath.isDirectory()) {
            System.out.println(className + ": Logging directory does not exist");
            m_logStream = null;
            return m_logStream;
        }

        // Find the file name and open it
        // Creating a file based on timestamp would be ideal; however, since roboRIO has
        // no RTC,
        // there is no guarantee that a meaningful time has been set by the time we get
        // here.
        for (int i = 0; i <= 999; i++) {
            // fNum represents a number of the format 001
            // fName represents the full file path, including file name
            String fNum = String.format("%03d", i);
            fName = logPath + "/" + logPrefix + fNum;
            if (!logSuffix.isEmpty()) {
                fName += "." + logSuffix;
            }

            // See if the file exists
            File fLogFile = new File(fName);
            if (!fLogFile.isFile()) {
                try {
                    m_OutStream = new FileOutputStream(fLogFile);
                    m_logStream = new PrintStream(m_OutStream);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    m_logStream = null;
                    return m_logStream;
                }
                break;
            }
        }

        if (m_logStream == null) {
            System.out.println(className + ": Couldn't determine log file name");
        } else {
            System.out.println(className + ": Log file: " + fName);
            m_logStream.print(m_today + "\r\n");
        }

        return m_logStream;
    }

    /**
     * Print a string to the system console, and to a log file if one has been
     * opened.
     * 
     * @param str String to be printed
     */
    public static void printLog(String str) {
        System.out.println(LocalTime.now() + "  " + str);
        // ToDo: Add timestamp to the string written to m_logStream
        if (m_logStream != null) {
            if (!m_today.equals(LocalDate.now())) {
                m_today = LocalDate.now();
                m_logStream.print(m_today + "\r\n");
            }
            m_logStream.print(LocalTime.now() + "  " + str + "\r\n");

            // flush the output stream, to reduce the chance of file corruption
            try {
                m_OutStream.getFD().sync();
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    private static void printDate() {
        if (!m_today.equals(LocalDate.now())) {
            m_today = LocalDate.now();
            m_logStream.print(m_today + "\r\n");
        }
    }

    private static void flushFile() {
        try {
            m_OutStream.getFD().sync();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Internal generalized log printing method.
     *
     * @param msg The message to be printed
     * @param lvl The Level of the msg
     */
    private static void printLogAtLevel(String msg, Level lvl) {
        if (lvl.compareTo(logLevel) <= 0 || logLevel == Level.FATAL || logLevel == Level.ERROR) {
            String output = LocalTime.now() + " [" + lvl.name() + "] " + msg;

            // Print to console
            System.out.println(output);

            // Print to file
            if (m_logStream != null) {
                printDate();
                m_logStream.print(output + "\r\n");
                flushFile();
            }
        }
    }

    /**
     * Only allow the logLevel to be set once.
     */
    public static void setLogLevel(Level lvl) {
        if (levelIsSet)
            return;

        logLevel = lvl;
        levelIsSet = true;
    }

    /**
     * FATAL. The robot cannot not continue operation This level is printed
     * regardless of logLevel.
     *
     */
    public static void fatal(String msg) {
        printLogAtLevel(msg, Level.FATAL);
    }

    /**
     * ERROR. The robot can continue operation but some facility may be broken or
     * unresponsive A fully functional robot will have no ERROR messages
     * 
     * This level is printed regardless of logLevel
     */
    public static void error(String msg) {
        printLogAtLevel(msg, Level.ERROR);
    }

    /**
     * WARNING. The robot is fully operational but some facility may act
     * unexpectedly
     */
    public static void warning(String msg) {
        printLogAtLevel(msg, Level.WARNING);
    }

    /**
     * INFO. The robot is fully operational
     */
    public static void info(String msg) {
        printLogAtLevel(msg, Level.INFO);
    }

    /**
     * DEBUG. Used for testing
     */
    public static void debug(String msg) {
        printLogAtLevel(msg, Level.DEBUG);
    }

    public static void printCommandStatus(Command coman, String status) {
        if (!(coman instanceof InstantCommand)) {
            info(coman.getName() + " " + status);
        }
    }
}
