import java.util.Random;

//用于乘客发送请求
	
public class PassSendReq implements Runnable{
	
	private void AddPassenger(Point location, Point destination){
		/*
		 * REQUIRES: Must provide two variables, which are the location and destination of passenger.
		 * MODIFIES: None.
		 * EFFECTS: Add new passenger requests to the passenger queue.
		 */
		if(location.getX()>79 || location.getX()<0 || location.getY()>79 || location.getY()<0 
				|| destination.getX()>79 || destination.getX()<0 || destination.getY()>79 || destination.getY()<0){
			return;
		}
		Passenger passenger = new Passenger(location, destination);
		PassengerQueue.pushPassenger(passenger);
		try {
			Thread.sleep(100);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
//--------------Please Insert Code Here-------------------------------
//		样例测试：
//		Random rand = new Random();
//		for(int i=0;i<100;i++){
//			int x = rand.nextInt(80);
//			int y = rand.nextInt(80);
//			AddPassenger(new Point(x,y), new Point(rand.nextInt(80),rand.nextInt(80)));
//		}
		AddPassenger(new Point(40,40),new Point(40,40));
		AddPassenger(new Point(40,40),new Point(40,40));
		AddPassenger(new Point(40,40),new Point(40,40));
//		AddPassenger(new Point(40,40), new Point(42,42));
		
//------------------------END-----------------------------------------
	
		
		
		
	}

}
