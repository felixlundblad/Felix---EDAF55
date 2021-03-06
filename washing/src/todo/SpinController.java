package todo;


import se.lth.cs.realtime.*;
import done.AbstractWashingMachine;


public class SpinController extends PeriodicThread {
	private AbstractWashingMachine mach;
	private int mode;
	private double time;
	private double speed;

	public SpinController(AbstractWashingMachine mach, double speed) {
		super((long) (10000/speed)); 
		this.mach = mach;
		mode = SpinEvent.SPIN_OFF;
		this.speed = speed;
		time = System.currentTimeMillis();
		System.out.println("SpinController is constructed");
	}

	public void perform() {
		SpinEvent e = (SpinEvent) mailbox.tryFetch();
		if(e != null) {
			mode = e.getMode();
		}
		time = System.currentTimeMillis();
		switch(mode) {
		case SpinEvent.SPIN_FAST:
			//System.out.println("Set to spin fast");
			mach.setSpin(AbstractWashingMachine.SPIN_FAST);
			break;
		case SpinEvent.SPIN_SLOW:
			if((int)((time / (10*60*1000/speed)) % 2) == 1) {
				//System.out.println("Set to spin slow to the left");				
				mach.setSpin(AbstractWashingMachine.SPIN_LEFT);
			}
			else {
				//System.out.println("Set to spin slow to the right");
				mach.setSpin(AbstractWashingMachine.SPIN_RIGHT);
			}
			break;
		default:
			//System.out.println("Set to stop spinning");
			mach.setSpin(AbstractWashingMachine.SPIN_OFF);
			break;
		}
	}
}
