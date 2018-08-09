/*
 * 存放文件信息类
 */
public class FileInfo {
	private String path;
	private long last_mod;
	private String name;
	private long size;
	
	public FileInfo(String path, long last_mod, String name, long size){
		this.path = path;
		this.last_mod = last_mod;
		this.name = name;
		this.size = size;
	}
	
	@Override
	public boolean equals(Object obj) {
		// TODO Auto-generated method stub
		FileInfo cmd = (FileInfo) obj;
		if(cmd.path.equals(this.path) && cmd.last_mod == this.last_mod && cmd.name.equals(this.name)
				&& cmd.size == this.size){
			return true;
		}
		else
			return false;
		
	}
	
	public String getPath() {
		return path;
	}

	public long getLast_mod() {
		return last_mod;
	}

	public String getName() {
		return name;
	}

	public long getSize() {
		return size;
	}

}
