package org.usfirst.frc.team177.robot;

import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Elevator {
	/* Elevator Motors */
	private WPI_TalonSRX elevatorMotor1; 
	private WPI_TalonSRX elevatorMotor2; 
	
	private DigitalInput topSwitch;  // Top Limit Switch
	private DigitalInput bottomSwitch;	// Bottom Limit Switch
	
	private double elevatorSpeed = 0.0;
	//private boolean isMoving = false;

	
	public Elevator() {
		super();
		elevatorMotor1 = new WPI_TalonSRX(RobotMap.elevatorMotor1canID);
		elevatorMotor2 = new WPI_TalonSRX(RobotMap.elevatorMotor2canID);
		
		topSwitch = new DigitalInput(RobotMap.elevatorTopSwitch);
		bottomSwitch = new DigitalInput(RobotMap.elevatorBottomSwitch);

		// Motor2 has a magnetic encoder attached
		elevatorMotor2.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Absolute,0,0);
		elevatorMotor2.setSelectedSensorPosition(0,0,0);

		reset();
	}	
	
	// CAUTION: there are two motors and one encoder, use correct motor!!
	public double getEncoderPosition() {
		return elevatorMotor2.getSelectedSensorPosition(0);
	}

	public double getEncoderVelocity() {
		return elevatorMotor2.getSelectedSensorVelocity(0);
	}

	public void reset() {
		elevatorMotor1.set(0.0);
		elevatorMotor2.set(0.0);
		
		elevatorSpeed = 0.0;
		//isMoving = false;
	}

	public void resetEncoder( ) {
		elevatorMotor2.setSelectedSensorPosition(0,0,0);
	}
	
	public void stop () {
		elevatorMotor1.stopMotor();
		elevatorMotor2.stopMotor();
		
		elevatorSpeed = 0.0;
	}
	
	public void elevate(double speed) {
		elevatorMotor1.set(speed);
		elevatorMotor2.set(speed);
		
		elevatorSpeed = speed;
	}
	
	public double getMotor1Speed() {
		return elevatorMotor1.get();
	}
	
	public double getMotor2Speed() {
		return elevatorMotor2.get();
	}

	public double getCurrentSpeed() {
		return elevatorSpeed;
	}
	
	public boolean upperSwitch() {
		return topSwitch.get();
	}
	
	public boolean lowerSwitch() {
		return bottomSwitch.get();
	}
	
	public void displayDashboard() {
	    SmartDashboard.putBoolean("Current Upper value:", topSwitch.get());
	    SmartDashboard.putBoolean("Current Lower value:", bottomSwitch.get());
       	SmartDashboard.putNumber("Current elevator encoder counts: ", getEncoderPosition());
       	SmartDashboard.putNumber("Current elevator encoder velocity cps: ", getEncoderVelocity());
	}

}
