
public class Request{
	
	private String type;
	private int number;
	private int tar_floor;
	private String direction;
	private int cur_time;
	
	//FR Request
	public Request(String type, int tar_floor, String direction, int cur_time){
		this.type = type;
		this.tar_floor = tar_floor;
		this.direction = direction;
		this.cur_time = cur_time;
	}
	
	//ER Request
	public Request(String type, int number, int tar_floor, int cur_time){
		this.type = type;
		this.number = number;
		this.tar_floor = tar_floor;
		this.cur_time = cur_time;
	}
	
	
	public String getType() {
		return type;
	}

	public int getNumber() {
		return number;
	}

	public int getTar_floor() {
		return tar_floor;
	}

	public String getDirection() {
		return direction;
	}
	
	public int getCur_time() {
		return cur_time;
	}
	
	@Override
	public boolean equals(Object obj) {
		// TODO Auto-generated method stub
		Request request = (Request) obj;
		if(request.direction.equals(this.direction) && request.tar_floor == this.tar_floor 
				&& request.type.equals(this.type) && request.number == this.number){
			return true;
		}
		else
			return false;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		if(type.equals("ER"))
			return "(ER," + "#" + number + "," + tar_floor + ")";
		else
			return "(FR," + tar_floor + "," + direction + ")";
	}

}
