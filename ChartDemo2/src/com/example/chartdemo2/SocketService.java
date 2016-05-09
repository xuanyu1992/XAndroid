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

	private Context context;// ��activity��������
	// ���ܷ�������Ϣ���߳�
	private Thread msg_thread = null;
	// �㲥������
	private MyReceiver receiver;

	// ����������ӵ�socket
	private Socket socket = null;
	// socket���׽�����Ϣ
	private InetSocketAddress isa = null;

	// socket���ӷ���������Ϣ
	private String ip = SocketInfo.IP;
	private String port = SocketInfo.Port;

	// socket�����������
	private DataInputStream dis = null;
	private DataOutputStream dos = null;

	private String reMsg = null;
	private Boolean isContect = false;

	private String chatKey = "SLEEKNETGEOCK4stsjeS";

	// �û���id��Ϣ
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
		// ע��㲥������
		receiver = new MyReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction(BasicInfo.action_sender);
		this.registerReceiver(receiver, filter);

		// Ӧ��������socket���ӷ�����
		new Thread(new Runnable() {
			public void run() {
				connect();
			}
		}).start();
	}

	/**
	 * socket�����������
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
				// ������������������߳�
				msg_thread = new Thread(null, doThread, "Message");
				msg_thread.start();
				System.out.println("connect");
				isContect = true;
			}
		} catch (UnknownHostException e) {
			Toast.makeText(context, "�B��ʧ��", Toast.LENGTH_SHORT).show();
			e.printStackTrace();
		} catch (SocketTimeoutException e) {
			Toast.makeText(context, "�B�ӳ��r��������δ�_����IP�e�`", Toast.LENGTH_SHORT).show();
			e.printStackTrace();
		} catch (IOException e) {
			Toast.makeText(context, "�B��ʧ��2", Toast.LENGTH_SHORT).show();
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
	 * �̼߳���Server��Ϣ ���շ�������������Ϣ��Ȼ��㲥��ȥ���û���Ϣ�б�������촰��
	 */
	private void ReceiveMsg() {
		if (isContect) {
			try {
				while ((reMsg = dis.readUTF()) != null) {
					System.out.println(reMsg);
					if (reMsg != null) {
						// �����յ�����Ϣ�㲥��ȥ
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
	 * �㲥�����ߣ����ڽ����������촰�ڹ㲥����Ϣ��Ȼ���͸�������
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
					Toast.makeText(context, "�B�ӳ��r��������δ�_����IP�e�`", Toast.LENGTH_SHORT).show();
					e.printStackTrace();
				}
			}
		}

	}

}
