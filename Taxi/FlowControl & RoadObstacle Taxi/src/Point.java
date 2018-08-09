
public class Point {
//存放顶点信息，并判断它和上下左右是否联通
	private int x;
	private int y;
	private int value;
	private Boolean[] connect;
	/*
	 * connect值表示该顶点的联通情况：
	 * 0 true：上联通
	 * 1 true：下联通
	 * 2 true: 左联通
	 * 3 true: 右联通
	 */

	public Point(int x, int y, int value){
		/*
		 * Constructor. Used to initialize and structure a point.
		 * REQUIRES: Must provide three variables, which are the horizontal, the vertical ordinate and the number on map.
		 * MODIFIES: None.
		 * EFFECTS: Let this.x equals to provided x.
		 * 			Let this.y equals to provided y.
		 * 			Let this.value equals to provided value.
		 * 			Initialize the connected relations
		 */
		this.x = x;
		this.y = y;
		this.value = value;
		this.connect = new Boolean[] {false,false,false,false};
		//Point father;
		if(value == 2 || value == 3){
			connect[1] = true;
		}
		if(value == 1 || value == 3){
			connect[3] = true;
		}
		//对于左和上联通只能通过在外部其父节点来判断
	}
	
	public Point(int x, int y){
		/*
		 * Constructor. Used to initialize and structure a point.
		 * REQUIRES: Must provide three variables, which are the horizontal and the vertical ordinate.
		 * MODIFIES: None.
		 * EFFECTS: Let this.x equals to provided x.
		 * 			Let this.y equals to provided y.
		 */
		this.x = x;
		this.y = y;
	}
	
	@Override
	public boolean equals(Object obj) {
		/*
		 * Override the method of equal
		 * REQUIRES: Must provide a variable.
		 * MODIFIES: None.
		 * EFFECTS: If the provided variable equals to the object, then return true. Else, return false.
		 */
		// TODO Auto-generated method stub
		Point p = (Point) obj;
			if(p.getX()==this.x && p.getY()==this.y){
				return true;
			}
		return false;
	}
	
	public int getX() {
		/*
		 * Get the value of y.
		 * REQUIRES: None.
		 * MODIFIES: None.
		 * EFFECTS: Return x.
		 */
		return x;
	}

	public int getY() {
		/*
		 * Get the value of y.
		 * REQUIRES: None.
		 * MODIFIES: None.
		 * EFFECTS: Return y.
		 */
		return y;
	}
	
	public void setX(int x) {
		/*
		 * Set the value of x.
		 * REQUIRES: Must provide a variable, x.
		 * MODIFIES: None.
		 * EFFECTS: Set this.x equals to the provided variable.
		 */
		this.x = x;
	}

	public void setY(int y) {
		/*
		 * Set the value of y.
		 * REQUIRES: Must provide a variable, y.
		 * MODIFIES: None.
		 * EFFECTS: Set this.y equals to the provided variable.
		 */
		this.y = y;
	}
	
	public boolean getConnect(int i) {
		/*
		 * Get the connected information of different direction.
		 * REQUIRES: Must provide a variable which standards the direction.
		 * MODIFIES: None.
		 * EFFECTS: Return the information of provided direction.
		 */
		return connect[i];
	}

	public void setConnect(int i) {
		/*
		 * Set the connected information of different direction.
		 * REQUIRES: Must provide a variable which standards the direction.
		 * MODIFIES: None.
		 * EFFECTS: If the direction is connected, then set the value as true.
		 */
		this.connect[i] = true;
	}
	
	public void closeConnect(int i){
		/*
		 * Set the connected information of different direction.
		 * REQUIRES: Must provide a variable which standards the direction.
		 * MODIFIES: None.
		 * EFFECTS: If the direction is not connected, then set the value as false.
		 */
		this.connect[i] = false;
	}
	
	public int getValue() {
		/*
		 * Get the value on map.
		 * REQUIRES: None.
		 * MODIFIES: None.
		 * EFFECTS: Return value.
		 */
		return value;
	}
	
	public void setValue(int value) {
		/*
		 * Set the value on map.
		 * REQUIRES: Must provide a variable.
		 * MODIFIES: None.
		 * EFFECTS: Set this.value equals to the provided variable.
		 */
		this.value = value;
	}


}
