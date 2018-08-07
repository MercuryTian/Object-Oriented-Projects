/*
 * 调度器：
 * 属性：
 * 1.当前指令
 * 2.电梯的楼层
 * 3.时间
 * 4.电梯运行方向
 */

package elevator;

public class Control {
	protected Elevator elevator = new Elevator();
	
	public Control(){
	} 
	
	public void con(Request request){
		if(request.getTime() > elevator.getTime()){
			elevator.setTime(request.getTime());
		}
		if(request.getType().equals("FR")){
			elevator.runstay(request.getTar_floor());
		}
		else{
			elevator.runstay(request.getTar_floor());
		}		
	}	
}


















