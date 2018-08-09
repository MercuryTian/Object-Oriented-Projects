import java.io.File;
import java.io.FileWriter;
import java.util.TimerTask;
import java.util.Vector;

public class Detail extends TimerTask{
	
	private static Vector<String> infor = new Vector<String>();
	
	public static synchronized void Add(String det){
		infor.addElement(det);
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		synchronized (this) {
			File writefile = new File("detail.txt");
			if(writefile.exists()){
				writefile.delete();
			}
			try {
				writefile.createNewFile();
				FileWriter filewriter = new FileWriter(writefile);
				for(int i=0; i<infor.size(); i++){
					filewriter.write(infor.get(i) + "\n");
					filewriter.flush();
				}
				filewriter.close();
				
			} catch (Exception e) {
				// TODO: handle exception
				System.out.println("Can't build detail.txt. WRITTING FAILED");
				return;
			}
		}
	}

}
