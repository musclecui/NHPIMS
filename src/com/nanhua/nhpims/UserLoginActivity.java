package com.nanhua.nhpims;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.nanhua.lib.*;
import com.nanhua.nhpims.webservice.*;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import android.widget.*;

public class UserLoginActivity extends Activity {

	private static final String MOD_NAME = "�û���¼"; // ģ����
	private Button btnLogin;
	private EditText etUserName;
	private EditText etPassword;
	private Menu menu;
	private int menuItemId = Menu.FIRST;
	private ShaPreOpe shaPreOpe;
	private Handler handler;
	private ProgressDialog progressDialog;

	private static final int MSG_WHAT_LOGIN_SUCCESS = 1;
	private static final int MSG_WHAT_LOGIN_FAILED = 2;

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

		handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case MSG_WHAT_LOGIN_SUCCESS: {
					// ��¼�ɹ�
					progressDialog.dismiss(); //��������ʧ
					final String userName = msg.getData().getString("userName"); // ����msg���ݹ����Ĳ���
					// ��������ɹ���¼�û�
					shaPreOpe.write("username", userName);

					Intent intent = new Intent(UserLoginActivity.this,
							FunSelActivity.class);
					startActivity(intent);
					finish(); // �رձ�ҳ��
					break;
				}
				case MSG_WHAT_LOGIN_FAILED: {
					// ��¼ʧ��
					progressDialog.dismiss(); //��������ʧ
					final String errMsg = msg.getData().getString("errMsg"); // ����msg���ݹ����Ĳ���
					new AlertDialog.Builder(UserLoginActivity.this)
							.setTitle(MOD_NAME).setMessage(errMsg)
							.setPositiveButton("ȷ��", null).show();
					break;
				}
				default:
					break;
				}
			}
		};

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
				
				// ��ʾProgressDialog
				progressDialog = ProgressDialog.show(UserLoginActivity.this, "���Ժ�", "���ڵ�¼...", true, false);
				
				Thread userLoginThead = new Thread() {
					@Override
					public void run() {
						
						final String userName = etUserName.getText().toString();
						final String password = etPassword.getText().toString();
						
						WsErr err = new WsErr();
						WebService.UserLogin(userName, password, GloVar.curUser, err);
						if (err.errCode.equals(WsErr.ERR_NO)) {
							// ��¼�ɹ�
							Message msg = new Message();
							Bundle bundle = new Bundle();
							bundle.putString("userName", userName); // ��Bundle�д������
							msg.setData(bundle);// msg����Bundle��������
							msg.what = MSG_WHAT_LOGIN_SUCCESS;
							handler.sendMessage(msg);
						} else {
							// ��¼ʧ��
							Message msg = new Message();
							Bundle bundle = new Bundle();
							bundle.putString("errMsg", err.errMsg); // ��Bundle�д������
							msg.setData(bundle);// msg����Bundle��������
							msg.what = MSG_WHAT_LOGIN_FAILED;
							handler.sendMessage(msg);
						}
					}
				};
				userLoginThead.start();	

//				WsErr err = new WsErr();
//				WebService.UserLogin(userName, password, GloVar.curUser, err);
//				if (err.errCode.equals(WsErr.ERR_NO)) {
//					// ��¼�ɹ�
//
//					// ��������ɹ���¼�û�
//					shaPreOpe.write("username", userName);
//
//					Intent intent = new Intent(UserLoginActivity.this,
//							FunSelActivity.class);
//					startActivity(intent);
//					finish(); // �رձ�ҳ��
//				} else {
//					// ��¼ʧ��
//					new AlertDialog.Builder(UserLoginActivity.this)
//					.setTitle(MOD_NAME).setMessage(err.errMsg)
//					.setPositiveButton("ȷ��", null).show();
//				}
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

		// ����Ӳ˵�
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

		// Log.d("������", String.valueOf(item.getItemId()));
		// Toast.makeText(this, "�����ˡ�" + item.getTitle() + "��",
		// Toast.LENGTH_SHORT).show();

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

	// ����/���η��ؼ����˵���ʵ�ִ���
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			// ���/����/���η��ؼ�

			new AlertDialog.Builder(this)
					.setTitle(MOD_NAME)
					.setMessage("ȷ���˳���")
					.setPositiveButton("ȷ��",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									// TODO Auto-generated method stub
									finish();
									System.exit(0);
								}
							}).setNegativeButton("ȡ��", null).show();
		} else if (keyCode == KeyEvent.KEYCODE_MENU) {
			// ���/���ز˵���
		} else if (keyCode == KeyEvent.KEYCODE_HOME) {
			// ����Home��Ϊϵͳ�����˴����ܲ�����Ҫ��дonAttachedToWindow()
		}
		return super.onKeyDown(keyCode, event);
	}
}
