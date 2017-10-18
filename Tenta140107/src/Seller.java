
public class Seller extends Thread{
	Monitor m;

	public Seller(Monitor m) {
		this.m = m;
	}

	public void run() {
		while(true) {
			m.handleOrder(); 
		}
	}
}
