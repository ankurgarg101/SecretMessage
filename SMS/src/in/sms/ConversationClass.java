package in.sms;

import android.net.Uri;

public class ConversationClass {

	private int thread;
	private String snippet, name, number;
	private long contactId, time;

	public int compareTo(ConversationClass obj)
	{
	     if((time - obj.time) < 0)  return 1;
	     else return -1;
	}
	public void setThread(int th) {
		thread = th;
	}

	public void setUri(long id){
		contactId = id;
	}
	public void setName(String nm) {
		name = nm;
	}

	public void setDate(long date){
		time = date;
	}
	public void setNumber(String num) {
		number = num;
	}

	public void setSnippet(String snip) {
		snippet = snip;
	}

	public int getThread() {
		return thread;
	}

	public String getName() {
		return name;
	}

	public String getNumber() {
		return number;
	}

	public String getSnippet() {
		return snippet;
	}
	
	public long getUri(){
		return contactId;
	}
	
	public long getTime(){
		return time;
	}
}
