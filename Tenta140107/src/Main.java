import java.util.Iterator;

public class Main {
	public static void main(String[] args) {
		Monitor m = new Monitor();
		Customer customer = new Customer(m);
		Seller sellers[] = new Seller[4];
		for (int i = 0; i < sellers.length; i++) {
			sellers[i] = new Seller(m);
		}
		Chef chefs[] = new Chef[7];
		for (int i = 0; i < chefs.length; i++) {
			chefs[i] = new Chef(m);
		}
		
		customer.start();
		for(Seller s : sellers) s.start();
		for(Chef c : chefs) c.start();
	}
}
