/*
 * 请求队列类：
 * 1.请求对象的队列
 */
package elevator;
//进队：.offer
//出队：.poll

import java.util.LinkedList;

public class RequestQueue {
	private LinkedList<Request> requestsQueue = new LinkedList<Request>();
	//NEW: 把Queue更改为LinkedList
	public RequestQueue(){
	}
	
	public void ReqList(Request req) throws Exception{
		if(!requestsQueue.offer(req)){
			throw new Exception("The queue is full");
		}
	}
	
	public Request sendRequest(){
		return requestsQueue.poll();
	}

	public int getSize() {
		// TODO Auto-generated method stub
		return requestsQueue.size();
	}
	
	public Request getRequest(int j){
		return requestsQueue.get(j);
	}
		
	public void deleteRequest(int j){
		requestsQueue.remove(j);
	}
	
	public void judgeSame(){
		for(int i=0; i<requestsQueue.size(); i++){
			Request tmp = requestsQueue.get(i);
			for(int j=i+1; j<requestsQueue.size(); j++){
				if(requestsQueue.get(j).equals(tmp)){
					requestsQueue.remove(j);
					j--;
				}
			}
		}
	}
}
