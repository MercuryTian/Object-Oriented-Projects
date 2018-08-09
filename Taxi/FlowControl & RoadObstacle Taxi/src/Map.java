import java.io.BufferedReader;
import java.io.FileReader;
import java.util.LinkedList;
import java.util.Vector;

public class Map {
	
	private String PATH = "Map.txt";	//此处输入文件的路径
	private static Point[][] matrix;
	private static Vector<Passenger>[][] map_matrix;	//一个点可以存放多个乘客
	private static Vector<ChangeInfo> changeInfo;	//记录被关闭的点和边信息
	private static int[][] trafficFlow;	//车流量统计二维数组
	
	
	@SuppressWarnings("unchecked")
	public Map(){
		/*
		 * Constructor. Used to initialize and structure a map.
		 * REQUIRES: None.
		 * MODIFIES: None.
		 * EFFECTS: Build a map. 
		 */
		matrix = new Point[80][80];
		map_matrix = new Vector[80][80];
		changeInfo = new Vector<ChangeInfo>();
		trafficFlow = new int[161][80];
		for(int i=0; i<161; i++){
			for(int j=0; j<80; j++){
				trafficFlow[i][j] = 0;
			}
		}
		
		for(int i=0; i<80; i++){
			for(int j=0; j<80; j++){
				map_matrix[i][j] = new Vector<Passenger>();
			}
		}
		
		int i = 0;
		try {
			FileReader file = new FileReader(PATH);
			BufferedReader buf = new BufferedReader(file);
			String str = null;
			while((str=buf.readLine())!= null){
				str = str.replace(" ", "");
				str = str.replace("\t", "");
				if(str.length() == 0){
					continue;
				}
				
				if(i > 80){
					System.out.println("Points are out of requests");
				}
				if(!str.matches("[0-3]{80}")){
					System.out.println("Points format are illegal");
				}
				for(int j=0; j<str.length(); j++){
					int value = str.charAt(j) - '0' ;
					matrix[i][j] = new Point(i,j,value);
					if(i>0 && (matrix[i-1][j].getValue()==2 || matrix[i-1][j].getValue()==3)){
						//父节点：判断上联通
						matrix[i][j].setConnect(0);
					}
					if(j>0 && (matrix[i][j-1].getValue()==1 || matrix[i][j-1].getValue()==3)){
						//父节点：判断左联通
						matrix[i][j].setConnect(2);
					}
				}
				i++;
				//判断行
//				if(i!=80){
//					System.out.println("Points are out of requests");
//				}	
			}
			buf.close();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			System.out.println("File Illegal");
		}
	}
	
	//测试者关闭连通边
	public static void DeleteEdge(int x_change, int y_change, int value_change){
		/*
		 * Close the edge that meets the requirement.
		 * REQUIRES: Must provide three variables, which are the horizontal ordinate, the vertical ordinate and the value need to change.
		 * MODIFIES: Modifying the value number on the map, thereby the state of edges also change.
		 * EFFECTS:	Close the edge that meets the requirement.
		 */
		//对于地图上已存在的边，只能关闭；不连通的边不能打开。所以，3只能改成1/2；1,2只能改成0；0不能改
		Point c;
		c = matrix[x_change][y_change];
		if(c.getValue() == 3){
			if(value_change == 1){
				//下边被关闭，记录
				c.setValue(1);
				c.closeConnect(1);
				changeInfo.addElement(new ChangeInfo(c, 2));
				System.out.println("Close successfully");
			}
			else if(value_change == 2){
				//右边被关闭，记录
				c.setValue(2);
				c.closeConnect(3);
				changeInfo.addElement(new ChangeInfo(c, 1));
				System.out.println("Close successfully");
			}
			else{
				System.out.println("Command is illegal");
			}
		}
		else if(c.getValue() == 2){
			if(value_change == 0){
				//下边被关闭，记录
				c.setValue(0);
				c.closeConnect(1);
				changeInfo.addElement(new ChangeInfo(c, 2));	
				System.out.println("Close successfully");
			}
			else{
				System.out.println("Command is illegal");
			}
		}
		else if(c.getValue() == 1){
			if(value_change == 0){
				//右边被关闭，记录
				c.setValue(0);
				c.closeConnect(3);
				changeInfo.addElement(new ChangeInfo(c, 1));
				System.out.println("Close successfully");
			}
			else{
				System.out.println("Command is illegal");
			}
		}
		else{	//如果c.getValue==0，不能删除，对于新打开边另写一个方法
			System.out.println("Cmmand is illegal");
		}
		
	}
	
