import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.TimerTask;

public class Monitor extends TimerTask{
	//如果监视的是文件
	private boolean is_file;
	private boolean is_delete;
	private boolean is_add;
	private FileInfo file_wp;	//跟踪文件
	private File WorkPlace;		//工作区
	private String trigger;
	private String task;
	private File Found_Reserve;
	
	//如果监视的是文件夹
	//记录新文件+旧文件: 路径+文件信息
	Map<String,FileInfo> oldMap;// = new HashMap<String,FileInfo>();
	Map<String,FileInfo> newMap;// = new HashMap<String,FileInfo>();
	//记录新文件夹+旧文件夹: 路径+文件夹
	Map<String,File> oldMap_Folder;// = new HashMap<String,String>();
	Map<String,File> newMap_Folder;// = new HashMap<String,String>();
	/*
	 * oldMap：
	 * 1.如果一个文件被delete，那它一定在旧的HashMap的KeySet里面
	 * 2.如果一个文件被rename，那它先出现在旧的HashMap的KeySet里面，然后出现在新的里面
	 * 这样，对于删除和重命名操作有了不同于一个HashMap时的区分: 在valueSet里面的值不一样
	 * newMap:
	 * 1.如果一个新的文件被创建，那它一定在新的HashMap的KeySet里面
	 */
	
//	Set<String> oldKeySet = oldMap.keySet();
//	Set<String> newKeySet = newMap.keySet();
//	
//	Set<String> deleteSet = new HashSet<String>();
//	Set<String> createSet = new HashSet<String>();
	
	public Monitor(File file, String trigger, String task){
		this.trigger = trigger;
		this.task = task;
		if(file.isFile()){		//如果是文件
			is_file = true;
			file_wp = new FileInfo(file.getAbsolutePath(),file.lastModified(),file.getName(),file.length());
			WorkPlace = new File(file.getParent()); 
		}
		else{					//如果是文件夹
			is_file = false;
			WorkPlace = new File(file.getAbsolutePath());
			newMap = new HashMap<String,FileInfo>();
			newMap_Folder = new HashMap<String,File>();
			Reccur(file);
			oldMap = newMap;
			oldMap_Folder = newMap_Folder;
		}
	}
	
	/*
	 * 递归遍历：
	 * if(里面还有文件夹)
	 *    继续访问看有没有文件夹;
	 * else if(没有文件夹)
	 * 	  那就把里面的文件都访问一遍;
	 * 比较old和new有什么区别
	 * 
	 */
	public synchronized void Reccur(File wp){
		File[] f = wp.listFiles();
		for(int i=0; i<f.length; i++){
			if(f[i].isDirectory()){
				newMap_Folder.put(f[i].getAbsolutePath(), f[i]);
				Reccur(f[i]);
			}
			else if(f[i].isFile()){
				FileInfo infor = new FileInfo(f[i].getAbsolutePath(),f[i].lastModified(),f[i].getName(),f[i].length());
				newMap.put(f[i].getAbsolutePath(), infor);
			}
		}
	}
	
	public synchronized int File_Inspect(File file){
		file = new File(file_wp.getPath());
		if(file.exists()){
			//如果文件存在：时间 or 大小(修改，新增)
			//判断上次是否存在
			if(is_delete){
				is_delete = false;
				return 4;	//文件恢复新建(size-changed)，返回4
			}
			else{
				if(file.getAbsolutePath().equals(file_wp.getPath()) && file.getName().equals(file_wp.getName())
						&& file.length() == file_wp.getSize() && file.lastModified() == file.lastModified()){
					return 0;	//没有变化，返回0
				}
				else if((file.lastModified() != file_wp.getLast_mod()) && file.getAbsolutePath().equals(file_wp.getPath())
						&& file.length() == file_wp.getSize() && file.getName().equals(file_wp.getName())){
					return 2;	//修改时间(modified)，返回2
				}
				else if(file.getAbsolutePath().equals(file_wp.getPath()) && file.getName().equals(file_wp.getName())
						&& file.length() != file_wp.getSize() && file.lastModified() == file.lastModified()){
					if(trigger.equals("modified"))
						return 2;
					else
						return 4;	//修改文件大小(size-changed)，返回4
				}
			}
			
		}
		//如果文件不存在：删除 or 重命名 or 修改路径
		else{
			//先在当前目录下查找，看是否存在
			SearchUsing s;
			int sort;
			File ParentFile = new File(WorkPlace.getPath());
//			FileInfo fileInformation = new FileInfo(file.getAbsolutePath(),file.lastModified(),file.getName(),file.length());
			s = Search_File(ParentFile, file_wp, Found_Reserve);
			sort = s.found;
//			System.out.println(sort);
			Found_Reserve = s.found_file;
			if(sort == 2){
				is_delete = false;
				return 3;	//修改路径(path-changed)，返回3
			}
			else if(sort == -1){
				is_delete = true;
				return 4;	//删除文件(size-changed)，返回4
			}
			else if(sort == 1){
				is_delete = false;
				return 1;	//重命名(renamed)，返回1
			}
		}
		return 0;
		
	}
	
//	//对文件夹进行比较操作
//	public synchronized int Folder_Inspect(){
//		return 0;
//	}
	
