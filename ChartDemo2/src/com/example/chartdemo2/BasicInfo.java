package com.example.chartdemo2;

public class BasicInfo {

	public static String user = "xuan";
	//接收服务器消息，广播给用户列表或聊天窗口
	public static String action_recever = "com.xuan.chat.msg_recever";
	//将聊天窗口的消息广播给socketService，在发给服务器
	public static String action_sender = "com.xuan.chat.msg_sender";
}
