
public class Statistics extends Thread{
	Monitor m;
	int wokeUp;
	
	public Statistics(Monitor m) {
		this.m = m;
		wokeUp = Util.currentTime();
	}
	
	public void run() {
		try {
			Thread.sleep(Util.waitTime(60*60 + wokeUp - Util.currentTime()));
			wokeUp = Util.currentTime();
		} catch (InterruptedException e) {
			System.out.println("Statistics thread was interrupted.");
		}
		int[] stats = m.gatherStatistics();
		System.out.println("Ordered burgers: " + stats[2] + '\n');
		System.out.println("Thrown burgers: " + stats[0] + '\n');
		System.out.println("Missed deadlines: " + stats[1] + '\n');
		
	}
}