	/*
	 * 搜索：
	 * -1: 没有找到文件
	 * 1：重命名
	 * 2：路径改变
	 */
	
	class SearchUsing
	{
		public File found_file;
		public int found;
		
		public SearchUsing(File found_file, int found){
			this.found_file = found_file;
			this.found = found;
		}
	}
	public synchronized SearchUsing Search_File(File directory, FileInfo wanted_file, File found_file){
		SearchUsing f = new SearchUsing(found_file, -1);
		//int found = -1;
		File[] catalog = directory.listFiles();
		for(int i=0; i<catalog.length; i++){
			if(catalog[i].isFile()){
				//重命名
				File a = new File(wanted_file.getPath());
				if(catalog[i].getParent().equals(a.getParent()) && !catalog[i].getName().equals(wanted_file.getName())
						&& catalog[i].length() == wanted_file.getSize() && catalog[i].lastModified() == catalog[i].lastModified()){
					f.found_file = catalog[i];
					f.found = 1;
					return f;
				}
				//路径改变
				else if(!catalog[i].getAbsolutePath().equals(wanted_file.getPath()) && catalog[i].getName().equals(wanted_file.getName())
						&& catalog[i].length() == wanted_file.getSize() && catalog[i].lastModified() == catalog[i].lastModified()){
					f.found_file = catalog[i];
					f.found = 2;
					return f;
				}
			}
			else if(catalog[i].isDirectory()){
				f = Search_File(catalog[i], wanted_file, found_file);
				if(f.found != -1){
					return f;
				}
			}
		}
		return f;
	}
	

