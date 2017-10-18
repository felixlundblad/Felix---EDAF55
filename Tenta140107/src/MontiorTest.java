
public class MontiorTest {
	public synchronized void a(){
		System.out.println("a");
		notifyAll();
		try {
			wait();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public synchronized void b(){
		System.out.println("b");
		notifyAll();
		try {
			wait();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		MontiorTest mt = new MontiorTest();
		Thread t1 = new MonitorTestingThread(0, mt);
		Thread t2 = new MonitorTestingThread(1, mt);
		t1.start();
		t2.start();
	}

}


class MonitorTestingThread extends Thread{
	int x;
	MontiorTest mt;

	public MonitorTestingThread(int x, MontiorTest mt) {
		this.x = x;
		this.mt = mt;
	}

	public void run(){
		while(true) {
			if(x == 0) {
				mt.a();
			} else {
				mt.b();
			}
		}
	}
}