package in.sms;

import java.util.Comparator;

public class ConversationClass implements Comparator<ConversationClass>,
		Comparable<ConversationClass> {

	private int thread;
	private String snippet, name, number;
	private long contactId, time;

	@Override
	public int compare(ConversationClass lhs, ConversationClass rhs) {
		// TODO Auto-generated method stub

		long lTime = (lhs).getTime();
		long rTime = (rhs).getTime();

		if ((lTime - rTime) <= 0)
			return 1;
		else
			return -1;

	}

	@Override
	public int compareTo(ConversationClass another) {
		// TODO Auto-generated method stub
		return 0;
	}

	public void setThread(int th) {
		thread = th;
	}

	public void setContactId(long id) {
		contactId = id;
	}

	public void setName(String nm) {
		name = nm;
	}

	public void setDate(long date) {
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

	public long getContactId() {
		return contactId;
	}

	public long getTime() {
		return time;
	}
}
