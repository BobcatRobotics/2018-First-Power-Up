/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.usfirst.frc.team177.robot;

import org.usfirst.frc.team177.lib.CommandFile;
import org.usfirst.frc.team177.lib.Commands;
import org.usfirst.frc.team177.lib.RioLogger;
import org.usfirst.frc.team177.lib.SmartDash;
import org.usfirst.frc.team177.lib.SpeedFile;
import org.usfirst.frc.team177.robot.commands.AutoCommand;
import org.usfirst.frc.team177.robot.commands.AutoFromCenter;
import org.usfirst.frc.team177.robot.commands.AutoFromLeftRight;
import org.usfirst.frc.team177.robot.commands.DriveWithJoysticks;
import org.usfirst.frc.team177.robot.commands.MoveClimberArm;
import org.usfirst.frc.team177.robot.commands.MoveElevator;
import org.usfirst.frc.team177.robot.commands.MoveElevatorWithJoystick;
import org.usfirst.frc.team177.robot.commands.PlaybackCommands;
import org.usfirst.frc.team177.robot.commands.RobotConstants;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;

/**
 * 2018 Robot Code - Main Class
 */
public class Robot extends TimedRobot {
	/* Controls */
	public static final OI Controls = new OI();
	
	// First limit switch (bottom or top? TBD)
	//DigitalInput limitswitch1 = new DigitalInput(9);
	
	/* Commands */
	AutoCommand auto;
	DriveWithJoysticks driveJoy;
	MoveElevator moveElevator;
	MoveClimberArm moveClimberArm;
	
	/* SmartDashboard Information */
	private String gameData = "";
	private String startPosition = "";
	private String autoFileName = "";
	private String allowCrossOver = "";
	private String recordState = "";
	private String enableElevatorLimits = "";
	private String enableClimberPullin = "";
	private boolean gameDataFromField = false;
	
	SendableChooser<String> robotStartPosition = new SendableChooser<>();
	SendableChooser<String> recorder = new SendableChooser<>();
	SendableChooser<String> fileRecorder = new SendableChooser<>();
	SendableChooser<String> crossOver = new SendableChooser<>();
	SendableChooser<String> climberPullin = new SendableChooser<>();
	SendableChooser<String> elevatorLimits = new SendableChooser<>();
	
	// This boolean controls if the robot is in test recording or the robot
	// is running in competition mode
	boolean isCompetition = true;
	boolean processedGameInfo = false;
	boolean runSimpleAuto = false;
	int autoGameChecks;
	int autoGameCheckLimit = 250;
	
	// Recording Variables
	// Inorder to Capture all commands SpeedFile, CommandFile -> OI
	boolean isRecording = false;
	boolean isCmdFileEOF = false;
	boolean isElevatorInTolerance = true;

