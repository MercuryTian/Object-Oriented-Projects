import java.util.TimerTask;

public class Scheduler extends TimerTask{
	
	private static int i = 0;

	@Override
	public void run() {
		// TODO Auto-generated method stub
		if(PassengerQueue.getSize() != 0){
			Passenger passenger = PassengerQueue.pullPassenger();
			Thread thread = new Thread(passenger);
			thread.setName("Passenger" + i);
			i++;
			thread.start();
		}
	}
	
	public boolean repOK(){
		if(i>=0)
			return true;
		return false;
	}

}