	@Override
	public void run() {
		// TODO Auto-generated method stub
		//System.out.println(file_wp.getPath());
		
		int trigger_type = 0;
		synchronized (this) {
			try {
				//如果是文件
				if(is_file){
					File TrackedFile = new File(file_wp.getPath());
					trigger_type = File_Inspect(TrackedFile);
					//System.out.println(trigger_type + "," + task);
					switch(trigger_type){
						case 1:
						{	
							if(trigger.equals("renamed")){
								if(task.equals("summary")){
									Summary.Counter(1);
									//System.out.println(trigger_type + "," + task);
								}
								else if(task.equals("detail")){
									String str = "Trigger: renamed " + file_wp.getName() + " to " + Found_Reserve.getName();
									Detail.Add(str);
									//System.out.println(trigger_type + "," + task);
								}
								else if(task.equals("recover")){
									Found_Reserve.renameTo(new File(TrackedFile.getAbsolutePath()));
									//System.out.println(trigger_type + "," + task);
								}
							}
							break;
						}
						case 2:
						{
							if(trigger.equals("modified")){
								if(task.equals("summary")){
									Summary.Counter(2);
									//System.out.println(trigger_type + "," + task);
								}
								else if(task.equals("detail")){
									String str = "Trigger: modified " + file_wp.getSize() + " to " + TrackedFile.length();
									Detail.Add(str);
									//System.out.println(trigger_type + "," + task);
								}
							}
							break;
						}
						case 3:
						{
							if(trigger.equals("pathchanged")){
								if(task.equals("summary")){
									Summary.Counter(3);
									//System.out.println(trigger_type + "," + task);
								}
								else if(task.equals("detail")){
									String str = "Trigger: pathchanged " + file_wp.getPath() + " to " + Found_Reserve.getAbsolutePath();
									Detail.Add(str);
									//System.out.println(trigger_type + "," + task);
								}
								else if(task.equals("recover")){
									Found_Reserve.renameTo(new File(TrackedFile.getAbsolutePath()));
									//System.out.println(trigger_type + "," + task);
								}
							}
							break;
						}
						case 4:
						{
							if(trigger.equals("sizechanged")){
								if(task.equals("summary")){
									Summary.Counter(4);
									//System.out.println(trigger_type + "," + task);
								}
								else if(task.equals("detail")){
									String str = "Trigger: sizechanged " + file_wp.getSize() + " to " + TrackedFile.length();
									Detail.Add(str);
									//System.out.println(trigger_type + "," + task);
								}
							}
							break;
						}
						default:
							break;
					}
					
					if((trigger.equals("renamed") || trigger.equals("pathchanged")) && (trigger_type == 1 || trigger_type == 3)){
						file_wp = new FileInfo(Found_Reserve.getAbsolutePath(), Found_Reserve.lastModified(), Found_Reserve.getName(), Found_Reserve.length());
					}
					else if((trigger.equals("modified") || trigger.equals("sizechanged")) && (trigger_type == 2 || trigger_type == 4)){
						file_wp = new FileInfo(TrackedFile.getAbsolutePath(), TrackedFile.lastModified(), TrackedFile.getName(), TrackedFile.length());
					}
				}
				//如果是目录
				else{
					newMap = new HashMap<String, FileInfo>();
					newMap_Folder = new HashMap<String, File>();
					Reccur(WorkPlace);
					
					File record_Direcory = null;
					FileInfo record_File = null;
					//File record_Directory_new = null;
					FileInfo record_File_new = null;
					
					
					int newMap_Folder_size = newMap_Folder.size();
					int newMap_size = newMap.size();
					int oldMap_Folder_size = oldMap_Folder.size();
					int oldMap_size = oldMap.size();
					
					//如果新旧目录大小不同：新增 or 删除
					if(oldMap_Folder_size != newMap_Folder_size){
						//新增
						if(oldMap_Folder_size == newMap_Folder_size - 1){
							//在新的目录里找一个旧的没有的，就是新增的文件
							Iterator<Map.Entry<String, File>> iterator = newMap_Folder.entrySet().iterator();
							while(iterator.hasNext()){
								Map.Entry<String, File> entry = iterator.next();
								String key = entry.getKey();
								if(!oldMap_Folder.containsKey(key)){
									trigger_type = 4;	//新增文件夹(sizechanged)，触发标记4
									record_Direcory = entry.getValue();
									is_add = true;
									break;
								}
							}
						}
						//删除
						else if(oldMap_Folder_size == newMap_Folder_size + 1){
							//在旧的目录里找一个新的没有的，就是删除的文件
							Iterator<Map.Entry<String, File>> iterator = oldMap_Folder.entrySet().iterator();
							while(iterator.hasNext()){
								Map.Entry<String, File> entry = iterator.next();
								String key = entry.getKey();
								if(!newMap_Folder.containsKey(key)){
									trigger_type = 4;	//删除文件夹(sizechanged)，触发标记4
									record_Direcory = entry.getValue();
									is_delete = true;
									break;
								}
							}
						}
					}
					
					//如果新旧目录大小相同：修改 (文件夹不能renamed和pathchanged)
					else{
						//再看里面的文件的增删情况
						if(oldMap_size != newMap_size){
							//新增
							if(oldMap_size == newMap_size - 1){
								Iterator<Map.Entry<String, FileInfo>> iterator = newMap.entrySet().iterator();
								while(iterator.hasNext()){
									Map.Entry<String, FileInfo> entry = iterator.next();
									String key = entry.getKey();
									if(!oldMap.containsKey(key)){
										trigger_type= 4;	//新增文件夹里的文件(sizechange)，触发标记4
										record_File = entry.getValue();
										is_add = true;
										break;
									}
								}
							}
							//删除
							else if(oldMap_size == newMap_size + 1){
								Iterator<Map.Entry<String, FileInfo>> iterator = oldMap.entrySet().iterator();
								while(iterator.hasNext()){
									Map.Entry<String, FileInfo> entry = iterator.next();
									String key =  entry.getKey();
									if(!newMap.containsKey(key)){
										trigger_type = 4;	//删除文件夹里的文件(sizechanged)，触发标记4
										record_File = entry.getValue();
										is_delete = true;
										break;
									}
								}
							}
						}
						else{
							//看文件夹里的文件是否修改
							Iterator<Map.Entry<String, FileInfo>> iterator = oldMap.entrySet().iterator();
							while(iterator.hasNext()){
								Map.Entry<String, FileInfo> entry = iterator.next();
								String key = entry.getKey();
								FileInfo fileinfo = entry.getValue();
								if(newMap.containsKey(key)){
									FileInfo nMap = newMap.get(key);
									//大小改变
									if(nMap.getSize() != fileinfo.getSize()){
										record_File = fileinfo;
										record_File_new = nMap;
										//修改大小，修改时间也随之改变
										if(trigger.equals("modified"))
											trigger_type = 2;	//对应修改时间(modified)，则触发标记2
										else if(trigger.equals("sizechanged"))
											trigger_type = 4;	//对应修改大小(sizechanged)，则触发标记4
									}
									//时间改变
									else{
										if(nMap.getLast_mod() != fileinfo.getLast_mod()){
											record_File = fileinfo;
											record_File_new = nMap;
											trigger_type = 2;	//修改时间(modified)，触发标记2
											break;
										}
									}
								}
								else{
									File findfile = null;
									SearchUsing su = Search_File(WorkPlace, fileinfo, findfile);
									int type = su.found;
									findfile = su.found_file;
									if(type != -1){
										record_File = fileinfo;
										record_File_new = new FileInfo(findfile.getAbsolutePath(),findfile.lastModified(),findfile.getName(),findfile.length());
										break;
									}
								}
							}
						}
					}
					
					if(trigger_type != -1){
						switch (trigger_type) {
						case 1:
						{
							if(trigger.equals("renamed")){
								if(task.equals("summary")){
									Summary.Counter(1);
								}
								else if(task.equals("detail")){
									String str = "Trigger: renamed " + record_File.getName() + " to " + record_File_new.getName();
									Detail.Add(str);
								}
								else if(task.equals("recover")){
									File xfile = new File(record_File_new.getPath());
									xfile.renameTo(new File(record_File.getPath()));
								}
							}
							break;
						}
						case 2:
						{
							if(trigger.equals("modified")){
								if(task.equals("summary")){
									Summary.Counter(2);
									//System.out.println(trigger_type + "," + task);
								}
								else if(task.equals("detail")){
									String str = "Trigger: modified " + record_File.getName() + "'s time " + record_File.getLast_mod() + " to " + record_File_new.getLast_mod()
													+ " ALL PARENT FOLDERS TIME HAVE BEEN MODIFIED";
									Detail.Add(str);
									//System.out.println(trigger_type + "," + task);
 								}
							}
							break;
						}
						case 3:
						{
							if(trigger.equals("pathchanged")){
								if(task.equals("summary")){
									Summary.Counter(3);
									//System.out.println(trigger_type + "," + task);
								}
								else if(task.equals("detail")){
									String str = "Trigger: pathchanged " + record_File.getName() + "'s path" + record_File.getPath() + " to " + record_File_new.getPath();
									Detail.Add(str);
									//System.out.println(trigger_type + "," + task);
								}
							}
							break;
						}
						case 4:
						{
							if(trigger.equals("sizechanged")){
								if(task.equals("summary")){
									Summary.Counter(4);
									//System.out.println(trigger_type + "," + task);
								}
								else if(task.equals("detail")){
									String str;
									if(is_add){
										if(record_File == null)
											str = "Trigger: sizechanged " + "ADD: " + record_Direcory.getName() + " ALL PARENT FOLDER SIZE HAVE BEEN CHANGED";
										else
											str = "Trigger: sizechanged " + "ADD: " + record_File.getName() + " ALL PARENT FOLDER SIZE HAVE BEEN CHANGED";
									}
									else if(is_delete){
										if(record_File == null)
											str = "Trigger: sizechanged " + "DELETE: " + record_Direcory.getName() + " ALL PARENT FOLDER SIZE HAVE BEEN CHANGED";
										else
											str = "Trigger: sizechanged " + "DELETE: " + record_File.getName() + " ALL PARENT FOLDER SIZE HAVE BEEN CHANGED";
									}
									else{
										str = "Trigger: sizechanged " + record_File.getName() + "'s size" + record_File.getSize() + " to " + record_File_new.getSize()
												+ " ALL PARENT FOLDER SIZE HAVE BEEN CHANGED";
									}
									//System.out.println(str);
									Detail.Add(str);
								}
							}
							break;
						}
						default:
							break;
						}
					}
					oldMap_Folder = newMap_Folder;
					oldMap = newMap;
					
				}
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				System.out.println(e.getMessage());
			}
		}
		
		

		
	}

}


//删除：
//deleteSet.addAll(oldKeySet);
//deleteSet.removeAll(oldKeySet);
//System.out.println("Deleted File" + deleteSet);
//
//创建：
//createSet.addAll(newKeySet);
//createSet.removeAll(oldKeySet);
//System.out.println("Created File" + createSet);
