
public class LightControl extends Thread{
	private TrafficLight[][] light;	//红绿灯情况
	
	public LightControl(TrafficLight[][] light){
		this.light = light;
	}
	
	public void run() {
		// TODO Auto-generated method stub
		while(true){
			try {
				sleep(300);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			for(int i=0; i<80; i++){
				for(int j=0; j<80; j++){
					if(light[i][j].getLight_NS() == 2 && light[i][j].getLight_WE() == 1){
						light[i][j].setLight_NS(1);
						light[i][j].setLight_WE(2);	
					}
					else if(light[i][j].getLight_NS() == 1 && light[i][j].getLight_WE() == 2){
						try {
							light[i][j].setLight_NS(2);
							light[i][j].setLight_WE(1);
						} catch (Exception e) {
							// TODO: handle exception
							e.printStackTrace();
						}
					}
				}
			}
			
		}
	}
	
	public boolean repOK(){
		if(light == null)
			return false;
		return true;
	}
}
