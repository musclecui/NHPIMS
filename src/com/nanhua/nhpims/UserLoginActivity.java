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

	private static final String MOD_NAME = "用户登录"; // 模块名
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
							.setTitle(MOD_NAME).setMessage("用户名不能为空")
							.setPositiveButton("确定", null).show();
					etUserName.requestFocus();
					return;
				}
				if (TextUtils.isEmpty(password)) {
					new AlertDialog.Builder(UserLoginActivity.this)
							.setTitle(MOD_NAME).setMessage("密码不能为空")
							.setPositiveButton("确定", null).show();
					etPassword.requestFocus();
					return;
				}
				if (TextUtils.isEmpty(shaPreOpe.read("ip", ""))
						|| TextUtils.isEmpty(shaPreOpe.read("port", ""))) {
					new AlertDialog.Builder(UserLoginActivity.this)
							.setTitle(MOD_NAME)
							.setMessage("服务器参数（ip或端口）为空，请设置")
							.setPositiveButton("确定", null).show();
					return;
				}

				WsErr err = new WsErr();
				WebService.UserLogin(userName, password, GloVar.curUser, err);
				if (err.errCode.equals(WsErr.ERR_NO)) {
					// 登录成功

					// 保存最近成功登录用户
					shaPreOpe.write("username", userName);
					
					Intent intent = new Intent(UserLoginActivity.this,
							FunSelActivity.class);
					startActivity(intent);
					finish(); // 关闭本页面
				} else {
					// 登录失败

					new AlertDialog.Builder(UserLoginActivity.this)
							.setTitle(MOD_NAME).setMessage(err.errMsg)
							.setPositiveButton("确定", null).show();
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
				// SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); // 设置日期格式
				// Date dt = new Date();

				// AlertDialog.Builder builder = new
				// AlertDialog.Builder(UserLoginActivity.this);
				// builder.setTitle(R.string.app_name);
				// builder.setIcon(R.drawable.home);
				// builder.setMessage("你正在登录");
				// builder.setPositiveButton("确定", new
				// DialogInterface.OnClickListener() {
				//
				// @Override
				// public void onClick(DialogInterface dialog, int which) {
				// // TODO Auto-generated method stub
				// Toast.makeText(UserLoginActivity.this, "你按了确定",
				// Toast.LENGTH_LONG).show();
				// }
				// });
				// builder.setNegativeButton("取消", new
				// DialogInterface.OnClickListener() {
				//
				// @Override
				// public void onClick(DialogInterface dialog, int which) {
				// // TODO Auto-generated method stub
				// Toast.makeText(UserLoginActivity.this, "你按了取消",
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

		MenuItem addMenuItem = menu.add(1, menuItemId++, 1, "设置");
		addMenuItem.setIcon(R.drawable.options);
		// // addMenuItem.setOnMenuItemClickListener(UserLoginActivity.this);
		// MenuItem deleteMenuItem = menu.add(1, menuItemId++, 2, "删除");
		// deleteMenuItem.setIcon(R.drawable.close_delete);
		// // deleteMenuItem.setOnMenuItemClickListener(this);
		// MenuItem menuItem1 = menu.add(1, menuItemId++, 3, "菜单1");
		// // menuItem1.setOnMenuItemClickListener(this);
		// MenuItem menuItem2 = menu.add(1, menuItemId++, 4, "菜单2");
	}

	private void addSubMenu(Menu menu) {

		// 添加子菜单
		SubMenu fileSubMenu = menu.addSubMenu(1, menuItemId++, 5, "文件");

		fileSubMenu.setHeaderIcon(R.drawable.check);
		// 子菜单不支持图像
		MenuItem newMenuItem = fileSubMenu.add(1, menuItemId++, 1, "新建");
		newMenuItem.setCheckable(true);
		newMenuItem.setChecked(true);
		MenuItem openMenuItem = fileSubMenu.add(2, menuItemId++, 2, "打开");
		MenuItem exitMenuItem = fileSubMenu.add(2, menuItemId++, 3, "退出");
		exitMenuItem.setChecked(true);
		fileSubMenu.setGroupCheckable(2, true, true);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		Log.d("你点击了", String.valueOf(item.getItemId()));
		// new
		// AlertDialog.Builder(UserLoginActivity.this).setMessage("你点击了“"+item.getTitle()+"”").show();
		Toast.makeText(this, "你点击了“" + item.getTitle() + "”",
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

	// // 拦截/屏蔽返回键、菜单键实现代码
	// @Override
	// public boolean onKeyDown(int keyCode, KeyEvent event) {
	// if(keyCode == KeyEvent.KEYCODE_BACK) {
	// //监控/拦截/屏蔽返回键
	// return false;
	// } else if(keyCode == KeyEvent.KEYCODE_MENU) {
	// //监控/拦截菜单键
	// return false;
	// } else if(keyCode == KeyEvent.KEYCODE_HOME) {
	// //由于Home键为系统键，此处不能捕获，需要重写onAttachedToWindow()
	// }
	// return super.onKeyDown(keyCode, event);
	// }
}
