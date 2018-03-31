/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.usfirst.frc.team177.robot;

import org.usfirst.frc.team177.lib.CommandFile;
import org.usfirst.frc.team177.lib.RioLogger;
import org.usfirst.frc.team177.lib.RioLoggerThread;
import org.usfirst.frc.team177.lib.SmartDashLog;
import org.usfirst.frc.team177.lib.SpeedFile;
import org.usfirst.frc.team177.robot.commands.CubeArms;
import org.usfirst.frc.team177.robot.commands.CubeArmsClose;
import org.usfirst.frc.team177.robot.commands.CubeArmsOpen;
import org.usfirst.frc.team177.robot.commands.EjectCube;
import org.usfirst.frc.team177.robot.commands.FourBarUpDown;
import org.usfirst.frc.team177.robot.commands.PickupCube;
import org.usfirst.frc.team177.robot.commands.PlaybackCommands;
import org.usfirst.frc.team177.robot.commands.ShiftHigh;
import org.usfirst.frc.team177.robot.commands.ShiftLow;
import org.usfirst.frc.team177.robot.commands.WinchIn;
import org.usfirst.frc.team177.robot.commands.WinchOut;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.buttons.Button;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.buttons.Trigger;

/**
 * This class is the glue that binds the controls on the physical operator
 * interface to the commands and command groups that allow control of the robot.
 */
public class OI {
	/* Loggers */
	public static RioLoggerThread logFile;
	public static SmartDashLog smartLog = new SmartDashLog();

	/* Drive Chain */
	public static DriveTrain driveTrain = new DriveTrain();

	/* Elevator */
	public static Elevator elevator = new Elevator();
	public static boolean elevatorLimitIsEnabled = true;

	// Instantiate Climber
	public static Climber climber = new Climber();
	public static boolean climbPullinIsEnabled = true;
	public static boolean disableClimberPullIn=false;

	/* Motors */
	// Competition bot has VictorSPXs for the cube arm motors
	//public static WPI_VictorSPX cubeLeftMotor = new WPI_VictorSPX(RobotMap.cubePickupLeft);
	//public static WPI_VictorSPX cubeRightMotor = new WPI_VictorSPX(RobotMap.cubePickupRight);

	// Practice bot has TalonSRXs for the cube arm motors (but CanIDs should be same)
	public static WPI_TalonSRX cubeLeftMotor = new WPI_TalonSRX(RobotMap.cubePickupLeft);
	public static WPI_TalonSRX cubeRightMotor = new WPI_TalonSRX(RobotMap.cubePickupRight);

	// public static TalonMagEncoder elevatorEncoder = new TalonMagEngcoder(3);

	/* Solenoids */
	public static Solenoid cubeArms = new Solenoid(RobotMap.cubePickupSolenoid); /* Controls the Cube Arms */
	public static Solenoid shifter = new Solenoid(RobotMap.driveShiftSolenoid); /* Control Drive Shifter */
	public static Solenoid fourBar = new Solenoid(RobotMap.fourBarSolenoid); /* deploy/retract the cube grabber */

	/* Gyro */
	public static NavxGyro gyro;
	public static double AutoInitYawValue;

	/* Joysticks */
	public static Joystick rightStick = new Joystick(RobotMap.rightJoystick);
	public static Joystick leftStick = new Joystick(RobotMap.leftJoystick);
	public static Joystick gamePad = new Joystick(RobotMap.gamePad);

	/* Buttons */
	public static Button btnCubePickup = new JoystickButton(gamePad, RobotMap.gamePadCubePickup);
	public static Button btnCubeArmsOpen = new JoystickButton(gamePad, RobotMap.gamePadCubeArmsOpen);
	public static Button btnCubeArmsClose = new JoystickButton(gamePad, RobotMap.gamePadCubeArmsClose);

	public static Button btnCubePickupReverse = new JoystickButton(gamePad, RobotMap.gamePadCubePickupReverse);
	public static Button btnFourBarUpDown = new JoystickButton(gamePad, RobotMap.gamePadFourBarUpDown);
	// public static Button btnClimberUp = new JoystickButton(gamePad,
	// RobotMap.gamePadClimberUp);
	// public static Button btnClimberDown = new JoystickButton(gamePad,
	// RobotMap.gamePadClimberDown);
	public static Button btnClimberWinchIn = new JoystickButton(gamePad, RobotMap.gamePadClimberWinchIn);
	public static Button btnClimberWinchOut = new JoystickButton(gamePad, RobotMap.gamePadClimberWinchOut);
	public static Button btnChangeCurrent1 = new JoystickButton(gamePad, RobotMap.gamePadChangeCurrent1);
	public static Button btnChangeCurrent2 = new JoystickButton(gamePad, RobotMap.gamePadChangeCurrent2); // Current;
	/* Triggers */
	public static Trigger trigShifter = new JoystickButton(rightStick, RobotMap.rightJoystickShifter);

	/* DigitBoard */
	// public static DigitBoard digitBoard = DigitBoard.getInstance();

	// These commands are in OI so that the teleop commands can access
	public static SpeedFile sFile = null;
	public static CommandFile cmdFile = null;
	public static PlaybackCommands playCmd = null;
	public static boolean isRecording = false;
	
	static {

		driveTrain.setRightMotors(RobotMap.driveRightMotorFront, RobotMap.driveRightMotorMiddle,
				RobotMap.driveRightMotorRear);
		driveTrain.setLeftMotors(RobotMap.driveLeftMotorFront, RobotMap.driveLeftMotorMiddle,
				RobotMap.driveLeftMotorRear);

		driveTrain.setLeftMotorsReverse(false);
		driveTrain.setLeftEncoder(RobotMap.leftEncoderChannel1, RobotMap.leftEncoderChannel2);
		driveTrain.setRightEncoder(RobotMap.rightEncoderChannel1, RobotMap.rightEncoderChannel2);

		btnCubePickup.whileHeld(new PickupCube());
		btnCubePickupReverse.whileHeld(new EjectCube());
		btnCubeArmsOpen.whenPressed(new CubeArmsOpen());
		btnCubeArmsClose.whenPressed(new CubeArmsClose());
		btnFourBarUpDown.toggleWhenPressed(new FourBarUpDown());

		trigShifter.whenActive(new ShiftHigh());
		trigShifter.whenInactive(new ShiftLow());

		// btnClimberUp.whileHeld(new DeployClimber());
		// btnClimberDown.whileHeld(new RetractClimber());
		btnClimberWinchIn.whileHeld(new WinchIn());
		btnClimberWinchOut.whileHeld(new WinchOut());

		/* Navx mxp Gyro */
		gyro = new NavxGyro(SPI.Port.kMXP);
		RioLogger.log("robotInit() called. navx-mxp initialized");
		gyro.calibrate();

		// Start Logging Thread
		logFile = RioLoggerThread.getInstance();
		RioLogger.log("OI static block finished.");
	}
}
