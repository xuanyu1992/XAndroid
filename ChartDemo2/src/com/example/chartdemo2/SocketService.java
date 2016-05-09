package com.example.chartdemo2;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.os.Message;
import android.view.View;
import android.widget.Toast;

public class SocketService extends Service {

	private Context context;// 本activity的上下文
	// 接受服务器消息的线程
	private Thread msg_thread = null;
	// 广播接受者
	private MyReceiver receiver;

	// 与服务器连接的socket
	private Socket socket = null;
	// socket的套接字信息
	private InetSocketAddress isa = null;

	// socket连接服务器的信息
	private String ip = SocketInfo.IP;
	private String port = SocketInfo.Port;

	// socket的输入输出流
	private DataInputStream dis = null;
	private DataOutputStream dos = null;

	private String reMsg = null;
	private Boolean isContect = false;

	private String chatKey = "SLEEKNETGEOCK4stsjeS";

	// 用户的id信息
	private String name = BasicInfo.user;

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		context = this;
		// 注册广播接收者
		receiver = new MyReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction(BasicInfo.action_sender);
		this.registerReceiver(receiver, filter);

		// 应用启动后socket连接服务器
		new Thread(new Runnable() {
			public void run() {
				connect();
			}
		}).start();
	}

	/**
	 * socket与服务器连接
	 */
	private void connect() {
		try {
			socket = new Socket();
			isa = new InetSocketAddress(ip, Integer.parseInt(port));
			socket.connect(isa, 5000);

			if (socket.isConnected()) {
				dos = new DataOutputStream(socket.getOutputStream());
				dis = new DataInputStream(socket.getInputStream());
				dos.writeUTF(chatKey + "online:" + name);
				// 创建与服务器交流的线程
				msg_thread = new Thread(null, doThread, "Message");
				msg_thread.start();
				System.out.println("connect");
				isContect = true;
			}
		} catch (UnknownHostException e) {
			Toast.makeText(context, "連接失敗", Toast.LENGTH_SHORT).show();
			e.printStackTrace();
		} catch (SocketTimeoutException e) {
			Toast.makeText(context, "連接超時，服務器未開啟或IP錯誤", Toast.LENGTH_SHORT).show();
			e.printStackTrace();
		} catch (IOException e) {
			Toast.makeText(context, "連接失敗2", Toast.LENGTH_SHORT).show();
			e.printStackTrace();
		}
	}

	public void disConnect() {
		if (dos != null) {
			try {
				dos.writeUTF(chatKey + "offline:" + name);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			try {
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private Runnable doThread = new Runnable() {
		public void run() {
			System.out.println("running!");
			ReceiveMsg();
		}
	};

	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		disConnect();
		if(receiver != null){
			this.unregisterReceiver(receiver);
		}
	};

	/**
	 * 线程监视Server信息 接收服务器发来的消息，然后广播出去给用户消息列表或者聊天窗口
	 */
	private void ReceiveMsg() {
		if (isContect) {
			try {
				while ((reMsg = dis.readUTF()) != null) {
					System.out.println(reMsg);
					if (reMsg != null) {
						// 将接收到的消息广播出去
						try {
							Intent intent = new Intent(BasicInfo.action_recever);
							intent.putExtra("msg", reMsg);
							SocketService.this.sendBroadcast(intent);
							Thread.sleep(100);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			} catch (SocketException e) {
				// TODO: handle exception
				System.out.println("exit!");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

	/**
	 * 广播接受者：用于接收来自聊天窗口广播的消息，然后传送给服务器
	 * 
	 * @author xuan
	 *
	 */
	public class MyReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			if (intent.getAction().equals(BasicInfo.action_sender)) {
				String send_str = intent.getStringExtra("msg");
				try {
					dos.writeUTF(chatKey + ":" + send_str);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					Toast.makeText(context, "連接超時，服務器未開啟或IP錯誤", Toast.LENGTH_SHORT).show();
					e.printStackTrace();
				}
			}
		}

	}

}
