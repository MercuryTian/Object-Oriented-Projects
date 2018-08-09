import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import java.util.TimerTask;
import java.util.Vector;

public class Taxi extends TimerTask{
	
	/*
	 * 状态参数：
	 * 1:busy 		正在服务
	 * 2:immediate	即将服务
	 * 3:wait		等待服务
	 * 4:offline	停止运行
	 * 初始状态设置成:state=3
	 */
	private int state;
//	private ArrayList<Taxi> taxi = new ArrayList<Taxi>();
	private Point location;	//出租车的当前位置
//	private Point dest;		//目的地
	private int ID;			//出租车标号信息
	private int credit;		//出租车信用
	private int time;		//所需时间
	private Passenger passenger;
	private int direction;	//方向，标记同Point类
	private int rest;		//剩余可调用出租车数量
	private Vector<Point> way_to_go; 
	
	public Taxi(int id){
		this.ID = id;
		Random rand = new Random();
//		int x = 79;
//		int y = 79;
		int x = rand.nextInt(80);
		int y = rand.nextInt(80);
		int k = rand.nextInt(4);
		this.location = new Point(x, y, 0);
		state = 3;	//出租车初始状态为等待状态
		passenger = null;
		this.direction = k;
		this.rest = 200;
		this.credit = 0;
		this.time = 0;
	}
	
	public void setPassenger(Passenger passenger) {
		this.passenger = passenger;
		this.credit += 3;	//成功服务乘客一次信用度+3
	}	

	
	//对于等待服务的出租车的随机行走方向
	public int RandomMove(){		
		ArrayList<Integer> choice = new ArrayList<Integer>();
		if(Map.JudgeLink(location, new Point (location.getX()-1, location.getY(), 0))){
			choice.add(0);
		}
		if(Map.JudgeLink(location, new Point(location.getX()+1, location.getY(), 0))){
			choice.add(1);
		}
		if(Map.JudgeLink(location, new Point(location.getX(), location.getY()-1, 0))){
			choice.add(2);
		}
		if(Map.JudgeLink(location, new Point(location.getX(), location.getY()+1, 0))){
			choice.add(3);
		}

			Random random = new Random();
			if(choice.size() == 0)
				System.out.println(location.getX() + "," + location.getY());
			int j = random.nextInt(choice.size());	//随机取个数赋值给j
			direction = choice.get(j);				//得到当前需要去的方向
			if(direction == 0 && location.getX()>=0){		//上
//				location.setX(location.getX()-1);
//				location.setY(location.getY());
				return 0;
			}
			else if(direction == 1 && location.getX()<=80){ //下
//				location.setX(location.getX()+1);
//				location.setY(location.getY());
				return 1;
			}
			else if(direction == 2 && location.getY()>=0){ 	//左
//				location.setX(location.getX());
//				location.setY(location.getY()-1);
				return 2;
			}
			else if(direction == 3 && location.getY()<=80){	//右
//				location.setX(location.getX());
//				location.setY(location.getY()+1);
				return 3;
			}
//			try {
//				Thread.sleep(100);
//			} catch (Exception e) {
//				// TODO: handle exception
//				e.printStackTrace();
//			}			
			else
				return -1;
	}
	
