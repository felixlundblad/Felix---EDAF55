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
		myMachine.setLock(true);
		/*
		 * Main wash
		 */
		doWash(0.5, 60, mainWashTime);

		/*
		 * Rinsing with cold water
		 */
		for (int i = 0; i < 5; i++) {
			doWash(0.5, 0, rinseTime);
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

	private void doWash(double fillLevel, int temp, int washTimeMillis) {
		myWaterController.putEvent(new WaterEvent(this, WaterEvent.WATER_FILL, fillLevel));
		mailbox.doFetch();
		System.out.println("Did fetch event from WaterController");
		myWaterController.putEvent(new WaterEvent(this, WaterEvent.WATER_IDLE, fillLevel));
		myTempController.putEvent(new TemperatureEvent(this, TemperatureEvent.TEMP_SET, temp));
		mailbox.doFetch();
		myTempController.putEvent(new TemperatureEvent(this, TemperatureEvent.TEMP_IDLE, temp));	
		System.out.println("Did fetch event from TemperatureController");
		mySpinController.putEvent(new SpinEvent(this, SpinEvent.SPIN_SLOW));
		System.out.println("\t Gonna sleep now");
		sleep((int)(1/speed));

		mySpinController.putEvent(new SpinEvent(this, SpinEvent.SPIN_OFF));
		myWaterController.putEvent(new WaterEvent(this, WaterEvent.WATER_DRAIN, 0));
		mailbox.doFetch();
		myWaterController.putEvent(new WaterEvent(this, WaterEvent.WATER_IDLE, 0));
		//mailbox.doFetch();
		// Should I have something to empty the mailbox?
	}
}







