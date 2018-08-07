package elevator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
			BufferedReader words = new BufferedReader(new InputStreamReader(System.in));
			String[] cmd = new String[10000];
			
			RequestQueue requestqueue = new RequestQueue();
			Request request = null;
			Control control = new Control();
			
			int i = 0;
			int time = 0;
			
			do{
				try {
					cmd[i] = words.readLine();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					//e.printStackTrace();
					System.out.println(e.getMessage());
				}
				//cmd只是个暂时性的判断时候用的，如果对了再放进请求队列里
				
				if(cmd[0] == null){
					//System.out.println("Please input something~");
					return;
				}
				
				
				if(cmd[i].equals("run")){
					break;
				}
				
				
				
					cmd[i] = cmd[i].replaceAll("\\s", "");	//change all space, \t...into ""	
					String regex = "(\\(FR,\\d+,((UP)|(DOWN)),\\d+\\))|(\\(ER,\\d+,\\d+\\))";
					if(cmd[i].length()>1000){
						cmd[i].equals(cmd[i].substring(0, 500));
						System.out.println("Command list is out of space");
						continue;
					}
					try{
						if(cmd[i].length() == 0 || !cmd[i].matches(regex)){
							System.out.println("Input Error");
							continue;
						}
					} catch(Exception e){
						System.out.println("Input Illegal");
					}
					cmd[i] = cmd[i].substring(1, cmd[i].length()-1);
					String[] tmp = cmd[i].split(",");
					int t1 = 0;
					int floor = 1;
					try{
						floor = Integer.parseInt(tmp[1]);
					} catch(Exception e){
						System.out.println("Input Illegal");
						continue;
					}
					
					
					
					//判断字符串是否有效
					if(tmp[0].equals("FR")){
						if(floor>=1 && floor<=10){
							if(floor==1){
								if(tmp[2].equals("UP")){
									try{
										t1 = Integer.parseInt(tmp[3]);
									} catch(Exception e){
										System.out.println("Input Illegal");
										continue;
									}	
								}
								else{
									System.out.println("Input Illegal");
									continue;
								}
							}
							else if(floor==10){
								if(tmp[2].equals("DOWN")){
									try{
										t1 = Integer.parseInt(tmp[3]);
									} catch(Exception e){
										System.out.println("Input Illegal");
										continue;
									}
								}
								else{
									System.out.println("Input Illegal.Please input right commands.");
									continue;
								}
							}
							else{
								try{
									t1 = Integer.parseInt(tmp[3]);
								} catch(Exception e){
									System.out.println("Input Illegal");
									continue;
								}
							}
						}
						else{
							System.out.println("Input Illegal.Please input right commands.");
							continue;
						}
					}
					
					else if(tmp[0].equals("ER")){
						if(floor>=1 && floor<=10){
							try{
								t1 = Integer.parseInt(tmp[2]);
							} catch(Exception e){
								System.out.println("Input Illegal");
								continue;
							}
							
						}
						else{
							System.out.println("Input Illegal.Please input right commands.");
							continue;
						}
							
					}
					
					else{
						System.out.println("Input Illegal");
					}
					if(time > t1){
						System.out.println("Input illegal");
						return;
					}
					time = t1; 
					//把tmp,t1和floor传给Request类
					request = new Request(tmp[0],floor,tmp[2],t1);
					try {
						requestqueue.ReqList(request);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						//e.printStackTrace();
						System.out.println("The queue is full, the elevator can't deal with such numorous commands");
						break;
					}
					
				
				i++;
				if(i>1000){
					System.out.println("The command list is too long");
					break;
				}
			}while(true); //i-1 -> i
			
			for(int k=0; k<i; k++){
				control.con(requestqueue.sendRequest());
			}
			
			
			
			
			
			
			
	}

}
