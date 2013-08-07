package in.sms;

import java.sql.Date;
import java.text.SimpleDateFormat;

/**
 * This class represents SMS.
 * 
 * @author itcuties
 * 
 */
public class SMSData {

	// Number from witch the sms was send
	private String number, body, strDate, name;
	// SMS text body
	private long longDate;
	private int type;
	long id;
	private String status, personId;

	public void setId(long l){
		this.id = l;
	}
	
	public long getId(){
		return id;
	}
	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public void setPersonId(String person_id) {
		this.personId = person_id;
	}

	public String getPersonId() {
		return personId;
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
	
	public String getDate(){
		return strDate;
	}
	
	public void setName(String n){
		name = n;
	}
	
	public String getName(){
		return name;
	}
	
	public void setStatus(String s){
		status = s;
	}
	public String getStatus(){
		return status;
	}
}
