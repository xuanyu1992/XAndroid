package com.example.chartdemo2;

import java.io.Serializable;

public class ChatMsg implements Serializable{

	private String sender;
	private String recever;
	private String msg;

	public ChatMsg() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ChatMsg(String sender, String recever, String msg) {
		super();
		this.sender = sender;
		this.recever = recever;
		this.msg = msg;
	}

	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public String getRecever() {
		return recever;
	}

	public void setRecever(String recever) {
		this.recever = recever;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

}
