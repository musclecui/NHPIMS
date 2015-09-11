package com.nanhua.nhpims;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.nanhua.nhpims.model.ProInfo;
import com.nanhua.nhpims.webservice.WebService;
import com.nanhua.nhpims.webservice.WsErr;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.*;
import android.widget.AdapterView.OnItemClickListener;

public class DelRegActivity extends Activity {

	private static final String MOD_NAME = "发货登记";
	private EditText etProNum;
	private Button btnScan;
	private Button btnAdd;
	private Button btnClr;
	private Button btnDeli;
	private ListView lvProToDel; // 待发货的产品
	private List<HashMap<String, Object>> data;
	private MyAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
		setContentView(R.layout.activity_del_reg);

		etProNum = (EditText) findViewById(R.id.etProNum);
		btnScan = (Button) findViewById(R.id.btnScan);
		btnAdd = (Button) findViewById(R.id.btnAdd);
		btnClr = (Button) findViewById(R.id.btnClear);
		btnDeli = (Button) findViewById(R.id.btnDelivery);
		lvProToDel = (ListView) findViewById(R.id.lvProToDel);

		btnScan.setOnClickListener(new ClickEvent());
		btnAdd.setOnClickListener(new ClickEvent());
		btnClr.setOnClickListener(new ClickEvent());
		btnClr.setOnClickListener(new ClickEvent());
		btnDeli.setOnClickListener(new ClickEvent());

