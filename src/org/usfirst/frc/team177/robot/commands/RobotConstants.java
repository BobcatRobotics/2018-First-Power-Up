package org.usfirst.frc.team177.robot.commands;

public class RobotConstants {
	// Speed Constants for Auto and TeleopMode
	protected static final double INITIAL_LEFT_POWER_FORWARD = -0.68;
	protected static final double INITIAL_RIGHT_POWER_FORWARD = -0.6;
	
	protected static final double INITIAL_ELEVATOR_UP_POWER = 0.450;
	protected static final double INITIAL_ELEVATOR_UP_STOP_POSITION = 600000.0;

	
	// Speed Recording File Names
	public static final String RECORD_FILE_NAME = "speedfile.txt";
	public static final String CENTER_2_RIGHT =  "center2right.txt";
	public static final String CENTER_2_RIGHT_SIMPLE = "center2right_simple.txt";
	public static final String CENTER_2_LEFT = "center2left.txt";
	public static final String LEFT_2_SCALE =  "left2scale.txt";
	public static final String RIGHT_2_SCALE =  "right2scale.txt";
	public static final String RIGHT_2_SCALE_LEFT =  "right2scaleleft.txt";
	public static final String LEFT_2_SCALE_RIGHT =  "left2scaleright.txt";
	public static final String LEFT_2_SCALE_SHORT =  "left2scale_short.txt";
	public static final String RIGHT_2_SCALE_SHORT =  "right2scale_short.txt";
	public static final String LEFT_2_SCALE_SHORT_SWITCH =  "left2scale_short_switch.txt";
	public static final String RIGHT_2_SCALE_SHORT_SWITCH =  "right2scale_short_switch.txt";
	// These files are generated from LEFT_2_SCALE, RIGHT_2_SCALE
	public static final String LEFT_2_SCALE_NOSWITCH =  "left2scale_noswitch.txt";
	public static final String RIGHT_2_SCALE_NOSWITCH =  "right2scale_noswitch.txt";
	


	// Options for Robot Starting position
	public static final String AUTO_ROBOT_LEFT = "aRobotLeft";
	public static final String AUTO_ROBOT_MIDDLE = "aRobotMiddle";
	public static final String AUTO_ROBOT_RIGHT = "aRobotRight";

	// Options for Autonomous Crossing Over
	public static final String AUTO_SCALE_CROSS = "cross";
	public static final String AUTO_NO_SCALE_CROSS = "noCross";

	// Options for Climber Pull In
	public static final String CLIMBER_PULLIN_ON = "on";
	public static final String CLIMBER_PULLIN_OFF = "off";

	// Options for Climber Pull In
	public static final String ELEVATOR_LIMITS_ON = "on";
	public static final String ELEVATOR_LIMITS_OFF = "off";

	// Constants for Starting Position Left or Right
	public static final double LR_DISTANCE_1 = 192.0;
	public static final double LR_DISTANCE_2 = 30.0;
	public static final double LR_DISTANCE_3 = 30.0;
	public static final double LR_TURN_ANGLE_1 = 36.0;
	public static final double LR_TURN_ANGLE_2 = -36.0;

	// Constants for Starting  Position Middle
	public static final double MID_MOTOR_SPEED = 0.4;
	public static final double MID_DISTANCE_1 = 30.0;
	public static final double MID_DISTANCE_2 = 45.0;
	public static final double MID_DISTANCE_3 = 45.0;
	public static final double MID_TURN_ANGLE_1 = 69.0;
	public static final double MID_TURN_ANGLE_2 = -69.0;
	
	// Constant to flip elevator motor speed command so
	// that pushing up on gamepad elevator thumb stick
	// makes the elevator go up.  This can also be used if
	// the motors get wired and you only need to flip one.
	//   -1.0 flips direction, 1.0 doesn't flip direction
	public static final double FLIP_ELEV_DIRECTION1 = -1.0;
	
	// Constant to flip winch motor speed command so
	// that pushing up on gamepad elevator thumb stick
	// makes the winch go in/out.  This can also be used if
	// the motors get wired and you only need to flip one.
	//   -1.0 flips direction, 1.0 doesn't flip direction
	public static final double FLIP_WINCH_DIRECTION1 = 1.0;
	public static final double FLIP_WINCH_DIRECTION2 = 1.0;
	
	// Constant to flip climber arm motor speed command so
	// that pushing up on gamepad elevator thumb stick
	// makes the arm go up/down.  
	public static final double FLIP_ARM_DIRECTION = 1.0;

	// Constant to scale back climber arm command
	public static final double CLIMBER_ARMMOTOR_COMMAND_SCALER = 0.6;

	// Constant 
	public static final double CLIMBER_ARMMOTOR_COMMAND_PULLIN = 0.15;
	public static final double CLIMBER_ARMMOTOR_COMMAND_OFF_TOL = 0.05;
	
	// Elevator Power Tolerance Amount
	public static final double ELEVATOR_POWER_TOL = 0.05;
	
	// Elevator Bias Amount
	// This pwer will be constantly supplied to the elevator in teleop as
	// part of MoveElevatorWithJoystick()
	public static final double ELEVATOR_BIAS_POWER = -0.0; 
	
	
	// Index of characters in the game data 
	// 
	public static final int NEAR_SWITCH = 0;
	public static final int SCALE = 1;
	public static final int FAR_SWITCH = 2;
	


}
