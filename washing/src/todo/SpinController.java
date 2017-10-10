package todo;


import se.lth.cs.realtime.*;
import done.AbstractWashingMachine;


public class SpinController extends PeriodicThread {
	AbstractWashingMachine mach;
	double speed;

	public SpinController(AbstractWashingMachine mach, double speed) {
		super((long) (1000/speed)); 
		this.mach = mach;
		this.speed = speed;
	}

		public void perform() {
			// TODO: implement this method
	}
}
