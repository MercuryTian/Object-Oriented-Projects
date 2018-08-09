import java.util.Vector;

public class PassengerQueue {
	private static Vector<Passenger> passengers = new Vector<Passenger>();
	
	public static void pushPassenger(Passenger p){
		/*
		 * REQUIRES: Must provide a variable, which is the passenger.
		 * MODIFIES: Add passenger to the passenger array.
		 * EFFECTS: Add passenger to the passenger array.
		 */
		passengers.addElement(p);
	}
	
	public static Passenger pullPassenger(){
		/*
		 * REQUIRES: None.
		 * MODIFIES: Remove passenger from the passenger array.
		 * EFFECTS: Remove passenger from the passenger array.
		 */
		Passenger passenger = passengers.get(0);
		passengers.remove(0);
		return passenger;
	}
	
	public static int getSize(){
		/*
		 * REQUIRES: None.
		 * MODIFIES: None.
		 * EFFECTS: Return the size of array.
		 */
		return passengers.size();
	}
}
