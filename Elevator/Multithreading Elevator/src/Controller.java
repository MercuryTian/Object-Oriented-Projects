import java.util.TimerTask;

public class Controller extends TimerTask{
	
	private Elevator[] ele;
	private int cur_time;
	
	public Controller(){
		super();
		ele = new Elevator[3];
		for(int i=0; i<3; i++){
			ele[i] = new Elevator(i);
			ele[i].start();
		}
		cur_time = -1;
	}
	
	
	public int FindChoice(Request request, int amount){
		/*
		 * 顺路捎带请求：
		 * 1.(电梯当前的运动状态是UP: 当前楼层<目标楼层<=10) || (电梯当前运动状态是DOWN: 1<=目标楼层<当前楼层);
		 * 2.对于任意的FR类型请求，如果是当前运动状态下的顺路请求，则：
		 * 	 (外部请求楼层方向=当前运动状态) && ((①) || (②))
		 * 		①: 外部请求楼层方向是UP: (外部请求楼层<=目标楼层) && (外部请求楼层>当前楼层)
		 * 		②: 外部请求楼层方向是DOWN：(外部请求楼层>=目标楼层) && (外部请求楼层<当前楼层)
		 * 3.对于任意的ER类型请求，如果是当前运动状态下的顺路请求，则：
		 * 	 (当前状态是UP: 请求楼层>当前楼层) || (当前状态是DOWN: 请求楼层<当前楼层)
		 */
		if(ele[2].getAmount() < amount && request.getDirection().equals(ele[2].getDirection()) && 
				   ((request.getDirection().equals("UP") && request.getTar_floor() <= ele[2].getTar_floor() && request.getTar_floor() > ele[2].getFloor()) 
					|| (request.getDirection().equals("DOWN") && request.getTar_floor() >= ele[2].getTar_floor() && request.getTar_floor() > ele[2].getFloor())) ){
			amount = ele[2].getAmount();
			return 3;
		}
		if(ele[1].getAmount() < amount && request.getDirection().equals(ele[1].getDirection()) && 
				   ((request.getDirection().equals("UP") && request.getTar_floor() <= ele[1].getTar_floor() && request.getTar_floor() > ele[1].getFloor()) 
					|| (request.getDirection().equals("DOWN") && request.getTar_floor() >= ele[1].getTar_floor() && request.getTar_floor() > ele[1].getFloor())) ){
			amount = ele[1].getAmount();
			return 2;
		}
		if(request.getDirection().equals(ele[0].getDirection()) && 
		   ((request.getDirection().equals("UP") && request.getTar_floor() <= ele[0].getTar_floor() && request.getTar_floor() > ele[0].getFloor()) 
			|| (request.getDirection().equals("DOWN") && request.getTar_floor() >= ele[0].getTar_floor() && request.getTar_floor() > ele[0].getFloor())) ){
			amount = ele[0].getAmount();
			return 1;
		}		
		return 0;
	}
	
	public void StayRun(Elevator ele, int num){
		for(int i=0; i<RequestList.getSize(); i++){
			Request request = RequestList.getRequest(i);
			if(request.getType().equals("FR")){
				if(request.getTar_floor() == ele.getFloor()){
					ele.addRqst(request);
					ele.openDoor();
					RequestList.deletRequest(i);
					i--;
					break;
				}
				else{
					ele.addRqst(request);
					ele.setFlo(request.getTar_floor(), true);
					ele.setTarget(request.getTar_floor());
					RequestList.deletRequest(i);
					i--;
					break;
				}
			}
			else if(request.getType().equals("ER") && request.getNumber() == num){
				if(request.getTar_floor() == ele.getFloor()){
					ele.addRqst(request);
					ele.openDoor();
					RequestList.deletRequest(i);
					i--;
					break;
				}
				else{
					ele.addRqst(request);
					ele.setFlo(request.getTar_floor(), true);
					ele.setTarget(request.getTar_floor());
					RequestList.deletRequest(i);
					i--;
					break;
				}
			}	
		}
	}
	