	// These booleans are used to trigger recording teleop during a match
	// Record from teleopInit() --> disabledInit() 
	// 3/15 - Will not be needed in competition
	/**
	boolean recordTeleop = false;
	boolean isTeleopRecording = false;
	*/
	
	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */
	@Override
	public void robotInit() {
		robotStartPosition.addDefault("Robot Starts Left", RobotConstants.AUTO_ROBOT_LEFT);
		robotStartPosition.addObject("Robot Starts Middle", RobotConstants.AUTO_ROBOT_MIDDLE);
		robotStartPosition.addObject("Robot Starts Right", RobotConstants.AUTO_ROBOT_RIGHT);

		crossOver.addDefault("Auto - Cross Over", RobotConstants.AUTO_SCALE_CROSS);
		crossOver.addObject("Auto - Do Not Cross Over", RobotConstants.AUTO_NO_SCALE_CROSS);
 
		recorder.addDefault("Do Nothing", "nothing");
		recorder.addObject("Start Recording ", "start");
		recorder.addObject("Stop Recording", "stop");
	
		fileRecorder.addDefault("Center --> Go Right", RobotConstants.CENTER_2_RIGHT);
		fileRecorder.addObject("Center --> Go Left", RobotConstants.CENTER_2_LEFT);
		fileRecorder.addObject("Left --> To Scale", RobotConstants.LEFT_2_SCALE);
		fileRecorder.addObject("Right --> To Scale", RobotConstants.RIGHT_2_SCALE);
		fileRecorder.addObject("Left --> To Scale Right", RobotConstants.LEFT_2_SCALE_RIGHT);
		fileRecorder.addObject("Right --> To Scale Left", RobotConstants.RIGHT_2_SCALE_LEFT);
		fileRecorder.addObject("Left --> Scale Right, No Cross, No Switch", RobotConstants.LEFT_2_SCALE_SHORT);
		fileRecorder.addObject("Right --> Scale Left, No Cross, No Switch", RobotConstants.RIGHT_2_SCALE_SHORT);
		fileRecorder.addObject("Left --> Scale Right, No Cross, Switch", RobotConstants.LEFT_2_SCALE_SHORT_SWITCH);
		fileRecorder.addObject("Right --> Scale Left, No Cross, Switch", RobotConstants.RIGHT_2_SCALE_SHORT_SWITCH);

		climberPullin.addDefault("Climber Pullin Enabled", RobotConstants.CLIMBER_PULLIN_ON);
		climberPullin.addObject("Climber Pullin !!!DISABLED!!!", RobotConstants.CLIMBER_PULLIN_OFF);
		
		elevatorLimits.addDefault("Elevator Limits Enabled", RobotConstants.ELEVATOR_LIMITS_ON);
		elevatorLimits.addObject("Elevator Limits !!!DISABLED!!!", RobotConstants.ELEVATOR_LIMITS_OFF);
		
		SmartDash.displayCompetitionChoosers(robotStartPosition, crossOver, elevatorLimits, climberPullin);
		
        if (!isCompetition)		{
             SmartDash.displayRecordPlaybackChoosers(recorder, fileRecorder);  
        }
  	}

	/**
	 * This function is called once each time the robot enters Disabled mode.
	 * You can use it to reset any subsystem information you want to clear when
	 * the robot is disabled.
	 */
	@Override
	public void disabledInit() {
		//Clear out the scheduler for testing, since we may have been in teleop before
		//we came int autoInit() change for real use in competition
		Scheduler.getInstance().removeAll();

		// If I'm disabled clear any playback object I've defined in a prior trip through the code
		OI.playCmd = null;

		
		// If recording in Competition, stop and write file
		// Shut if off
		//		if (isCompetition && isTeleopRecording ) {
		//			sFile.stopRecording();
		//			recordTeleop = false;
		//			isTeleopRecording = false;
		//		}
		
	}

	@Override
	public void disabledPeriodic() {
		SmartDash.displayControlValues();
		displayAutoData();
		// Reset the climber arm pullin disable flag since we're disabled, and
		// when we re-enable we want to be pulling the arm in again (until an
		// arm command happens
		OI.disableClimberPullIn=false;

	}

	/**
	 * Determine which side of the switches and scales is our color
	 * Drive there and drop off a cube
	 */
	@Override
	public void autonomousInit() {
		//Clear out the scheduler for testing, since we may have been in teleop before
		//we came int autoInit() change for real use in competition
		Scheduler.getInstance().removeAll();
		
		// Get initial Yaw when Auto mode initializes
		OI.gyro.zeroYaw();
		OI.AutoInitYawValue = OI.gyro.getYaw();

		// Reset Drive Train
		OI.driveTrain.reset();
		
		// Initial Elevator
		OI.elevator.reset();
		OI.elevator.resetEncoder();
		
		// Reset the climber arm pullin disable flag since we're starting auto, and
		// we want to be pulling the arm in again (until an arm command happens -- shouldn't in auto)
		OI.disableClimberPullIn=false;

		
		SmartDash.displayControlValues();
		RioLogger.debugLog("Autoinit start position is " + startPosition);
		displayAutoData();
		RioLogger.debugLog("Autoinit start position (2) is " + startPosition);

		// Beginning of a match, clear flag that says we have received game data
		// from the field, until we actually read a good game data message in this match
		gameDataFromField = false;
		processedGameInfo = false;
		runSimpleAuto = false;
		autoGameChecks = 0;

		// Get Game Data from field, Driver Station or default to no game data
		gameData = getGameData();

        if (gameDataFromField) {
        	if (isCompetition) {
				isCmdFileEOF = false;
				processedGameInfo = true;
				autonomousCompetition(gameData,gameDataFromField);
			} else {
				isCmdFileEOF = false;
				autonomousTestRecording();
			}
        }
	}


