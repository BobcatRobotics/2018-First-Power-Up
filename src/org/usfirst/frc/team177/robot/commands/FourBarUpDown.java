package org.usfirst.frc.team177.robot.commands;

import org.usfirst.frc.team177.lib.Commands;
import org.usfirst.frc.team177.robot.OI;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class FourBarUpDown extends Command {
	// TODO :: Check for a different type of command. I believe that there is a command that is executed once
	// TODO :: would this work with button.toggleWhenPressed()
	public static boolean state = false;

    public FourBarUpDown() {
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	state = !state;
    	OI.fourBar.set(state);
    	// DriverStation.reportError("FourBarUpDown Initialize Called state is " + state, false);
   }

    protected void execute() {
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return true;
    }

    // Called once after isFinished returns true
    protected void end() {
    	// DriverStation.reportError("FourBarUpDown End Called = " + state, false);
    	OI.fourBar.set(state);
    	if (OI.isRecording) {
    		OI.cmdFile.addCommand(Commands.FOURBAR, 0.0, 0.0, state);
    	}
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	// DriverStation.reportError("FourBarUpDown Interupted Called", false);
    	OI.fourBar.set(state);
   }
}
