/*
 * 请求类：
 * 属性：
 * 1.请求类型
 * 2.请求目标楼层N
 * 3.请求方向（只对于FR）
 * 4.请求产生时间
 */

package elevator;

public class Request {
	private String type;
	private int tar_floor;
	private String direction;
	private double time;
	
	//FR请求
	public Request(String type, int tar_floor, String direction, double time){
		this.type = type;
		this.tar_floor = tar_floor;
		this.direction = direction;
		this.time = time;
	}
	
	//ER请求
	public Request(String type, int tar_floor, double time){
		this.type = type;
		this.tar_floor = tar_floor;
		this.time = time;
	}
	
	public String getType() {
		return type;
	}

	public int getTar_floor() {
		return tar_floor;
	}

	public String getDirection() {
		return direction;
	}

	public double getTime() {
		return time;
	}
	
	
	
}
