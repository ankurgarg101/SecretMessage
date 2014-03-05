package in.sms;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Comparator;

/**
 * This class represents SMS.
 * 
 * @author itcuties
 * 
 */
public class SMSData implements Comparator<SMSData>,
Comparable<SMSData>{

	// Number from witch the sms was send
	private String  body, strDate, contact, number;
	// SMS text body
	private long longDate;
	private int type, thread;
	long id;
	long contactId;
	boolean secret;

	@Override
	public int compare(SMSData lhs, SMSData rhs) {
		// TODO Auto-generated method stub

		long lTime = (lhs).getDate();
		long rTime = (rhs).getDate();

		if ((lTime - rTime) <= 0)
			return -1;
		else
			return 1;

	}

	@Override
	public int compareTo(SMSData another) {
		// TODO Auto-generated method stub
		return 0;
	}
	
	public void setSecret(boolean s){
		secret = s;
	}
	public boolean getSecret(){
		return secret;
	}
	public void setId(long l){
		this.id = l;
	}
	
	public long getId(){
		return id;
	}
	
	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public void setContactId(long contact_id) {
		this.contactId = contact_id;
	}

	public long getContactId() {
		return contactId;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public void setDate(long lDate) {
		longDate = lDate;
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"yyyy-MM-dd hh:mm:ss");
		Date d = new Date(longDate);
		strDate = dateFormat.format(longDate);

	}
	
	public long getDate(){
		return longDate;
	}
	public String getStrDate(){
		return strDate;
	}
	
	public void setName(String n){
		contact = n;
	}
	
	public String getName(){
		return contact;
	}
	public void setNumber(String n){
		number = n;
	}
	
	public String getNumber(){
		return number;
	}
	
	public void setThread(int th){
		thread = th;
	}
	
	public int getThread(){
		return thread;
	}
}
