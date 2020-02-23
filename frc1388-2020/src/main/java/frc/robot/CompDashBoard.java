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
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInLayouts;

import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.ComplexWidget;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardLayout;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;

/**
 * Add your docs here.
 */
public class CompDashBoard {
    private final int visionProcessPipeline = 0;
    private final int visionDrivePipeline = 1;
    private final int camHeight = 256;
    private final int camWidth = 256;
    private final int camColumnIndex = 256;
    private final int camRowIndex = 256;
    private final int colorSpinnerGridHeight = 48;
    private final int colorSpinnerGridWidth = 128;
    private final int maxCapacityHeight = 32;
    private final int maxCapacityWidth = 32;
    private final int autonChooserHeight = 32;
    private final int autonChooserWidth = 32;
    private final int shooterRPMHeight = 32;
    private final int shooterRPMWidth = 48;
    private final int shooterColumnIndex = 32;
    private final int shooterRowIndex = 256;
    private final int desiredColorHeight = 32;
    private final int desiredColorWidth = 48;
    private final int desiredColorColumnIndex = 64;
    private final int desiredColorRowIndex = 32;
    private final int cam2Height = 256;
    private final int cam2Width = 256;
    
    private RobotContainer m_robotContainer;

    private ShuffleboardTab shuffleboard;
    private ComplexWidget complexWidgetCam;
    private ComplexWidget complexWidgetCam2;
    private ComplexWidget complexWidgetAuton;
    private ComplexWidget complexWidgetEscape;
    private SendableChooser<Objective> autonChooser = new SendableChooser<>();
    private SendableChooser<EscapePlan> escapeChooser = new SendableChooser<>();
    private NetworkTableEntry maxCapacityBox;
    private ShuffleboardLayout colorSpinnerGrid;
    private NetworkTableEntry shooterRPM;
    private NetworkTableEntry colorGridRed;
    private NetworkTableEntry colorGridGreen;
    private NetworkTableEntry colorGridYellow;
    private NetworkTableEntry colorGridBlue;
    private NetworkTableEntry desiredColorDisplay;

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

    public CompDashBoard() { 
        camStuff();
        constructShuffleLayout();
    }

    private void camStuff() {
        m_cameraIntake = CameraServer.getInstance().startAutomaticCapture(Constants.USB_cameraIntake);
        m_cameraClimber = CameraServer.getInstance().startAutomaticCapture( Constants.USB_cameraClimber);

        m_limeLight = new HttpCamera("limelight", "http://limelight.local:5800/stream.mjpg");
        
        // sets the pipeline of the limelight
        NetworkTableInstance.getDefault().getTable("limelight").getEntry("pipeline").setNumber(visionDrivePipeline);
        // NetworkTableInstance.getDefault().getTable("limelight").getEntry("pipeline").setNumber(visionProcessPipeline);
        NetworkTableInstance.getDefault().getTable("limelight").getEntry("camMode").setNumber(1);
        
        // m_videoSources = new VideoSource[] { 
        //     m_limeLight, 
        //     m_cameraIntake, 
        //     m_cameraClimber
        // };

        m_videoSources = new VideoSource[] {
            m_cameraIntake, 
            m_cameraClimber
        };

        m_videoSink = CameraServer.getInstance().getServer();

        m_videoSink.setSource(m_cameraIntake);

    }


    public void constructShuffleLayout() {
        shuffleboard = Shuffleboard.getTab("Competition");

        complexWidgetCam = shuffleboard.add( "Cams", m_videoSink.getSource())
            .withWidget(BuiltInWidgets.kCameraStream)
            .withSize(camHeight, camWidth)
            .withPosition(camColumnIndex, camRowIndex)
            .withProperties(Map.of("Show Crosshair", true, "Show Controls", false));

        complexWidgetCam2 = shuffleboard.add( "LimeLight", m_limeLight)
            .withWidget(BuiltInWidgets.kCameraStream)
            .withSize(cam2Height, cam2Width)
            .withProperties(Map.of("Show Crosshair", true, "Show Controls", false));

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
            .withProperties(Map.of("colorWhenTrue", "green", "colorWhenFalse", "grey"))
            .getEntry();

        colorSpinnerGrid = shuffleboard.getLayout("Color Spinner", BuiltInLayouts.kGrid)
            .withSize(colorSpinnerGridWidth, colorSpinnerGridHeight)
            .withProperties(Map.of("Number of columns", 4, "Number of Rows", 1));

        colorGridBlue = colorSpinnerGrid.add("Blue", false)
            .withWidget(BuiltInWidgets.kBooleanBox)
            .withProperties(Map.of("colorWhenTrue", "blue", "colorWhenFalse", "grey"))
            .getEntry();

        colorGridYellow = colorSpinnerGrid.add("Yellow", false)
            .withWidget(BuiltInWidgets.kBooleanBox)
            .withProperties(Map.of("colorWhenTrue", "yellow", "colorWhenFalse", "grey"))
            .getEntry();

        colorGridRed = colorSpinnerGrid.add("Red" , false)
            .withWidget(BuiltInWidgets.kBooleanBox)
            .withProperties(Map.of("colorWhenTrue", "red", "colorWhenFalse", "grey"))
            .getEntry();

        colorGridGreen = colorSpinnerGrid.add("Green", false)
            .withWidget(BuiltInWidgets.kBooleanBox)
            .withProperties(Map.of("colorWhenTrue", "green", "colorWhenFalse", "grey"))
            .getEntry();

        shooterRPM = shuffleboard.add("ShooterRPM", 0 ) // m_shooter.getShooterRPM())
            .withWidget(BuiltInWidgets.kTextView)
            .withSize( shooterRPMHeight, shooterRPMWidth )
            .withPosition( shooterColumnIndex, shooterRowIndex )
            .getEntry();

        desiredColorDisplay = shuffleboard.add( "DesiredColor", getDesiredColor() )
            .withWidget(BuiltInWidgets.kTextView)
            .withSize( desiredColorHeight, desiredColorWidth )
            .withPosition( desiredColorColumnIndex, desiredColorRowIndex )
            .getEntry();
    }

    public void switchVideoSource() {
        m_currVideoSourceIndex = (m_currVideoSourceIndex + 1) % m_videoSources.length;
        if( m_videoSources[m_currVideoSourceIndex] != null ){
            m_videoSink.setSource(m_videoSources[m_currVideoSourceIndex]);
        }
    }

    public String getDesiredColor(){
        String gameMessage = DriverStation.getInstance().getGameSpecificMessage();
        if( gameMessage.length() > 0 ){
            switch( gameMessage.charAt(0) ){
            case 'R':
                return "Red";
            case 'B':
                return "Blue";
            case 'G':
                return "Green";
            case 'Y':
                return "Yellow";
            }
        }
        return "No game message";
    }

    public void setShooterRPMEntry( String value ){
        shooterRPM.setString(value);
    }

    public void setMaxCapacity( boolean isFull ){
        shooterRPM.setBoolean(isFull);
    }

    public void setRed( boolean colorIsPresent ){
        colorGridRed.setBoolean(colorIsPresent);
    }

    public void setBlue( boolean colorIsPresent ){
        colorGridBlue.setBoolean(colorIsPresent);
    }

    public void setYellow( boolean colorIsPresent ){
        colorGridYellow.setBoolean(colorIsPresent);
    }

    public void setGreen( boolean colorIsPresent ){
        colorGridGreen.setBoolean(colorIsPresent);
    }

    public Objective getSelectedObjective(){
        return autonChooser.getSelected();
    }

    public EscapePlan getSelectedEscapePlan(){
        return escapeChooser.getSelected();
    }

}
