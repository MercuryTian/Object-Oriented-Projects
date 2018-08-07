/*
 * 电梯类：
 * 属性：
 * 1.当前楼层号：1-10（1层只有UP，10层只有DOWN）
 * 2.当前运行时间
 * 3.运行方向
 * 方法：
 * 1.收到请求运行一次电梯并输出
 */

package elevator;

public class Elevator implements Move{
	private int floor;				//当前楼层
	private double time;			//当前运行时间
	private String direction;		//运行方向
	
	//构造方法初始化
	public Elevator(){
		setTime(0);
		floor = 1;
		this.direction = "UP";		//NEW:运行方向初始化为UP
	}
	
	public void runstay_First(int tar_floor){
		if(tar_floor>floor){
			this.direction = "UP";
		}
		else if(tar_floor<floor){
			this.direction = "DOWN";
		}
		if(tar_floor!=floor){
			setTime(getTime() + Math.abs(tar_floor-floor)*0.5);
			this.floor = tar_floor;
			System.out.println(this.toString());
			setTime(getTime() + 1);
		}
		else
			setTime(getTime() + 1);
	}
	
	public void runstay(int tar_floor){
		if(tar_floor>floor){
			this.direction = "UP";
		}
		else if(tar_floor<floor){
			this.direction = "DOWN";
		}
		if(tar_floor!=floor){
			setTime(getTime() + Math.abs(tar_floor-floor)*0.5);
			this.floor = tar_floor;
			System.out.println(this.toString());
			setTime(getTime() + 1);
		}
	}
	
	public String toString(){
		return "("+floor+","+Elevator.this.direction+","+time+")";	
	}
	
	/*
	//ER请求
	public void runstayER(int n){		//n is target floor
		if(n > floor){
			this.direction = "UP";
			//direction = "UP";
			setTime(getTime() + ((n-floor)*0.5 + 1));
			floor = n;
		}
		else if(n < floor){
			this.direction = "DOWN";
			//direction = "DOWN";
			setTime(getTime() + ((floor-n)*0.5 + 1));
			floor = n;
		}
		//如果相等不改变电梯方向，电梯开关门；
		//NEW: 增加STAY状态
		else{
			setTime(getTime() + 1);
			this.direction = "STAY";
		}
		System.out.println(this.toString());
	}
	
	//FR请求
	public void runstayFR(int n, String direction){
		if(this.direction.equals("UP")){
			if(n>floor){
				setTime(getTime() + ((n-floor)*0.5 + 1));
				floor = n;
				this.direction = "UP";
			}
			else if(n<floor){
				setTime(getTime() + ((floor-n)*0.5 +1));	//先下去等待下一条请求
				floor = n;
				this.direction = "DOWN";
			}
			else{
				setTime(getTime() + 1);
				this.direction = "STAY";
			}
		}
		else if(this.direction.equals("DOWN")){
			if(n>floor){ 
				setTime(getTime() + ((n-floor)*0.5 + 1));	//先上去等待下一条请求
				floor = n;
				this.direction = "UP";
			}
			else if(n<floor){
				setTime(getTime() + ((floor-n)*0.5 +1));	
				floor = n;
				this.direction = "DOWN";
			}
			else{
				setTime(getTime() + 1);
				this.direction = "STAY";
			}
		}
		else{
			System.out.println("The direction is invalid");
		}
		
		System.out.println(this.toString());
		
	}
	
	public String toString(){
		return "("+floor+","+Elevator.this.direction+","+time+")";
		
	}
	*/

	public double getTime() {
		return time;
	}

	public void setTime(double time) {
		this.time = time;
	}
	
	public int getFloor() {
		return floor;
	}

	public String getDirection() {
		return direction;
	}
	


}