	@Override
	public void run() {
		cur_time +=1;
//		boolean[] r = {false, false, false};
//		if(ele[0].getTimer() != 0)
//			r[0] = true;
//		if(ele[1].getTimer() != 0)
//			r[1] = true;
//		if(ele[2].getTimer() != 0)
//			r[2] = true;
//		
//		Print(r);
		
				for(int i=0; i<RequestList.getSize(); i++){
					Request request = RequestList.getRequest(i);
					int choice = 0;
					int amount = 10000000;
					if(request.getType().equals("FR")){
						//判断大捎带情况，根据运动量分配电梯
						choice = FindChoice(request, amount);	
						if(choice != 0){
							switch(choice){
							case 3:
							{
								if(ele[2].getDirection().equals("UP") && request.getTar_floor() > ele[2].getTar_floor()){
									ele[2].setTarget(request.getTar_floor());
								}
								else if(ele[2].getDirection().equals("DOWN") && request.getTar_floor() < ele[2].getTar_floor()){
									ele[2].setTarget(request.getTar_floor());
								}
								ele[2].addRqst(request);
								ele[2].setFlo(request.getTar_floor(), true);
								RequestList.deletRequest(i);
								i--;
								break;
							}
							case 2:
							{
								if(ele[1].getDirection().equals("UP") && request.getTar_floor() > ele[1].getTar_floor()){
									ele[1].setTarget(request.getTar_floor());
								}
								else if(ele[1].getDirection().equals("DOWN") && request.getTar_floor() < ele[1].getTar_floor()){
									ele[1].setTarget(request.getTar_floor());
								}
								ele[1].addRqst(request);
								ele[1].setFlo(request.getTar_floor(), true);
								RequestList.deletRequest(i);
								i--;
								break;
							}
							case 1:
							{
								if(ele[0].getDirection().equals("UP") && request.getTar_floor() > ele[0].getTar_floor()){
									ele[0].setTarget(request.getTar_floor());
								}
								else if(ele[0].getDirection().equals("DOWN") && request.getTar_floor() < ele[0].getTar_floor()){
									ele[0].setTarget(request.getTar_floor());
								}
								ele[0].addRqst(request); //把请求分配给电梯
								ele[0].setFlo(request.getTar_floor(), true);
								RequestList.deletRequest(i);
								i--;
								break;
							}
							}
						}
						
					}
		
					/*
					 * 顺路捎带请求：
					 * 1.(电梯当前的运动状态是UP: 当前楼层<目标楼层<=10) || (电梯当前运动状态是DOWN: 1<=目标楼层<当前楼层);
					 * 2.对于任意的FR类型请求，如果是当前运动状态下的顺路请求，则：
					 * 	 (外部请求楼层方向=当前运动状态) && ((①) || (②))
					 * 		①: 外部请求楼层方向是UP: (外部请求楼层<=目标楼层) && (外部请求楼层>当前楼层)
					 * 		②: 外部请求楼层方向是DOWN：(外部请求楼层>=目标楼层) && (外部请求楼层<当前楼层)
					 * 3.对于任意的ER类型请求，如果是当前运动状态下的顺路请求，则：
					 * 	 (当前状态是UP: 请求楼层>当前楼层) || (当前状态是DOWN: 请求楼层<当前楼层)
					 */
					else if(request.getType().equals("ER")){
						int elenum = request.getNumber();
						switch(elenum){
						case 3:
						{
							if((ele[2].getDirection().equals("UP") && request.getTar_floor() > ele[2].getFloor())
									|| (ele[2].getDirection().equals("DOWN") && request.getTar_floor() < ele[2].getFloor())){
									if(ele[2].getDirection().equals("UP") && request.getTar_floor() > ele[2].getTar_floor()){
										ele[2].setTarget(request.getTar_floor());
									}
									else if(ele[2].getDirection().equals("DOWN") && request.getTar_floor() < ele[2].getTar_floor()){
										ele[2].setTarget(request.getTar_floor());
									}
									ele[2].addRqst(request);
									ele[2].setFlo(request.getTar_floor(), true);
									RequestList.deletRequest(i);
									i--;
								}
								break;
						}
						case 2:
						{
							if((ele[1].getDirection().equals("UP") && request.getTar_floor() > ele[1].getFloor())
									|| (ele[1].getDirection().equals("DOWN") && request.getTar_floor() < ele[1].getFloor())){
									if(ele[1].getDirection().equals("UP") && request.getTar_floor() > ele[1].getTar_floor()){
										ele[1].setTarget(request.getTar_floor());
									}
									else if(ele[1].getDirection().equals("DOWN") && request.getTar_floor() < ele[1].getTar_floor()){
										ele[1].setTarget(request.getTar_floor());
									}
									ele[1].addRqst(request);
									ele[1].setFlo(request.getTar_floor(), true);
									RequestList.deletRequest(i);
									i--;
								}
								break;
						}
						case 1:
						{
							if((ele[0].getDirection().equals("UP") && request.getTar_floor() > ele[0].getFloor())
								|| (ele[0].getDirection().equals("DOWN") && request.getTar_floor() < ele[0].getFloor())){
								if(ele[0].getDirection().equals("UP") && request.getTar_floor() > ele[0].getTar_floor()){
									ele[0].setTarget(request.getTar_floor());
								}
								else if(ele[0].getDirection().equals("DOWN") && request.getTar_floor() < ele[0].getTar_floor()){
									ele[0].setTarget(request.getTar_floor());
								}
								ele[0].addRqst(request);
								ele[0].setFlo(request.getTar_floor(), true);
								RequestList.deletRequest(i);
								i--;
							}
							break;
						}
						default:
							break;
						}
					}			
				}
				
		//如果电梯是空闲状态
		for(int k=0; k<3; k++){
			if(ele[k].getDirection().equals("STAY")){
				StayRun(ele[k], k+1);
			}
		}
	}
		
	
}
