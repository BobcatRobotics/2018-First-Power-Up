package org.usfirst.frc.team177.robot.commands;

import org.usfirst.frc.team177.lib.RioLogger;

public class AutoFromCenter extends AutoCommand {
	private String fileName;

	public AutoFromCenter(String gameData, boolean gameDataFromField) {
		super(gameData);
		if (autoGameData.charAt(RobotConstants.NEAR_SWITCH) == 'L') { 
			RioLogger.debugLog("AutoFromCenter driving left");
			fileName = RobotConstants.CENTER_2_LEFT;
		} else {
			RioLogger.debugLog("AutoFromCenter driving right");
			fileName = RobotConstants.CENTER_2_RIGHT;
		}
		setAutoCommands(fileName,gameDataFromField);
	}

	@Override
	protected void initialize() {
		super.initialize();
	}

	private void setAutoCommands(String fileName,boolean gameDataFromField) {
		addSequential(new ShiftHigh());
		addSequential(new PlaybackSpeeds(fileName)); 
		RioLogger.debugLog("AutoFromCenter added PlaybackSpeeds. gameDataFromField " + gameDataFromField);
		//addSequential(new MoveElevatorAuto(ElevatorSetPosition.UP));
		if (gameDataFromField) {
			addSequential(new EjectCube());
			RioLogger.debugLog("AutoFromCenter added ejectCube ");
		}
	}
}
