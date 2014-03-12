package com.antares.currency.global;

import android.app.Application;
import android.content.ContentResolver;
import android.content.Context;
import android.content.res.Resources;

public class GlobalApplication extends Application {
	
	private static Context mContext;
	private static Resources mResources;
	
	@Override
	public void onCreate() {
		super.onCreate();
		mContext = getApplicationContext();
		mResources = getResources();
		
		
	}
	
	public static Context getContexts(){
		return mContext;
	}
	
	public static Resources getAppResource(){
		return mResources;
	}
	
	public static ContentResolver getResolver(){
		return getContexts().getContentResolver();
	}
	
	
	
}
