import java.util.Timer;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			new Map();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			System.out.println(e.getMessage());
			return;
		}
		
//		Map.SearchMin(new Point(0, 2), new Point(20, 20));
//		Map.SearchMin(new Point(0, 21), new Point(20, 20));
//		Map.SearchMin(new Point(0, 2), new Point(20, 79));
		
		Thread thread = new Thread(new PassSendReq());
		thread.setName("Passenger Send Requests");
		Scheduler scheduler = new Scheduler();
		Timer timer = new Timer();
		timer.schedule(scheduler, 0, 100);
		thread.start();
		for(int i=0; i<100; i++){
			Thread threadTaxi = new Thread(new Taxi(i));
			threadTaxi.setName("Taxi" + i);
			threadTaxi.start();
		}
	}

}
