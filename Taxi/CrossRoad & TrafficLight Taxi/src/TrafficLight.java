
public class TrafficLight extends Thread{
	private int light_NS;	//南北方向
	private int light_WE;	//东西方向
	/*
	 * 1：红灯
	 * 2：绿灯
	 */
	private boolean if_set;
	
	public TrafficLight(boolean if_set) {
		// TODO Auto-generated constructor stub
		this.if_set = if_set;
		if(if_set){
			// 初始值：南北方向为绿灯，东西方向为红灯
			this.light_NS = 2;
			this.light_WE = 1;
		}
		else{
			this.light_NS = 0;
			this.light_WE = 0;
		}
	}
	
	
	
	
	public int getLight_NS() {
		return light_NS;
	}
	public void setLight_NS(int light_NS) {
		this.light_NS = light_NS;
	}
	public int getLight_WE() {
		return light_WE;
	}
	public void setLight_WE(int light_WE) {
		this.light_WE = light_WE;
	}
	
	public boolean repOK(){
		if(light_NS!=1 && light_NS!=2)
			return false;
		if(light_WE!=1 && light_WE!=2)
			return false;
		return true;
	}
	
	
}
