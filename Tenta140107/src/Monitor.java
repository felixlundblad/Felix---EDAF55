import java.util.LinkedList;

public class Monitor {
	// Suggested state
	private LinkedList<Integer> customers;// Customer list
	private LinkedList<Integer> orders; // Order list
	private LinkedList<Integer>[] shelfs; // Shelfs
	private int[] inProgress;
	private int thrownBurgers;
	private int missedDeadlines;
	private int orderedBurgers;
	private static int AMOUNT_OF_SHELFS = Util.shelfs();
	private static int KINDS_OF_BURGERS = Util.shelfs();
	// Constructor
	public Monitor() { 
		customers = new LinkedList<Integer>();
		orders = new LinkedList<Integer>();
		shelfs = new LinkedList[AMOUNT_OF_SHELFS];
		for (int i = 0; i < AMOUNT_OF_SHELFS; i++) {
			shelfs[i] = new LinkedList<Integer>();
		}
		inProgress = new int[KINDS_OF_BURGERS];
		thrownBurgers = 0;
		missedDeadlines = 0;
		orderedBurgers = 0;
	}

	private boolean shelfFull() {
		for (int i = 0; i < shelfs.length; i++) {
			if(shelfs[i].size() + inProgress[i] < Util.keepOnShelf(i)) {
				return false;
			}
		}
		return true;
	}
	// Simulate a chef manufacturing a burger
	public synchronized void manufactureBurger() { 
		while(orders.peek() == null && shelfFull()) {
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		int burgerType = 0;
		if(orders.peek() != null) {
			burgerType = orders.poll();
		} else {
			for (int i = 0; i < shelfs.length; i++) {
				if(shelfs[i].size() < Util.keepOnShelf(i)) {
					burgerType = i;
					break;
				}
			}
		}
		inProgress[burgerType]++;
		int manufacturingTime = Util.manufacturingTime(burgerType);
		int startTime = Util.currentTime();
		while(startTime + manufacturingTime < Util.currentTime()) {
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		shelfs[burgerType].add(Util.currentTime());
		inProgress[burgerType]--;
		notifyAll();
	}
	// Simulate a seller handling an order
	public synchronized void handleOrder() {
		try {
			while(customers.isEmpty()) wait();
			
			int burgerType = customers.poll();
			orderedBurgers++;
			int orderTime = Util.currentTime();
			if(shelfs[burgerType].isEmpty()) {
				orders.add(burgerType);			
			} 
			notifyAll();
			while(!shelfs[burgerType].isEmpty() && shelfs[burgerType].peek() > Util.currentTime() - Util.maxTimeOnShelf()) {
				shelfs[burgerType].removeFirst();
				thrownBurgers++;
			}
			while(shelfs[burgerType].isEmpty()) {
				wait();
			}
			shelfs[burgerType].removeFirst();
			if((Util.currentTime() - orderTime) > Util.maxWaitingTime()) missedDeadlines++;
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	// Placing a new customer in queue
	public synchronized void newCustomer(int burgerType) { 
		customers.add(burgerType);
		notifyAll();
	}
	// Return a vector containing {thrownBurgers,missedDeadlines,orderedBurgers}
	public synchronized int[] gatherStatistics() { 
		int [] stats = new int[3];
		stats[0] = thrownBurgers;
		stats[1] = missedDeadlines;
		stats[2] = orderedBurgers;
		thrownBurgers = 0;
		missedDeadlines = 0;
		orderedBurgers = 0;
		return stats;
	}
}