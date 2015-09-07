package com.nanhua.nhpims;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.nanhua.lib.*;
import com.nanhua.nhpims.webservice.*;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

public class UserLoginActivity extends Activity {

	private static final String MOD_NAME = "�û���¼"; // ģ����
	Button btnLogin;
	EditText etUserName;
	EditText etPassword;
	Menu menu;
	private int menuItemId = Menu.FIRST;
	ShaPreOpe shaPreOpe;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_login);

		btnLogin = (Button) findViewById(R.id.btnLogin);
		etUserName = (EditText) findViewById(R.id.etUserName);
		etPassword = (EditText) findViewById(R.id.etPassword);

		shaPreOpe = new ShaPreOpe(ContextUtil.getInstance());
		
		String lastUserName = shaPreOpe.read("username", "");
		etUserName.setText(lastUserName);
		if ("" != lastUserName) {
			etPassword.requestFocus();
		}

		btnLogin.setOnClickListener(new OnClickListener() {

			// @Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				String userName = etUserName.getText().toString();
				String password = etPassword.getText().toString();

				if (TextUtils.isEmpty(userName)) {
					new AlertDialog.Builder(UserLoginActivity.this)
							.setTitle(MOD_NAME).setMessage("�û�������Ϊ��")
							.setPositiveButton("ȷ��", null).show();
					etUserName.requestFocus();
					return;
				}
				if (TextUtils.isEmpty(password)) {
					new AlertDialog.Builder(UserLoginActivity.this)
							.setTitle(MOD_NAME).setMessage("���벻��Ϊ��")
							.setPositiveButton("ȷ��", null).show();
					etPassword.requestFocus();
					return;
				}
				if (TextUtils.isEmpty(shaPreOpe.read("ip", ""))
						|| TextUtils.isEmpty(shaPreOpe.read("port", ""))) {
					new AlertDialog.Builder(UserLoginActivity.this)
							.setTitle(MOD_NAME)
							.setMessage("������������ip��˿ڣ�Ϊ�գ�������")
							.setPositiveButton("ȷ��", null).show();
					return;
				}

				WsErr err = new WsErr();
				WebService.UserLogin(userName, password, GloVar.curUser, err);
				if (err.errCode.equals(WsErr.ERR_NO)) {
					// ��¼�ɹ�

					// ��������ɹ���¼�û�
					shaPreOpe.write("username", userName);
					
					Intent intent = new Intent(UserLoginActivity.this,
							FunSelActivity.class);
					startActivity(intent);
					finish(); // �رձ�ҳ��
				} else {
					// ��¼ʧ��

					new AlertDialog.Builder(UserLoginActivity.this)
							.setTitle(MOD_NAME).setMessage(err.errMsg)
							.setPositiveButton("ȷ��", null).show();
				}
				
//				LayoutInflater inflater = (LayoutInflater) UserLoginActivity.this
//						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//				final View vPopupWin = inflater.inflate(
//						R.layout.dialog_popup, null, false);
//				final PopupWindow pw = new PopupWindow(vPopupWin, 600, 400,
//						true);
//				Button btnOk = (Button) vPopupWin.findViewById(R.id.btnOk);
//				Button btnCancel = (Button) vPopupWin
//						.findViewById(R.id.btnCancel);
//				TextView tvTip = (TextView) vPopupWin
//						.findViewById(R.id.tvTip);
//				tvTip.setText(err.errMsg);
//				btnOk.setOnClickListener(new OnClickListener() {
//
//					@Override
//					public void onClick(View v) {
//						// TODO Auto-generated method stub
//						pw.dismiss();
//					}
//				});
//				btnCancel.setOnClickListener(new OnClickListener() {
//
//					@Override
//					public void onClick(View v) {
//						// TODO Auto-generated method stub
//						pw.dismiss();
//					}
//				});
//				pw.showAtLocation(v, Gravity.CENTER, 0, 0);	
				
				

				// SimpleDateFormat df = new
				// SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); // �������ڸ�ʽ
				// Date dt = new Date();

				// AlertDialog.Builder builder = new
				// AlertDialog.Builder(UserLoginActivity.this);
				// builder.setTitle(R.string.app_name);
				// builder.setIcon(R.drawable.home);
				// builder.setMessage("�����ڵ�¼");
				// builder.setPositiveButton("ȷ��", new
				// DialogInterface.OnClickListener() {
				//
				// @Override
				// public void onClick(DialogInterface dialog, int which) {
				// // TODO Auto-generated method stub
				// Toast.makeText(UserLoginActivity.this, "�㰴��ȷ��",
				// Toast.LENGTH_LONG).show();
				// }
				// });
				// builder.setNegativeButton("ȡ��", new
				// DialogInterface.OnClickListener() {
				//
				// @Override
				// public void onClick(DialogInterface dialog, int which) {
				// // TODO Auto-generated method stub
				// Toast.makeText(UserLoginActivity.this, "�㰴��ȡ��",
				// Toast.LENGTH_LONG).show();
				// }
				// });
				// builder.create().show();

				// EditText etUserName =
				// (EditText)findViewById(R.id.etUserName);
				// EditText etPassword =
				// (EditText)findViewById(R.id.etPassword);
				// String userName = etUserName.getText().toString();
				// String password = etPassword.getText().toString();
				// String s = userName + password;
				// Intent intent = new Intent(UserLoginActivity.this,
				// ToDetVehActivity.class);
				// Bundle bundle = new Bundle();
				// bundle.putString("1", "One");
				// bundle.putString("2", "Two");
				// bundle.putString("3", "Three");
				// bundle.putInt("4", 4);
				// intent.putExtras(bundle);
				// startActivity(intent);
				// finish();

				// int i = 1;
				// float f = 2.2f;
				// String s = "hello -";
				// s += df.format(dt);
				// s += "-";
				// s += String.valueOf(true);
				// s += "-";
				// s += String.valueOf(i);
				// s += "-";
				// s += String.valueOf(f);
				// Toast tst = Toast.makeText(UserLoginActivity.this, s,
				// Toast.LENGTH_LONG);
				// tst.show();
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		this.menu = menu;
		addMenu(menu);
		// addSubMenu(menu);

		return super.onCreateOptionsMenu(menu);
	}

	private void addMenu(Menu menu) {

		MenuItem addMenuItem = menu.add(1, menuItemId++, 1, "����");
		addMenuItem.setIcon(R.drawable.options);
		// // addMenuItem.setOnMenuItemClickListener(UserLoginActivity.this);
		// MenuItem deleteMenuItem = menu.add(1, menuItemId++, 2, "ɾ��");
		// deleteMenuItem.setIcon(R.drawable.close_delete);
		// // deleteMenuItem.setOnMenuItemClickListener(this);
		// MenuItem menuItem1 = menu.add(1, menuItemId++, 3, "�˵�1");
		// // menuItem1.setOnMenuItemClickListener(this);
		// MenuItem menuItem2 = menu.add(1, menuItemId++, 4, "�˵�2");
	}

	private void addSubMenu(Menu menu) {

		// �����Ӳ˵�
		SubMenu fileSubMenu = menu.addSubMenu(1, menuItemId++, 5, "�ļ�");

		fileSubMenu.setHeaderIcon(R.drawable.check);
		// �Ӳ˵���֧��ͼ��
		MenuItem newMenuItem = fileSubMenu.add(1, menuItemId++, 1, "�½�");
		newMenuItem.setCheckable(true);
		newMenuItem.setChecked(true);
		MenuItem openMenuItem = fileSubMenu.add(2, menuItemId++, 2, "��");
		MenuItem exitMenuItem = fileSubMenu.add(2, menuItemId++, 3, "�˳�");
		exitMenuItem.setChecked(true);
		fileSubMenu.setGroupCheckable(2, true, true);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		Log.d("������", String.valueOf(item.getItemId()));
		// new
		// AlertDialog.Builder(UserLoginActivity.this).setMessage("�����ˡ�"+item.getTitle()+"��").show();
		Toast.makeText(this, "�����ˡ�" + item.getTitle() + "��",
				Toast.LENGTH_SHORT).show();

		switch (item.getItemId()) {
		case 1:
			settingMenuItemSelected();
			break;
		case 2:
			break;
		case 3:
			break;
		case 4:
			break;
		case 5:
			break;
		default:
			return super.onOptionsItemSelected(item);
		}

		return true;
	}

	private void settingMenuItemSelected() {

		startActivity(new Intent(this, SettingActivity.class));
	}

	// // ����/���η��ؼ����˵���ʵ�ִ���
	// @Override
	// public boolean onKeyDown(int keyCode, KeyEvent event) {
	// if(keyCode == KeyEvent.KEYCODE_BACK) {
	// //���/����/���η��ؼ�
	// return false;
	// } else if(keyCode == KeyEvent.KEYCODE_MENU) {
	// //���/���ز˵���
	// return false;
	// } else if(keyCode == KeyEvent.KEYCODE_HOME) {
	// //����Home��Ϊϵͳ�����˴����ܲ�����Ҫ��дonAttachedToWindow()
	// }
	// return super.onKeyDown(keyCode, event);
	// }
}