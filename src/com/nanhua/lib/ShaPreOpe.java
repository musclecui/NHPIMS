/*
 * SharedPreferences²Ù×÷
 * By Cyx
 */

package com.nanhua.lib;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.nanhua.nhpims.R;

public class ShaPreOpe {

	SharedPreferences sp;
        
    public ShaPreOpe(Context c){    
    	Context context = c;
        String name = context.getResources().getString(R.string.app_name);
        sp = context.getSharedPreferences(name, Context.MODE_PRIVATE);    
    }  
 
    public String read(String key, String defValue){    
        return sp.getString(key, defValue);    
    }
 
    public void write(String key, String value){    
        Editor editor = sp.edit();    
        editor.putString(key, value);    
        editor.commit();    
    } 
}
