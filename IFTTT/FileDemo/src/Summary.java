import java.io.File;
import java.io.FileWriter;
import java.util.TimerTask;

public class Summary extends TimerTask{
	
	private static int[] times = {0,0,0,0,0};
	
	public static synchronized void Counter(int index){
		times[index]++;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		synchronized (this) {
			File writefile = new File("summary.txt");
			if(writefile.exists()){
				writefile.delete();
			}
			try {
				writefile.createNewFile();
				FileWriter filewriter = new FileWriter(writefile);
				filewriter.write("renamed: " + times[1] + " " + "modified: " + times[2] + " " + "path-changed: " + times[3] + " " + "size-changed: " + times[4]);
				filewriter.close();
				
			} catch (Exception e) {
				// TODO: handle exception
				System.out.println("Can't build summary.txt. WRITTING FAILED");
				return;
			}
		}
	}
	
}
