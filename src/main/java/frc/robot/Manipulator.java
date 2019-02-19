package frc.robot;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Talon;

public class Manipulator {
	DigitalInput intakeLimitSwitch = new DigitalInput(0);
	DigitalInput elevatorTopLimitSwitch = new DigitalInput(1);
	DigitalInput elevatorBottomLimitSwitch = new DigitalInput(2);
	Talon manipIntakeAndEject = new Talon(3);
	Talon liftMotor = new Talon(2);
	Talon elevatorMotor = new Talon(4);
	Talon cameraServo = new Talon(5);
	double cameraValue = 0;

	public void intake() {
		manipIntakeAndEject.set(-0.25);	
	}
	public void release() {
		manipIntakeAndEject.set(1);
	}
	public void stopIntake() {
		manipIntakeAndEject.set(0);
	}
	public boolean containsBall() {
		return intakeLimitSwitch.get();
	}
	public void raise() {
		liftMotor.set(1);
	}
	public void lower() {
		liftMotor.set(-0.8);
	}
	public void stopRaise() {
		liftMotor.set(0);
	}
	public void elevatorRaise(){
		elevatorMotor.set(1);
	}
	public void elevatorLower(){
		elevatorMotor.set(-0.75);
	}
	public void elevatorStop(){
		elevatorMotor.set(0);
	}
	public boolean elevatorTopLimit() {
		return elevatorTopLimitSwitch.get();
	}
	public boolean elevatorBottomLimit() {
		return elevatorBottomLimitSwitch.get();
	}	
	public void cameraStart(){
		cameraServo.set(cameraValue);
	}
	public void cameraLeft(){
		cameraValue = cameraValue - 0.01;
		cameraServo.set(cameraValue);
	}
	public void cameraRight(){
		cameraValue = cameraValue + 0.01;
		cameraServo.set(cameraValue);
	}
	public double getCameraValue(){
		return cameraValue;
	}
}
