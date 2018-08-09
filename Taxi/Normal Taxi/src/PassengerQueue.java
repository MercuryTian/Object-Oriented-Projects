import java.util.Vector;

public class PassengerQueue {
	private static Vector<Passenger> passengers = new Vector<Passenger>();
	
	public static void pushPassenger(Passenger p){
		passengers.addElement(p);
	}
	
	public static Passenger pullPassenger(){
		Passenger passenger = passengers.get(0);
		passengers.remove(0);
		return passenger;
	}
	
	public static int getSize(){
		return passengers.size();
	}
}
