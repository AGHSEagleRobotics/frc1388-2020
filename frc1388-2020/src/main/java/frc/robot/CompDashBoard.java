/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import java.util.List;
import java.util.Map;

import edu.wpi.cscore.HttpCamera;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.cscore.VideoSink;
import edu.wpi.cscore.VideoSource;
import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInLayouts;

import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.ComplexWidget;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardComponent;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardLayout;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj.shuffleboard.SimpleWidget;
import edu.wpi.first.wpilibj.shuffleboard.SuppliedValueWidget;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import frc.robot.subsystems.ColorSpinner;
import frc.robot.subsystems.ColorSpinner.ColorWheel;

/**
 * Add your docs here.
 */
public class CompDashBoard {
    private final int visionProcessPipeline = 0;
    private final int visionDrivePipeline = 1;
    private final int cam2Height = 4;
    private final int cam2Width = 4;

    // auton chooser
    private final int autonChooserWidth = 8;
    private final int autonChooserHeight = 2;
    private final int autonChooserColumnIndex = 0;
    private final int autonChooserRowIndex = 0;
    // escape Chooser
    private final int escapeWidth = 8;
    private final int escapeHeight = 2;
    private final int escapeColumnIndex = 0;
    private final int escapeRowIndex = 2;
    // shooter RPM
    private final int shooterRPMWidth = 5;
    private final int shooterRPMHeight = 2;
    private final int shooterColumnIndex = 0;
    private final int shooterRowIndex = 4;
    // max capacity
    private final int maxCapacityWidth = 3;
    private final int maxCapacityHeight = 3;
    private final int maxCapacityColumnIndex = 0;
    private final int maxCapacityRowIndex = 6;
    // cam 
    private final int camWidth = 10;
    private final int camHeight = 10;
    private final int camColumnIndex = 8;
    private final int camRowIndex = 0;
    // color spinner grid
    private final int colorSpinnerGridWidth = 5;
    private final int colorSpinnerGridHeight = 7;
    private final int colorSpinnerGridColumnIndex = 21;
    private final int colorSpinnerGridRowIndex = 0;
    // desired color
    private final int desiredColorWidth = 5;
    private final int desiredColorHeight = 2;
    private final int desiredColorColumnIndex = 21;
    private final int desiredColorRowIndex = 7;
    
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
    private int m_currCamMode = 1;
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
        
        m_videoSources = new VideoSource[] { 
            m_limeLight, 
            m_cameraIntake, 
            m_cameraClimber
        };

        // m_videoSources = new VideoSource[] {
        //     m_cameraIntake, 
        //     m_cameraClimber
        // };

        m_videoSink = CameraServer.getInstance().getServer();

