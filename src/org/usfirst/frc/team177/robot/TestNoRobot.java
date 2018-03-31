package org.usfirst.frc.team177.robot;

import org.usfirst.frc.team177.lib.CommandFile;
import org.usfirst.frc.team177.lib.CommandRecord;
import org.usfirst.frc.team177.lib.Commands;
import org.usfirst.frc.team177.lib.RioLogger;
import org.usfirst.frc.team177.lib.SpeedFile;
import org.usfirst.frc.team177.lib.SpeedRecord;

public class TestNoRobot {
	//static final String filename = "right2scale.txt";
	static final String basefilename = "commands.txt";
	static final double NBR_TEST_RECS = 50;

	public static void main(String[] args) throws InterruptedException {
		boolean testCmd = true;
		boolean testSpeed = false;
		log("TestNoRobot. Started testing filename is  - " + basefilename);
		String[] namesplit = basefilename.split("\\.");
		String speedFileName = namesplit[0] + ".speeds." + namesplit[1];
		log("TestNoRobot. speed filename is  - " + speedFileName);
	
		if (testCmd) {
			//recordCommandFile(basefilename);
			readCommandFile(basefilename);
		}
		if (testSpeed) {
			recordSpeedFile(speedFileName);
			readSpeedFile(speedFileName);
		}
	}
	
	private static void recordCommandFile(String filename) throws InterruptedException {
		CommandFile cmdFile = new CommandFile(filename);
		log("Recording Command File -  " + filename);
		cmdFile.startRecording();
		for (int x = 0; x < NBR_TEST_RECS; x++) {
			cmdFile.addCommand(Commands.DRIVE_CHAIN,new Double(x / NBR_TEST_RECS), new Double(x / NBR_TEST_RECS * -1.0), false);
			Thread.currentThread();
			Thread.sleep(20);
			if (x >= NBR_TEST_RECS - 20 && x <= NBR_TEST_RECS - 2) {
				cmdFile.addCommand(Commands.ELEVATOR,new Double(x / NBR_TEST_RECS + 1.0), new Double(x / NBR_TEST_RECS  * -1.0 - 1.0), false);
			}
			if (x == NBR_TEST_RECS - 1) {
				cmdFile.addCommand(Commands.CUBE_ARMS,0.0, 0.0, true);
				cmdFile.addCommand(Commands.CUBE_ARMS,0.0, 0.0, false);
				cmdFile.addCommand(Commands.FOURBAR,0.0, 0.0, true);
				cmdFile.addCommand(Commands.FOURBAR,0.0, 0.0, false);
				cmdFile.addCommand(Commands.CUBE_SPINNERS,-1.0, 1.0, true);
				cmdFile.addCommand(Commands.CUBE_SPINNERS,0.0, 0.0, false);
			}
		}
		cmdFile.stopRecording();
		log("Finished recording command file. # commands = " + cmdFile.getNbrOfCommands());
	}
	
	private static void readCommandFile(String filename) throws InterruptedException {
		CommandFile cmdFile = new CommandFile(filename);
		cmdFile.readRecordingFile();
		double startTime = System.currentTimeMillis();
		int cmdRecNbr = 0;
		double currentTime = (System.currentTimeMillis() - startTime) / 1000.0;
		Commands cmd = Commands.NONE;
		while (cmd != Commands.EOF) {
			CommandRecord cmdRec = cmdFile.getRawData(cmdRecNbr);
			if (Commands.EOF.equals(cmdRec.getID())) {
				break;
			}
//			if (cmdRec.getTotalTime() == 9999.0) {
//				break;
//			}
			double cmdTime = cmdRec.getTotalTime();
			log(cmdRecNbr + " [ " + cmdTime + ", " + currentTime + "]");
			if (cmdTime < currentTime) {
				log("Processing command - " + cmdRec);
				switch (cmdRec.getID()) {
					case CUBE_SPINNERS:
						System.out.println("CUBE_SPINNERS");
						break;
					case DRIVE_CHAIN:
						System.out.println("DRIVE_CHAIN");
						break;
					case ELEVATOR:
						System.out.println("ELEVATOR");
						break;
					case EOF:
						System.out.println("EOF");
						break;
					case FOURBAR:
						System.out.println("FOURBAR");
						break;
					case NONE:
					default:
						break;
				}
				cmdRecNbr++;
				System.out.println("continue");
				continue;
			}
			System.out.println("new loop");
			currentTime = (System.currentTimeMillis() - startTime) / 1000.0;
			Thread.currentThread();
			Thread.sleep(17 + (int)(Math.random() * 6));
		}
		System.out.println("Finished Processing");
	}

