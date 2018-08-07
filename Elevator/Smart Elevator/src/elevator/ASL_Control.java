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

package elevator;

import java.util.ArrayList;

public class ASL_Control extends Control{
	private boolean if_main;
	private boolean if_firstrun;
	private Request mainRequest;
	private double max_time = 0;
	private int cur_floor = 1;
	
	public ASL_Control(){
		super();
		mainRequest = null;
		if_firstrun = true;
		if_main = false;
		
	}
	
	public void con(Request request){
		if(if_firstrun){
			elevator.runstay_First(request.getTar_floor());
			if_firstrun = false;
		}
		else{
			elevator.runstay(request.getTar_floor());
		}
	}
	
	public void con_Monitor(RequestQueue requestqueue){
		Request request;
		String mainDirect;
		ArrayList<Request> incidentReq = new ArrayList<Request>();
		
		requestqueue.judgeSame();
		
		while(requestqueue.getSize()!=0 || !incidentReq.isEmpty() || if_main){
			if(!if_main){
				//如果没有主请求
				mainRequest = requestqueue.sendRequest();
				if_main = true;
			}
			//寻找请求
			Floor floor = new Floor();
			
			if(mainRequest.getTime() > elevator.getTime()){
				elevator.setTime(mainRequest.getTime());
				if_firstrun = true;
			}
			//如果有一个熊孩子一直按同一层本宝宝只给他算一次哼！
			else if((mainRequest.getTime() == elevator.getTime()-0.5) && (mainRequest.getTar_floor() == elevator.getFloor())){
				elevator.setTime(elevator.getTime() + 1);
			} 
			
			if(mainRequest.getTar_floor() < elevator.getFloor()){
				mainDirect = "DOWN";
			}
			else if(mainRequest.getTar_floor() > elevator.getFloor()){
				mainDirect = "UP";
			}
			else{
				mainDirect = "STAY";
			}
			
			//Imitate Elevator
			max_time = elevator.getTime() + Math.abs(mainRequest.getTar_floor() - elevator.getFloor())*0.5;
			if(mainDirect.equals("UP")){
				cur_floor = elevator.getFloor() + 1;
			}
			else{
				cur_floor = elevator.getFloor() - 1;
			}
			
			for(double i=elevator.getTime()+0.5; i<=max_time; i+=0.5){
				if(requestqueue.getSize() == 0){
					break;
				}
				for(int j=0; j<requestqueue.getSize(); j++){
					request = requestqueue.getRequest(j);
					if(request.getTime() > i || (request.getTime()==i && request.getTar_floor()==cur_floor && !floor.getScan_floor(cur_floor))){
						//当前楼层不停
						continue;
					}
					if(request.getTime() == i && request.getTar_floor() == cur_floor && floor.getScan_floor(cur_floor)){
						//当前楼层停
						incidentReq.add(requestqueue.getRequest(j));
						requestqueue.deleteRequest(j);
						j--;
						continue;
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
					if((request.getType().equals("FR")) && (request.getDirection().equals(mainDirect)) && 
							((request.getDirection().equals("UP") && request.getTar_floor() <= mainRequest.getTar_floor() && request.getTar_floor() > cur_floor) 
									|| (request.getDirection().equals("DOWN") && request.getTar_floor() >= mainRequest.getTar_floor() && request.getTar_floor() < cur_floor))
										){	
						incidentReq.add(requestqueue.getRequest(j));
						requestqueue.deleteRequest(j);
						j--;
						floor.setScan_floor(request.getTar_floor(), true);
					}
					else if((request.getType().equals("ER")) && 
							((mainDirect.equals("UP") && request.getTar_floor() >cur_floor) 
									|| (mainDirect.equals("DOWN") && request.getTar_floor() < cur_floor))){
						incidentReq.add(requestqueue.getRequest(j));
						requestqueue.deleteRequest(j);
						j--;
						floor.setScan_floor(request.getTar_floor(), true);
						
					}
				}
				
				if(floor.getScan_floor(cur_floor)){
					i++;
					max_time++;
				}
				if(mainDirect == "DOWN"){
					cur_floor--;
				}
				else{
					cur_floor++;
				}	
			}
			
			//Print
			System.out.print(mainRequest.toString() + "(");
			for(int i=0; i<incidentReq.size(); i++){
				System.out.print(incidentReq.get(i).toString());
			}
			System.out.println(")");
			
			if(mainDirect.equals("UP")){
				cur_floor = elevator.getFloor() + 1;
			}
			else{
				cur_floor = elevator.getFloor() - 1;
			}
			
			for( ; cur_floor <= mainRequest.getTar_floor(); cur_floor++){
				for(int i=0; i<incidentReq.size(); i++){
					if(incidentReq.get(i).getTar_floor() == cur_floor){
						try{
							con(incidentReq.get(i));
							incidentReq.remove(i);
							i--;
						} catch(Exception e){
							System.out.println("Error");
						}
					}
				}
			}
			
			try{
				con(mainRequest);
			} catch(Exception e){
				System.out.println("Error");
				return;
			}
			
			if(incidentReq.isEmpty()){
				mainRequest = null;
				if_main = false;
			}
			else{
				//捎带请求变为主请求
				mainRequest = incidentReq.get(0);
				incidentReq.remove(0);
				if_main = true;
			}
			
			
		}
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
	}
	
	
}
