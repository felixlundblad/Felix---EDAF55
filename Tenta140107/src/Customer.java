
public class Customer extends Thread{
	Monitor m;
	
	public Customer(Monitor m) {
		this.m = m;
	}

	public void run(){
		while(true) {
			m.newCustomer(Util.newCustomer());
		}
	}
}
