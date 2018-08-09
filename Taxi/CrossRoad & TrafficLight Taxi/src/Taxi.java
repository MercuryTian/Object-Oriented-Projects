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
//	private Vector<Point> way_to_go; 
	private int pre_dir;	//出租车从哪里来的，上一个方向
	
	public Taxi(int id){
		/*
		 * Constructor. Used to initialize and structure a parameter in Taxi type.
		 * REQUIRES: Must provide a variable ID of a car.
		 * MODIFIES: Set this.ID equals to the provided variable.
		 * EFFECTS: Let this.ID equals to the provided variable.
		 * 			Let this.direction equals to a random direction which is indicated numerically.
		 * 			Let present state equals to 3, which means the car is waiting for service.
		 * 			Let rest frequency equals to 200.
		 * 			Let credit equals to 0.
		 * 			Let time equals to 0.
		 * 			Initialize the coordinate of present location. To be specific, x and y is a random number.
		 */
		this.ID = id;
		Random rand = new Random();
//		int x = 39;
//		int y = 39;
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
		this.pre_dir = -1;	
	}
	
	public void setPassenger(Passenger passenger) {
		/*
		 * Used to set passenger.
		 * REQUIRES: Must provide a variable of Passenger type.
		 * MODIFIES: Set this.passenger equals to the provided variable.
		 * EFFECTS: Let this.passenger equals to the provided variable.
		 * 			If a car has succeeded in serving a passenger, his credit will plus 3.
		 */
		this.passenger = passenger;
		this.credit += 3;	//成功服务乘客一次信用度+3
	}	

	
	//对于等待服务的出租车的随机行走方向
	public int RandomMove(){		
		/*
		 * Used to set a random direction for the car which is waiting for service;
		 * REQUIRES: None.
		 * MODIFIES: None.
		 * EFFECTS: The premise is that the random direction should meet the requirements.
		 * 			So, we need to judge whether the direction heading to is connected.
		 * 			Also add new codes for the new rule of traffic flow.
		 * 			Then, if the random direction is upward, return 0;
		 * 			If the random direction is downward, return 1;
		 * 			If the random direction is left, return 2;
		 * 			If the random direction is right, return 3;
		 */
//		ArrayList<Integer> choice = new ArrayList<Integer>();
//		if(Map.JudgeLink(location, new Point (location.getX()-1, location.getY(), 0))){
//			choice.add(0);
//		}
//		if(Map.JudgeLink(location, new Point(location.getX()+1, location.getY(), 0))){
//			choice.add(1);
//		}
//		if(Map.JudgeLink(location, new Point(location.getX(), location.getY()-1, 0))){
//			choice.add(2);
//		}
//		if(Map.JudgeLink(location, new Point(location.getX(), location.getY()+1, 0))){
//			choice.add(3);
//		}
		
		int i = 0;
		int[] flow_record = {10000, 10000, 10000, 10000};	//记录四个方向的流量

			try {
				if(location == null){
					System.out.println("Location is null");
				}
				Point loc_up;
				loc_up = new Point(location.getX()-1, location.getY());
				if(Map.JudgeLink(location, loc_up)){
					flow_record[0] = Map.getTrafficFlow(location.getX(), location.getY(), 0);
				}
				Point loc_down;
				loc_down = new Point(location.getX()+1, location.getY());
				if(Map.JudgeLink(location, loc_down)){
					flow_record[1] = Map.getTrafficFlow(location.getX(), location.getY(), 1);
				}
				Point loc_left;
				loc_left = new Point(location.getX(), location.getY()-1);
				if(Map.JudgeLink(location, loc_left)){
					flow_record[2] = Map.getTrafficFlow(location.getX(), location.getY(), 2);
				}
				Point loc_right;
				loc_right = new Point(location.getX(), location.getY()+1);
				if(Map.JudgeLink(location, loc_right)){
					flow_record[3] = Map.getTrafficFlow(location.getX(), location.getY(), 3);
				}
				
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		
		int minFlow = flow_record[0];
		Vector<Integer> min;
		min = new Vector<Integer>();
		//min.add(i);
		int d = -1;	//方向
		for(i=0; i<flow_record.length; i++){
			if(flow_record[i] == 10000){
				continue;
			}
			else if(minFlow > flow_record[i]){
				minFlow = flow_record[i];
				min.removeAllElements();
				min.add(i);
			}
			else if(minFlow == flow_record[i]){
				//如果有多个流量最小的，随机跑
				min.add(i);
			}
		}
		
		Random rand = new Random();
		d = min.get(rand.nextInt(min.size()));

		
		if(d == 0 && location.getX()>0){		//上
			return 0;
		}
		else if(d == 1 && location.getX()<79){ //下
			return 1;
		}
		else if(d == 2 && location.getY()>0){ 	//左
			return 2;
		}
		else if(d == 3 && location.getY()<79){	//右
			return 3;
		}		
		else
			return -1;

	}
	
	public int DirExchange(int dir){
		/*
		 * direction值:
		 * 0：向上走
		 * 1：向下走
		 * 2：向左走
		 * 3：向右走
		 */
		if(dir == 0)
			return 1;
		else if(dir == 1)
			return 0;
		else if(dir == 2)
			return 3;
		else if(dir == 3)
			return 2;
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
//			System.out.println("Current: (" + location.getX() + "," + location.getY() + ")");
			/*
			 * 新的出租车行走方式：
			 * 1.在等待服务状态时：
			 * 	 出租车如果遇到分支,选择流量最小的边行走；
			 * 	 如果有多条流量最小的边,可随机选择一条分支边行走。
			 * 2.在即将服务和正在服务状态时：
			 *	 在即将服务和正在服务状态时,要求出租车按照最短路径行走；
			 *	 如果最短路径对应多条可以行走的边,选择流量最小的边行走；
			 *	 如果仍有多条流量最小的边,可随机选择一条边行走。
			 */
			
			switch(state){
			case 1:	//正在服务busy
			{
				Point passengerInfo = new Point(passenger.getDest().getX(), passenger.getDest().getY());
				if(location.equals(passengerInfo)){
					System.out.println("Taxi: No." + ID + " served " + passenger.toString());
					state = 4;
					rest = 10;
					passenger = null;
				}
				else{
					//System.out.println("Taxi: No." + ID + " 233333 " + passenger.toString());
					//direction = DirExchange(location, way_to_go.get(0));
					//way_to_go.remove(0);
					//Point passengerInfo = new Point(passenger.getDest().getX(), passenger.getDest().getY());
					pre_dir = DirExchange(direction);
					Map.setSubFlow(location.getX(), location.getY(), pre_dir);
					direction = Map.SearchFlow(location, passengerInfo);
					while(true){
						if(pre_dir == 1){
							if(direction == 0){
								//从下面来，要向上去：直行
								if(Map.getLight(location,0).equals("NS") || Map.getLight(location,0).equals("NO")){
									//如果是绿灯，通行
									WayToGo(direction);
									Map.setAddFlow(location.getX(), location.getY(), direction);
									break;
								}
								else if(Map.getLight(location,0).equals("NSred")){
									//如果是红灯，睡100ms
									try {
										Thread.sleep(100);
									} catch (InterruptedException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
								}
							}
							else if(direction == 2 || direction == 1){
								//从下面来，要往左去：左转
								if(Map.getLight(location,0).equals("NS") || Map.getLight(location,0).equals("NO")){
									//如果是绿灯，通行
									WayToGo(direction);
									Map.setAddFlow(location.getX(), location.getY(), direction);
									break;
								}
								else if(Map.getLight(location,0).equals("NSred")){
									//如果是红灯，睡100ms
									try {
										Thread.sleep(100);
									} catch (InterruptedException e) {
										// TODO Auto-generated catch block
									}
								}
							}
							else if(direction == 3){
									//从下面来，要往右去：右转；不用看灯
									WayToGo(direction);
									Map.setAddFlow(location.getX(), location.getY(), direction);
									break;
							}
						}
						else if(pre_dir == 0){
							if(direction == 1){
								//从上面来，要向下去：直行
								if(Map.getLight(location,0).equals("NS") || Map.getLight(location,0).equals("NO")){
									//如果是绿灯，通行
									WayToGo(direction);
									Map.setAddFlow(location.getX(), location.getY(), direction);
									break;
								}
								else if(Map.getLight(location,0).equals("NSred")){
									//如果是红灯，睡100ms
									try {
										Thread.sleep(100);
									} catch (InterruptedException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
								}
							}
							else if(direction == 3 || direction == 0){
								//从上面来，要往右去：左转或掉头
								if(Map.getLight(location,0).equals("NS") || Map.getLight(location,0).equals("NO")){
									//如果是绿灯，通行
									WayToGo(direction);
									Map.setAddFlow(location.getX(), location.getY(), direction);
									break;
								}
								else if(Map.getLight(location,0).equals("NSred")){
									//如果是红灯，睡100ms
									try {
										Thread.sleep(100);
									} catch (InterruptedException e) {
										// TODO Auto-generated catch block
									}
								}
							}
							else if(direction == 2){
									//从上面来，要往左去：右转；不用看灯
									WayToGo(direction);
									Map.setAddFlow(location.getX(), location.getY(), direction);
									break;
							}
						}
						else if(pre_dir == 2){
							if(direction == 3){
								//从左面来，要向右去：直行
								if(Map.getLight(location,1).equals("WE") || Map.getLight(location,1).equals("NO")){
									//如果是绿灯，通行
									WayToGo(direction);
									Map.setAddFlow(location.getX(), location.getY(), direction);
									break;
								}
								else if(Map.getLight(location,1).equals("WEred")){
									//如果是红灯，睡100ms
									try {
										Thread.sleep(100);
									} catch (InterruptedException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
								}
							}
							else if(direction == 0 || direction == 2){
								//从左面来，要向上去：左转
								if(Map.getLight(location,1).equals("WE") || Map.getLight(location,1).equals("NO")){
									//如果是绿灯，通行
									WayToGo(direction);
									Map.setAddFlow(location.getX(), location.getY(), direction);
									break;
								}
								else if(Map.getLight(location,1).equals("WEred")){
									//如果是红灯，睡100ms
									try {
										Thread.sleep(100);
									} catch (InterruptedException e) {
										// TODO Auto-generated catch block
									}
								}
							}
							else if(direction == 1){
									//从左面来，要往下去：右转；不用看灯
									WayToGo(direction);
									Map.setAddFlow(location.getX(), location.getY(), direction);
									break;
							}
						}
						else if(pre_dir == 3){
							if(direction == 2){
								//从右面来，要向左去：直行
								if(Map.getLight(location,1).equals("WE") || Map.getLight(location,1).equals("NO")){
									//如果是绿灯，通行
									WayToGo(direction);
									Map.setAddFlow(location.getX(), location.getY(), direction);
									break;
								}
								else if(Map.getLight(location,1).equals("WEred")){
									//如果是红灯，睡100ms
									try {
										Thread.sleep(100);
									} catch (InterruptedException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
								}
							}
							else if(direction == 1 || direction == 3){
								//从右面来，要向下去：左转
								if(Map.getLight(location,1).equals("WE") || Map.getLight(location,1).equals("NO")){
									//如果是绿灯，通行
									WayToGo(direction);
									Map.setAddFlow(location.getX(), location.getY(), direction);
									break;
								}
								else if(Map.getLight(location,1).equals("WEred")){
									//如果是红灯，睡100ms
									try {
										Thread.sleep(100);
									} catch (InterruptedException e) {
										// TODO Auto-generated catch block
									}
								}
							}
							else if(direction == 0){
									//从右面来，要往上去：右转；不用看灯
									WayToGo(direction);
									Map.setAddFlow(location.getX(), location.getY(), direction);
									break;
							}
						}
				}
			}
			}
			case 2:	//即将服务immediate
			{
				Point passengerInfo = null;
				try {
					passengerInfo = new Point(passenger.getDest().getX(), passenger.getDest().getY());
				} catch (Exception e) {
					// TODO: handle exception
					state = 3;
					continue;
				}
				if(location.equals(passengerInfo)){
					//接到乘客，停1s
					//System.out.println("Taxi: No." + ID + "got " + passenger.toString());
					state = 4;
					rest = 10;
				}
				else{
					//System.out.println("Taxi: No." + ID + "is at" + "(" + location.getX() + "," + location.getY() + ")");
					//direction = DirExchange(location, way_to_go.get(0));
					//way_to_go.remove(0);
					//Point passengerInfo = new Point(passenger.getDest().getX(), passenger.getDest().getY());
					pre_dir = DirExchange(direction);
					Map.setSubFlow(location.getX(), location.getY(), pre_dir);
					direction = Map.SearchFlow(location, passengerInfo);
					while(true){
						if(pre_dir == 1){
							if(direction == 0){
								//从下面来，要向上去：直行
								if(Map.getLight(location,0).equals("NS") || Map.getLight(location,0).equals("NO")){
									//如果是绿灯，通行
									WayToGo(direction);
									Map.setAddFlow(location.getX(), location.getY(), direction);
									break;
								}
								else if(Map.getLight(location,0).equals("NSred")){
									//如果是红灯，睡100ms
									try {
										Thread.sleep(100);
									} catch (InterruptedException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
								}
							}
							else if(direction == 2 || direction == 1){
								//从下面来，要往左去：左转
								if(Map.getLight(location,0).equals("NS") || Map.getLight(location,0).equals("NO")){
									//如果是绿灯，通行
									WayToGo(direction);
									Map.setAddFlow(location.getX(), location.getY(), direction);
									break;
								}
								else if(Map.getLight(location,0).equals("NSred")){
									//如果是红灯，睡100ms
									try {
										Thread.sleep(100);
									} catch (InterruptedException e) {
										// TODO Auto-generated catch block
									}
								}
							}
							else if(direction == 3){
									//从下面来，要往右去：右转；不用看灯
									WayToGo(direction);
									Map.setAddFlow(location.getX(), location.getY(), direction);
									break;
							}
						}
						else if(pre_dir == 0){
							if(direction == 1){
								//从上面来，要向下去：直行
								if(Map.getLight(location,0).equals("NS") || Map.getLight(location,0).equals("NO")){
									//如果是绿灯，通行
									WayToGo(direction);
									Map.setAddFlow(location.getX(), location.getY(), direction);
									break;
								}
								else if(Map.getLight(location,0).equals("NSred")){
									//如果是红灯，睡100ms
									try {
										Thread.sleep(100);
									} catch (InterruptedException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
								}
							}
							else if(direction == 3 || direction == 0){
								//从上面来，要往右去：左转
								if(Map.getLight(location,0).equals("NS") || Map.getLight(location,0).equals("NO")){
									//如果是绿灯，通行
									WayToGo(direction);
									Map.setAddFlow(location.getX(), location.getY(), direction);
									break;
								}
								else if(Map.getLight(location,0).equals("NSred")){
									//如果是红灯，睡100ms
									try {
										Thread.sleep(100);
									} catch (InterruptedException e) {
										// TODO Auto-generated catch block
									}
								}
							}
							else if(direction == 2){
									//从上面来，要往左去：右转；不用看灯
									WayToGo(direction);
									Map.setAddFlow(location.getX(), location.getY(), direction);
									break;
							}
						}
						else if(pre_dir == 2){
							if(direction == 3){
								//从左面来，要向右去：直行
								if(Map.getLight(location,1).equals("WE") || Map.getLight(location,1).equals("NO")){
									//如果是绿灯，通行
									WayToGo(direction);
									Map.setAddFlow(location.getX(), location.getY(), direction);
									break;
								}
								else if(Map.getLight(location,1).equals("WEred")){
									//如果是红灯，睡100ms
									try {
										Thread.sleep(100);
									} catch (InterruptedException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
								}
							}
							else if(direction == 0 || direction == 2){
								//从左面来，要向上去：左转
								if(Map.getLight(location,1).equals("WE") || Map.getLight(location,1).equals("NO")){
									//如果是绿灯，通行
									WayToGo(direction);
									Map.setAddFlow(location.getX(), location.getY(), direction);
									break;
								}
								else if(Map.getLight(location,1).equals("WEred")){
									//如果是红灯，睡100ms
									try {
										Thread.sleep(100);
									} catch (InterruptedException e) {
										// TODO Auto-generated catch block
									}
								}
							}
							else if(direction == 1){
									//从左面来，要往下去：右转；不用看灯
									WayToGo(direction);
									Map.setAddFlow(location.getX(), location.getY(), direction);
									break;
							}
						}
						else if(pre_dir == 3){
							if(direction == 2){
								//从右面来，要向左去：直行
								if(Map.getLight(location,1).equals("WE") || Map.getLight(location,1).equals("NO")){
									//如果是绿灯，通行
									WayToGo(direction);
									Map.setAddFlow(location.getX(), location.getY(), direction);
									break;
								}
								else if(Map.getLight(location,1).equals("WEred")){
									//如果是红灯，睡100ms
									try {
										Thread.sleep(100);
									} catch (InterruptedException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
								}
							}
							else if(direction == 1 || direction == 3){
								//从右面来，要向下去：左转
								if(Map.getLight(location,1).equals("WE") || Map.getLight(location,1).equals("NO")){
									//如果是绿灯，通行
									WayToGo(direction);
									Map.setAddFlow(location.getX(), location.getY(), direction);
									break;
								}
								else if(Map.getLight(location,1).equals("WEred")){
									//如果是红灯，睡100ms
									try {
										Thread.sleep(100);
									} catch (InterruptedException e) {
										// TODO Auto-generated catch block
									}
								}
							}
							else if(direction == 0){
									//从右面来，要往上去：右转；不用看灯
									WayToGo(direction);
									Map.setAddFlow(location.getX(), location.getY(), direction);
									break;
							}
						}
					}
				}
				break;
			}
			case 3:	//等待服务wait
			{
				if(passenger != null){
					state = 2;	//一旦分配乘客，转变为即将服务状态
					System.out.println("Taxi: " + ID + " is going to pick up " + passenger.toString());
					try {
						Point passengerInfo = new Point(passenger.getLocaltion_P().getX(), passenger.getLocaltion_P().getY());
						//System.out.println(location.getX() + " " + location.getY() + " " + passengerInfo.getX() + " " + passengerInfo.getY());
						//way_to_go = Map.SearchMin(location,passengerInfo);
						//因为是实时的，所以每判断一次行进方向都要判断是否到达终点
						if(location.equals(passengerInfo)){
							//到达乘客所在地点，接客停1s
							state = 4;
							rest = 10;
						}
						else{
							//还没有到达乘客所在地点
							pre_dir = DirExchange(direction);
							direction = Map.SearchFlow(location, passengerInfo);
							Map.setSubFlow(location.getX(), location.getY(), pre_dir);
							while(true){
								if(pre_dir == 1){
									if(direction == 0){
										//从下面来，要向上去：直行
										if(Map.getLight(location,0).equals("NS") || Map.getLight(location,0).equals("NO")){
											//如果是绿灯，通行
											WayToGo(direction);
											Map.setAddFlow(location.getX(), location.getY(), direction);
											break;
										}
										else if(Map.getLight(location,0).equals("NSred")){
											//如果是红灯，睡100ms
											try {
												Thread.sleep(100);
											} catch (InterruptedException e) {
												// TODO Auto-generated catch block
												e.printStackTrace();
											}
										}
									}
									else if(direction == 2 || direction == 1){
										//从下面来，要往左去：左转
										if(Map.getLight(location,0).equals("NS") || Map.getLight(location,0).equals("NO")){
											//如果是绿灯，通行
											WayToGo(direction);
											Map.setAddFlow(location.getX(), location.getY(), direction);
											break;
										}
										else if(Map.getLight(location,0).equals("NSred")){
											//如果是红灯，睡100ms
											try {
												Thread.sleep(100);
											} catch (InterruptedException e) {
												// TODO Auto-generated catch block
											}
										}
									}
									else if(direction == 3){
											//从下面来，要往右去：右转；不用看灯
											WayToGo(direction);
											Map.setAddFlow(location.getX(), location.getY(), direction);
											break;
									}
								}
								else if(pre_dir == 0){
									if(direction == 1){
										//从上面来，要向下去：直行
										if(Map.getLight(location,0).equals("NS") || Map.getLight(location,0).equals("NO")){
											//如果是绿灯，通行
											WayToGo(direction);
											Map.setAddFlow(location.getX(), location.getY(), direction);
											break;
										}
										else if(Map.getLight(location,0).equals("NSred")){
											//如果是红灯，睡100ms
											try {
												Thread.sleep(100);
											} catch (InterruptedException e) {
												// TODO Auto-generated catch block
												e.printStackTrace();
											}
										}
									}
									else if(direction == 3 || direction == 0){
										//从上面来，要往右去：左转
										if(Map.getLight(location,0).equals("NS") || Map.getLight(location,0).equals("NO")){
											//如果是绿灯，通行
											WayToGo(direction);
											Map.setAddFlow(location.getX(), location.getY(), direction);
											break;
										}
										else if(Map.getLight(location,0).equals("NSred")){
											//如果是红灯，睡100ms
											try {
												Thread.sleep(100);
											} catch (InterruptedException e) {
												// TODO Auto-generated catch block
											}
										}
									}
									else if(direction == 2){
											//从上面来，要往左去：右转；不用看灯
											WayToGo(direction);
											Map.setAddFlow(location.getX(), location.getY(), direction);
											break;
									}
								}
								else if(pre_dir == 2){
									if(direction == 3){
										//从左面来，要向右去：直行
										if(Map.getLight(location,1).equals("WE") || Map.getLight(location,1).equals("NO")){
											//如果是绿灯，通行
											WayToGo(direction);
											Map.setAddFlow(location.getX(), location.getY(), direction);
											break;
										}
										else if(Map.getLight(location,1).equals("WEred")){
											//如果是红灯，睡100ms
											try {
												Thread.sleep(100);
											} catch (InterruptedException e) {
												// TODO Auto-generated catch block
												e.printStackTrace();
											}
										}
									}
									else if(direction == 0 || direction == 2){
										//从左面来，要向上去：左转
										if(Map.getLight(location,1).equals("WE") || Map.getLight(location,1).equals("NO")){
											//如果是绿灯，通行
											WayToGo(direction);
											Map.setAddFlow(location.getX(), location.getY(), direction);
											break;
										}
										else if(Map.getLight(location,1).equals("WEred")){
											//如果是红灯，睡100ms
											try {
												Thread.sleep(100);
											} catch (InterruptedException e) {
												// TODO Auto-generated catch block
											}
										}
									}
									else if(direction == 1){
											//从左面来，要往下去：右转；不用看灯
											WayToGo(direction);
											Map.setAddFlow(location.getX(), location.getY(), direction);
											break;
									}
								}
								else if(pre_dir == 3){
									if(direction == 2){
										//从右面来，要向左去：直行
										if(Map.getLight(location,1).equals("WE") || Map.getLight(location,1).equals("NO")){
											//如果是绿灯，通行
											WayToGo(direction);
											Map.setAddFlow(location.getX(), location.getY(), direction);
											break;
										}
										else if(Map.getLight(location,1).equals("WEred")){
											//如果是红灯，睡100ms
											try {
												Thread.sleep(100);
											} catch (InterruptedException e) {
												// TODO Auto-generated catch block
												e.printStackTrace();
											}
										}
									}
									else if(direction == 1 || direction == 3){
										//从右面来，要向下去：左转
										if(Map.getLight(location,1).equals("WE") || Map.getLight(location,1).equals("NO")){
											//如果是绿灯，通行
											WayToGo(direction);
											Map.setAddFlow(location.getX(), location.getY(), direction);
											break;
										}
										else if(Map.getLight(location,1).equals("WEred")){
											//如果是红灯，睡100ms
											try {
												Thread.sleep(100);
											} catch (InterruptedException e) {
												// TODO Auto-generated catch block
											}
										}
									}
									else if(direction == 0){
											//从右面来，要往上去：右转；不用看灯
											WayToGo(direction);
											Map.setAddFlow(location.getX(), location.getY(), direction);
											break;
									}
								}
							}
//							WayToGo(direction);
//							Map.setAddFlow(location.getX(), location.getY(), direction);
						}
						
					} catch (Exception e) {
						// TODO: handle exception
						//System.out.println(location.getX() + " " + location.getY() + " " + passenger.getLocaltion_P().getX() + " " + passenger.getLocaltion_P().getY());
						e.printStackTrace();
					}
				}
				else if(rest == 0){
					state = 4;
					rest = 10;
				}
				//以上两种情况都不符合，就是随机移动
				else{
					pre_dir = DirExchange(direction);
					Map.setSubFlow(location.getX(), location.getY(), pre_dir);
					direction = RandomMove();
					while(true){

						if(pre_dir == 1){
							if(direction == 0){
								//从下面来，要向上去：直行
								if(Map.getLight(location,0).equals("NS") || Map.getLight(location,0).equals("NO")){
									//如果是绿灯，通行
									WayToGo(direction);
									Map.setAddFlow(location.getX(), location.getY(), direction);
									break;
								}
								else if(Map.getLight(location,0).equals("NSred")){
									//如果是红灯，睡100ms
									try {
										Thread.sleep(100);
									} catch (InterruptedException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
								}
							}
							else if(direction == 2 || direction == 1){
								//从下面来，要往左去：左转
								if(Map.getLight(location,0).equals("NS") || Map.getLight(location,0).equals("NO")){
									//如果是绿灯，通行
									WayToGo(direction);
									Map.setAddFlow(location.getX(), location.getY(), direction);
									break;
								}
								else if(Map.getLight(location,0).equals("NSred")){
									//如果是红灯，睡100ms
									try {
										Thread.sleep(100);
									} catch (InterruptedException e) {
										// TODO Auto-generated catch block
									}
								}
							}
							else if(direction == 3){
									//从下面来，要往右去：右转；不用看灯
									WayToGo(direction);
									Map.setAddFlow(location.getX(), location.getY(), direction);
									break;
							}
						}
						else if(pre_dir == 0){
							if(direction == 1){
								//从上面来，要向下去：直行
								if(Map.getLight(location,0).equals("NS") || Map.getLight(location,0).equals("NO")){
									//如果是绿灯，通行
									WayToGo(direction);
									Map.setAddFlow(location.getX(), location.getY(), direction);
									break;
								}
								else if(Map.getLight(location,0).equals("NSred")){
									//如果是红灯，睡100ms
									try {
										Thread.sleep(100);
									} catch (InterruptedException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
								}
							}
							else if(direction == 3 || direction == 0){
								//从上面来，要往右去：左转
								if(Map.getLight(location,0).equals("NS") || Map.getLight(location,0).equals("NO")){
									//如果是绿灯，通行
									WayToGo(direction);
									Map.setAddFlow(location.getX(), location.getY(), direction);
									break;
								}
								else if(Map.getLight(location,0).equals("NSred")){
									//如果是红灯，睡100ms
									try {
										Thread.sleep(100);
									} catch (InterruptedException e) {
										// TODO Auto-generated catch block
									}
								}
							}
							else if(direction == 2){
									//从上面来，要往左去：右转；不用看灯
									WayToGo(direction);
									Map.setAddFlow(location.getX(), location.getY(), direction);
									break;
							}
						}
						else if(pre_dir == 2){
							if(direction == 3){
								//从左面来，要向右去：直行
								if(Map.getLight(location,1).equals("WE") || Map.getLight(location,1).equals("NO")){
									//如果是绿灯，通行
									WayToGo(direction);
									Map.setAddFlow(location.getX(), location.getY(), direction);
									break;
								}
								else if(Map.getLight(location,1).equals("WEred")){
									//如果是红灯，睡100ms
									try {
										Thread.sleep(100);
									} catch (InterruptedException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
								}
							}
							else if(direction == 0 || direction == 2){
								//从左面来，要向上去：左转
								if(Map.getLight(location,1).equals("WE") || Map.getLight(location,1).equals("NO")){
									//如果是绿灯，通行
									WayToGo(direction);
									Map.setAddFlow(location.getX(), location.getY(), direction);
									break;
								}
								else if(Map.getLight(location,1).equals("WEred")){
									//如果是红灯，睡100ms
									try {
										Thread.sleep(100);
									} catch (InterruptedException e) {
										// TODO Auto-generated catch block
									}
								}
							}
							else if(direction == 1){
									//从左面来，要往下去：右转；不用看灯
									WayToGo(direction);
									Map.setAddFlow(location.getX(), location.getY(), direction);
									break;
							}
						}
						else if(pre_dir == 3){
							if(direction == 2){
								//从右面来，要向左去：直行
								if(Map.getLight(location,1).equals("WE") || Map.getLight(location,1).equals("NO")){
									//如果是绿灯，通行
									WayToGo(direction);
									Map.setAddFlow(location.getX(), location.getY(), direction);
									break;
								}
								else if(Map.getLight(location,1).equals("WEred")){
									//如果是红灯，睡100ms
									try {
										Thread.sleep(100);
									} catch (InterruptedException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
								}
							}
							else if(direction == 1 || direction == 3){
								//从右面来，要向下去：左转
								if(Map.getLight(location,1).equals("WE") || Map.getLight(location,1).equals("NO")){
									//如果是绿灯，通行
									WayToGo(direction);
									Map.setAddFlow(location.getX(), location.getY(), direction);
									break;
								}
								else if(Map.getLight(location,1).equals("WEred")){
									//如果是红灯，睡100ms
									try {
										Thread.sleep(100);
									} catch (InterruptedException e) {
										// TODO Auto-generated catch block
									}
								}
							}
							else if(direction == 0){
									//从右面来，要往上去：右转；不用看灯
									WayToGo(direction);
									Map.setAddFlow(location.getX(), location.getY(), direction);
									break;
							}
						}
				
					}
//					WayToGo(direction);
//					Map.setAddFlow(location.getX(), location.getY(), direction);
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
						//随机跑，流量约束
						pre_dir = DirExchange(direction);
						direction = RandomMove();
						Map.setSubFlow(location.getX(), location.getY(), pre_dir);
						while(true){

							if(pre_dir == 1){
								if(direction == 0){
									//从下面来，要向上去：直行
									if(Map.getLight(location,0).equals("NS") || Map.getLight(location,0).equals("NO")){
										//如果是绿灯，通行
										WayToGo(direction);
										Map.setAddFlow(location.getX(), location.getY(), direction);
										break;
									}
									else if(Map.getLight(location,0).equals("NSred")){
										//如果是红灯，睡100ms
										try {
											Thread.sleep(100);
										} catch (InterruptedException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
									}
								}
								else if(direction == 2 || direction == 1){
									//从下面来，要往左去：左转
									if(Map.getLight(location,0).equals("NS") || Map.getLight(location,0).equals("NO")){
										//如果是绿灯，通行
										WayToGo(direction);
										Map.setAddFlow(location.getX(), location.getY(), direction);
										break;
									}
									else if(Map.getLight(location,0).equals("NSred")){
										//如果是红灯，睡100ms
										try {
											Thread.sleep(100);
										} catch (InterruptedException e) {
											// TODO Auto-generated catch block
										}
									}
								}
								else if(direction == 3){
										//从下面来，要往右去：右转；不用看灯
										WayToGo(direction);
										Map.setAddFlow(location.getX(), location.getY(), direction);
										break;
								}
							}
							else if(pre_dir == 0){
								if(direction == 1){
									//从上面来，要向下去：直行
									if(Map.getLight(location,0).equals("NS") || Map.getLight(location,0).equals("NO")){
										//如果是绿灯，通行
										WayToGo(direction);
										Map.setAddFlow(location.getX(), location.getY(), direction);
										break;
									}
									else if(Map.getLight(location,0).equals("NSred")){
										//如果是红灯，睡100ms
										try {
											Thread.sleep(100);
										} catch (InterruptedException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
									}
								}
								else if(direction == 3 || direction == 0){
									//从上面来，要往右去：左转
									if(Map.getLight(location,0).equals("NS") || Map.getLight(location,0).equals("NO")){
										//如果是绿灯，通行
										WayToGo(direction);
										Map.setAddFlow(location.getX(), location.getY(), direction);
										break;
									}
									else if(Map.getLight(location,0).equals("NSred")){
										//如果是红灯，睡100ms
										try {
											Thread.sleep(100);
										} catch (InterruptedException e) {
											// TODO Auto-generated catch block
										}
									}
								}
								else if(direction == 2){
										//从上面来，要往左去：右转；不用看灯
										WayToGo(direction);
										Map.setAddFlow(location.getX(), location.getY(), direction);
										break;
								}
							}
							else if(pre_dir == 2){
								if(direction == 3){
									//从左面来，要向右去：直行
									if(Map.getLight(location,1).equals("WE") || Map.getLight(location,1).equals("NO")){
										//如果是绿灯，通行
										WayToGo(direction);
										Map.setAddFlow(location.getX(), location.getY(), direction);
										break;
									}
									else if(Map.getLight(location,1).equals("WEred")){
										//如果是红灯，睡100ms
										try {
											Thread.sleep(100);
										} catch (InterruptedException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
									}
								}
								else if(direction == 0 || direction == 2){
									//从左面来，要向上去：左转
									if(Map.getLight(location,1).equals("WE") || Map.getLight(location,1).equals("NO")){
										//如果是绿灯，通行
										WayToGo(direction);
										Map.setAddFlow(location.getX(), location.getY(), direction);
										break;
									}
									else if(Map.getLight(location,1).equals("WEred")){
										//如果是红灯，睡100ms
										try {
											Thread.sleep(100);
										} catch (InterruptedException e) {
											// TODO Auto-generated catch block
										}
									}
								}
								else if(direction == 1){
										//从左面来，要往下去：右转；不用看灯
										WayToGo(direction);
										Map.setAddFlow(location.getX(), location.getY(), direction);
										break;
								}
							}
							else if(pre_dir == 3){
								if(direction == 2){
									//从右面来，要向左去：直行
									if(Map.getLight(location,1).equals("WE") || Map.getLight(location,1).equals("NO")){
										//如果是绿灯，通行
										WayToGo(direction);
										Map.setAddFlow(location.getX(), location.getY(), direction);
										break;
									}
									else if(Map.getLight(location,1).equals("WEred")){
										//如果是红灯，睡100ms
										try {
											Thread.sleep(100);
										} catch (InterruptedException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
									}
								}
								else if(direction == 1 || direction == 3){
									//从右面来，要向下去：左转
									if(Map.getLight(location,1).equals("WE") || Map.getLight(location,1).equals("NO")){
										//如果是绿灯，通行
										WayToGo(direction);
										Map.setAddFlow(location.getX(), location.getY(), direction);
										break;
									}
									else if(Map.getLight(location,1).equals("WEred")){
										//如果是红灯，睡100ms
										try {
											Thread.sleep(100);
										} catch (InterruptedException e) {
											// TODO Auto-generated catch block
										}
									}
								}
								else if(direction == 0){
										//从右面来，要往上去：右转；不用看灯
										WayToGo(direction);
										Map.setAddFlow(location.getX(), location.getY(), direction);
										break;
								}
							}
					
						}
//						WayToGo(direction);
//						Map.setAddFlow(location.getX(), location.getY(), direction);
					}
					else{
						state = 1;
						System.out.println("Taxi: No." + ID + " is serving " + passenger.toString());
						Point passengerInfo = new Point(passenger.getDest().getX(), passenger.getDest().getY());
						if(location.equals(passengerInfo)){
							//如果接到乘客，停1s；原先是：way_to_go.size() == 0；
							//System.out.println("impossible");
							state = 4;
							rest = 10;
							System.out.println("Taxi: No." + ID + " served " + passenger.toString());
							passenger = null;
						}
						else{
							//如果没接到乘客
							//direction = DirExchange(location, way_to_go.get(0));
							//way_to_go.remove(0);
							//Point passengerInfo = new Point(passenger.getLocaltion_P().getX(), passenger.getLocaltion_P().getY());
							pre_dir = DirExchange(direction);
							direction = Map.SearchFlow(location, passengerInfo);
							Map.setSubFlow(location.getX(), location.getY(), pre_dir);
							while(true){

								if(pre_dir == 1){
									if(direction == 0){
										//从下面来，要向上去：直行
										if(Map.getLight(location,0).equals("NS") || Map.getLight(location,0).equals("NO")){
											//如果是绿灯，通行
											WayToGo(direction);
											Map.setAddFlow(location.getX(), location.getY(), direction);
											break;
										}
										else if(Map.getLight(location,0).equals("NSred")){
											//如果是红灯，睡100ms
											try {
												Thread.sleep(100);
											} catch (InterruptedException e) {
												// TODO Auto-generated catch block
												e.printStackTrace();
											}
										}
									}
									else if(direction == 2 || direction == 1){
										//从下面来，要往左去：左转
										if(Map.getLight(location,0).equals("NS") || Map.getLight(location,0).equals("NO")){
											//如果是绿灯，通行
											WayToGo(direction);
											Map.setAddFlow(location.getX(), location.getY(), direction);
											break;
										}
										else if(Map.getLight(location,0).equals("NSred")){
											//如果是红灯，睡100ms
											try {
												Thread.sleep(100);
											} catch (InterruptedException e) {
												// TODO Auto-generated catch block
											}
										}
									}
									else if(direction == 3){
											//从下面来，要往右去：右转；不用看灯
											WayToGo(direction);
											Map.setAddFlow(location.getX(), location.getY(), direction);
											break;
									}
								}
								else if(pre_dir == 0){
									if(direction == 1){
										//从上面来，要向下去：直行
										if(Map.getLight(location,0).equals("NS") || Map.getLight(location,0).equals("NO")){
											//如果是绿灯，通行
											WayToGo(direction);
											Map.setAddFlow(location.getX(), location.getY(), direction);
											break;
										}
										else if(Map.getLight(location,0).equals("NSred")){
											//如果是红灯，睡100ms
											try {
												Thread.sleep(100);
											} catch (InterruptedException e) {
												// TODO Auto-generated catch block
												e.printStackTrace();
											}
										}
									}
									else if(direction == 3 || direction == 0){
										//从上面来，要往右去：左转
										if(Map.getLight(location,0).equals("NS") || Map.getLight(location,0).equals("NO")){
											//如果是绿灯，通行
											WayToGo(direction);
											Map.setAddFlow(location.getX(), location.getY(), direction);
											break;
										}
										else if(Map.getLight(location,0).equals("NSred")){
											//如果是红灯，睡100ms
											try {
												Thread.sleep(100);
											} catch (InterruptedException e) {
												// TODO Auto-generated catch block
											}
										}
									}
									else if(direction == 2){
											//从上面来，要往左去：右转；不用看灯
											WayToGo(direction);
											Map.setAddFlow(location.getX(), location.getY(), direction);
											break;
									}
								}
								else if(pre_dir == 2){
									if(direction == 3){
										//从左面来，要向右去：直行
										if(Map.getLight(location,1).equals("WE") || Map.getLight(location,1).equals("NO")){
											//如果是绿灯，通行
											WayToGo(direction);
											Map.setAddFlow(location.getX(), location.getY(), direction);
											break;
										}
										else if(Map.getLight(location,1).equals("WEred")){
											//如果是红灯，睡100ms
											try {
												Thread.sleep(100);
											} catch (InterruptedException e) {
												// TODO Auto-generated catch block
												e.printStackTrace();
											}
										}
									}
									else if(direction == 0 || direction == 2){
										//从左面来，要向上去：左转
										if(Map.getLight(location,1).equals("WE") || Map.getLight(location,1).equals("NO")){
											//如果是绿灯，通行
											WayToGo(direction);
											Map.setAddFlow(location.getX(), location.getY(), direction);
											break;
										}
										else if(Map.getLight(location,1).equals("WEred")){
											//如果是红灯，睡100ms
											try {
												Thread.sleep(100);
											} catch (InterruptedException e) {
												// TODO Auto-generated catch block
											}
										}
									}
									else if(direction == 1){
											//从左面来，要往下去：右转；不用看灯
											WayToGo(direction);
											Map.setAddFlow(location.getX(), location.getY(), direction);
											break;
									}
								}
								else if(pre_dir == 3){
									if(direction == 2){
										//从右面来，要向左去：直行
										if(Map.getLight(location,1).equals("WE") || Map.getLight(location,1).equals("NO")){
											//如果是绿灯，通行
											WayToGo(direction);
											Map.setAddFlow(location.getX(), location.getY(), direction);
											break;
										}
										else if(Map.getLight(location,1).equals("WEred")){
											//如果是红灯，睡100ms
											try {
												Thread.sleep(100);
											} catch (InterruptedException e) {
												// TODO Auto-generated catch block
												e.printStackTrace();
											}
										}
									}
									else if(direction == 1 || direction == 3){
										//从右面来，要向下去：左转
										if(Map.getLight(location,1).equals("WE") || Map.getLight(location,1).equals("NO")){
											//如果是绿灯，通行
											WayToGo(direction);
											Map.setAddFlow(location.getX(), location.getY(), direction);
											break;
										}
										else if(Map.getLight(location,1).equals("WEred")){
											//如果是红灯，睡100ms
											try {
												Thread.sleep(100);
											} catch (InterruptedException e) {
												// TODO Auto-generated catch block
											}
										}
									}
									else if(direction == 0){
											//从右面来，要往上去：右转；不用看灯
											WayToGo(direction);
											Map.setAddFlow(location.getX(), location.getY(), direction);
											break;
									}
								}
						
							}
//							WayToGo(direction);
//							Map.setAddFlow(location.getX(), location.getY(), direction);
						}
					}
				}
				else
					rest--;
			}
			default:
				break;
			}
			
//			if(state != 4){
//				switch (direction) {
//				case 0:	//上
//				{
//					location.setX(location.getX()-1);
//					break;
//				}
//				case 1:	//下
//				{
//					location.setX(location.getX()+1);
//					break;
//				}
//				case 2:	//左
//				{
//					location.setY(location.getY()-1);
//					break;
//				}
//				case 3:	//右
//				{
//					location.setY(location.getY()+1);
//					break;
//				}
//				default:
//					System.out.println("wrong");
//					break;
//				}
//			}
			if(location.getX()<0 || location.getX()>79 || location.getY()<0 || location.getY()>79){
				System.out.println("Taxi: No." + ID + "IS FIRING!BIBOBIBO..." + "Current location is: (" + location.getX() + "," + location.getY() + ")");
				break;
			}
//			System.out.println(direction);
		}
	}
	
	public void WayToGo(int direction){
		/*
		 * Used to decide which way to go.
		 * REQUIRES: Must provide a variable, which is the direction.
		 * MODIFIES: None.
		 * EFFECTS: Decide how to go according to the direction provided.
		 */
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
	
	public int getState() {
		/*
		 * Used to get present state of a car.
		 * REQUIRES: None.
		 * MODIFIES: None.
		 * EFFECTS: Return the present state.
		 */
		return state;
	}

	public Point getLocation() {
		/*
		 * Used to get present location of a car.
		 * REQUIRES: None.
		 * MODIFIES: None.
		 * EFFECTS: Return the present location.
		 */
		return location;
	}

	public int getID() {
		/*
		 * Used to get present ID of car.
		 * REQUIRES: None.
		 * MODIFIES: None.
		 * EFFECTS: Return the present ID.
		 */
		return ID;
	}

	public int getCredit() {
		/*
		 * Used to get present credit of car.
		 * REQUIRES: None.
		 * MODIFIES: None.
		 * EFFECTS: Return the present credit.
		 */
		return credit;
	}
	
	public String getCurLoc(){
		//得到出租车当前位置的方法
		/*
		 * REQUIRES: None.
		 * MODIFIES: None.
		 * EFFECTS: Return current position of taxi.
		 */
		return "Current position: (" + location.getX() + "," + location.getY() + ")";
	}
	
	public boolean repOK(){
		if(state!=1 && state!=2 && state!=3 && state!=4)
			return false;
		if(location.getX()>=80 || location.getY()>=80 || location.getX()<0 || location.getY()<0)
			return false;
		if(credit<0)
			return false;
		if(time<0)
			return false;
		if(direction!=0 && direction!=1 && direction!=2 && direction!=3)
			return false;
		if(rest<0 || rest>200)
			return false;
		if(pre_dir!=0 && pre_dir!=1 && pre_dir!=2 && pre_dir!=3)
			return false;
		return true;
	}


}
