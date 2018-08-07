import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class RequestMonitor implements Runnable{

	private final long start_time;
	
	public RequestMonitor(){
		//取相对时间计算，第一个请求输入的时间为初始时间
		this.start_time = System.currentTimeMillis();
	}
	
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		BufferedReader words = new BufferedReader(new InputStreamReader(System.in));
		String[] cmd = new String[10000];
	
		Request req = null;
		
		int i = 0;
		int time = 0;
		int number = 1; 	//电梯号
		try {
			do {
				try {
					cmd[i] = words.readLine();
				} catch (IOException e) {
					// TODO: handle exception
					System.out.println(e.getMessage());
				}
				
				if(cmd[0] == null){
					return;
				}
				//如果输入stop，电梯停止运行
				if(cmd[i].equals("stop")){
					break;
				}
				
				cmd[i] = cmd[i].replaceAll("\\s", "");
				String regex = "(\\(FR,\\d+,((UP)|(DOWN))\\))|(\\(ER,\\#\\d+,\\d+\\))";
				if(cmd[i].length() > 1000){
					cmd[i].equals(cmd[i].substring(0, 500));
					System.out.println("Command list is full");
					continue;
				}
				
				try {
					if(cmd[i].length() == 0 || !cmd[i].matches(regex)){
						System.out.println("Input Error");
						continue;
					}
				} catch (Exception e) {
					// TODO: handle exception
					System.out.println("Input Illegal");
				}
				
				cmd[i] = cmd[i].substring(1, cmd[i].length()-1);
				String[] tmp = cmd[i].split(",");
				int t1 = 0;
				int floor = 1;
				
				//判断字符串是否有效
				if(tmp[0].equals("FR")){
					try {
						floor = Integer.parseInt(tmp[1]);
					} catch (Exception e) {
						// TODO: handle exception
						System.out.println("Input Illegal");
						continue;
					}
					if(floor>=1 && floor<=20){
						if(floor == 1){
							if(tmp[2].equals("UP")){
								try {
									t1 = (int) ((System.currentTimeMillis() - start_time)/100);
									//请求相对时间 = 输入时间 - 初始时间
								} catch (Exception e) {
									// TODO: handle exception
									System.out.println("Input Illegal");
									continue;
								}
							}
							else{
								System.out.println("Input Illegal");
								continue;
							}
						}
						else if(floor == 20){
							if(tmp[2].equals("DOWN")){
								try {
									t1 = (int) ((System.currentTimeMillis() - start_time)/100);
								} catch (Exception e) {
									// TODO: handle exception
									System.out.println("Input Illegal");
									continue;
								}
							}
							else{
								System.out.println("Input Illegal. Please input right commands");
								continue;
							}
						}
						else{
							try {
								t1 = (int) ((System.currentTimeMillis() - start_time)/100);
							} catch (Exception e) {
								// TODO: handle exception
								System.out.println("Input Illegal");
								continue;
							}
						}	
					}
					else{
						System.out.println("Input Illegal. Please input right commands");
						continue;
					}
					
					time = t1;
					//把tmp,t1和floor传给Request类
					req = new Request(tmp[0],floor,tmp[2],time);
					try {
						RequestList.ReqList(req);
					} catch (Exception e) {
						// TODO: handle exception
						System.out.println("The queue is full, the elevator can't deal with such numorous commands");
						break;
					}
					
				}
				

				else if(tmp[0].equals("ER")){
					try {
						floor = Integer.parseInt(tmp[2]);
						number = Integer.parseInt(tmp[1].substring(1,tmp[1].length())); 	//不能直接转换成整数，因为前面有#
					} catch (Exception e) {
						// TODO: handle exception
						System.out.println("Input Illegal");
						continue;
					}
					if(floor>=1 && floor<=20){
						try {
							t1 = (int) ((System.currentTimeMillis() - start_time)/100);
						} catch (Exception e) {
							// TODO: handle exception
							System.out.println("Input Illegal");
							continue;
						}
					}
					else{
						System.out.println("Input Illegal");
						continue;
					}
					if(number<1 || number>3){
						System.out.println("Inpput Illegal");
						continue;
					}
					time = t1;
					//把tmp,t1和floor传给Request类
					req = new Request(tmp[0],number,floor,time);
					try {
						RequestList.ReqList(req);
					} catch (Exception e) {
						// TODO: handle exception
						System.out.println("The queue is full, the elevator can't deal with such numorous commands");
						break;
					}
					
				}
		
				else{
					System.out.println("Input Illegal");
				}
				
				
				i++;
				if(i>1000){
					System.out.println("The command list is too long");
					break;
				}
			} while (true);
			
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("Input Error");
		}
		
		
		
		
	}


	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
