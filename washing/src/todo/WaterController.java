package todo;


import se.lth.cs.realtime.*;
import done.AbstractWashingMachine;


public class WaterController extends PeriodicThread {
	private AbstractWashingMachine mach;
	private int mode;
	private boolean hasSent;
	private double wantedLevel;
	WashingProgram wp;

	public WaterController(AbstractWashingMachine mach, double speed) {
		super((long) (1000/speed)); 
		this.mach = mach;
		mode = WaterEvent.WATER_IDLE;
		hasSent = false;
		wantedLevel = 0;
		wp = null;
		System.out.println("WaterController is constructed");
	}

	public void perform() {
		WaterEvent e = (WaterEvent) mailbox.tryFetch();
		if(e != null) {
			hasSent = false;
			mode = e.getMode();	
			wantedLevel = e.getLevel();
			System.out.println("WaterController is done");
			wp = (WashingProgram) e.getSource();
		}
		switch(mode) {
		case WaterEvent.WATER_FILL:
			if(wantedLevel > mach.getWaterLevel() && mach.getWaterLevel() <= 0.98)
				mach.setFill(true);
			else mach.setFill(false);
			if(!hasSent && wantedLevel <= mach.getWaterLevel()) {
				hasSent = true;
				wp.putEvent(new AckEvent(this));
				System.out.println("Water filling is done");
			}
			break;
		case WaterEvent.WATER_DRAIN:
			if(wantedLevel < mach.getWaterLevel() && mach.getWaterLevel() > 0)
				mach.setDrain(true);
			else mach.setDrain(false);
			if(!hasSent && wantedLevel >= mach.getWaterLevel()) {
				hasSent = true;
				wp.putEvent(new AckEvent(this));
				System.out.println("Water draining is done");
			}
			break;
		case WaterEvent.WATER_IDLE:
			mach.setDrain(false);
			mach.setFill(false);
			//System.out.println("Water level is set to idle");
			break;
		}
	}
}
