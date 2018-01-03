package com.haoyun.commons.result;

import java.io.Serializable;

public class EmailTemplate implements Serializable {
	 private String pids;
	 private  String name;
	 private String title;
	 private String content;
	 private String sendtime;
	 private String pasttime;
	 private String mail_type;
	 private String money;
	 private String rmb;
	 private String item_tid;
	 private String item_count;
	 private String timestamp;
    
    private static final long serialVersionUID = 1L;

	public String getPids() {
		return pids;
	}

	public void setPids(String pids) {
		this.pids = pids;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getSendtime() {
		return sendtime;
	}

	public void setSendtime(String sendtime) {
		this.sendtime = sendtime;
	}

	public String getPasttime() {
		return pasttime;
	}

	public void setPasttime(String pasttime) {
		this.pasttime = pasttime;
	}

	public String getMail_type() {
		return mail_type;
	}

	public void setMail_type(String mail_type) {
		this.mail_type = mail_type;
	}

	public String getMoney() {
		return money;
	}

	public void setMoney(String money) {
		this.money = money;
	}

	public String getRmb() {
		return rmb;
	}

	public void setRmb(String rmb) {
		this.rmb = rmb;
	}

	public String getItem_tid() {
		return item_tid;
	}

	public void setItem_tid(String item_tid) {
		this.item_tid = item_tid;
	}

	public String getItem_count() {
		return item_count;
	}

	public void setItem_count(String item_count) {
		this.item_count = item_count;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	@Override
	public String toString() {
		return "EmailTemplate [pids=" + pids + ", name=" + name + ", title=" + title + ", content=" + content
				+ ", sendtime=" + sendtime + ", pasttime=" + pasttime + ", mail_type=" + mail_type + ", money=" + money
				+ ", rmb=" + rmb + ", item_tid=" + item_tid + ", item_count=" + item_count + ", timestamp="
				+ timestamp + "]";
	}
    
}