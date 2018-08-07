
public class Floor {
	
	private boolean[] floors;
	
	public boolean getFloors(int i) {
		return floors[i];
	}

	public void setFloors(int i, boolean x) {
		floors[i] = x;
	}

	public Floor(){
		floors = new boolean[21];
		for(int i=0; i<floors.length; i++){
			floors[i] = false;
		}
	}
	
	

}
