package frc.robot;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Talon;

public class Manipulator {
	DigitalInput limitSwitch = new DigitalInput(0);
	Talon leftManip = new Talon(2);
	Talon rightManip = new Talon(3);
	Talon liftMotor = new Talon(4);
	Talon elevatorMotor = new Talon(5);

	public void intake() {
		leftManip.set(-0.25);
		rightManip.set(0.25);
	}
	public void release() {
		leftManip.set(1);
		rightManip.set(-1);
	}
	public void stopIntake() {
		leftManip.set(0);
		rightManip.set(0);
	}
	public boolean containsBall() {
		return limitSwitch.get();
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
		elevatorMotor.set(-1);
	}
	public void elevatorStop(){
		elevatorMotor.set(0);
	}
}