		data = new ArrayList<HashMap<String, Object>>();
		String[] from = new String[] { "ibDel", "tvProNum", "tvProModel" };
		int[] to = new int[] { R.id.ibDel, R.id.tvProNum, R.id.tvProModel };
		// 创建SimpleAdapter适配器将数据绑定到item显示控件上
		adapter = new MyAdapter();
		// 实现列表的显示
		lvProToDel.setAdapter(adapter);
		// 条目点击事件
		// lvProToDel.setOnItemClickListener(new ItemClickEvent());
	}

	private class ClickEvent implements OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.btnScan:
				onClick_Scan();
				break;
			case R.id.btnAdd:
				onClick_Add();
				break;
			case R.id.btnClear:
				onClick_Clr();
				break;
			case R.id.btnDelivery:
				onClick_Deli();
				break;
			}

		}
	}

	/**
	 * 针对条目的点击事件
	 */
	private class ItemClickEvent implements OnItemClickListener {

		/**
		 * view:当前所点击条目的view对象 position:当前所点击的条目它所绑定的数据在集合中的索引值
		 */
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			ListView lView = (ListView) parent;
			HashMap<String, Object> map = (HashMap<String, Object>) lView
					.getItemAtPosition(position);
			Toast.makeText(getApplicationContext(),
					"你点击了" + String.valueOf(map.get("tvProNum")),
					Toast.LENGTH_SHORT).show();
		}

	}

	private void onClick_Scan() {
		// 打开扫描界面扫描条形码或二维码
		Intent openCameraIntent = new Intent(this, CaptureActivity.class);
		startActivityForResult(openCameraIntent, 0);
	}

	private void onClick_Add() {
		String proNum = etProNum.getText().toString();
		proNum.trim();
		if (proNum.equals("")) {

			new AlertDialog.Builder(this).setTitle(MOD_NAME)
					.setMessage("请输入要发货的产品编号").setPositiveButton("确定", null)
					.show();
			etProNum.requestFocus();
			return;
		}

		addItem(proNum);
		etProNum.setText("");
		etProNum.requestFocus();
	}

	private void onClick_Clr() {
		adapter.removeAll();
	}

	private void onClick_Deli() {

		final int size = data.size();
		if (0 == size) {
			new AlertDialog.Builder(this).setTitle(MOD_NAME)
					.setMessage("没有要发货的产品").setPositiveButton("确定", null)
					.show();
			return;
		}

		String[] proNumToDeliArray = new String[size];
		for (int i = 0; i < size; ++i) {
			HashMap<String, Object> map = data.get(i);
			proNumToDeliArray[i] = String.valueOf(map.get("tvProNum"));
		}
		ArrayList<String> delRegResList = new ArrayList<String>();
		WsErr err = new WsErr();
		WebService.deliveryRegister(proNumToDeliArray, delRegResList, err);
		System.out.println(proNumToDeliArray.toString());
		System.out.println(delRegResList.toString());
		
		if (err.errCode.equals(WsErr.ERR_NO)) {
			// 成功
			
			// 弹出发货结果界面
			Intent intent = new Intent(this, DelRegResActivity.class);
			Bundle bundle = new Bundle();
			bundle.putStringArrayList("DelRegResList", delRegResList);
			intent.putExtras(bundle);
			startActivity(intent);

			// 清空
			adapter.removeAll();
		} else {
			// 失败

			new AlertDialog.Builder(this)
					.setTitle(MOD_NAME).setMessage(err.errMsg)
					.setPositiveButton("确定", null).show();
		}
	}

	// 增加发货项
	private void addItem(String proNum) {

		// 检查是否重复
		if (checkRepeatItem(proNum)) {
			new AlertDialog.Builder(this).setTitle(MOD_NAME)
					.setMessage("产品编号" + proNum + "已有，不能重复增加")
					.setPositiveButton("确定", null).show();
			return;
		}
		
		// 查询该产品信息
		ProInfo proInfo = new ProInfo();
		WsErr err = new WsErr();
		WebService.queryProduct(proNum, proInfo, err);
		if (err.errCode.equals(WsErr.ERR_NO)) {
			if (null != proInfo.proNum) {
				// TODO 验证产品状态是否处于可发货

				HashMap<String, Object> map = new HashMap<String, Object>();
				map.put("ibDel", R.drawable.close_delete);
				map.put("tvProNum", proInfo.proNum);
				map.put("tvProModel", proInfo.proModel);
				// data.add(map);
				data.add(0, map); // 增加到首位置
				adapter.notifyDataSetChanged();
				
			} else {
				final String msg = "没有产品编号:" + proNum + "的信息";
				new AlertDialog.Builder(this).setTitle(MOD_NAME)
						.setMessage(msg).setPositiveButton("确定", null).show();
			}

		} else {
			new AlertDialog.Builder(this).setTitle(MOD_NAME)
					.setMessage(err.errMsg).setPositiveButton("确定", null)
					.show();
		}
	}

	// 检查重复项
	private boolean checkRepeatItem(String proNum) {
		boolean repeat = false;
		final int size = data.size();
		for (int i = 0; i < size; i++) {
			HashMap<String, Object> map = data.get(i);
			String tvProNum = (String) map.get("tvProNum");
			if (proNum.equals(tvProNum)) {
				repeat = true; // 出现重复项
				break;
			}
		}
		return repeat;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		// 处理扫描结果（在界面上显示）
		if (resultCode == RESULT_OK) {
			Bundle bundle = data.getExtras();
			String scanResult = bundle.getString("result");
			etProNum.setText(scanResult);
			addItem(scanResult);
			etProNum.setText("");
			etProNum.requestFocus();
		}
	}

	// 自定义Adapter
	private class MyAdapter extends BaseAdapter {

		private class ViewHolder {
			public ImageButton ibDel;
			public TextView tvProNum;
			public TextView tvProModel;
		}

		private LayoutInflater mInflater;

		public MyAdapter() {
			this.mInflater = (LayoutInflater) DelRegActivity.this
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return DelRegActivity.this.data.size();
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return DelRegActivity.this.data.get(arg0);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		public void remove(int index) {
			if (index < 0) {
				return;
			}
			DelRegActivity.this.data.remove(index);
			notifyDataSetChanged();
		}

		public void removeAll() {
			DelRegActivity.this.data.clear();
			notifyDataSetChanged();
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			ViewHolder holder;
			if (convertView == null) {
				convertView = mInflater.inflate(R.layout.del_item, null);
				holder = new ViewHolder();
				/** 得到各个控件的对象 */
				holder.ibDel = (ImageButton) convertView
						.findViewById(R.id.ibDel);
				holder.tvProNum = (TextView) convertView
						.findViewById(R.id.tvProNum);
				holder.tvProModel = (TextView) convertView
						.findViewById(R.id.tvProModel);
				convertView.setTag(holder); // 绑定ViewHolder对象
			} else {
				holder = (ViewHolder) convertView.getTag();// 取出ViewHolder对象
			}
			/** 设置TextView显示的内容，即我们存放在动态数组中的数据 */
			holder.tvProNum.setText(data.get(position).get("tvProNum")
					.toString());
			holder.tvProModel.setText(data.get(position).get("tvProModel")
					.toString());

			final int pos = position;
			/** 为Button添加点击事件 */
			holder.ibDel.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					remove(pos);
				}
			});

			return convertView;
		}
	}
}
