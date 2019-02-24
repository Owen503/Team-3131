/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

//I'M IN!!!!
import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.GenericHID.RumbleType;
import edu.wpi.first.wpilibj.buttons.Button;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.cscore.MjpegServer;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.cameraserver.*;
import java.nio.ByteBuffer;
import edu.wpi.first.wpilibj.I2C;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {

	public Robot() {
		try {
			frontClimbPiston = new DoubleSolenoid(1, 0);
			backClimbPiston = new DoubleSolenoid(3, 2);
			compressor = new Compressor(0);
		} catch (Exception e) {
			System.out.print("Cannot initialize all pneumatics!!!!!!!!!!!!!!!!!!!!");
			System.out.print(e.toString());
		}
	}

	Joystick controller = new Joystick(0);
	Button aButton = new JoystickButton(controller, 1),
			bButton = new JoystickButton(controller, 2),
			xButton = new JoystickButton(controller, 3),
			yButton = new JoystickButton(controller, 4),
			leftBumper = new JoystickButton(controller, 5),
			rightBumper = new JoystickButton(controller, 6),
			backButton = new JoystickButton(controller, 7),
			startButton = new JoystickButton(controller, 8),
			leftJoystickButton = new JoystickButton(controller , 9),
			rightJoystickButton = new JoystickButton(controller, 10);
	Manipulator manipulator = new Manipulator();
	DifferentialDrive driveTrain = new DifferentialDrive(new Talon(0), new Talon(1));
	AnalogInput angleSensor = new AnalogInput(0);
	AnalogInput lightSensor = new AnalogInput(1);
	boolean autoRaiseToMiddle = false;
	DoubleSolenoid frontClimbPiston;
	DoubleSolenoid backClimbPiston;
	Compressor compressor;
	UsbCamera frontCamera;
	UsbCamera backCamera;
	MjpegServer imageServer;
	

	/* Init functions are run ONCE when the robot is first started up and should be
	 * used for any initialization code. */
	public void robotInit() {
		if (compressor != null) {
			compressor.setClosedLoopControl(true);
		}
		CameraServer.getInstance().startAutomaticCapture();
	}
	
	public void cameraPeriodic(){
		if (backButton.get()/* && manipulator.getCameraValue() > -0.5*/){
			manipulator.cameraLeft();
		} else if (startButton.get()/* && manipulator.getCameraValue() < 0.5*/){
			manipulator.cameraRight();
		}
	}
	
	/* Periodic functions are ran several times a second the entire time the robot
	 * is enabled */
	public void robotPeriodic() {
	
	}

	public void autonomousInit() {
	}

	public void autonomousPeriodic() {

	}

	public void teleopInit() {
		
	}

	public void teleopPeriodic() {
		teleopManipulatorPeriodic();
		teleopDrivePeriodic();
		doubleSolenoidControl();
		//Move line below to ShuffleBoard
		//System.out.println("potentiometer: " + angleSensor.getVoltage());
		
	}

	private void teleopDrivePeriodic() {
		/*
		driveTrain.arcadeDrive(
			-Math.pow(controller.getRawAxis(1),3), 
			Math.pow(controller.getRawAxis(0),3));
		*/

		double speed = 0.7;
		if (controller.getRawAxis(2) == 1){
			speed = 1;
		}
		driveTrain.arcadeDrive(
			speed * -controller.getRawAxis(1), 
			speed * controller.getRawAxis(0));
	}
	
	final int DPAD_UP = 0;
	final int DPAD_DOWN = 180;
	final int DPAD_RIGHT = 90;
	final int DPAD_LEFT = 270;
	
	private void teleopManipulatorPeriodic() {
		if(aButton.get() && bButton.get()){
			controller.setRumble(RumbleType.kLeftRumble, 1);
			controller.setRumble(RumbleType.kRightRumble, 1);
		} else {
			controller.setRumble(RumbleType.kLeftRumble, 0);
			controller.setRumble(RumbleType.kRightRumble, 0);
		}

		if(aButton.get() && !manipulator.containsBall()) {
			manipulator.intake();
		}
		else if(bButton.get()) {
			manipulator.release();
		} else {
			manipulator.stopIntake();
		}

		double angleVoltage = angleSensor.getVoltage();
		int dpadValue = controller.getPOV();

		if(rightJoystickButton.get()) {
			autoRaiseToMiddle = true;
		}

		double topAngleValue = 0.385;
		double bottomAngleValue = 0.6;
		double presetAngleValue = 0.436;
		double presetAngleRange = .02;
		boolean rightJoystickDown = controller.getRawAxis(5) < -0.5;
		boolean rightJoystickUp = controller.getRawAxis(5) > 0.5;

		if ( rightJoystickUp /*&& angleVoltage > topAngleValue*/) {
			manipulator.angleRaise();
			autoRaiseToMiddle = false;
		} else if (rightJoystickDown /*&& angleVoltage < bottomAngleValue*/){
			manipulator.angleLower();
			autoRaiseToMiddle = false;

		} /*else if(autoRaiseToMiddle && angleVoltage < (presetAngleValue - presetAngleRange / 2)) {
			manipulator.lower();
		} else if (autoRaiseToMiddle && angleVoltage > (presetAngleValue + presetAngleRange / 2)) { 
			manipulator.raise();
		} */else {
			manipulator.angleStop();
			
		}
		
		boolean intendToGoUp;
		boolean intendToGoDown;
		boolean wasWhite;
		double tabValue = 10; //value isn't accurate; will change later

		if (dpadValue == DPAD_UP /*&& !manipulator.elevatorTopLimit() */){
			manipulator.elevatorRaise();
		} else if (dpadValue == DPAD_DOWN /*&& !manipulator.elevatorBottomLimit() */){
			manipulator.elevatorLower();
		} else {
			manipulator.elevatorStop();
		}

		/*if (rightJoystickUp){
			intendToGoUp = true;
		}
		if (manipulator.elevatorTopLimit()){
			intendToGoUp = false;
		} else if (intendToGoUp = true) {
			manipulator.elevatorRaise();
			if (lightSensor.getValue() > tabValue){
				wasWhite = true;
			}
			if (wasWhite = true && lightSensor.getValue() <= tabValue){
				manipulator.elevatorStop();
				wasWhite = false;
				intendToGoUp = false;
			}
		}

		if (rightJoystickDown){
			intendToGoDown = true;
		}
		if (manipulator.elevatorBottomLimit()){
			intendToGoDown = false;
		} else if (intendToGoUp = true) {
			manipulator.elevatorLower();
			if (lightSensor.getValue() > tabValue){
				wasWhite = true;
			}
			if (wasWhite = true && lightSensor.getValue() <= tabValue){
				manipulator.elevatorStop();
				wasWhite = false;
				intendToGoDown = false;
			}
		}*/
	}
	
	public void testPeriodic() {

	}

	public void doubleSolenoidControl() {
	
		if (frontClimbPiston == null || backClimbPiston == null) {
			return;
		}
		
		if(!leftBumper.get()) {
			frontClimbPiston.set(DoubleSolenoid.Value.kOff);
		} else {
			if (previousFrontButton == false) {
				nextDirectionIsForward = !nextDirectionIsForward;
			}
			
			if (nextDirectionIsForward) {
				frontClimbPiston.set(DoubleSolenoid.Value.kForward);
			} else {
				frontClimbPiston.set(DoubleSolenoid.Value.kReverse);
			}
		}

		if(!rightBumper.get()) {
			backClimbPiston.set(DoubleSolenoid.Value.kOff);
		} else  {
			if (previousBackButton == false) {
				nextDirectionIsForward = !nextDirectionIsForward;
			}
			
			if (nextDirectionIsForward) {
				backClimbPiston.set(DoubleSolenoid.Value.kForward);
			} else {
				backClimbPiston.set(DoubleSolenoid.Value.kReverse);
			}
		}
		previousFrontButton = leftBumper.get();
		previousBackButton = rightBumper.get();
	}

	boolean previousBackButton = false;
	boolean previousFrontButton = false;
	boolean nextDirectionIsForward = true;
	
}
