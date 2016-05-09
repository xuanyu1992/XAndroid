package com.example.chartdemo2;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ChartActivity extends ActionBarActivity {

	Context context;// ��activity��������

	// ���ܷ�������Ϣ���߳�
	private Thread msg_thread = null;

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
	private EditText chattxt;
	private EditText chatbox;
	private Button chatok;

	private String chatKey = "SLEEKNETGEOCK4stsjeS";

	// �û���id��Ϣ
	private String name = BasicInfo.user;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chart);
		context = this;

		chattxt = (EditText) findViewById(R.id.chattxt);
		chatbox = (EditText) findViewById(R.id.chatbox);
		chatok = (Button) findViewById(R.id.chatOk);
		chatbox.setCursorVisible(false);
		chatbox.setFocusable(false);
		chatbox.setFocusableInTouchMode(false);
		chatbox.setGravity(2);

		chatok.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				String str = chattxt.getText().toString().trim();
				// System.out.println(socket);
				try {
					dos.writeUTF(chatKey + "name:" + name + "end;" + str);
					chattxt.setText("");
				} catch (SocketTimeoutException e) {
					Toast.makeText(context, "�B�ӳ��r��������δ�_����IP�e�`", Toast.LENGTH_SHORT).show();
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					Toast.makeText(context, "�B�ӳ��r��������δ�_����IP�e�`", Toast.LENGTH_SHORT).show();
					e.printStackTrace();
				}
			}
		});

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
				//������������������߳�
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

	/**
	 * �̼߳���Server��Ϣ
	 */
	private void ReceiveMsg() {
		if (isContect) {
			try {
				while ((reMsg = dis.readUTF()) != null) {
					System.out.println(reMsg);
					if (reMsg != null) {

						try {
							Message msgMessage = new Message();
							msgMessage.what = 0x1981;
							msg_handler.sendMessage(msgMessage);
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
	 * ͨ��handler����UI
	 */
	Handler msg_handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0x1981:
				chatbox.setText(chatbox.getText() + reMsg + '\n');
				chatbox.setSelection(chatbox.length());
				break;
			}
		}
	};

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		disConnect();
		// System.exit(0);
	}
}
