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
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.GenericHID.RumbleType;
import edu.wpi.first.wpilibj.buttons.Button;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.cameraserver.*;

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
			front = new DoubleSolenoid(1, 0);
			back = new DoubleSolenoid(3, 2);
			clothesPinExtender = new DoubleSolenoid(5, 4);
			clothesPinOpener = new DoubleSolenoid(7, 6);
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
			startButton = new JoystickButton(controller, 8);
	Manipulator manipulator = new Manipulator();
	DifferentialDrive driveTrain = new DifferentialDrive(new Talon(0), new Talon(1));
	AnalogInput angleSensor = new AnalogInput(0);
	boolean autoRaiseToMiddle = false;
	DoubleSolenoid front;
	DoubleSolenoid back;
	DoubleSolenoid clothesPinExtender;
	DoubleSolenoid clothesPinOpener;
	Compressor compressor;

	/* Init functions are run ONCE when the robot is first started up and should be
	 * used for any initialization code. */
	public void robotInit() {
		if (compressor != null) {
			compressor.setClosedLoopControl(true);
		}
		CameraServer.getInstance().startAutomaticCapture();
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
		System.out.println("potentiometer: " + angleSensor.getVoltage());
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

		if(dpadValue == DPAD_RIGHT) {
			autoRaiseToMiddle = true;
		}

		if (dpadValue == DPAD_UP && angleVoltage < 4.8) {
			manipulator.raise();
			autoRaiseToMiddle = false;
		} else if (dpadValue == DPAD_DOWN && angleVoltage > 3.112){
			manipulator.lower();
			autoRaiseToMiddle = false;
		} else if(autoRaiseToMiddle && angleVoltage > 4.44) {
			manipulator.lower();
		} else if (autoRaiseToMiddle && angleVoltage < 4.36) {
			manipulator.raise();
		} else {
			manipulator.stopRaise();
		}
	}

	public void testPeriodic() {

	}

	public void doubleSolenoidControl() {
	
		if (front == null || back == null) {
			return;
		}

		if(rightBumper.get()) {
			front.set(DoubleSolenoid.Value.kForward);
		} else { 
			front.set(DoubleSolenoid.Value.kReverse);
		}

		if(leftBumper.get()) {
			back.set(DoubleSolenoid.Value.kForward);
		} else {
			back.set(DoubleSolenoid.Value.kReverse);
		}

		if(backButton.get()) {
			clothesPinExtender.set(DoubleSolenoid.Value.kForward);
		}

		if(startButton.get()) {
			clothesPinExtender.set(DoubleSolenoid.Value.kReverse);
		}

		if(xButton.get()) {
			clothesPinOpener.set(DoubleSolenoid.Value.kForward);
		} else {
			clothesPinOpener.set(DoubleSolenoid.Value.kReverse);
		}
	}
}
