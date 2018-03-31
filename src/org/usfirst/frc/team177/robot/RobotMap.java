/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.usfirst.frc.team177.robot;

public class RobotMap {

    // Drive 
	/** These are the drive train controllers for 2018 competition AND PRACTICE (now) bot **/
	public static int driveRightMotorFront = 2;
	public static int driveRightMotorMiddle = 23;
	public static int driveRightMotorRear = 24;
	public static int driveLeftMotorFront = 1;
	public static int driveLeftMotorMiddle = 21;
	public static int driveLeftMotorRear = 22;
	
	
	// Drive Train Encoders for 2018 practice bot
	public static int leftEncoderChannel1 = 2;
	public static int leftEncoderChannel2 = 3;
	public static int rightEncoderChannel1 = 0;
	public static int rightEncoderChannel2 = 1;
	// Drive train encoders for 2018 competition bot
	
	// Drive Train Solenoids
	public static int driveShiftSolenoid = 0;
	
	// Robot Casters Solenoid -- NOT used anywhere yet, but maybe later
	public static int casterSolenoid = 1;
	
	// Cube Pickup Motors -- practict bot has Talons instead with the same canIDs
	public static int cubePickupLeft = 25;   // Competition bot --> Practice bot 5,1
	public static int cubePickupRight = 26;
	
	// Cube Pickup Solenoids
	public static int cubePickupSolenoid = 2;
	
	// Elevator Motors
	public static int elevatorMotor1canID=8; // Competition bot --> change for skate and practice
	public static int elevatorMotor2canID=9;
	
	// Elevator MAG encoders should be on Talon with ID ?

	// Climber Motors
	public static int climberMotor1canID=41; // This is the single motor to rotate the climber arm
	public static int climberMotor2canID=42; // \_These two motors are in a gearbox connected to the winch
	public static int climberMotor3canID=43; // /

	// Four Bar mechanism solenoid
	public static int fourBarSolenoid=3;
	
	// Joy Sticks
	public static int leftJoystick = 0;
	public static int rightJoystick = 1;
	public static int gamePad = 2;
	
	// Game Pad Buttons
	public static int gamePadCubePickupReverse = 8;
	public static int gamePadCubePickup = 6;
	public static int gamePadCubeArmsOpen = 1;
	public static int gamePadCubeArmsClose = 2; 

	public static int gamePadFourBarUpDown = 3;

//	public static int gamePadClimberUp = 5; 		//climber arm is now on gamepad thumb stick 3
//	public static int gamePadClimberDown = 7; 		//
	public static int gamePadClimberWinchIn = 5;	//right-side bumper
	public static int gamePadClimberWinchOut = 7;	//right-side trigger
	
	public static int gamePadChangeCurrent1 = 9; //Current
	public static int gamePadChangeCurrent2 = 10;
	// Joy Stick and Game Pad Switches and Triggers
	public static int rightJoystickShifter = 3;
	public static int gamePadElevatorCommandStick = 1;
	public static int gamePadClimberArmCommandStick = 3; 
	
	// Limit Switch 
	public static int elevatorTopSwitch = 8;
	public static int elevatorBottomSwitch = 7;
	

	
}
