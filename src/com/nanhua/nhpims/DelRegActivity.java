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

	private static final String MOD_NAME = "�����Ǽ�";
	private EditText etProNum;
	private Button btnScan;
	private Button btnAdd;
	private Button btnClr;
	private Button btnDeli;
	private ListView lvProToDel; // �������Ĳ�Ʒ
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
		// ����SimpleAdapter�����������ݰ󶨵�item��ʾ�ؼ���
		adapter = new MyAdapter();
		// ʵ���б����ʾ
		lvProToDel.setAdapter(adapter);
		// ��Ŀ����¼�
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
	 * �����Ŀ�ĵ���¼�
	 */
	private class ItemClickEvent implements OnItemClickListener {

		/**
		 * view:��ǰ�������Ŀ��view���� position:��ǰ���������Ŀ�����󶨵������ڼ����е�����ֵ
		 */
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			ListView lView = (ListView) parent;
			HashMap<String, Object> map = (HashMap<String, Object>) lView
					.getItemAtPosition(position);
			Toast.makeText(getApplicationContext(),
					"������" + String.valueOf(map.get("tvProNum")),
					Toast.LENGTH_SHORT).show();
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

	private void onClick_Clr() {
		adapter.removeAll();
	}

	private void onClick_Deli() {

		final int size = data.size();
		if (0 == size) {
			new AlertDialog.Builder(this).setTitle(MOD_NAME)
					.setMessage("û��Ҫ�����Ĳ�Ʒ").setPositiveButton("ȷ��", null)
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
			// �ɹ�
			
			// ���������������
			Intent intent = new Intent(this, DelRegResActivity.class);
			Bundle bundle = new Bundle();
			bundle.putStringArrayList("DelRegResList", delRegResList);
			intent.putExtras(bundle);
			startActivity(intent);

			// ���
			adapter.removeAll();
		} else {
			// ʧ��

			new AlertDialog.Builder(this)
					.setTitle(MOD_NAME).setMessage(err.errMsg)
					.setPositiveButton("ȷ��", null).show();
		}
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
		
		// ��ѯ�ò�Ʒ��Ϣ
		ProInfo proInfo = new ProInfo();
		WsErr err = new WsErr();
		WebService.queryProduct(proNum, proInfo, err);
		if (err.errCode.equals(WsErr.ERR_NO)) {
			if (null != proInfo.proNum) {
				// TODO ��֤��Ʒ״̬�Ƿ��ڿɷ���

				HashMap<String, Object> map = new HashMap<String, Object>();
				map.put("ibDel", R.drawable.close_delete);
				map.put("tvProNum", proInfo.proNum);
				map.put("tvProModel", proInfo.proModel);
				// data.add(map);
				data.add(0, map); // ���ӵ���λ��
				adapter.notifyDataSetChanged();
				
			} else {
				final String msg = "û�в�Ʒ���:" + proNum + "����Ϣ";
				new AlertDialog.Builder(this).setTitle(MOD_NAME)
						.setMessage(msg).setPositiveButton("ȷ��", null).show();
			}

		} else {
			new AlertDialog.Builder(this).setTitle(MOD_NAME)
					.setMessage(err.errMsg).setPositiveButton("ȷ��", null)
					.show();
		}
	}

	// ����ظ���
	private boolean checkRepeatItem(String proNum) {
		boolean repeat = false;
		final int size = data.size();
		for (int i = 0; i < size; i++) {
			HashMap<String, Object> map = data.get(i);
			String tvProNum = (String) map.get("tvProNum");
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
			holder.tvProNum.setText(data.get(position).get("tvProNum")
					.toString());
			holder.tvProModel.setText(data.get(position).get("tvProModel")
					.toString());

			final int pos = position;
			/** ΪButton��ӵ���¼� */
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