	//测试者打开连通边
	public static void RecoverEdge(int ID){
		/*
		 * Open the edge that meets the requirement.
		 * REQUIRES: Must provide a variable, which is the position number in array changeInfo.
		 * MODIFIES: Modifying the value number on the map, thereby the state of edges also change.
		 * EFFECTS:	Open the edge that meets the requirement.
		 */
		//能打开的边只能是之前关闭的边，所以只能从ChangeInfo数组里面选一个
		Point c;
		c = changeInfo.get(ID).getPoint();
		if(changeInfo.get(ID).getEdge() == 1){
			//打开右边
			c.getConnect(3);
			System.out.println("Open successfully");
		}
		else{
			//打开下边
			c.getConnect(1);
			System.out.println("Open successfully");
		}
		changeInfo.remove(ID);
	}
	
	public static Vector<ChangeInfo> getChangeInfo() {
		/*
		 * Get Information of array changeInfo
		 * REQUIRES: None.
		 * MODIFIES: None.
		 * EFFECTS: Return changeInfo.
		 */
		return changeInfo;
	}
	
	
	/*
	 * 新的方法：
	 * 找这个点周围相邻的点到目标点的最短路，比较长短；
	 * 如果有一样长的，就比较车流；
	 * 返回一个方向，告诉出租车往哪里走；
	 */
	public static int SearchFlow(Point loc, Point dest){
		/*
		 * Used to search the minimal traffic flow.
		 * REQUIRES: Must provide two variables, which are the current location and the destination(for taxi).
		 * MODIFIES: None.
		 * EFFECTS: Finding the minimal flow. Then return the moving direction.
		 */
		/*
		 * 返回值：
		 * -1：same point
		 * 0：上
		 * 1：下
		 * 2：左
		 * 3：右
		 */
		if(loc.getX() == dest.getX() && loc.getY() == dest.getY()){
			return -1;
		}
		int[] length = {100000, 100000, 100000, 100000};
		//判断当前位置上下左右四个点到目的地的距离哪一条最短
//		上
		Point loc_up = null;
		if(loc.getX()-1 >= 0){
			loc_up = matrix[loc.getX()-1][loc.getY()];
			if(JudgeLink(loc, loc_up)){
				if(loc.getX()-1 >=0 && (loc_up.getValue()==3 || loc_up.getValue()==2)){
					try {
						length[0] = SearchMin(loc_up,dest).size();
					} catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
					}
				}
			}
		}	
		
//		下
		Point loc_down = null;
		if(loc.getX()+1 < 79){
			loc_down = matrix[loc.getX()+1][loc.getY()];
			if(JudgeLink(loc, loc_down)){
				if(loc.getX()+1 <80 && (loc_down.getValue()==3 || loc_down.getValue()==2)){
					try {
						length[1] = SearchMin(loc_down,dest).size();
					} catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
					}
				}
			}
		}
	
//		左
		Point loc_left = null;
		if(loc.getY()-1 >= 0){
			loc_left = matrix[loc.getX()][loc.getY()-1];
			if(JudgeLink(loc, loc_left)){
				if(loc.getY()-1 >=0 && (loc_left.getValue()==3 || loc_left.getValue()==1)){
					try {
						length[2] = SearchMin(loc_left,dest).size();
					} catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
					}
				}
			}
		}
		
