import java.util.Vector;

public class Elevator extends Thread{
	
	/*
	 * NEW THOUGHTS:
	 * 每来一个请求，调度类从电梯类查看当前状态，判断是否捎带
	 * 三个电梯三个线程
	 * while每100ms扫描一次
	 */
	private int floor;
	private int tar_floor;
	private String direction;
	
	private boolean is_open;
	private int open_times;
	private int timer;
	private int amount;	//运动量计数器
	
	private Floor flo;
	private Vector<Request> requests;
	private int elenum;
	
	private int cur_time;
	
	public Elevator(int num){
		this.floor = 1;
		this.tar_floor = 1;
		this.direction = "STAY";
		this.open_times = 0;
		this.timer = 0;
		this.is_open = false;
		this.amount = 0;
		this.flo = new Floor();
		requests = new Vector<Request>();
		this.elenum = num+1;
		this.cur_time = -1;
	}


	public void runstay(){}
	
	
	
	public void openDoor(){
		this.is_open = true;
	}
	
	public void setTarget(int tar_floor){
		if(tar_floor > floor){
			this.direction = "UP";
		}
		else if(tar_floor < floor){
			this.direction = "DOWN";
		}
		else{
			this.direction = "STAY";
		}
		this.tar_floor = tar_floor;
	}
	
	public void search(int num){
		for(int i=0; i<requests.size(); i++){
			if(requests.get(i).getTar_floor() == num){
				System.out.println("Finished Request: #" + elenum + ": " + requests.get(i).toString());
				requests.remove(i);
				i--;
			}
		}
	}
	
	public void addRqst(Request rsq){
		requests.addElement(rsq);
	}
	
	public void setFlo(int num, boolean x) {
		flo.setFloors(num, x);
	}


	@Override
	public void run() {
		// TODO Auto-generated method stub
		while(true){
			if(timer == 0 && is_open && open_times == 0){
				System.out.println("(#" + elenum + ",#" + getFloor() + "," + getDirection() + "," + getAmount() + "," + cur_time + ")" );
			}
			if(timer == 0){
				if(is_open){
					search(floor);
					if(open_times != 59){
						this.open_times++;
					}
					else if(open_times == 59){
						this.open_times = 0;
						this.is_open = false;
						if(tar_floor > floor){
							this.direction = "UP";
						}
						else if(tar_floor < floor){
							this.direction = "DOWN";
						}
						else{
							this.direction = "STAY";
						}
					}
				}
				else if(this.direction.equals("UP")){
					this.timer++;
				}
				
				else if(this.direction.equals("DOWN")){
					this.timer++;
				}	
			}
			
			else{
				this.timer++;
				if(timer == 30){
					if(this.direction.equals("UP")){
						this.floor++;
						this.amount++;
						if(flo.getFloors(floor)){
							this.is_open = true;
							flo.setFloors(floor, false);
						}
					}
					else if(this.direction.equals("DOWN")){
						this.floor--;
						this.amount++;
							if(flo.getFloors(floor)){
								this.is_open = true;
								flo.setFloors(floor, false);
							}
					}
					this.timer = 0;
				}
				
			}
			try {
				Thread.sleep(100);
				cur_time++;
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public int getFloor() {
		return floor;
	}

	public String getDirection() {
		return direction;
	}
	
	public void setDirection(String direction) {
		this.direction = direction;
	}

	public boolean isIs_open() {
		return is_open;
	}

	public int getopen_times() {
		return open_times;
	}

	public int getTimer() {
		return timer;
	}

	public int getAmount() {
		return amount;
	}
	
	public int getTar_floor() {
		return tar_floor;
	}



}
