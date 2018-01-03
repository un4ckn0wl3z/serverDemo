package com.haoyun.commons.result;

import java.io.Serializable;

public class Racelamp implements Serializable {
	 /**
	 * 
	 */
	private static final long serialVersionUID = -4340761704870690289L;
	private String channel;
	 private  String text;
	 private String timestamp;
	 private String sid;
	 private String caller;
	public String getChannel() {
		return channel;
	}
	public void setChannel(String channel) {
		this.channel = channel;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
	public String getSid() {
		return sid;
	}
	public void setSid(String sid) {
		this.sid = sid;
	}
	public String getCaller() {
		return caller;
	}
	public void setCaller(String caller) {
		this.caller = caller;
	}
	@Override
	public String toString() {
		return "Racelamp [channel=" + channel + ", text=" + text + ", timestamp=" + timestamp + ", sid=" + sid
				+ ", caller=" + caller + "]";
	}
	 
    
}