
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
		this.x = x;
		this.y = y;
	}
	
	@Override
	public boolean equals(Object obj) {
		// TODO Auto-generated method stub
		Point p = (Point) obj;
			if(p.getX()==this.x && p.getY()==this.y){
				return true;
			}
		return false;
	}
	
	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}
	
	public void setX(int x) {
		this.x = x;
	}

	public void setY(int y) {
		this.y = y;
	}
	
	public boolean getConnect(int i) {
		return connect[i];
	}

	public void setConnect(int i) {
		this.connect[i] = true;
	}
	
	public int getValue() {
		return value;
	}
	

}
