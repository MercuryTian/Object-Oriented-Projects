import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Timer;

public class Main {

	public static void main(String[] args) {
		/*
		 * Start all threads and run the program
		 * REQUIRES: Please input at the console if you want to close or open edges on map. 
		 * 			 For further details, please read 'Readme'.
		 * MODIFIES: None.
		 * EFFECTS: Start all threads and run the program.
		 */
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
		
		//测试者通过改变地图上的value值来选择关闭的边
		//每一条指令一行
		
		BufferedReader change = new BufferedReader(new InputStreamReader(System.in));
		String cmd = null;
		int ID = -1;
		String edge;
				
		while(true){
			try {
				cmd = change.readLine();
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			if(cmd == null){
				continue;
			}
			cmd = cmd.replaceAll("\\s", "");	//过滤空格
			String regex = "\\([0-9]{1,2},[0-9]{1,2}\\),[0-3]";
			 
			
			if(cmd.equals("open")){
			//测试者输入open表示要打开边
				if(Map.getChangeInfo().size() == 0){
				//如果数组里面没有关闭过的边，则不能打开边
					System.out.println("No available edge to recover");
				}
				else{
				//先输出可以打开边的信息供测试者选择
					for(int i=0; i<Map.getChangeInfo().size(); i++){
						if(Map.getChangeInfo().get(i).getEdge() == 1){
							edge = "Right edge is available";
						}
						else{
							edge = "Under edge is available";
						}
						System.out.println("Available edge for recovering: No." + i + " (" + Map.getChangeInfo().get(i).getPoint().getX() + "," +
											Map.getChangeInfo().get(i).getPoint().getY() + ") " + edge);	
					}
						System.out.println("Please choose an edge you want to recover. Please only input the ID of the edge.");	
						try {
							//读进来想要打开的边的编号
							cmd = change.readLine();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						try {
							ID = Integer.parseInt(cmd);
						} catch (Exception e) {
							// TODO: handle exception
							e.printStackTrace();
						}
						if(ID>=0 && ID<Map.getChangeInfo().size()){
							//如果符合要求，则打开相应的边
							Map.RecoverEdge(ID);
						}
						else{
							System.out.println("ID is illegal");
						}
				}
			}
			else if(cmd.equals("close")){
			//测试者输入close来关闭边
			//否则，判断处理是否要删除边
			    System.out.println("Please input the coordinate and value of the point you want to change, like (1,2),1");
				try {
					cmd = change.readLine();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				try {
					if(!cmd.matches(regex) || cmd.length()==0){
						System.out.println("Input format is illegal");
						continue;
					}
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
				int x_change = 0;
				int y_change = 0;
				int value_change = 0;
				
				try {
					cmd = cmd.substring(1, cmd.length());
					String[] tmp = cmd.split(",");
					x_change = Integer.parseInt(tmp[0]);
					y_change = Integer.parseInt(tmp[1].substring(0, tmp[1].length()-1));
					value_change = Integer.parseInt(tmp[2]);
					if(x_change>79 || y_change>79 || x_change<0 || y_change<0){
						System.out.println("Point is out of range");
						continue;
					}
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
				Map.DeleteEdge(x_change, y_change, value_change);
			}
			else{
				System.out.println("Input FORMAT is illegal");
			}
				
		}
				
	}

}
