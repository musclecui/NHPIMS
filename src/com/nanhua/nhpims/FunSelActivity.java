package com.nanhua.nhpims;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.*;

public class FunSelActivity extends Activity {

	TextView tvLoginUser;
	Button btnQueryProduct;
	Button btnDeliveryRegister;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_fun_sel);

		String s = "当前登录用户：" + GloVar.curUser.userName + "("
				+ GloVar.curUser.realName + ")";
		tvLoginUser = (TextView)findViewById(R.id.tvLoginUser);
		tvLoginUser.setText(s);

		btnQueryProduct = (Button) findViewById(R.id.btnQueryProduct);
		btnDeliveryRegister = (Button) findViewById(R.id.btnDeliveryRegister);
		
		btnQueryProduct.setOnClickListener(new ClickEvent());
		btnDeliveryRegister.setOnClickListener(new ClickEvent());
	}
	
	class ClickEvent implements OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.btnQueryProduct:
				onClick_QueryProduct();
				break;
			case R.id.btnDeliveryRegister:
				onClick_DeliveryRegister();
				break;
			default:
				break;
			}
		}
	}
	
	void onClick_QueryProduct() {
		Intent intent = new Intent(FunSelActivity.this,
				QueProActivity.class);
		startActivity(intent);
	}
	
	void onClick_DeliveryRegister() {
		Intent intent = new Intent(FunSelActivity.this,
				DelRegActivity.class);
		startActivity(intent);
	}	
}
