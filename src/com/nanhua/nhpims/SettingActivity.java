package com.nanhua.nhpims;

import com.nanhua.lib.ContextUtil;
import com.nanhua.lib.ShaPreOpe;
import com.nanhua.nhpims.R.string;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SettingActivity extends Activity {
	
	private static final String MOD_NAME = "设置"; // 模块名
	Button btnSave;
	Button btnCancel;
	EditText etIp;
	EditText etPort;
	
	ShaPreOpe shaPreOpe;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
		
		btnSave = (Button)findViewById(R.id.btnSave);
		btnCancel = (Button)findViewById(R.id.btnCancel);
		etIp = (EditText)findViewById(R.id.etIp);
		etPort = (EditText) findViewById(R.id.etPort);

		shaPreOpe = new ShaPreOpe(ContextUtil.getInstance());

		etIp.setText(shaPreOpe.read("ip", ""));
		etPort.setText(shaPreOpe.read("port", ""));

		btnSave.setOnClickListener(new ClickEvent());
		btnCancel.setOnClickListener(new ClickEvent());
	}

	class ClickEvent implements OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			
			switch (v.getId()) {
			case R.id.btnSave:
				saveSetting();
				break;
			case R.id.btnCancel:
				finish();
				break;
			default:
				break;
			}
		}
	}
	
	private void saveSetting() {
		
		String ip = etIp.getText().toString();
		ip.trim();
		if (ip.equals("")) {
			AlertDialog.Builder builder = new AlertDialog.Builder(
					SettingActivity.this);
			builder.setTitle(MOD_NAME);
			builder.setIcon(R.drawable.options);
			builder.setMessage("请输入IP");
			builder.setPositiveButton("确定", null);
			builder.create().show();

			etIp.setFocusable(true);
			etIp.requestFocus();
			return;
		}
		String port = etPort.getText().toString();
		port.trim();
		if (port.equals("")) {
			AlertDialog.Builder builder = new AlertDialog.Builder(
					SettingActivity.this);
			builder.setTitle(MOD_NAME);
			builder.setIcon(R.drawable.options);
			builder.setMessage("请输入端口");
			builder.setPositiveButton("确定", null);
			builder.create().show();

			etPort.setFocusable(true);
			etPort.requestFocus();
			return;
		}
		final int intPort = Integer.parseInt(port);
		final int minPort = 1;
		final int maxPort = 65535;
		if (intPort<minPort || intPort>maxPort){
			String msg = "端口只能是" + String.valueOf(minPort) + "~" + String.valueOf(maxPort);
			AlertDialog.Builder builder = new AlertDialog.Builder(SettingActivity.this);
			builder.setTitle(MOD_NAME);
			builder.setIcon(R.drawable.options);
			builder.setMessage(msg);
			builder.setPositiveButton("确定", null);
			builder.create().show();
			
			etPort.setFocusable(true);
			etPort.requestFocus();
			return;
		}
		
		shaPreOpe.write("ip", ip);
		shaPreOpe.write("port", port);
		
		finish();
	}
}
