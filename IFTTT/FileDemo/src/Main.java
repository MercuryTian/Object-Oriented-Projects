import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.Timer;

public class Main {
	
	public static void main(String[] args) {
		BufferedReader words = new BufferedReader(new InputStreamReader(System.in));
		String[] cmd = new String[8];
		Timer[] timer = new Timer[8];
		Monitor[] monitor = new Monitor[8];
		
		int n = 0;
		int t = 0;
		try {
			do {
				try {
					cmd[n] = words.readLine();
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
					return;
				}
				if(cmd[n] == null){
					System.out.println("Please input commands");
					return;
				}
				n++;
				if(n == 8){
					System.out.println("Command list is full");
					break;
				}
			} while (!cmd[n-1].equals("start"));	//输入start开始执行程序
			
			for(int i=0; i<n-1; i++){
				String tmp = cmd[i];
				String[] cmd_split = tmp.split("\\s");
				String trigger = null;
				String task = null;
				if(cmd_split.length != 5 || !cmd_split[0].equals("IF") || !cmd_split[3].equals("THEN")){
					System.out.println("Input Illegal");
					continue;
				}
				File file = new File(cmd_split[1]);
				if(cmd_split[2].equals("renamed") && file.isFile() && file.exists()){
					trigger = "renamed";
				}
				else if(cmd_split[2].equals("modified")){
					trigger = "modified";
				}
				else if(cmd_split[2].equals("pathchanged") && file.isFile() && file.exists()){
					trigger = "pathchanged";
				}
				else if(cmd_split[2].equals("sizechanged")){
					trigger = "sizechanged";
				}
				else{
					System.out.println("Input Illegal in Wrong Format");
					continue;
				}
				
				if(cmd_split[4].equals("summary")){
					task = "summary";
				}
				else if(cmd_split[4].equals("detail")){
					task = "detail";
				}
				else if(cmd_split[4].equals("recover")){
					task = "recover";
				}
				else{
					System.out.println("Input Illegal in Wrong Format");
					continue;
				}
				if(!file.exists()){
					System.out.println("File or Directory doesnt exists");
					continue;
				}
				monitor[t] = new Monitor(file,trigger,task);
				timer[t] = new Timer();
				t++;
			}
			
			for(int j=0; j<t; j++){
				timer[j].schedule(monitor[j], 0, 1000);
			}
			
			Timer timer_summary = new Timer(true);
			timer_summary.schedule(new Summary(), 0, 3000);
			Timer timer_detail = new Timer(true);
			timer_detail.schedule(new Detail(), 0, 3000);
			
			
			Thread rundemo = new Thread(new Developer());
			rundemo.run();
	
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			System.out.println("Error.Please double check your commands");
		}
		
	}
}
