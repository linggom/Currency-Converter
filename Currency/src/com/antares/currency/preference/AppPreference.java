package com.antares.currency.preference;

import android.content.Context;
import android.content.SharedPreferences;

import com.antares.currency.global.GlobalApplication;

public class AppPreference {
	
	private SharedPreferences mSharedPreferences;
	private static AppPreference mAppPreference;
	
	
	public static synchronized AppPreference getInstance(){
		if (mAppPreference == null ){
			mAppPreference = new AppPreference();
		}
		return mAppPreference;
	}
	
	private AppPreference(){
		mSharedPreferences = GlobalApplication.getContexts().getSharedPreferences(PreferenceConstant.PREF_MODE, Context.MODE_PRIVATE);		
		
	}
	
	public SharedPreferences getAppPreferences(){
		return mSharedPreferences;
	}
}
