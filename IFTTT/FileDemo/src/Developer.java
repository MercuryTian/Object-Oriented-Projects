import java.io.File;
import java.io.FileOutputStream;

public class Developer implements Runnable{

	public synchronized String getName(File file){
		return file.getName();
	}
	
	public synchronized long getLastModified(File file){
		return file.lastModified();	
	}
	
	public synchronized String getPath(File file){
		return file.getAbsolutePath();
	}
	
	public synchronized long getSize(File file){
		return file.length();
	}
	
	public synchronized void Renamed(File old, String name){
		if(!old.exists() || old.isDirectory()){
			System.out.println("FILE DOSE NOT EXIST: " + old);
			return;
		}
		File newFile = new File(name);
		if(old.renameTo(newFile)){
			System.out.println("FILE HAS BEEN RENAMED");
		}
		else{
			System.out.println("RENAMING FAILED");
		}
	}
	
	public synchronized void Move(File file, String path){
		File check = new File(path);
		if(!check.exists()){
			System.out.println("PATH IS NOT A DIRECTORY");
			return;
		}
		File tofile = new File(path + file.getName());
		if(file.renameTo(tofile)){
			System.out.println("MOVE PASSED");
		}
		else{
			System.out.println("MOVE FAILED");
		}	
	}
	
	public synchronized void Add(String path){
		File newfile = new File(path);
		if(newfile.exists()){
			System.out.println("FILE EXISTS");
		}
		else{
			try {
				newfile.createNewFile();
			} catch (Exception e) {
				// TODO: handle exception
				System.out.println(e.getMessage());
			}
		}
	}
	
	public synchronized void Remove(String path){
		File newfile = new File(path);
		if(!newfile.exists()){
			System.out.println("FILE DOES NOT EXIST");
		}
		else{
			newfile.delete();
		}
	}
	
	public synchronized void Write(File file){
		String content = "HAPPY";
		try(FileOutputStream fresh = new FileOutputStream(file,true)) {
			if(!file.exists()){
				System.out.println("FILE DOES NOT EXIST");
				return;
			}
			byte[] contentinBytes = content.getBytes();
			fresh.write(contentinBytes);
			fresh.flush();
			fresh.close();
			System.out.println("FINISHED");
			
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println(e.getMessage());
		}
	}
	

	@Override
	public void run() {
		// TODO Auto-generated method stub
//--------------------------Please Insert Commands Here----------------------------------
	
		
//------------------------------------The End--------------------------------------------	
	}
	
	

//	File file = new File("/Users/macbook/Desktop/test/cc.txt");
//	Renamed(file,"/Users/macbook/Desktop/test/ccplus.txt");
	
//	File file = new File("/Users/macbook/Desktop/test/hi.txt");
//	//Add("/Users/macbook/Desktop/test/hi.txt");
//	Write(file);
//	try {
//		Thread.sleep(1500);
//	} catch (InterruptedException e1) {
//		// TODO Auto-generated catch block
//		e1.printStackTrace();
//	}
//	Write(file);
//	try {
//		Thread.sleep(1500);
//	} catch (InterruptedException e1) {
//		// TODO Auto-generated catch block
//		e1.printStackTrace();
//	}
//	synchronized (this) {
//		try {
//			Thread.sleep(2000);
//		} catch (Exception e) {
//			// TODO: handle exception
//			e.printStackTrace();
//		}
//	}

}
