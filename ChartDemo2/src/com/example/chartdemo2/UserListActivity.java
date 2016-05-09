package com.example.chartdemo2;

import android.support.v7.app.ActionBarActivity;
import android.transition.Visibility;

import java.util.List;
import java.util.zip.Inflater;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class UserListActivity extends ActionBarActivity {

	//用户列表数据
	private List<UserChatInfo> user_chat_list;
	//用户列表view
	private ListView lv_user;
	//用户列表adapter
	private UserListAdapter adapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_list);
		initData();
		initView();
	}

	/**
	 * 初始化数据
	 */
	private void initData() {
		// TODO Auto-generated method stub
		
	}

	/**
	 * 初始化布局
	 */
	private void initView() {
		// TODO Auto-generated method stub
		lv_user = (ListView) findViewById(R.id.lv_user);
		adapter = new UserListAdapter();
		lv_user.setAdapter(adapter);
		lv_user.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				//跳转到聊天窗口
				//传递消息队列中的消息
				//从消息列表中删除
			}
		});
		
	}
	
	private class UserListAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return user_chat_list.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			View view = convertView;
			Holder holder = null;
			if (view!=null) {
				holder = (Holder) view.getTag();
			}else{
				view = getLayoutInflater().inflate(R.layout.user_lv_item, null);
				holder = new Holder();
				holder.tv_user_name = (TextView) view.findViewById(R.id.tv_user_name);
				holder.tv_user_state = (TextView) view.findViewById(R.id.tv_user_state);
				holder.tv_msg_count = (TextView) view.findViewById(R.id.tv_msg_count);
				view.setTag(holder);
			}
			holder.tv_user_name.setText(user_chat_list.get(position).getUsername());
			holder.tv_user_state.setText(user_chat_list.get(position).getUserstate());
			int count = user_chat_list.get(position).getChatList().size();
			if(count<=0){
				holder.tv_msg_count.setVisibility(View.INVISIBLE);
			}else{
				holder.tv_msg_count.setVisibility(View.VISIBLE);
				holder.tv_msg_count.setText(count);
			}
			return view;
		}
		
	}
	
	public static class Holder{
		public TextView tv_user_name;
		public TextView tv_user_state;
		public TextView tv_msg_count;
	}
}
