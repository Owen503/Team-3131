package frc.robot;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Talon;

public class Manipulator {
	DigitalInput intakeLimitSwitch = new DigitalInput(0);
	DigitalInput elevatorTopLimitSwitch = new DigitalInput(1);
	DigitalInput elevatorBottomLimitSwitch = new DigitalInput(2);
	Talon manipIntakeAndEject = new Talon(3);
	Talon angleMotor = new Talon(2);
	Talon elevatorMotor = new Talon(4);
	Talon cameraServo = new Talon(5);

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
	public void angleRaise() {
		angleMotor.set(1);
	}
	public void angleLower() {
		angleMotor.set(-0.8);
	}
	public void angleStop() {
		angleMotor.set(0);
	}
	public void elevatorRaise(){
		elevatorMotor.set(-1);
	}
	public void elevatorLower(){
		elevatorMotor.set(0.75);
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
		cameraServo.set(-0.5);
	}
	public void cameraRight(){
		cameraServo.set(0.5);
	}
	public double getCameraValue(){
		return cameraValue;
	}
}
