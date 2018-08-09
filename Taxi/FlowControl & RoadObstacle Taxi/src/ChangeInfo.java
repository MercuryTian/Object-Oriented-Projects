
public class ChangeInfo {
	//用来记录被删除的点和边的信息
	private Point point;
	private int edge;
	/*
	 * edge值代表哪条边被关闭(与value一致)：
	 * 1：右边
	 * 2：下边
	 */
	
	public ChangeInfo(Point point, int edge){
		/*
		 * Constructor. Used to initialize and structure the class.
		 * REQUIRES: Must provide two variables, which are the point and edge to change.
		 * MODIFIES: None.
		 * EFFECTS: Let this.point equals to the provided variable point.
		 * 			Let this.edge equals to the provided variable edge.
		 */
		this.point = point;
		this.edge = edge;
	}
	
	public Point getPoint() {
		/*
		 * Used to get the value of point.
		 * REQUIRES: None.
		 * MODIFIES: None.
		 * EFFECTS: Return point.
		 */
		return point;
	}

	public int getEdge() {
		/*
		 * Used to get the value of edge.
		 * REQUIRES: None.
		 * MODIFIES: None.
		 * EFFECTS: Return edge.
		 */
		return edge;
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
		ChangeInfo c = (ChangeInfo) obj;
		if(c.getPoint().equals(this.point) && c.getEdge()==this.getEdge()){
			return true;
		}
	return false;
	}

}
