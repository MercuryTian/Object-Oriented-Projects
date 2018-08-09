import java.util.Vector;

public class Passenger implements Runnable{
	
	private Point localtion_P;	//乘客的当前位置
	private Point dest;			//乘客的目的地
	private Vector<Taxi> taxis;	//存放抢单的出租车
	
	public Passenger(Point localtion_P, Point destination){
		/*
		 * Constructor.Used to initialize and structure a parameter in Passenger type.
		 * REQUIRES: Must provide two variable in Point type. 
		 * 			 The former one is the present location of a passenger.
		 * 			 The later one is the destination of a passenger.
		 * MODIFIES: Set this.location_P equals to the former provided variable.
		 * 			 Set this.dest equals to the later provided variable.
		 * EFFECTS: Let this.location_P equals to the former provided variable.
		 * 			Let this.dest equals to the later provided variable.
		 * 			Newly created a vector for this.taxis.
		 */
		this.localtion_P = localtion_P;
		this.dest = destination;
		this.taxis = new Vector<Taxi>();
	}
	
	public boolean AddTaxi(Taxi taxi){
		/*
		 * Used to add taxi.
		 * REQUIRES: Must provide a variable, which is the taxi.
		 * MODIFIES: None.
		 * EFFECTS: If successfully add a new taxi, then return true. Else, return false.
		 */
		if(!taxis.contains(taxi)){
			taxis.addElement(taxi);
			return true;
		}
		else 
			return false;
	}
	
	public Taxi SelectCar(){
		/*
		 * Used to select a taxi.
		 * REQUIRES: None.
		 * MODIFIES: None.
		 * EFFECTS: Used to select a taxi that meet the requirement.
		 */
		Vector<Taxi> starTaxi = new Vector<Taxi>();	//存放高信用的出租车
		Taxi taxi = null;
		//如果出租车不处于等待状态，没法接单，删去，后面的出租车顶替上来
		while(taxis.size()!=0 && taxis.get(0).getState()!=3){
			taxis.remove(0);
		}
		if(taxis.size() == 0){
			//System.out.println("Taxi: null");
			return null;
		}
		taxi = taxis.get(0);
		starTaxi.addElement(taxi);
		
		//先找高信用的出租车
		for(int i=1; i<taxis.size(); i++){
			Taxi tmp = taxis.get(i);
			//如果找到信用更高的车，并且处于等待服务状态
			if(tmp.getCredit()>taxi.getCredit() && tmp.getState()==3){
				//更新高信用出租车数组
				starTaxi.removeAllElements();
				taxi = tmp;
				starTaxi.addElement(taxi);
			}
			//如果信用相等
			else if(tmp.getCredit()==taxi.getCredit() && tmp.getState()==3){
				starTaxi.addElement(tmp);
			}
		}
		//再在高信用出租车里找距离最近的
		
		taxi = starTaxi.get(0);
		int length = 0;
		try {
			Point taxiInfo = new Point(taxi.getLocation().getX(), taxi.getLocation().getY(), taxi.getLocation().getValue());
			Point passengerInfo = new Point(localtion_P.getX(), localtion_P.getY(), localtion_P.getValue());
			//System.out.println("find high");
			length = Map.SearchMin(taxiInfo,passengerInfo).size();
			//System.out.println("find high!!!!!");
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return null;
		}
		
		for(int j=1; j<starTaxi.size(); j++){
			int tmp = 0;
			try {
				Point starInfo = new Point(starTaxi.get(j).getLocation().getX(), starTaxi.get(j).getLocation().getY(), starTaxi.get(j).getLocation().getValue());
				Point passengerInfo = new Point(localtion_P.getX(), localtion_P.getY(), localtion_P.getValue());
				tmp = Map.SearchMin(starInfo, passengerInfo).size();
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				return null;
			}
			if(tmp < length){
				length = tmp;
				taxi = starTaxi.get(j);
			}
		}
		//System.out.println("Taxi: find!");
		return taxi;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		Map.AddReq(localtion_P.getX(), localtion_P.getY(), this);
		try {
			Thread.sleep(3000);		//3s的抢单时间窗
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		Taxi taxi;
		//如果有车来接，则把这个乘客给出租车
		if((taxi = SelectCar()) != null){
			taxi.setPassenger(this);	
		}
		else{
			//System.out.println(this.toString() + "Can't find any taxis to call");
		}
		//然后把该乘客从地图上删除
		Map.DeleteReq(localtion_P.getX(), localtion_P.getY(), this);
	}
	
	public Point getLocaltion_P() {
		/*
		 * REQUIRES: None.
		 * MODIFIES: None.
		 * EFFECTS: Return the current position of passenger.
		 */
		return localtion_P;
	}

	public Point getDest() {
		/*
		 * REQUIRES: None.
		 * MODIFIES: None.
		 * EFFECTS: Return the destination of passenger.
		 */
		return dest;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		/*
		 * REQUIRES: None.
		 * MODIFIES: None.
		 * EFFECTS: Override the method of toString
		 */
		return "(" + localtion_P.getX() + "," + localtion_P.getY() + ")" + " to (" + dest.getX() + "," + dest.getY() + ")";
	}
		

}
