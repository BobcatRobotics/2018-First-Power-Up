package org.usfirst.frc.team177.robot;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;

public class DriveTrain {
	/** Inverts drive direction **/
	private static final double INVERT_MOTOR = -1.0;
	
	private WPI_TalonSRX leftFront;
	private WPI_VictorSPX leftMiddle;
	private WPI_VictorSPX leftRear;
	private WPI_TalonSRX rightFront;
	private WPI_VictorSPX rightMiddle;
	private WPI_VictorSPX rightRear;
	
	private GrayHill leftEncoder;
	private GrayHill rightEncoder;
	
	private boolean invertLeft = true;
	
	private double leftPower = 0.0;
	private double rightPower = 0.0;
	
	
	public DriveTrain() {
		super();
	}
	
	public void setLeftMotors(int lf,int lm,int lr) {
		leftFront = new WPI_TalonSRX(lf);
		leftMiddle = new WPI_VictorSPX(lm);
		leftRear = new WPI_VictorSPX(lr);
	}
	
	public void setRightMotors(int rf,int rm,int rr) {
		rightFront = new WPI_TalonSRX(rf);
		rightMiddle = new WPI_VictorSPX(rm);
		rightRear = new WPI_VictorSPX(rr);
	}

	public void setLeftMotorsReverse(boolean invert) {
		invertLeft = invert;
	}
	
	public void setLeftEncoder(int leftEncCh1, int leftEncCh2) {
		leftEncoder = new GrayHill(leftEncCh1, leftEncCh2, false);
	}

	public double getLeftDistance() {
		return leftEncoder.getDistance();
	}

	public double getLeftRate() {
		return leftEncoder.getRate();
	}
	
	public void setRightEncoder(int rightEncCh1, int rightEncCh2) {
		rightEncoder = new GrayHill(rightEncCh1, rightEncCh2, true);
	}

	public double getRightDistance() {
		return rightEncoder.getDistance();
	}

	public double getRightRate() {
		return rightEncoder.getRate();
	}

	public double getLeftPower() {
		return leftPower;
	}

	public void setLeftPower(double leftPwr) {
		if (leftPwr > 1.0)
			leftPwr = 1.0;
		else
		if (leftPwr < -1.0)
			leftPwr = 1.0;
		
		this.leftPower = leftPwr;
	}

	public double getRightPower() {
		return rightPower;
	}

	public void setRightPower(double rightPwr) {
		if (rightPwr > 1.0)
			rightPwr = 1.0;
		else
		if (rightPwr < -1.0)
			rightPwr = 1.0;
		this.rightPower = rightPwr;
	}

	public void drive() {
		drive(leftPower,rightPower);
	}
	
	public void drive(double leftPwr, double rightPwr) {
		if (invertLeft )
			leftPwr *= INVERT_MOTOR;
		else
			rightPwr *= INVERT_MOTOR;
		
		leftFront.set(leftPwr);
		leftMiddle.set(leftPwr);
		leftRear.set(leftPwr);
		rightFront.set(rightPwr);
		rightMiddle.set(rightPwr);
		rightRear.set(rightPwr);
	}

	public void stop() {
		leftPower = 0.0;
		rightPower = 0.0;
		
		// TODO :: Could also use .stopMotor()
		leftFront.set(0.0);
		leftMiddle.set(0.0);
		leftRear.set(0.0);
		rightFront.set(0.0);
		rightMiddle.set(0.0);
		rightRear.set(0.0);
	}

	public void reset() {
		leftPower = 0.0;
		rightPower = 0.0;
		leftEncoder.reset();
		rightEncoder.reset();
	}
}