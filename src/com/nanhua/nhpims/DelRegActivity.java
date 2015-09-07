package com.nanhua.nhpims;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.*;

public class DelRegActivity extends Activity {

	private static final String MOD_NAME = "发货登记";
	EditText etProNum;
	Button btnScan;
	Button btnAdd;
	Button btnClr;
	ListView lvProToDel; // 待发货的产品
	List<HashMap<String, Object>> data;
	SimpleAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_del_reg);

		etProNum = (EditText) findViewById(R.id.etProNum);
		btnScan = (Button) findViewById(R.id.btnScan);
		btnAdd = (Button) findViewById(R.id.btnAdd);
		btnClr = (Button) findViewById(R.id.btnClear);
		lvProToDel = (ListView) findViewById(R.id.lvProToDel);

		btnScan.setOnClickListener(new ClickEvent());
		btnAdd.setOnClickListener(new ClickEvent());

		data = new ArrayList<HashMap<String, Object>>();
		String[] from = new String[] { "ibDel", "tvProNum", "tvProModel" };
		int[] to = new int[] { R.id.ibDel, R.id.tvProNum, R.id.tvProModel };
		// 创建SimpleAdapter适配器将数据绑定到item显示控件上
		adapter = new SimpleAdapter(this, data, R.layout.del_item, from, to);
		// 实现列表的显示
		lvProToDel.setAdapter(adapter);
		// //条目点击事件
		// listView.setOnItemClickListener(new ItemClickListener());
	}

	class ClickEvent implements OnClickListener {

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
			}

		}
	}

	void onClick_Scan() {
		// 打开扫描界面扫描条形码或二维码
		Intent openCameraIntent = new Intent(this, CaptureActivity.class);
		startActivityForResult(openCameraIntent, 0);
	}

	void onClick_Add() {
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

	// 增加发货项
	private void addItem(String proNum) {
		
		// 检查是否重复
		if (checkRepeatItem(proNum)) {
			new AlertDialog.Builder(this).setTitle(MOD_NAME)
					.setMessage("产品编号" + proNum + "已有，不能重复增加")
					.setPositiveButton("确定", null).show();
			return;
		}

		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("ibDel", R.drawable.close_delete);
		map.put("tvProNum", proNum);
		map.put("tvProModel", "型号");
		data.add(map);
		adapter.notifyDataSetChanged();
	}

	// 删除发货项
	private void delItem() {
		// int size = listItem.size();
		// if( size > 0 )
		// {
		// listItem.remove(listItem.size() - 1);
		// listItemAdapter.notifyDataSetChanged();
		// }
	}

	// 检查重复项
	private boolean checkRepeatItem(String proNum) {
		boolean repeat = false;
		final int size = data.size();
		for (int i=0; i<size; i++) {
			HashMap<String, Object> map = data.get(i);
			String tvProNum = (String)map.get("tvProNum");
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
}
