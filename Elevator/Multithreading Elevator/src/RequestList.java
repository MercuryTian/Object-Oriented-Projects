import java.util.LinkedList;
/*
 * 进队：.offer
 * 出队：.poll
 * 托盘
 */

public class RequestList {
	
	protected static LinkedList <Request> reqlist = new LinkedList<Request>();
	
	public RequestList(){}
	
	public static void ReqList(Request req) throws Exception{
		if(!reqlist.offer(req)){
			throw new Exception("The queue is full");
		}
	}
	
	public static Request sendRequest(){
		return reqlist.poll();
	}
	
	public static int getSize(){
		return reqlist.size();
	}
	
	public static Request getRequest(int j){
		return reqlist.get(j);
		
	}
	
	public static void deletRequest(int j){
		reqlist.remove(j);
	}
	
	public static void judgeSame(){
		for(int i=0; i<reqlist.size(); i++){
			Request tmp = reqlist.get(i);
			for(int j=i+1; j<reqlist.size(); j++){
				if(reqlist.get(j).equals(tmp)){
					reqlist.remove(j);
					j--;
				}
			}
		}
	}
	

}
