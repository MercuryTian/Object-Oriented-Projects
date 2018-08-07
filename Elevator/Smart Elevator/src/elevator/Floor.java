/*
 * 楼层类：
 * 属性：
 * 1.楼层数：1-10层（1层只有UP，10层只有DOWN）
 */

package elevator;

public class Floor {
	private boolean[] scan_floor;
	
	public boolean getScan_floor(int i) {
		return scan_floor[i];
	}

	public void setScan_floor(int i, boolean rather) {
		this.scan_floor[i] = rather;
	}

	public Floor(){
		scan_floor = new boolean[11];
		for(int i=0; i<scan_floor.length; i++){
			scan_floor[i] = false;
		}
	}
	
	

}
