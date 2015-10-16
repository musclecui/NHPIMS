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

	private static final String MOD_NAME = "用户登录"; // 模块名
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
					// 登录成功
					progressDialog.dismiss(); //进度条消失
					final String userName = msg.getData().getString("userName"); // 接受msg传递过来的参数
					// 保存最近成功登录用户
					shaPreOpe.write("username", userName);

					Intent intent = new Intent(UserLoginActivity.this,
							FunSelActivity.class);
					startActivity(intent);
					finish(); // 关闭本页面
					break;
				}
				case MSG_WHAT_LOGIN_FAILED: {
					// 登录失败
					progressDialog.dismiss(); //进度条消失
					final String errMsg = msg.getData().getString("errMsg"); // 接受msg传递过来的参数
					new AlertDialog.Builder(UserLoginActivity.this)
							.setTitle(MOD_NAME).setMessage(errMsg)
							.setPositiveButton("确定", null).show();
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
				
				// 显示ProgressDialog
				progressDialog = ProgressDialog.show(UserLoginActivity.this, "请稍候", "正在登录...", true, false);
				
				Thread userLoginThead = new Thread() {
					@Override
					public void run() {
						
						final String userName = etUserName.getText().toString();
						final String password = etPassword.getText().toString();
						
						WsErr err = new WsErr();
						WebService.UserLogin(userName, password, GloVar.curUser, err);
						if (err.errCode.equals(WsErr.ERR_NO)) {
							// 登录成功
							Message msg = new Message();
							Bundle bundle = new Bundle();
							bundle.putString("userName", userName); // 往Bundle中存放数据
							msg.setData(bundle);// msg利用Bundle传递数据
							msg.what = MSG_WHAT_LOGIN_SUCCESS;
							handler.sendMessage(msg);
						} else {
							// 登录失败
							Message msg = new Message();
							Bundle bundle = new Bundle();
							bundle.putString("errMsg", err.errMsg); // 往Bundle中存放数据
							msg.setData(bundle);// msg利用Bundle传递数据
							msg.what = MSG_WHAT_LOGIN_FAILED;
							handler.sendMessage(msg);
						}
					}
				};
				userLoginThead.start();	

//				WsErr err = new WsErr();
//				WebService.UserLogin(userName, password, GloVar.curUser, err);
//				if (err.errCode.equals(WsErr.ERR_NO)) {
//					// 登录成功
//
//					// 保存最近成功登录用户
//					shaPreOpe.write("username", userName);
//
//					Intent intent = new Intent(UserLoginActivity.this,
//							FunSelActivity.class);
//					startActivity(intent);
//					finish(); // 关闭本页面
//				} else {
//					// 登录失败
//					new AlertDialog.Builder(UserLoginActivity.this)
//					.setTitle(MOD_NAME).setMessage(err.errMsg)
//					.setPositiveButton("确定", null).show();
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

		// Log.d("你点击了", String.valueOf(item.getItemId()));
		// Toast.makeText(this, "你点击了“" + item.getTitle() + "”",
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

	// 拦截/屏蔽返回键、菜单键实现代码
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			// 监控/拦截/屏蔽返回键

			new AlertDialog.Builder(this)
					.setTitle(MOD_NAME)
					.setMessage("确定退出？")
					.setPositiveButton("确定",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									// TODO Auto-generated method stub
									finish();
									System.exit(0);
								}
							}).setNegativeButton("取消", null).show();
		} else if (keyCode == KeyEvent.KEYCODE_MENU) {
			// 监控/拦截菜单键
		} else if (keyCode == KeyEvent.KEYCODE_HOME) {
			// 由于Home键为系统键，此处不能捕获，需要重写onAttachedToWindow()
		}
		return super.onKeyDown(keyCode, event);
	}
}