	private static void recordSpeedFile(String filename) throws InterruptedException {
		log("Recording Speed File -  " + filename);
		SpeedFile sFile = new SpeedFile(filename);
		sFile.startRecording();
		for (int x = 0; x < NBR_TEST_RECS; x++) {
			sFile.addSpeed(new Double(x / NBR_TEST_RECS), new Double(x / NBR_TEST_RECS), x, -x, x+Math.PI, x-Math.PI);
			Thread.currentThread();
			Thread.sleep(20);
			if (x == NBR_TEST_RECS - 1) {
				log("last record");
			}
		}
		sFile.stopRecording();
		log("Finished recording. FileSize = " + sFile.getNbrOfRows());
	}
	
	private static void readSpeedFile(String filename) throws InterruptedException {
		log("Reading Speed File -  " + filename);
		SpeedFile sFile = new SpeedFile(filename);
		sFile.readRecordingFile();
		int recCtr = 0;
		do {
			SpeedRecord sRec = sFile.getRawData(recCtr);
			log(sRec.toString());
			if (SpeedRecord.EOF==sRec.getID()) {
				log("EOF Record found. Rec Nbr  = " + recCtr);
				break;
			}

			double [] power = sFile.getPower();
			if (power[0] == 999.0) {
				break;
			}
			log("[" + power[0] + "," + power[1] + "]");
			recCtr++;
		} while(true);
		//sFile.stopRecording();
		log("Finished reading. FileSize = " + sFile.getNbrOfRows());
	}
	
	private static void log(String text) {
		RioLogger.log(text);
		System.out.println(text);
	}
	
	/**
	private static void testFileParts() {
		FileUtils.readRecording();
		String log = "";
		int nbrRecords = 0;
		int partNbr = 0;
		log = "Finished reading file. Total number of records is " + FileUtils.getTotalNbrOfRows();
		RioLogger.log(log);
		System.out.println(log);
		log = "Reading through parts. Total Parts is " + FileUtils.getTotalFileParts();
		RioLogger.log(log);
		System.out.println(log);
		log = "Part " + partNbr + " has total rows " + FileUtils.getRowsInFilePart(partNbr);
		RioLogger.log(log);
		System.out.println(log);
		while (partNbr <= FileUtils.getTotalFileParts()) {
			double[] distances = FileUtils.getDistance();
			double[] speeds = FileUtils.getSpeed();
			log = partNbr + " - " + speeds[0] + " " + speeds[1] + " " + distances[0] + " " + distances[1];
			if (speeds[0] == 999.0) {
				partNbr++;
				if (partNbr > FileUtils.getTotalFileParts())
					break;
				FileUtils.setPartNbr(partNbr);
				log = "Part " + partNbr + " has total rows " + FileUtils.getRowsInFilePart(partNbr);
				RioLogger.log(log);
				FileUtils.setPartNbr(partNbr);
			}
			else {
				RioLogger.log(log);
				System.out.println(log);
				nbrRecords++;
			}
		}
		log = "Finished Reading. Nbr of Records read is " + nbrRecords;
		RioLogger.log(log);
		System.out.println(log);
	}
	*/
}