        if( m_cameraIntake != null ){
            m_videoSink.setSource(m_cameraIntake);
        }

    }


    public void constructShuffleLayout() {
        shuffleboard = Shuffleboard.getTab("Competition");

        complexWidgetCam = shuffleboard.add( "Cams", m_videoSink.getSource())
            .withWidget(BuiltInWidgets.kCameraStream)
            .withSize(camHeight, camWidth)
            .withPosition(camColumnIndex, camRowIndex)
            .withProperties(Map.of("Show Crosshair", true, "Show Controls", false));

        // complexWidgetCam2 = shuffleboard.add( "LimeLight", m_limeLight)
        //     .withWidget(BuiltInWidgets.kCameraStream)
        //     .withSize(cam2Height, cam2Width)
        //     .withProperties(Map.of("Show Crosshair", true, "Show Controls", false));

        for( CompDashBoard.Objective o: Objective.values()){
            autonChooser.addOption(o.getName(), o );
        }
        autonChooser.setDefaultOption(Objective.DEFAULT.getName(), Objective.DEFAULT);

        complexWidgetAuton = shuffleboard.add( "AutonChooser", autonChooser)
            .withWidget(BuiltInWidgets.kSplitButtonChooser)
            .withSize(autonChooserWidth, autonChooserHeight)
            .withPosition(autonChooserColumnIndex, autonChooserRowIndex);

        for( EscapePlan ep: EscapePlan.values()){
            escapeChooser.addOption(ep.getName(), ep );
        }
        escapeChooser.setDefaultOption( EscapePlan.DEFAULT.getName(), EscapePlan.DEFAULT );

        complexWidgetEscape = shuffleboard.add( "EscapeOption",escapeChooser)
            .withWidget(BuiltInWidgets.kSplitButtonChooser)
            .withSize(escapeWidth, escapeHeight)
            .withPosition(escapeColumnIndex, escapeRowIndex);
        
        maxCapacityBox = shuffleboard.add("MaxCapacity", false)
            .withWidget(BuiltInWidgets.kBooleanBox)
            .withSize(maxCapacityWidth, maxCapacityHeight)
            .withPosition(maxCapacityColumnIndex, maxCapacityRowIndex)
            .withProperties(Map.of("colorWhenTrue", "green", "colorWhenFalse", "grey"))
            .getEntry();

        colorSpinnerGrid = shuffleboard.getLayout("Color Spinner", BuiltInLayouts.kGrid)
            .withSize(colorSpinnerGridWidth, colorSpinnerGridHeight)
            .withPosition(colorSpinnerGridColumnIndex, colorSpinnerGridRowIndex)
            .withProperties(Map.of("Number of columns", 1, "Number of Rows", 4));

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

        colorGridGreen = colorSpinnerGrid.add("Green", false )
            .withWidget(BuiltInWidgets.kBooleanBox)
            .withProperties(Map.of("colorWhenTrue", "green", "colorWhenFalse", "grey"))
            .getEntry();
        
        shooterRPM = shuffleboard.add("ShooterRPM", "" ) // m_shooter.getShooterRPM())
            .withWidget(BuiltInWidgets.kTextView)
            .withSize( shooterRPMWidth, shooterRPMHeight )
            .withPosition( shooterColumnIndex, shooterRowIndex )
            .getEntry();

        desiredColorDisplay = shuffleboard.add( "DesiredColor | FMSColor",  "No Game Message yet" )
            .withWidget(BuiltInWidgets.kTextView)
            .withSize( desiredColorWidth, desiredColorHeight )
            .withPosition( desiredColorColumnIndex, desiredColorRowIndex )
            .getEntry();
        
        
    }

    public void toggleLimelightLED(){
        if( m_currCamMode == 1 ){
            m_currCamMode = 3;
        }else{
            m_currCamMode = 1;
        }
        NetworkTableInstance.getDefault().getTable("limelight").getEntry("ledMode").setNumber(m_currCamMode);
    }

    public void switchVideoSource() {
        m_currVideoSourceIndex = (m_currVideoSourceIndex + 1) % m_videoSources.length;
        if( m_videoSources[m_currVideoSourceIndex] != null ){
            m_videoSink.setSource(m_videoSources[m_currVideoSourceIndex]);
        }
    }

    public void setDesiredColor( ColorWheel wheelColor ){ 
        desiredColorDisplay.setString( wheelColor.getName() + "      |      " + getFMSColor());
    }
    
    public String getFMSColor(){
        String gameMessage = DriverStation.getInstance().getGameSpecificMessage();
        if( gameMessage.length() > 0 ){
            switch( gameMessage.charAt(0) ){
            case 'R':
                // desiredColorDisplay.setString( "Red" );
                // break;
                return "Red";
            case 'B':
                // desiredColorDisplay.setString( "Blue" );
                // break;
                return "Blue";
            case 'G':
                // desiredColorDisplay.setString( "Green" );
                // break;
                return "Green";
            case 'Y':
                // desiredColorDisplay.setString( "Yellow" );
                // break;
                return "Yellow";
            default:
                // desiredColorDisplay.setString( "No Game Data" );
                return "DataNotKnown";
            }
            
        }
        return "NoData";
    }

    public void setShooterRPMEntry( String value ){
        // USBLogging.debug( "Shooter: " + value);
        shooterRPM.setValue(value);
    }

    public boolean getGreen(){
        // USBLogging.debug( "" + RobotContainer.getAButton());
        return RobotContainer.getAButton();
    }
 
    public void setMaxCapacity( boolean isFull ){
        // USBLogging.debug("Max:" + isFull);
        maxCapacityBox.setBoolean(isFull);
    }

    public void setRed( boolean colorIsPresent ){
        colorGridRed.setBoolean(colorIsPresent);
    }

    public void setBlue( boolean colorIsPresent ){
        // USBLogging.debug("Blue: " + colorIsPresent);
        colorGridBlue.setBoolean(colorIsPresent);
    }

    public void setYellow( boolean colorIsPresent ){
        // USBLogging.debug("Yellow: " + colorIsPresent);
        colorGridYellow.setBoolean(colorIsPresent);
    }

    public void setGreen( boolean colorIsPresent ){
        // USBLogging.debug("Green: " + colorIsPresent);
        colorGridGreen.setBoolean(colorIsPresent);
    }

    public Objective getSelectedObjective(){
        return autonChooser.getSelected();
    }

    public EscapePlan getSelectedEscapePlan(){
        return escapeChooser.getSelected();
    }

}
