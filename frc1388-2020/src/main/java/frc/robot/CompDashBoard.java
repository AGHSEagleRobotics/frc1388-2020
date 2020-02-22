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
import edu.wpi.first.wpilibj.shuffleboard.SimpleWidget;
import edu.wpi.first.wpilibj.shuffleboard.SuppliedValueWidget;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import frc.robot.subsystems.ColorSpinner;
import frc.robot.subsystems.ShooterSubsystem;
import frc.robot.subsystems.ColorSpinner.ColorWheel;

/**
 * Add your docs here.
 */
public class CompDashBoard {
    // TODO do all these values
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
    private final int camColumnIndex = 5;
    private final int camRowIndex = 5;
    private final int shooterRPMHeight = 5;
    private final int shooterRPMWidth = 5;
    private final int shooterColumnIndex = 5;
    private final int shooterRowIndex = 5;

    private RobotContainer m_robotContainer;
    private ColorSpinner m_colorSpinner;
    private ShooterSubsystem m_shooter;

    private ShuffleboardTab shuffleboard;
    private ComplexWidget complexWidgetCam;
    private ComplexWidget complexWidgetAuton;
    private ComplexWidget complexWidgetEscape;
    private SendableChooser<Objective> autonChooser = new SendableChooser<>();
    private SendableChooser<EscapePlan> escapeChooser = new SendableChooser<>();
    private SimpleWidget maxCapacityBox;
    private ShuffleboardLayout colorSpinnerGrid;
    private SimpleWidget shooterRPM;

    // Cam
    private UsbCamera m_cameraIntake;
    private UsbCamera m_cameraClimber;
    private HttpCamera m_limeLight;
    private int m_currVideoSourceIndex = 0;
    private VideoSink m_videoSink;
    private VideoSource[] m_videoSources;

    public enum Objective{ 
        SHOOTMOVE( "ShootMove" ),
        MOVE( "Move" ),
        SHOOT( "Shoot" ),
        NOTHING( "Nothing" );

        public static final Objective DEFAULT = MOVE;

        private String name;

        private Objective( String setName ){
            name = setName;
        }

        public String getName(){
            return name;
        }
    }

    public enum EscapePlan{
        FOWARD( "Foward"),
        REVERSE( "Reverse"),
        DOUBLEREVERSE( "DoubleReverse"),
        NONE( "None");

        public static final EscapePlan DEFAULT = REVERSE;

        private String name;

        private EscapePlan( String setName){
            name = setName;
        }

        public String getName(){
            return name;
        }
    }

    public CompDashBoard(ColorSpinner colorSpinner ) { 
        m_colorSpinner = colorSpinner;
        camStuff();
        constructShuffleLayout();
    }

    private void camStuff() {
        m_cameraIntake = CameraServer.getInstance().startAutomaticCapture(Constants.USB_cameraIntake);
        m_cameraClimber = CameraServer.getInstance().startAutomaticCapture( Constants.USB_cameraClimber);

        m_cameraIntake.setConnectVerbose(0);

        m_limeLight = new HttpCamera("limelight", "http://limelight.local:5800/stream.mjpg");

        m_videoSources = new VideoSource[] { 
            m_limeLight, 
            m_cameraIntake, 
            m_cameraClimber
         };

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
            .withPosition(camColumnIndex, camRowIndex)
            .withProperties(Map.of("Show Crosshair", true, "Show Controls", false, "Title", "Camera"));

        for( CompDashBoard.Objective o: Objective.values()){
            autonChooser.addOption(o.getName(), o );
        }
        autonChooser.setDefaultOption(Objective.DEFAULT.getName(), Objective.DEFAULT);

        complexWidgetAuton = shuffleboard.add( "AutonChooser", autonChooser)
            .withWidget(BuiltInWidgets.kComboBoxChooser)
            .withSize(autonChooserWidth, autonChooserHeight);

        for( EscapePlan ep: EscapePlan.values()){
            escapeChooser.addOption(ep.getName(), ep );
        }
        escapeChooser.setDefaultOption( EscapePlan.DEFAULT.getName(), EscapePlan.DEFAULT );

        complexWidgetEscape = shuffleboard.add( "EscapeOption",escapeChooser)
            .withWidget(BuiltInWidgets.kSplitButtonChooser)
            .withSize(autonChooserWidth, autonChooserHeight);

        maxCapacityBox = shuffleboard.add("MaxCapacity", false)
            .withWidget(BuiltInWidgets.kBooleanBox)
            .withSize(maxCapacityWidth, maxCapacityHeight)
            .withProperties(Map.of("colorWhenTrue", "green", "colorWhenFalse", "grey"));

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
        
        shooterRPM = shuffleboard.add("ShooterRPM", 0 ) // m_shooter.getShooterRPM())
            .withWidget(BuiltInWidgets.kTextView)
            .withSize( shooterRPMHeight, shooterRPMWidth )
            .withPosition( shooterColumnIndex, shooterRowIndex );
    
    }

    public boolean checkColor(ColorSpinner.ColorWheel checkColor) {
        ColorSpinner.ColorWheel color = m_colorSpinner.checkColor();
        return color.equals(checkColor);
    }

    public void switchVideoSource() {
        m_currVideoSourceIndex = (m_currVideoSourceIndex + 1) % m_videoSources.length;
        if( m_videoSources[m_currVideoSourceIndex] != null ){
            m_videoSink.setSource(m_videoSources[m_currVideoSourceIndex]);
        }
    }

    public Objective getSelectedObjective(){
        return autonChooser.getSelected();
    }

    public EscapePlan getSelectedEscapePlan(){
        return escapeChooser.getSelected();
    }

}
