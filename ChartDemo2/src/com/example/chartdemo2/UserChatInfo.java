package com.example.chartdemo2;

import java.io.Serializable;
import java.util.List;

public class UserChatInfo implements Serializable{

	private boolean isInChat;
	private String username;
	private String userstate;
	private List<ChatMsg> chatList;//

	public UserChatInfo() {
		super();
		// TODO Auto-generated constructor stub
	}



	public UserChatInfo(boolean isInChat, String username, String userstate, List<ChatMsg> chatList) {
		super();
		this.isInChat = isInChat;
		this.username = username;
		this.userstate = userstate;
		this.chatList = chatList;
	}



	public boolean isInChat() {
		return isInChat;
	}

	public void setInChat(boolean isInChat) {
		this.isInChat = isInChat;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getUserstate() {
		return userstate;
	}

	public void setUserstate(String userstate) {
		this.userstate = userstate;
	}

	public List<ChatMsg> getChatList() {
		return chatList;
	}

	public void setChatList(List<ChatMsg> chatList) {
		this.chatList = chatList;
	}

}