	public int DirExchange(Point pre, Point togo){
		/*
		 * direction值:
		 * 0：向上走
		 * 1：向下走
		 * 2：向左走
		 * 3：向右走
		 */
		if(togo.getX()<pre.getX()){
			return 0;
		}
		else if(togo.getX()>pre.getX()){
			return 1;
		}
		else if(togo.getY()<pre.getY()){
			return 2;
		}
		else if(togo.getY()>pre.getY()){
			return 3;
		}
		else
			return -1;
		
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		while(true) {
			try {
				Thread.sleep(100);		
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			time++;
			
			switch(state){
			case 1:	//正在服务busy
			{
				if(way_to_go.size() == 0){
					System.out.println("Taxi: No." + ID + " served " + passenger.toString());
					state = 4;
					rest = 10;
					passenger = null;
				}
				else{
					//System.out.println("Taxi: No." + ID + " 233333 " + passenger.toString());
					direction = DirExchange(location, way_to_go.get(0));
					way_to_go.remove(0);
				}
				break;
			}
			case 2:	//即将服务immediate
			{
				if(way_to_go.size() == 0){
					//System.out.println("Taxi: No." + ID + "got " + passenger.toString());
					state = 4;
					rest = 10;
					try {
						//Point taxiInfo = new Point(location.getX(), location.getY(), location.getValue());
						Point passengerInfo = new Point(passenger.getDest().getX(), passenger.getDest().getY());
						way_to_go = Map.SearchMin(location, passengerInfo);
					} catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
					}
				}
				else{
					//System.out.println("Taxi: No." + ID + "is at" + "(" + location.getX() + "," + location.getY() + ")");
					direction = DirExchange(location, way_to_go.get(0));
					way_to_go.remove(0);
				}
				break;
			}
			case 3:	//等待服务wait
			{
				if(passenger!=null){
					state = 2;	//一旦分配乘客，转变为即将服务状态
					System.out.println("Taxi: " + ID + " is going to pick up " + passenger.toString());
					try {
						Point passengerInfo = new Point(passenger.getLocaltion_P().getX(), passenger.getLocaltion_P().getY());
						//System.out.println(location.getX() + " " + location.getY() + " " + passengerInfo.getX() + " " + passengerInfo.getY());
						way_to_go = Map.SearchMin(location,passengerInfo);
					} catch (Exception e) {
						// TODO: handle exception
						//System.out.println(location.getX() + " " + location.getY() + " " + passenger.getLocaltion_P().getX() + " " + passenger.getLocaltion_P().getY());
						e.printStackTrace();
					}
					//如果乘客就在出租车所在位置，则进入接客状态(offline+停1s)
					if(way_to_go.size() == 0){
						state = 4;
						rest = 10;	//每100ms扫描1次，休息1s就相当于休息10下
						try {
							//Point taxiInfo = new Point(location.getX(), location.getY(), location.getValue());
							Point passengerInfo = new Point(passenger.getDest().getX(), passenger.getDest().getY());
							way_to_go = Map.SearchMin(location, passengerInfo);
						} catch (Exception e) {
							// TODO: handle exception
							//System.out.println(location.getX() + " " + location.getY() + " " + passenger.getDest().getX() + " " + passenger.getDest().getY());
							e.printStackTrace();
						}
						//System.out.println("got");
						//passenger = null;
					}
					//否则，找到要去的方向，移除该点
					else{
						//标记要走的方向
						direction = DirExchange(location, way_to_go.get(0));
						way_to_go.remove(0);
					}
				}
				else if(rest == 0){
					state = 4;
					rest = 10;
				}
				//以上两种情况都不符合，就是随机移动
				else{
					direction = RandomMove();
					Vector<Passenger> passengers = Map.FindCustomer(location.getX(), location.getY());
					for (Iterator<Passenger> iterator = passengers.iterator(); iterator.hasNext();) {
						Passenger passenger = (Passenger) iterator.next();
						if(passenger.AddTaxi(this)){
							//抢单成功
							credit++;
							//System.out.println(ID + "Got the Order! Current location: " + "(" + location.getX() + "," + location.getY() + ")");
						}
					}
					rest--;
				}
				break;
			}
			case 4:	//停止运行offline
			{
				if(rest == 0){
					if(passenger == null){
						state = 3;
						rest = 200;
						direction = RandomMove();
					}
					else{
						state = 1;
						System.out.println("Taxi: No." + ID + " is serving " + passenger.toString());
						if(way_to_go.size() == 0){
							//System.out.println("impossible");
							state = 4;
							rest = 10;
							passenger = null;
						}
						else{
							direction = DirExchange(location, way_to_go.get(0));
							way_to_go.remove(0);
						}
					}
				}
				else
					rest--;
			}
			default:
				break;
			}
			
			if(state != 4){
				switch (direction) {
				case 0:	//上
				{
					location.setX(location.getX()-1);
					break;
				}
				case 1:	//下
				{
					location.setX(location.getX()+1);
					break;
				}
				case 2:	//左
				{
					location.setY(location.getY()-1);
					break;
				}
				case 3:	//右
				{
					location.setY(location.getY()+1);
					break;
				}
				default:
					System.out.println("wrong");
					break;
				}
			}
			if(location.getX()<0 || location.getX()>79 || location.getY()<0 || location.getY()>79){
				System.out.println("Taxi: No." + ID + "IS FIRING!BIBOBIBO..." + "Current location is: (" + location.getX() + "," + location.getY() + ")");
				break;
			}
			
		}
	}
	
	public int getState() {
		return state;
	}

	public Point getLocation() {
		return location;
	}

	public int getID() {
		return ID;
	}

	public int getCredit() {
		return credit;
	}


}