	/**
	 * This function is called periodically during autonomous.
	 */
	@Override
	public void autonomousPeriodic() {
		// If we got into auto periodic and we don't have fresh game data
		// try and get game data for a while
		if (!gameDataFromField && !runSimpleAuto) {
			gameData = getGameData();	
		}
        if ((gameDataFromField || runSimpleAuto) && !processedGameInfo) {
        	if (isCompetition) {
				isCmdFileEOF = false;
				processedGameInfo = true;
				// If we got here, because time elapsed and we have to run a simple auto,
				// then gameDataFromField should be false so we pick the right file.
				// RioLogger.errorLog("about to call autonomousCompetition with runsimple="+runSimpleAuto+" and gamedata="+gameDataFromField);
				autonomousCompetition(gameData,gameDataFromField);
				// RioLogger.errorLog("back from call to autonomousCompetition with runsimple="+runSimpleAuto+" and gamedata="+gameDataFromField);
        	}
        }

		if (!isCmdFileEOF && (gameDataFromField || runSimpleAuto)) {
			isCmdFileEOF = OI.playCmd.execute();
		}
		SmartDash.displayControlValues();
		displayAutoData();
		
		if ((autoGameChecks > autoGameCheckLimit) && !runSimpleAuto && !processedGameInfo) {
			// we checked as much as we can, time to just try and cross the line
			RioLogger.errorLog("no good data, setting runSimpleAuto to true!");
			runSimpleAuto = true;
		} else {
			if (!processedGameInfo) {
		    	autoGameChecks++;
			}
		}
	}

	private void autonomousCompetition(String gameData,boolean gameDataFromField) {
		boolean isCrossOver = RobotConstants.AUTO_SCALE_CROSS.equals(crossOver.getSelected());
		String autoFileName = determineAutoFile(startPosition,isCrossOver,gameData,gameDataFromField);
		RioLogger.errorLog("Autonomous CMD File is  " + autoFileName);
		if (OI.playCmd == null) {
			OI.playCmd = new PlaybackCommands(autoFileName);
			RioLogger.debugLog("created new PlaybackCommands");
		}
		OI.playCmd.initialize();
	}

	private void autonomousTestRecording() {
		String autoRecorderName = fileRecorder.getSelected();
		RioLogger.debugLog("autonomousTestRecording file is " + autoRecorderName);
		if (OI.playCmd == null) {
			OI.playCmd = new PlaybackCommands(autoRecorderName);
			RioLogger.debugLog("created new PlaybackCommands");
		}
		OI.playCmd.initialize();
	}
	