//		右
		Point loc_right = null;
		if(loc.getY()+1 <79){
			loc_right = matrix[loc.getX()][loc.getY()+1];
			if(JudgeLink(loc, loc_right)){
				if(loc.getY()+1 <80 && (loc_right.getValue()==3 || loc_right.getValue()==1)){
					try {
						length[3] = SearchMin(loc_right,dest).size();
					} catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
					}
				}
			}
		}
	
		//比较并找出最短路径
		int min_length = length[0];
		int direction = 0;
		for(int i=0; i<length.length; i++){
			if(length[i] == 100000)
				continue;
			if((min_length>length[i]) || 
				(min_length==length[i] && getTrafficFlow(loc.getX(), loc.getY(), direction)>getTrafficFlow(loc.getX(), loc.getY(), i))){
				min_length = length[i];
				direction = i;
			}
		}		
		return direction;		
	}
	
	public static int getTrafficFlow(int x, int y, int dir){
		/*
		 * Used to get current traffic flow.
		 * REQUIRES: Must provide three variables, which are the current coordinate of taxi and the direction.
		 * MODIFIES: None.
		 * EFFECTS: Get the value of flow. Return flow.
		 */
		/*
		 * 映射关系：80*80的地图格，需要用80*(80+81)=161*80的二维数组来存储
		 * 对于任意一点(x,y):
		 * 上：(2x-1,y)
		 * 下：(2x+1,y)
		 * 左：(2x,y)
		 * 右：(2x,y-1)
		 * 
		 */
		int flow = 0;
//		上
		if(dir == 0 && 2*x-1>=0){
			flow = trafficFlow[2*x-1][y];
		}
//		下
		else if(dir == 1 && 2*x+1<161){
			flow = trafficFlow[2*x+1][y];
		}
//		左
		else if(dir == 2 && 2*x<161){
			flow = trafficFlow[2*x][y];
		}
//		右
		else if(dir == 3 && 2*x<161 && y-1>=0){
			flow = trafficFlow[2*x][y-1];
		}
		else{
			//System.out.println("Wrong direction");
		}
		return flow;
	}
	
	public static void setAddFlow(int x, int y, int dir){
		/*
//		 * Used to add traffic flow.
//		 * REQUIRES: Must provide a variable, which is the current location and direction.
//		 * MODIFIES: Traffic flow plus 1.
//		 * EFFECTS: Traffic flow plus 1. And return flow.
//		 */
//		上
		if(dir == 0 && 2*x-1>=0){
			trafficFlow[2*x-1][y]++;
		}
//		下
		else if(dir == 1 && 2*x+1<161){
			trafficFlow[2*x+1][y]++;
		}
//		左
		else if(dir == 2 && 2*x<161){
			trafficFlow[2*x][y]++;
		}
//		右
		else if(dir == 3 && 2*x<161 && y-1>=0){
			trafficFlow[2*x][y-1]++;
		}
		else{
//			System.out.println(dir);//调试用
//			System.out.println("Wrong direction");
		}
}
	
	public static void setSubFlow(int x, int y, int dir){
		/*
//		 * Used to decrease traffic flow.
//		 * REQUIRES: Must provide a variable, which is the current location and direction.
//		 * MODIFIES: Traffic flow decrease 1.
//		 * EFFECTS: Traffic flow decrease 1. And return flow.
//		 */
//		上
		if(dir == 0 && 2*x-1>=0){
			trafficFlow[2*x-1][y]--;
		}
//		下
		else if(dir == 1 && 2*x+1<161){
			trafficFlow[2*x+1][y]--;
		}
//		左
		else if(dir == 2 && 2*x<161){
			trafficFlow[2*x][y]--;
		}
//		右
		else if(dir == 3 && 2*x<161 && y-1>=0){
			trafficFlow[2*x][y-1]--;
		}
		else{
//		System.out.println(dir);//调试用
//			System.out.println("Wrong direction");
		}
}
	
	
	//BFS搜索最短路径，传入Vs(起点)和Vd(终点)
	public static Vector<Point> SearchMin(Point Vs, Point Vd){
		/*
		 * Using BFS algorithm to search the minimal path.
		 * REQUIRES: Must provide two variables, which are the starting point and the terminal point.
		 * MODIFIES: None.
		 * EFFECTS: Calculate the minimal path from the starting point to the terminal point. And restore this shortest path(return the road).
		 */
		//System.out.println("FIND: " + Vs.getX() + " " + Vs.getY() + " " + Vd.getX() + " " + Vd.getY());
		Vs = matrix[Vs.getX()][Vs.getY()];
		Vd = matrix[Vd.getX()][Vd.getY()];
		Vector<Point> road = new Vector<Point>();	//最终找到的最短路径
		LinkedList<Point> queue = new LinkedList<Point>();
		Point Vn;
		int[][] visit = new int[80][80];
		for(int k=0; k<80; k++){
			for(int j=0; j<80; j++){
				visit[k][j] = 0;
			}
		}
		/*
		 * visit的值：
		 * 0：没有被访问
		 * 1：上一个结点是从上面来的
		 * 2：上一个结点是从下面来的
		 * 3：上一个结点是从左面来的
		 * 4：上一个结点是从右面来的
		 */
		
		//起点入队，并设置其被访问过
		queue.add(Vs);
		visit[Vs.getX()][Vs.getY()] = 1; //随便设置一个标记其被访问
		
		//如果队列不为空，继续搜索
		while(!queue.isEmpty()){
			//取出队头
			Vn = queue.poll();
			if(Vn.getX()-1>=0 && Vn.getConnect(0) && visit[Vn.getX()-1][Vn.getY()]==0){	//上联通
				Point V_temp = matrix[Vn.getX()-1][Vn.getY()];   //value值随便传一个
				queue.add(V_temp);	
				visit[V_temp.getX()][V_temp.getY()] = 2;	//标记被访问
				if(V_temp.equals(Vd)){
					break;	//如果是终点，break,跳到下面的while一直往回找直起点
				}
			}
			if(Vn.getX()+1<80 && Vn.getConnect(1) && visit[Vn.getX()+1][Vn.getY()]==0){	//下联通
				Point V_temp = matrix[Vn.getX()+1][Vn.getY()];
				queue.add(V_temp);
				visit[V_temp.getX()][V_temp.getY()] = 1;
				if(V_temp.equals(Vd)){
					break;	//如果是终点，break,跳到下面的while一直往回找直起点
				}
			}
			if(Vn.getY()-1>=0 && Vn.getConnect(2) && visit[Vn.getX()][Vn.getY()-1]==0){	//左联通
				Point V_temp = matrix[Vn.getX()][Vn.getY()-1];
				queue.add(V_temp);
				visit[V_temp.getX()][V_temp.getY()] = 4;
				if(V_temp.equals(Vd)){
					break;	//如果是终点，break,跳到下面的while一直往回找直起点
				}
			}
			if(Vn.getY()+1<80 && Vn.getConnect(3) && visit[Vn.getX()][Vn.getY()+1]==0){	//右联通
				Point V_temp = matrix[Vn.getX()][Vn.getY()+1];
				queue.add(V_temp);
				visit[V_temp.getX()][V_temp.getY()] = 3;
				if(V_temp.equals(Vd)){
					break;	//如果是终点，break,跳到下面的while一直往回找直起点
				}
			}
		}
		
		while(!Vd.equals(Vs)){
			//上：
			if(visit[Vd.getX()][Vd.getY()] == 1){
				road.add(0, new Point(Vd.getX(), Vd.getY()));
				Vd = matrix[Vd.getX()-1][Vd.getY()];
			}	
			//下：
			else if(visit[Vd.getX()][Vd.getY()] == 2){
				road.add(0, new Point(Vd.getX(), Vd.getY()));
				Vd = matrix[Vd.getX()+1][Vd.getY()];
			}
			//左：
			else if(visit[Vd.getX()][Vd.getY()] == 3){
				road.add(0, new Point(Vd.getX(), Vd.getY()));
				Vd = matrix[Vd.getX()][Vd.getY()-1];
			}
			//右：
			else if(visit[Vd.getX()][Vd.getY()] == 4){
				road.add(0, new Point(Vd.getX(), Vd.getY()));
				Vd = matrix[Vd.getX()][Vd.getY()+1];
			}
		}
		//System.out.println(road.size());
		return road;
		
	}
	
	public static boolean JudgeLink(Point a, Point b){
		/*
		 * Judge whether the two points are connected.
		 * REQUIRES: Must provide two variables, which are two points that need to judge.
		 * MODIFIES: None.
		 * EFFECTS: If these two points are linked, then return true. Else, return false.
		 */
		//判断两个点是否联通
		if(a.getX()<0 || a.getX()>79 || a.getY()<0 || a.getY()>79 || b.getX()<0 || b.getX()>79
				|| b.getY()<0 || b.getY()>79){
			return false;
		}
		
		if((a.getX()<b.getX() && matrix[a.getX()][a.getY()].getConnect(1)) || (a.getX()>b.getX() && matrix[a.getX()][a.getY()].getConnect(0))
				|| (a.getY()<b.getY() && matrix[a.getX()][a.getY()].getConnect(3)) || (a.getY()>b.getY() && matrix[a.getX()][a.getY()].getConnect(2))){
			return true;
		}
		else
			return false;
			
	}
	
	public static Vector<Passenger> FindCustomer(int x, int y){
		/*
		 * Finding customers
		 * REQUIRES: Must provide two variables, which are the current coordinate of taxi.
		 * MODIFIES: None.
		 * EFFECTS: If the taxi find all customers, then return the customers.
		 */
		Vector<Passenger> cus = new Vector<Passenger>();
		for(int i=x-2; i<=x+2; i++){
			for(int j=y-2; j<=y+2; j++){
				if(i<0 || j<0 || i>79 || j> 79){
					continue;
				}
				for(int k=0; k<map_matrix[i][j].size(); k++){
					cus.addElement(map_matrix[i][j].get(k));
				}
			}
		}
		return cus;
	}
			
	public static void AddReq(int x, int y, Passenger p){
		/*
		 * Used to add requests
		 * REQUIRES: Must provide three variables, which are the current coordinate of taxi and a Passenger variable.
		 * MODIFIES: Add the Passenger to the map.
		 * EFFECTS: Add the Passenger to the map.
		 */
		if(map_matrix[x][y].contains(p)){
			return;
		}
		map_matrix[x][y].addElement(p);
	}
	
	public static void DeleteReq(int x, int y, Passenger p){
		/*
		 * Used to remove requests
		 * REQUIRES: Must provide three variables, which are the current coordinate of taxi and a Passenger variable.
		 * MODIFIES: Remove the Passenger to the map.
		 * EFFECTS: Remove the Passenger to the map.
		 */
		map_matrix[x][y].remove(p);
	}

	
		
}
	
	
	
	
