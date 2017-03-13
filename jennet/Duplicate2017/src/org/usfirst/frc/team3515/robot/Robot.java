
package org.usfirst.frc.team3515.robot;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.RobotDrive.MotorType;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import edu.wpi.first.wpilibj.AnalogGyro;
import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.CameraServer;

//import java.util.prefs.Preferences;

import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.GyroBase;
import edu.wpi.first.wpilibj.SensorBase;
//import org.usfirst.frc.team3515.robot.commands.ExampleCommand;
//import org.usfirst.frc.team3515.robot.subsystems.ExampleSubsystem;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot {
	//gyro
	private static final int kGyroPort = 0;
	private ADXRS450_Gyro gyro = new ADXRS450_Gyro(SPI.Port.kOnboardCS0);
	// gyro calibration constant, may need to be adjusted;
	// gyro value of 360 is set to correspond to one full revolution
	private static final double kVoltsPerDegreePerSecond = 0.0128;
	private static double kAngleSetpoint = 0.0;
	private static final double kP = 0.005; // propotional turning constant
	
	//////ULTRASONIC
	
	private static final double holdDist = 185.3;
	private static final double toInches = 0.125;
	private static final int kUltrasonicPort = 0; //CHANGE THIS ACCORDINGLY
	private AnalogInput ultrasonic = new AnalogInput(kUltrasonicPort);

	
	
	
	
	//////


	//public static final ExampleSubsystem exampleSubsystem = new ExampleSubsystem();
	//public static OI oi;

	Command autonomousCommand;
	SendableChooser<Command> chooser = new SendableChooser<>();

	RobotDrive myRobot;
	Joystick stick;
	int autoLoopCounter;

	//SERVO
	Servo servoRight;
	Servo servoLeft;
	Servo servoRope;
	
	Preferences prefs;
	boolean mode1;
	boolean mode2;
	boolean mode3;

	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */
	@SuppressWarnings("deprecation")
	@Override
	public void robotInit() {
		CameraServer server = CameraServer.getInstance();
		//server.setSize(30);
		server.startAutomaticCapture("Real", autoLoopCounter);
		//	oi = new OI()
		//	chooser.addDefault("Default Auto", new ExampleCommand());
		// chooser.addObject("My Auto", new MytoCommand());
		SmartDashboard.putData("Auto mode", chooser);
		//fl, rl, fr, rr
		myRobot = new RobotDrive(0,1,2,3);
		myRobot.setInvertedMotor(MotorType.kFrontLeft, true);
		myRobot.setInvertedMotor(MotorType.kFrontRight, true);
		myRobot.setInvertedMotor(MotorType.kRearLeft, true);
		myRobot.setInvertedMotor(MotorType.kRearRight, true);
		stick = new Joystick(0);

		//GYRO
		//gyro.setSensitivity(.05);
		gyro.calibrate();
		
		//SERVOS
		servoRight = new Servo(5);
		servoLeft = new Servo(4);
		servoRope = new Servo(6);

		prefs = Preferences.getInstance();
		mode1 = prefs.getBoolean("default", true);
		mode2 = prefs.getBoolean("turnLeft", false);
		mode3 = prefs.getBoolean("turnRight", false);
		
	}

	/**
	 * This function is called once each time the robot enters Disabled mode.
	 * You can use it to reset any subsystem information you want to clear when
	 * the robot is disabled.
	 */
	@Override
	public void disabledInit() {

	}

	@Override
	public void disabledPeriodic() {
		Scheduler.getInstance().run();
	}

	/**
	 * This autonomous (along with the chooser code above) shows how to select
	 * between different autonomous modes using the dashboard. The sendable
	 * chooser code works with the Java SmartDashboard. If you prefer the
	 * LabVIEW Dashboard, remove all of the chooser code and uncomment the
	 * getString code to get the auto name from the text box below the Gyro
	 *
	 * You can add additional auto modes by adding additional commands to the
	 * chooser code above (like the commented example) or additional comparisons
	 * to the switch structure below with additional strings & commands.
	 */
	@Override
	public void autonomousInit() {
		autonomousCommand = chooser.getSelected();

		/*
		 * String autoSelected = SmartDashboard.getString("Auto Selector",
		 * "Default"); switch(autoSelected) { case "My Auto": autonomousCommand
		 * = new MyAutoCommand(); break; case "Default Auto": default:
		 * autonomousCommand = new ExampleCommand(); break; }
		 */

		// schedule the autonomous command (example)
		if (autonomousCommand != null)
			autonomousCommand.start();


	}

	/**
	 * This function is called periodically during autonomous
	 */
	@Override
	public void autonomousPeriodic() {
//		Scheduler.getInstance().run();
//		double turningValue = (kAngleSetpoint - gyro.getAngle()) * kP;
//		// Invert the direction of the turn if we are going backwards
//		turningValue = Math.copySign(turningValue, stick.getRawAxis(0));
//		myRobot.arcadeDrive(stick.getRawAxis(1), turningValue);
		
		int runtime = 10000; //runtime in ms
		long time = System.currentTimeMillis();
		long endtime = time + runtime;
		
		while ((time=System.currentTimeMillis()) < endtime){
			myRobot.arcadeDrive(1,0);
		}
		
		//this would drive the robot straight until a certain distance
		//'holdDist' away from the beginning wall
//		double currentDist = ultrasonic.getValue() * toInches;
//		if (currentDist != holdDist){
//			myRobot.arcadeDrive(1, 0);
//		}
	}

	@Override
	public void teleopInit() {
		// This makes sure that the autonomous stops running when
		// teleop starts running. If you want the autonomous to
		// continue until interrupted by another command, remove
		// this line or comment it out.
		if (autonomousCommand != null)
			autonomousCommand.cancel();
	}

	/**
	 * This function is called periodically during operator control
	 */
	@Override
	public void teleopPeriodic() {
		Scheduler.getInstance().run();

		if (stick.getRawButton(1)){

			double turningValue = (kAngleSetpoint - gyro.getAngle()) * kP;
			// Invert the direction of the turn if we are going backwards
			turningValue = Math.copySign(turningValue, stick.getRawAxis(1));
			myRobot.arcadeDrive(stick.getRawAxis(1), turningValue);
			SmartDashboard.putDouble("turning", turningValue);
		}else{
			//myRobot.arcadeDrive(stick);
			myRobot.tankDrive(stick.getRawAxis(1), stick.getRawAxis(5));
			//kAngleSetpoint = gyro.getAngle();
			gyro.reset();
		}

		try {
			if(stick.getRawButton(6)){
				servoLeft.setAngle(10);
				servoRight.setAngle(170);

			}
			else {
				servoLeft.setAngle(90);
				servoRight.setAngle(90);
			}
			if (stick.getRawButton(5)){
				servoRope.setAngle(110);
			}
			else if (stick.getPOV(0)== 0){
				servoRope.setAngle(140);
			}
			else if (stick.getPOV(0)== 180){
				servoRope.setAngle(75);
			}
			else {
				servoRope.setAngle(90);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		SmartDashboard.putNumber("Gyro", gyro.getAngle());


	}

	/**
	 * This function is called periodically during test mode
	 */
	@Override
	public void testPeriodic() {
		//LiveWindow.run();
	}
}
