package org.usfirst.frc.team177.lib;

import org.usfirst.frc.team177.robot.OI;
import org.usfirst.frc.team177.robot.commands.FourBarUpDown;

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * This class is used to centralize SmartDashboard operation (get and set)
 * @author bobcat177
 */
public class SmartDash {
	//public static PowerDistributionPanel pdp = new PowerDistributionPanel(); //Makes the PDP panel read 
	public static int currentSelection;
	public static int currentSelect() {
		
		if(OI.btnChangeCurrent1.get() == true) {
			currentSelection --;
		}
		else if (OI.btnChangeCurrent2.get() == true) {
			currentSelection ++;
		}
		if (currentSelection > 15) {
			currentSelection = 15;
		}
		if (currentSelection < 0) {
			currentSelection = 0;
		}
		return currentSelection;
	}
	
	// This method is used to display motors/controllers/switches
	public static void displayControlValues() {
        SmartDashboard.putNumber("Right Encoder Distance:", OI.driveTrain.getRightDistance());
        SmartDashboard.putNumber("Left Encoder Distance:", OI.driveTrain.getLeftDistance());
        SmartDashboard.putNumber("Right Encoder Rate:", OI.driveTrain.getRightRate());
        SmartDashboard.putNumber("Left Encoder Rate:", OI.driveTrain.getLeftRate());
        SmartDashboard.putNumber("Auto Initial Gyro Value:", OI.AutoInitYawValue);
        SmartDashboard.putNumber("Current Gyro Value:", OI.gyro.getYaw());
         
        boolean shifterSolenoidState = OI.shifter.get();
        SmartDashboard.putString("Shifter solenoid is: ", (shifterSolenoidState ? "ON" :"OFF"));
           
       	boolean shifterSwitchState = OI.trigShifter.get();
       	SmartDashboard.putString("Shifter switch is: ",(shifterSwitchState ?  "ON" : "OFF"));
       	
       	SmartDashboard.putBoolean("Current Four Bar up/down state:",  FourBarUpDown.state);
       	
       	OI.elevator.displayDashboard();
       	
       	// Real Time Logger
       	SmartDashboard.putData("RealTimeLog",OI.smartLog);
       	
      	/** Uncomment to test on Skateboard for lag 
       	SmartDashboard.putNumber("Current current draw of PDP channel 9: ", pdp.getCurrent(9));
       	SmartDashboard.putNumber("Current current draw of PDP channel 10: ", pdp.getCurrent(10)); 
       	SmartDashboard.putNumber("Total current draw on PDP: ", pdp.getTotalCurrent());
       	SmartDashboard.putNumber("Current Temperature of PDP in celcius: ", pdp.getTemperature());
       	SmartDashboard.putNumber("Current current draw of selected PDP channel: ", pdp.getCurrent(currentSelect()));
       	SmartDashboard.putNumber("Current PDP channel on current monitor", (double)currentSelect());
       	*/
	}
	
	// The following set of methods are used to display
	// runtime game data
	public static void displayStartPosition(String startPosition) {
	       SmartDashboard.putString("Chosen Start Position:", startPosition);
	}
	
	public static void displayGameData(String gameData) {
		SmartDashboard.putString("Platform Data:", gameData);
	}
	
	public static void displayCompetitionChoosers(SendableChooser<String> startPosition, SendableChooser<String> crossOver, SendableChooser<String> elevatorLimits, SendableChooser<String> climberPullin) {
		SmartDashboard.putData("Auto mode", startPosition);
		SmartDashboard.putData("Auto Cross Over", crossOver);
		SmartDashboard.putData("ELEVATOR LIMITS !!!", elevatorLimits);
		SmartDashboard.putData("CLIMBER PULL IN !!!", climberPullin);
	}
	
	public static void displayRecordPlaybackChoosers(SendableChooser<String> recorder, SendableChooser<String> fileRecorder) {
		SmartDashboard.putData("Record Swith", recorder);
		SmartDashboard.putData("Recorder File Name", fileRecorder);
	}

	public static void displayCrossOver(String allowCrossOver) {
		SmartDashboard.putString("Allow Crossover", allowCrossOver);
	}

	public static void displayElevatorLimits(String enableElevatorLimits) {
		SmartDashboard.putString("Elevator Limit Switches are (default = On):", enableElevatorLimits);
	}

	public static void displayClimberPullin(String enableClimberPullin) {
		SmartDashboard.putString("Climber Pullin is (default = On):", enableClimberPullin);
	}

	public static void displayRecordState(String recordState) {
		SmartDashboard.putString("Record On/Off",recordState);	
	}

	public static void displayAutoFileName(String autoFileName) {
		SmartDashboard.putString("Auto File Name", autoFileName);
	}
}
