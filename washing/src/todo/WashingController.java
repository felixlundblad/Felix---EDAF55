package todo;

import done.*;

public class WashingController implements ButtonListener {	
	private AbstractWashingMachine theMachine;
	private double theSpeed;
	TemperatureController tempController;
	WaterController waterController;
	SpinController spinController;
	WashingProgram wp;

	public WashingController(AbstractWashingMachine theMachine, double theSpeed) {
		this.theMachine = theMachine;
		this.theSpeed = theSpeed;			
		tempController = new TemperatureController(theMachine, theSpeed);
		waterController = new WaterController(theMachine, theSpeed);
		spinController = new SpinController(theMachine, theSpeed);
		tempController.start();
		waterController.start();
		spinController.start();

		// Solve this shite with the ButtonListener
	}

	public void processButton(int theButton) {
		/*
		 *  How should I treat if a user pushes a button when machine is already executing a program?
		 *   1. Interrupt and run next program
		 *   2. Check if program is already running, if so don't run new
		 *   
		 *   Went with 2 using a protected boolean in WashingProgram
		 */
		switch (theButton) {
		case 1:
			if (wp == null || !wp.isAlive()) {
				wp = new WashingProgram1(theMachine, theSpeed, tempController, waterController, spinController);
				wp.start();
			}
			break;
		case 2:
			if (wp == null || !wp.isAlive()) {
				wp = new WashingProgram2(theMachine, theSpeed, tempController, waterController, spinController);
				wp.start();
			}
			break;
		case 3:
			if (wp == null || !wp.isAlive()) {
				wp = new WashingProgram3(theMachine, theSpeed, tempController, waterController, spinController);
				wp.start();
			}
			break;
		default:
			if (wp != null && wp.isAlive()) {
				wp.interrupt();
			}
		}
	}
}
