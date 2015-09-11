package com.nanhua.nhpims;

import java.util.ArrayList;

import android.app.ListActivity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

public class DelRegResActivity extends ListActivity {
	
	private ArrayList<String> delRegResList;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Bundle extras = getIntent().getExtras();
		delRegResList = extras.getStringArrayList("DelRegResList");
		MyAdapter myAdapter = new MyAdapter();
		setListAdapter(myAdapter);

		ListView listView = getListView();
		listView.setBackgroundColor(Color.WHITE);
	}
	
	private class MyAdapter extends BaseAdapter {
		
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return delRegResList.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return delRegResList.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			
			TextView mTextView = new TextView(getApplicationContext());
			final String result = delRegResList.get(position);
            mTextView.setText(result);
            //  ß∞‹œ‘ æ∫Ï…´
			if (-1 != result.indexOf(" ß∞‹")) {
	            mTextView.setTextSize(25);
	            mTextView.setBackgroundColor(Color.parseColor("#ADADAD"));
				mTextView.setTextColor(Color.RED);
			} else {
	            mTextView.setTextSize(18);
	            mTextView.setTextColor(Color.BLUE);
			}
			
			return mTextView;
		}

	}
}
