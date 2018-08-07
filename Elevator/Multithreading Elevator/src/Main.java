import java.util.Timer;

public class Main {


	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		new Thread(new RequestMonitor()).start();
		Controller con = new Controller();
		
		Timer timer = new Timer(true);
		timer.schedule(con, 0, 100);
		

	}

}
