import java.io.BufferedReader;
import java.io.FileReader;
import java.util.LinkedList;
import java.util.Vector;

public class Map {
	
	private String PATH = "Map.txt";	//此处输入文件的路径
	private static Point[][] matrix;
	private static Vector<Passenger>[][] map_matrix;	//一个点可以存放多个乘客
	
	@SuppressWarnings("unchecked")
	public Map(){
		matrix = new Point[80][80];
		map_matrix = new Vector[80][80];
//		for(int i=0; i<80; i++){
//			matrix[0][i] = new Point(0,i,0);
//			matrix[i][0] = new Point(i,0,0);
//		}
		
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
	
	
	//BFS搜索最短路径，传入Vs(起点)和Vd(终点)
	public static Vector<Point> SearchMin(Point Vs, Point Vd){
		//System.out.println("FIND: " + Vs.getX() + " " + Vs.getY() + " " + Vd.getX() + " " + Vd.getY());
		Vs = matrix[Vs.getX()][Vs.getY()];
		Vd = matrix[Vd.getX()][Vd.getY()];
		Vector<Point> road = new Vector<Point>();	//最终找到的最短路径
		LinkedList<Point> queue = new LinkedList<Point>();
		Point Vn;
//		int i;	
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
		if(map_matrix[x][y].contains(p)){
			return;
		}
		map_matrix[x][y].addElement(p);
	}
	
	public static void DeleteReq(int x, int y, Passenger p){
		map_matrix[x][y].remove(p);
	}
	
//	public static boolean TestMap(){
//		//判断图是否联通,BFS算法
//		boolean[] visit = new boolean[6400];
//		Queue<Short> toVisit = new LinkedList<Short>();
//		int n = 0;
//		short v = 0;
//		toVisit.add(v);
//		visit[v] = true;
//		
//		while(toVisit.size() != 0){
//			v = toVisit.poll();
//			for(int i=0; i<6400; i++){
//				if(visit[i]==false && matrix[v][i] < Short.MAX_VALUE){
//					toVisit.add((short)i);
//					visit[i] = true;
//					n++;
//				}
//			}
//		}
//		return n == 6400;
//	}
	

	
}
