package org.usfirst.frc.team177.robot;

import org.usfirst.frc.team177.lib.RioLogger;
import org.usfirst.frc.team177.lib.SmartPID;

import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.SPI.Port;

public class NavxGyro extends AHRS implements PIDOutput {
	private SmartPID gyroPID = new SmartPID(0.0, 0.035, 0.003, 0.0);

	private PIDController turnController;
	private double toleranceDegrees = 2.0;
	private double rotateToAngleRate;

	public NavxGyro(Port spi_port_id) throws RuntimeException {
		super(spi_port_id);
	}

	public void initTurnController() {
		RioLogger.debugLog("Gyro PID (P,I,D) = " + gyroPID.getP() + ", " + gyroPID.getI() + ", " + gyroPID.getD());
		RioLogger.debugLog("Gyro Degree Tolerance = " + toleranceDegrees);
		// Configure PID
		turnController = new PIDController(gyroPID.getP(), gyroPID.getI(), gyroPID.getD(), gyroPID.getFF(), this, this);
		turnController.setInputRange(-180.0f, 180.0f);
		turnController.setOutputRange(-0.41, 0.41);
		turnController.setAbsoluteTolerance(toleranceDegrees);
		turnController.setContinuous(true);
		turnController.disable();
	}

	public void setGyroPID(SmartPID gyroPID) {
		this.gyroPID = gyroPID;
	}

	public void setToleranceDegrees(double toleranceDegrees) {
		this.toleranceDegrees = toleranceDegrees;
	}

	public double getRotateToAngleRate() {
		return rotateToAngleRate;
	}

	public void turnToAngle(double angle) {
		turnController.setSetpoint(angle);
		rotateToAngleRate = 0; // This value will be updated in the pidWrite() method.
		turnController.enable();
	}

	public void stopTurn() {
		turnController.disable();
		// zeroYaw();
	}

	public boolean hasStopped() {
		return (!isMoving() && !isRotating());
	}

	@Override
	/* This function is invoked periodically by the PID Controller, */
	/* based upon navX MXP yaw angle input and PID Coefficients. */
	public void pidWrite(double output) {
		rotateToAngleRate = output;
	}

	public void calibrate() {
		try {
			int maxCalibrationPasses = 20;
			for (int iCalibrationPasses = 0; iCalibrationPasses < maxCalibrationPasses; iCalibrationPasses++) {
				if (!OI.gyro.isCalibrating())
					break;
				RioLogger.log("robotInit() gyro is calibrating, pass " + iCalibrationPasses);
				try {
					Thread.sleep(100); // Sleep 1/10 of second
				} catch (InterruptedException e) {
					RioLogger.errorLog("navX-MXP initialization thread exception " + e);
				}
			}

			RioLogger.log("robotInit() gyro is calibrating " + OI.gyro.isCalibrating());
			if (!OI.gyro.isCalibrating())
				OI.gyro.zeroYaw();
			RioLogger.log("robotInit() currentYaw " + OI.gyro.getYaw());
		} catch (RuntimeException ex) {
			RioLogger.errorLog("navX-MXP initialization error " + ex);
		}
	}
}
