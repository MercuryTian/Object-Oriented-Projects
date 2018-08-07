/*
 * 请求队列类：
 * 1.请求对象的队列
 */
package elevator;
//进队：.offer
//出队：.poll

import java.util.LinkedList;
import java.util.Queue;

public class RequestQueue {
	private Queue<Request> request = new LinkedList<Request>();
	
	public RequestQueue(){
	}
	
	public void ReqList(Request req) throws Exception{
		if(!request.offer(req)){
			throw new Exception("The queue is full");
		}
	}
	
	public Request sendRequest(){
		return request.poll();
	}
		
	
}