	private String determineAutoFile(String startPosition, boolean isCrossOver, String gameData, boolean gameDataFromField) {
		String fileName = RobotConstants.CENTER_2_LEFT; // Default
		boolean isRobotCenter = RobotConstants.AUTO_ROBOT_MIDDLE.equals(startPosition);
		boolean isRobotLeft = RobotConstants.AUTO_ROBOT_LEFT.equals(startPosition);
		boolean isRobotRight = RobotConstants.AUTO_ROBOT_RIGHT.equals(startPosition);
		
		// Robot starts in Center
		if (isRobotCenter) {
			if (gameDataFromField) {
				if (gameData.charAt(RobotConstants.NEAR_SWITCH) == 'L') { 
					fileName = RobotConstants.CENTER_2_LEFT;
				} else {
					fileName = RobotConstants.CENTER_2_RIGHT;
				}
			} else {
				fileName = RobotConstants.CENTER_2_RIGHT_SIMPLE;
			}
		}
		// Robot starts on the Left or Right
		if (!isRobotCenter) {
			boolean isLeftSwitch = false;
			boolean isRightSwitch = false;
			boolean isLeftScale = false;
			boolean isRightScale = false;

			if (gameDataFromField) {
				isLeftSwitch = gameData.charAt(RobotConstants.NEAR_SWITCH) == 'L';
				isRightSwitch = gameData.charAt(RobotConstants.NEAR_SWITCH) == 'R';
				isLeftScale = gameData.charAt(RobotConstants.SCALE) == 'L';
				isRightScale = gameData.charAt(RobotConstants.SCALE) == 'R';
			}
			
			// Robot starts on Left
			if (isRobotLeft ) {
				if (gameDataFromField) {
					// Easy - Left Switch, Left Scale
					if (isLeftSwitch && isLeftScale) {
						fileName = RobotConstants.LEFT_2_SCALE;
					}
					// Left Scale, Right Switch
					if (isLeftScale && isRightSwitch) {
						fileName = RobotConstants.LEFT_2_SCALE_NOSWITCH; 
					}
					// Right Scale
					if (isRightScale) {
						if (isCrossOver) {
							fileName = RobotConstants.LEFT_2_SCALE_RIGHT;
						} else {
							// Left Switch - No Cross Over
							if (isLeftSwitch) {
								fileName = RobotConstants.LEFT_2_SCALE_SHORT_SWITCH;
							} else {
								fileName = RobotConstants.LEFT_2_SCALE_SHORT;
							}
						}
					}
				} else {
					fileName = RobotConstants.LEFT_2_SCALE_SHORT;
				}
			}
			// Robot starts on Right
			if (isRobotRight) {
				if (gameDataFromField) {
					// Easy -  Right Switch, Right Scale
					if (isRightSwitch && isRightScale) {
						fileName = RobotConstants.RIGHT_2_SCALE;
					}
					// Right Scale, Left Switch
					if (isRightScale && isLeftSwitch) {
						fileName = RobotConstants.RIGHT_2_SCALE_NOSWITCH; 
					}
					// Left Scale
					if (isLeftScale) {
						if (isCrossOver) {
							fileName = RobotConstants.RIGHT_2_SCALE_LEFT;
						} else {
							// Right Switch  - No Cross Over
							if (isRightSwitch) {
								fileName = RobotConstants.RIGHT_2_SCALE_SHORT_SWITCH;
							} else {
								fileName = RobotConstants.RIGHT_2_SCALE_SHORT;
							}
						}
					}
				} else {
					fileName = RobotConstants.RIGHT_2_SCALE_SHORT;
				}
			}
		}
		return fileName;
	}


	@Override
	public void teleopInit() {		
		//Clear out the scheduler for testing, since we may have been in teleop before
		//we came int autoInit() change for real use in competition
		Scheduler.getInstance().removeAll();
		OI.climber.reset();
		
		// Reset the climber arm pullin disable flag since we're just staring teleop, and
		//  we want to be pulling the arm in again (until an
		// arm command happens)
		OI.disableClimberPullIn=false;

		driveJoy = new DriveWithJoysticks();
		driveJoy.start();
		moveElevator = new MoveElevatorWithJoystick();
		moveElevator.start();
		moveClimberArm = new MoveClimberArm();
		moveClimberArm.start();
		
		// For Recording Files - In record mode record both CMD and SPEED files
		//
		if (!isCompetition) {
			String autoRecorderName = fileRecorder.getSelected();
			String[] namesplit = autoRecorderName.split("\\.");
			String speedFileName = namesplit[0] + ".speeds." + namesplit[1];
			OI.sFile = new SpeedFile(speedFileName);
			OI.cmdFile = new CommandFile(autoRecorderName);
		}

	}

