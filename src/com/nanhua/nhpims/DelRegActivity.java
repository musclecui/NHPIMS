package com.nanhua.nhpims;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.*;

public class DelRegActivity extends Activity {

	private static final String MOD_NAME = "�����Ǽ�";
	private EditText etProNum;
	private Button btnScan;
	private Button btnAdd;
	private Button btnClr;
	private ListView lvProToDel; // �������Ĳ�Ʒ
	private List<HashMap<String, Object>> data;
	private MyAdapter adapter;

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
		// ����SimpleAdapter�����������ݰ󶨵�item��ʾ�ؼ���
		adapter = new MyAdapter();
		// ʵ���б����ʾ
		lvProToDel.setAdapter(adapter);
		// //��Ŀ����¼�
		// listView.setOnItemClickListener(new ItemClickListener());
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
			}

		}
	}

	private void onClick_Scan() {
		// ��ɨ�����ɨ����������ά��
		Intent openCameraIntent = new Intent(this, CaptureActivity.class);
		startActivityForResult(openCameraIntent, 0);
	}

	private void onClick_Add() {
		String proNum = etProNum.getText().toString();
		proNum.trim();
		if (proNum.equals("")) {

			new AlertDialog.Builder(this).setTitle(MOD_NAME)
					.setMessage("������Ҫ�����Ĳ�Ʒ���").setPositiveButton("ȷ��", null)
					.show();
			etProNum.requestFocus();
			return;
		}

		addItem(proNum);
		etProNum.setText("");
		etProNum.requestFocus();
	}

	// ���ӷ�����
	private void addItem(String proNum) {
		
		// ����Ƿ��ظ�
		if (checkRepeatItem(proNum)) {
			new AlertDialog.Builder(this).setTitle(MOD_NAME)
					.setMessage("��Ʒ���" + proNum + "���У������ظ�����")
					.setPositiveButton("ȷ��", null).show();
			return;
		}

		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("ibDel", R.drawable.close_delete);
		map.put("tvProNum", proNum);
		map.put("tvProModel", "�ͺ�");
		data.add(map);
		adapter.notifyDataSetChanged();
	}

	// ɾ��������
	private void delItem() {
		// int size = listItem.size();
		// if( size > 0 )
		// {
		// listItem.remove(listItem.size() - 1);
		// listItemAdapter.notifyDataSetChanged();
		// }
	}

	// ����ظ���
	private boolean checkRepeatItem(String proNum) {
		boolean repeat = false;
		final int size = data.size();
		for (int i=0; i<size; i++) {
			HashMap<String, Object> map = data.get(i);
			String tvProNum = (String)map.get("tvProNum");
			if (proNum.equals(tvProNum)) {
				repeat = true; // �����ظ���
				break;
			}
		}
		return repeat;
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		// ����ɨ�������ڽ�������ʾ��
		if (resultCode == RESULT_OK) {
			Bundle bundle = data.getExtras();
			String scanResult = bundle.getString("result");
			etProNum.setText(scanResult);
			addItem(scanResult);
			etProNum.setText("");
			etProNum.requestFocus();
		}
	}
	
	// �Զ���Adapter
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

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			ViewHolder holder;
			if (convertView == null) {
				convertView = mInflater.inflate(R.layout.del_item, null);
				holder = new ViewHolder();
				/** �õ������ؼ��Ķ��� */
				holder.ibDel = (ImageButton) convertView
						.findViewById(R.id.ibDel);
				holder.tvProNum = (TextView) convertView
						.findViewById(R.id.tvProNum);
				holder.tvProModel = (TextView) convertView
						.findViewById(R.id.tvProModel);
				convertView.setTag(holder); // ��ViewHolder����
			} else {
				holder = (ViewHolder) convertView.getTag();// ȡ��ViewHolder����
			}
			/** ����TextView��ʾ�����ݣ������Ǵ���ڶ�̬�����е����� */
			holder.tvProNum.setText(DelRegActivity.this.data.get(position)
					.get("tvProNum").toString());
			holder.tvProModel.setText(DelRegActivity.this.data.get(position)
					.get("tvProModel").toString());

			/** ΪButton��ӵ���¼� */
			holder.ibDel.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					System.out.println("�����˰�ť");
				}
			});

			return convertView;
		}
	}
}
