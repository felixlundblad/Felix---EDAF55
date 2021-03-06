package todo;
import done.*;

public class WashingProgram2 extends WashingProgram{
	private double speed;
	private int mainWashTime, rinseTime, preWashTime;

	protected WashingProgram2(AbstractWashingMachine mach, 
			double speed, 
			TemperatureController tempController,
			WaterController waterController, 
			SpinController spinController) {
		super(mach, speed, tempController, waterController, spinController);
		this.speed = speed;
		int minutes = 60*1000;
		this.mainWashTime = 30*minutes;
		this.rinseTime = 5*minutes;
		this.preWashTime = 15*minutes;
	}

	
	protected void wash() throws InterruptedException {
		System.out.println("\t Starting WashingProgram2");
		/*
		 * Pre wash
		 */
		setWash(0.5, 40, preWashTime);
		
		
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
		System.out.println("____________________________ \n WashingProgram2 is finished");
	}
	
	private void setWash(double fillLevel, int temp, int washTimeMillis) {
		System.out.println("\t Setting up wash");
		myMachine.setLock(true);
		myWaterController.putEvent(new WaterEvent(this, WaterEvent.WATER_FILL, fillLevel));
		mailbox.doFetch();
		myWaterController.putEvent(new WaterEvent(this, WaterEvent.WATER_IDLE, fillLevel));
		myTempController.putEvent(new TemperatureEvent(this, TemperatureEvent.TEMP_SET, temp));
		mailbox.doFetch();
		myTempController.putEvent(new TemperatureEvent(this, TemperatureEvent.TEMP_IDLE, temp));	
		mySpinController.putEvent(new SpinEvent(this, SpinEvent.SPIN_SLOW));
		
		sleep((int)(washTimeMillis/speed));
		
		mySpinController.putEvent(new SpinEvent(this, SpinEvent.SPIN_OFF));
		myTempController.putEvent(new TemperatureEvent(this, TemperatureEvent.TEMP_IDLE, 0));
		myWaterController.putEvent(new WaterEvent(this, WaterEvent.WATER_DRAIN, 0));
		mailbox.doFetch();
		myWaterController.putEvent(new WaterEvent(this, WaterEvent.WATER_IDLE, 0));
		// Should I have something to empty the mailbox?
	}
}