	/**
	 * This function is called periodically during operator control.
	 */
	@Override
	public void teleopPeriodic() {
		Scheduler.getInstance().run();
		SmartDash.displayControlValues();
		displayAutoData();
				
		// If the robot is record mode, then this block will record CMD and SPEED files
		// File name --> DriverStation.dashboard control
		//
		if (!isCompetition) {
			String dashboardRecMode = recorder.getSelected();
			if (!isRecording && "start".equals(dashboardRecMode)) {
				OI.sFile.startRecording();
				OI.cmdFile.startRecording();
				isRecording = true;
				OI.isRecording = true;
			}
			if (isRecording && ("stop".equals(dashboardRecMode)) ) {
				OI.sFile.stopRecording();
				OI.cmdFile.addCommand(Commands.ELEVATOR, 0.0, 0.0, true);
				OI.cmdFile.stopRecording();
				isRecording = false;
				OI.isRecording = false;
			}
			if (isRecording) {
				double leftPwr = OI.driveTrain.getLeftPower();
				double rightPwr = OI.driveTrain.getRightPower();
				OI.sFile.addSpeed
				  (leftPwr, rightPwr,
				   OI.driveTrain.getLeftDistance(), OI.driveTrain.getRightDistance(),
				   OI.driveTrain.getLeftRate(), OI.driveTrain.getRightRate());
				OI.cmdFile.addCommand(Commands.DRIVE_CHAIN, leftPwr, rightPwr, false);
				
				double elevatorPwr = OI.elevator.getCurrentSpeed();
				if (Math.abs(elevatorPwr) > RobotConstants.ELEVATOR_POWER_TOL) {
					OI.cmdFile.addCommand(Commands.ELEVATOR, elevatorPwr, 0.0, false);
					isElevatorInTolerance = true;
				} else {
					if (isElevatorInTolerance) {
						OI.cmdFile.addCommand(Commands.ELEVATOR, 0.0, 0.0, false);
						isElevatorInTolerance = false;
					}
				}
				
			}

		}
		
		// In competition - Records a file during teleop
		// Start recording with a file 
		//		if (isCompetition) {
		//			String path =  File.separator + "home" + File.separator + "lvuser" + File.separator ;
		//			String filename = path+ new SimpleDateFormat("recorder.yyyy-MM-dd_hh.mm.ss'.txt'").format(new Date());
		//			if (recordTeleop && !isTeleopRecording) {
		//				sFile = new SpeedFile(filename);
		//				sFile.startRecording();
		//				isTeleopRecording = true;
		//			}
		//			if (isTeleopRecording) {
		//				sFile.addSpeed
		//				  (OI.driveTrain.getLeftPower(), OI.driveTrain.getRightPower(),
		//				   OI.driveTrain.getLeftDistance(), OI.driveTrain.getRightDistance(),
		//				   OI.driveTrain.getLeftRate(), OI.driveTrain.getRightRate());
		//			}
		//		}
	}

	/**
	 * This function is called periodically during test mode.
	 */
	@Override
	public void testPeriodic() {
		SmartDash.displayControlValues();
		displayAutoData();
	}
	

	
	// This method will attempt to get the game data from the field. If it is
	// invalid or cannot be retrieved then set a flag 
	private String getGameData() {
		final int MAX_GAMEDATA_LOOPS = 10;
		final double DELAY_FOR_GAMEDATA = 0.001;
		String gameData = "";

		// Read game data from driver station
		for (int i = 0; i < MAX_GAMEDATA_LOOPS; i++) {
			gameData = DriverStation.getInstance().getGameSpecificMessage();
			if (gameData != null && !gameData.isEmpty()) {
				gameDataFromField = true;
				break;
			}
			Timer.delay(DELAY_FOR_GAMEDATA);
		}
		RioLogger.debugLog("Robot.getGameData() retrieved - " + gameData);
		RioLogger.debugLog("Robot.getGameData() gameDataFromField - " + gameDataFromField);
		System.out.println("gamedata from driver station = " + gameData);
		return gameData;
	}
	
	private void  displayAutoData () {
		startPosition = robotStartPosition.getSelected();
		autoFileName = fileRecorder.getSelected();
		allowCrossOver= crossOver.getSelected();
		recordState = recorder.getSelected();
		enableElevatorLimits = elevatorLimits.getSelected();
		enableClimberPullin = climberPullin.getSelected();
		SmartDash.displayControlValues();
		SmartDash.displayGameData(gameData);
		SmartDash.displayStartPosition(startPosition);
		SmartDash.displayCrossOver(allowCrossOver);
		SmartDash.displayElevatorLimits(enableElevatorLimits);
		SmartDash.displayClimberPullin(enableClimberPullin);
		OI.elevatorLimitIsEnabled = RobotConstants.ELEVATOR_LIMITS_ON.equals(enableElevatorLimits);
		OI.climbPullinIsEnabled = RobotConstants.CLIMBER_PULLIN_ON.equals(enableClimberPullin);
		if (!isCompetition) {
			SmartDash.displayRecordState(recordState);
			SmartDash.displayAutoFileName(autoFileName);
		}
	}

}
