
package frc.robot;




import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Talon;

public class Manipulator {
	DigitalInput limitSwitch = new DigitalInput(0);
	Talon leftManip = new Talon(2);
	Talon rightManip = new Talon(3);
	Talon liftMotor = new Talon(4);
	double speed = 0.6;
	
	public void intake() {
		leftManip.set(-0.4);
		rightManip.set(0.4);
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
		liftMotor.set(-1);
		}
	public void stopRaise() {
		liftMotor.set(0);
	}
	
}
