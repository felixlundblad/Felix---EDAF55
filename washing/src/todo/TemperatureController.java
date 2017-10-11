package todo;


import se.lth.cs.realtime.*;
import done.AbstractWashingMachine;


public class TemperatureController extends PeriodicThread {
	private AbstractWashingMachine mach;
	private double wantedTemp;
	private int mode;
	private WashingProgram wp;
	private boolean hasSent;


	public TemperatureController(AbstractWashingMachine mach, double speed) {
		super((long) (1000/speed));
		hasSent = false;
		this.mach = mach;
		mode = TemperatureEvent.TEMP_IDLE;
		wp = null;
		System.out.println("TemperatureController is constructed");
	}

	public void perform() {
		TemperatureEvent e = (TemperatureEvent) mailbox.tryFetch();
		if(e != null) {
			wantedTemp = e.getTemperature();
			mode = e.getMode();	
			hasSent = false;
			wp = (WashingProgram) e.getSource();
		}
		switch(mode) {
		case TemperatureEvent.TEMP_SET:
			if(mach.getTemperature() >= wantedTemp - 1 && !hasSent) {				
					wp.putEvent(new AckEvent(this));
					hasSent = true;
					System.out.println("TemperatureController is done");
			}
			mach.setHeating(true);			
			break;
		case TemperatureEvent.TEMP_IDLE:
			if(mach.getTemperature() < wantedTemp - 1)
				mach.setHeating(true);
			else mach.setHeating(false);
			break;
		}
	}
}
