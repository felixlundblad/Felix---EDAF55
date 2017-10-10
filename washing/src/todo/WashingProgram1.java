package todo;
import done.*;

public class WashingProgram1 extends WashingProgram{
	private double speed;
	private int mainWashTime, rinseTime;

	protected WashingProgram1(AbstractWashingMachine mach, 
			double speed, 
			TemperatureController tempController,
			WaterController waterController, 
			SpinController spinController) {
		super(mach, speed, tempController, waterController, spinController);
		this.speed = speed;
		int minutes = 60*1000;
		this.mainWashTime = 30*minutes;
		this.rinseTime = 5*minutes;
	}


	protected void wash() throws InterruptedException {
		/*
		 * Main wash
		 */
		setWash(0.5, 90, mainWashTime);

		/*
		 * Rinsing with cold water
		 */
		for (int i = 0; i < 5; i++) {
			setWash(0.5, 0, rinseTime);
		}
		
		/*
		 * Centrifuging
		 */
		mySpinController.putEvent(new SpinEvent(this, SpinEvent.SPIN_FAST));
		sleep((int)(rinseTime/speed));
		
		/*
		 * Wash finished
		 */
		myTempController.putEvent(new TemperatureEvent(this, TemperatureEvent.TEMP_IDLE, 0));	
		mySpinController.putEvent(new SpinEvent(this, SpinEvent.SPIN_OFF));
		myMachine.setLock(false);
	}
	
	private void setWash(double fillLevel, int temp, int washTimeMillis) {
		myMachine.setLock(true);
		myWaterController.putEvent(new WaterEvent(this, WaterEvent.WATER_FILL, fillLevel));
		myWaterController.putEvent(new WaterEvent(this, WaterEvent.WATER_IDLE, fillLevel));
		//mailbox.doFetch();
		myTempController.putEvent(new TemperatureEvent(this, TemperatureEvent.TEMP_SET, temp));
		mailbox.doFetch();
		mySpinController.putEvent(new SpinEvent(this, SpinEvent.SPIN_SLOW));
		//mailbox.doFetch();
		mailbox.doFetch();
		
		sleep((int)(washTimeMillis/speed));
		
		myTempController.putEvent(new TemperatureEvent(this, TemperatureEvent.TEMP_IDLE, 0));	
		mySpinController.putEvent(new SpinEvent(this, SpinEvent.SPIN_OFF));
		//mailbox.doFetch();
		myWaterController.putEvent(new WaterEvent(this, WaterEvent.WATER_DRAIN, 0));
		myWaterController.putEvent(new WaterEvent(this, WaterEvent.WATER_IDLE, 0));
		mailbox.doFetch();
		// Should I have something to empty the mailbox?
	}
}







