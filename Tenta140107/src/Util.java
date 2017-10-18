public class Util {
// Return the number of shelfs in the burger cabinet.
// Each shelf equals a burger type.
public static int shelfs() {  }
// Return the number of burgers to store predictively on the shelf s
public static int keepOnShelf(int s) {  }
// Return the max time a burger is allowed to be stored on a shelf
// before it is too old. Time is returned in seconds.
public static int maxTimeOnShelf() { }
// Return the max allowed customer waiting time (2 mins) from the
// moment the order is taken by a seller until the burger is delivered.
// Time is returned in seconds.
public static int maxWaitingTime() {  }
// Return the time it takes a chef to anufacture a burger of certain type.
// Time is returned in seconds.
public static int manufacturingTime(int burgerType) {  }
// Return current simulation time. To be used instead of System.currentTimeMillis.
// Time is speeded up a scale factor as compared to real time.
// Time is returned in seconds.
public static int currentTime() {  }
// Return real time given simulation time. Time is scaled properly so
// Thread.sleep, etc., waits an appropriate amount. Time is given
// in seconds, but is returned in milliseconds.
public static int waitTime(int t) {  }
// Blocks until a new customer has arrived. Returns the burger type
// (modelled as an integer). The returned number corresponds directly
// to a shelf in the burger cabinet.
public static int newCustomer() {  }
}
