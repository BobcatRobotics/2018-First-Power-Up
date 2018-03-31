package org.usfirst.frc.team177.robot.commands;

import org.usfirst.frc.team177.robot.OI;
import org.usfirst.frc.team177.robot.RobotMap;

import edu.wpi.first.wpilibj.command.Command;

public class MoveClimberArm extends Command {
	public MoveClimberArm() {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
		double climberArmMotorCommand = RobotConstants.CLIMBER_ARMMOTOR_COMMAND_SCALER * OI.gamePad.getRawAxis(RobotMap.gamePadClimberArmCommandStick);
		double armMotorSpeed = RobotConstants.FLIP_ARM_DIRECTION*climberArmMotorCommand;
		
		// Set the climber arm to the scaled and flipped command.
		OI.climber.setClimberArmMotorSpeed(armMotorSpeed);
		
		// If the climber arm pull in feature is enabled (should be by default on the competition bot)
		// Then check if the scaled & flipped command is between -/+ the OFF tolerance, if so we
		// assume the climber is not being commanded and reset the command to the pull-in value.
		if (OI.climbPullinIsEnabled&&!OI.disableClimberPullIn) {
			if ((armMotorSpeed > -1.0*RobotConstants.CLIMBER_ARMMOTOR_COMMAND_OFF_TOL) &&  
				(armMotorSpeed < RobotConstants.CLIMBER_ARMMOTOR_COMMAND_OFF_TOL)) {  
				OI.climber.setClimberArmMotorSpeed(RobotConstants.CLIMBER_ARMMOTOR_COMMAND_PULLIN);
			}else {
				OI.disableClimberPullIn= true;
				//OI.climbPullinIsEnabled = false;
			}
		}
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    protected void end() {
		OI.climber.stop();
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
		OI.climber.stop();

    }
}
