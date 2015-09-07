package com.nanhua.nhpims;

import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

public class MyAdapter extends BaseAdapter {
	
	private class buttonViewHolder {
		ImageView appIcon;
		TextView appName;
		ImageButton buttonClose;
	}
	
    private LayoutInflater mInflater;
	private List<HashMap<String, Object>> list;
    private String[] keyString;
    private int[] valueViewID;
    private buttonViewHolder holder;
	
	public MyAdapter(Context context, List<HashMap<String, Object>> list,
            int resource, String from[], int to[]) {
		
		this.list = list;
        this.mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.keyString = new String[from.length];
        this.valueViewID = new int[to.length];
        System.arraycopy(from, 0, keyString, 0, from.length);
        System.arraycopy(to, 0, valueViewID, 0, to.length);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return list.get(arg0);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		return null;
	}

}
