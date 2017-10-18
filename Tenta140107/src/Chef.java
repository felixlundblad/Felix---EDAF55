
public class Chef extends Thread{
	Monitor m;
	
	public Chef(Monitor m){
		this.m = m;
	}
	
	public void run() {
		while(true) {
			m.manufactureBurger();
		}
	}
}
