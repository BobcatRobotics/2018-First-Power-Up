package org.usfirst.frc.team177.robot.commands;

import org.usfirst.frc.team177.lib.CommandFile;
import org.usfirst.frc.team177.lib.CommandRecord;
import org.usfirst.frc.team177.lib.Commands;
import org.usfirst.frc.team177.lib.RioLogger;
import org.usfirst.frc.team177.robot.OI;

import edu.wpi.first.wpilibj.Timer;

public class PlaybackCommands {
	private CommandFile cmdFile = null;
	private	CommandRecord cmdRec = null;
	private double startTime = 0.0;
	private double currentTime = 0.0;
	private int cmdRecNbr = 0;
	private boolean readCmd = true;

	private PlaybackCommands() {
	}

	public PlaybackCommands(String fileName) {
		this();
		cmdFile = new CommandFile(fileName);
		cmdFile.readRecordingFile();
		RioLogger.debugLog("PlaybackCommands finished reading " + fileName);
	}

	public void initialize() {
		startTime =Timer.getFPGATimestamp();
		//startTime = System.currentTimeMillis();
		currentTime = 0.001;
		cmdRecNbr = 0;
		readCmd = true;
    	cmdRec = new CommandRecord();
    	cmdRec.setCommandKeys(Commands.NONE, currentTime);
	}
	
    // Called this command to read and execute commands
	// This will run commands until currentTime > time in CMD File
    public boolean execute() {
    	boolean fileFinished = false;
    	while (cmdRec.getID() != Commands.EOF) {
    		if (readCmd) {
	    		// Read a line from the file of commands
	    		cmdRec = cmdFile.getRawData(cmdRecNbr);
	    		RioLogger.debugLog("cmd rec nbr " + cmdRecNbr + " CMD is " + cmdRec.getID() +  " leftPwr " + cmdRec.getSpeed()[0]); 
    		}
    		// Did I just read the EOF?, if so set finished and leave execute.
    		if (Commands.EOF.equals(cmdRec.getID())) {
    			fileFinished = true;
    			break;
    		}
    		
    		//See how must time has elapesed since I started running this command file
    		currentTime = (Timer.getFPGATimestamp() - startTime);
 
    		// If the currentTime, shows that I'm past the time to run the command I just
    		// read, then go ahead and process that command.
    		double cmdTime = cmdRec.getTotalTime();
    		//OI.debugLog("cmdTime " + cmdTime + " currentTime " + currentTime);
    		if (cmdTime < currentTime) {
    			processCommand(cmdRec);
    			cmdRecNbr++;
    			readCmd = true;
    			continue;
    		}
	    	// I read a command, but it's not yet time to execute, so set a flag so I don't read any more lines until I've executed a command
	    	readCmd = false;
	        // Ok, then, I want to leave the while loop
			break;
	   }
    	return fileFinished;
   }

	private void processCommand(CommandRecord cmdRec) {
		switch (cmdRec.getID()) {
			case CUBE_SPINNERS:
				double[] cubeMotorSpeeds = cmdRec.getSpeed();
				// When picking up speeds are set, when done speeds are
				// set to 0.0, 0.0. No need to check state
				OI.cubeLeftMotor.set(cubeMotorSpeeds[0]);
				OI.cubeRightMotor.set(cubeMotorSpeeds[1]);
				break;
			case CUBE_ARMS:
			    	OI.cubeArms.set(cmdRec.getState());
				break;
			case DRIVE_CHAIN:
				double[] driveTrainSpeeds = cmdRec.getSpeed();
				OI.driveTrain.drive(driveTrainSpeeds[0], driveTrainSpeeds[1]);
				break;
			case ELEVATOR:
				double[] elevSpeeds = cmdRec.getSpeed();
				OI.elevator.elevate(elevSpeeds[0]);
				break;
			case FOURBAR:
			   	OI.fourBar.set(cmdRec.getState());
			    break;
			case NONE:
			case EOF:
			default:
				break;
		}
		return;
	}
}
