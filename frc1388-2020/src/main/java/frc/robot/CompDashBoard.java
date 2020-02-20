/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import java.util.Map;

import edu.wpi.cscore.HttpCamera;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.cscore.VideoSink;
import edu.wpi.cscore.VideoSource;
import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInLayouts;

import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.ComplexWidget;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardLayout;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj.shuffleboard.WidgetType;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.util.Color;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.ColorSpinner;
import frc.robot.subsystems.ColorSpinner.ColorWheel;

/**
 * Add your docs here.
 */
public class CompDashBoard {
    private final int visionProcessPipeline = 0;
    private final int visionDrivePipeline = 1;
    private final int camHeight = 5;
    private final int camWidth = 4;
    private final int colorSpinnerGridHeight = 10;
    private final int colorSpinnerGridWidth = 8;
    private final int maxCapacityHeight = 4;
    private final int maxCapacityWidth = 4;
    private final int autonChooserHeight = 5;
    private final int autonChooserWidth = 5;

    private RobotContainer m_robotContainer;

    private ShuffleboardTab shuffleboard;
    private ComplexWidget complexWidgetCam;
    private ComplexWidget complexWidgetAuton;
    private ComplexWidget complexWidgetEscape;
    private SendableChooser<Command> autonChooser = new SendableChooser<>();
    private SendableChooser<EscapePlan> escapeChooser = new SendableChooser<>();
    private NetworkTableEntry MaxCapacityBox;
    private ShuffleboardLayout colorSpinnerGrid;

    // Cam
    private UsbCamera m_cameraIntake;
    private UsbCamera m_cameraClimber;
    private HttpCamera m_limeLight;
    private int m_currVideoSourceIndex = 0;
    private VideoSink m_videoSink;
    private VideoSource[] m_videoSources;

    public enum EscapePlan{
        FOWARD,
        REVERSE,
        DOUBLEREVERSE,
        NONE;
    }

    public CompDashBoard(RobotContainer robotContainer) {

        m_robotContainer = robotContainer;
        camStuff();
        constructShuffleLayout();

    }

    private void camStuff() {
        m_cameraIntake = CameraServer.getInstance().startAutomaticCapture(Constants.USB_cameraIntake);
        // m_cameraClimber = CameraServer.getInstance().startAutomaticCapture(
        // Constants.USB_cameraClimber);

        m_cameraIntake.setConnectVerbose(0);

        m_limeLight = new HttpCamera("limelight", "http://limelight.local:5800/stream.mjpg");

        m_videoSources = new VideoSource[] { m_limeLight, m_cameraIntake };

        m_videoSink = CameraServer.getInstance().getServer();

        m_videoSink.setSource(m_cameraIntake);

        // sets the pipeline of the limelight
        NetworkTableInstance.getDefault().getTable("limelight").getEntry("pipeline").setNumber(visionDrivePipeline);
        // NetworkTableInstance.getDefault().getTable("limelight").getEntry("pipeline").setNumber(visionProcessPipeline);
        NetworkTableInstance.getDefault().getTable("limelight").getEntry("camMode").setNumber(1);

    }

    public void constructShuffleLayout() {
        shuffleboard = Shuffleboard.getTab("Competition");

        complexWidgetCam = shuffleboard.add(m_videoSink.getSource()).withWidget(BuiltInWidgets.kCameraStream)
            .withSize(camHeight, camWidth)
            .withProperties(Map.of("Show Crosshair", true, "Show Controls", false, "Title", "Camera"));

        // autonChooser.addOption("FiveShoot", "FiveShoot" );
        // autonChooser.addOption("EightShoot", "EightShoot" );

        complexWidgetAuton = shuffleboard.add(autonChooser)
            .withWidget(BuiltInWidgets.kSplitButtonChooser)
            .withSize(autonChooserWidth, autonChooserHeight);

        complexWidgetAuton = shuffleboard.add(autonChooser)
            .withWidget(BuiltInWidgets.kSplitButtonChooser)
            .withSize(autonChooserWidth, autonChooserHeight);

        MaxCapacityBox = shuffleboard.add("MaxCapacity", false)
            .withWidget(BuiltInWidgets.kBooleanBox)
            .withSize(maxCapacityWidth, maxCapacityHeight)
            .withProperties(Map.of("colorWhenTrue", "green", "colorWhenFalse", "grey")).getEntry();

        colorSpinnerGrid = shuffleboard.getLayout("Color Spinner", BuiltInLayouts.kGrid)
            .withSize(colorSpinnerGridWidth, colorSpinnerGridHeight)
            .withProperties(Map.of("Number of columns", 4, "Number of Rows", 1));

        colorSpinnerGrid.addBoolean("Blue", () -> checkColor(ColorWheel.BLUE))
            .withWidget(BuiltInWidgets.kBooleanBox)
            .withProperties(Map.of("colorWhenTrue", "blue", "colorWhenFalse", "grey"));

        colorSpinnerGrid.addBoolean("Yellow", () -> checkColor(ColorWheel.YELLOW))
            .withWidget(BuiltInWidgets.kBooleanBox)
            .withProperties(Map.of("colorWhenTrue", "yellow", "colorWhenFalse", "grey"));

        colorSpinnerGrid.addBoolean("Red", () -> checkColor(ColorWheel.RED))
            .withWidget(BuiltInWidgets.kBooleanBox)
            .withProperties(Map.of("colorWhenTrue", "red", "colorWhenFalse", "grey"));

        colorSpinnerGrid.addBoolean("Green", () -> checkColor(ColorWheel.GREEN))
            .withWidget(BuiltInWidgets.kBooleanBox)
            .withProperties(Map.of("colorWhenTrue", "green", "colorWhenFalse", "grey"));
    }

    public boolean checkColor(ColorSpinner.ColorWheel checkColor) {
        ColorSpinner.ColorWheel color = m_robotContainer.getInstanceSpinner().checkColor();
        return color.equals(checkColor);
    }

    public void switchVideoSource() {
        m_currVideoSourceIndex = (m_currVideoSourceIndex + 1) % m_videoSources.length;
        m_videoSink.setSource(m_videoSources[m_currVideoSourceIndex]);
    }

    public void addAutonCommand(String name, Command autonCommand) {
        autonChooser.addOption(name, autonCommand);
    }

    public void addAutonCommandDefault(String name, Command autonCommand) {
        autonChooser.setDefaultOption(name, autonCommand);
    }

    public Command getAutonCommand() {
        return autonChooser.getSelected();
    }

}
