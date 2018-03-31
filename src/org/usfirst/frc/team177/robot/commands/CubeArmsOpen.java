package org.usfirst.frc.team177.robot.commands;

import org.usfirst.frc.team177.lib.Commands;
import org.usfirst.frc.team177.robot.OI;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class CubeArmsOpen extends Command {
	private boolean state = true;

    public CubeArmsOpen() {
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	OI.cubeArms.set(state);
    	if (OI.isRecording) {
			OI.cmdFile.addCommand(Commands.CUBE_ARMS, 0.0, 0.0, state);
    	}
    	// DriverStation.reportError("CubeArms Initialize Called state is " + state, false);
   }

    protected void execute() {
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return true;
    }

    // Called once after isFinished returns true
    protected void end() {
    	// DriverStation.reportError("CubeArms End Called = " + state, false);
    	OI.cubeArms.set(state);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	// DriverStation.reportError("CubeArms Interrupted Called", false);
    	OI.cubeArms.set(state);
   }

}
