package com.antares.currency.client;

import java.lang.reflect.Type;
import java.util.List;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.antares.currency.model.AvailableCurrencyResponse.Currency;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class ParsingJSONTask extends AsyncTask<Void, Void, Object> {

	private Context mContext;
	private Callback mCallback;
	private String mJson;
	private static ParsingJSONTask mTask;
	
	public static void parsing(Context context, String json, Callback callback){
		if (mTask == null){
			mTask = new ParsingJSONTask(context, json, callback);
		}
		mTask.execute();
	}
	
	private  ParsingJSONTask(Context context, String json, Callback callback) {
		this.mContext = context;
		this.mJson  = json;
		this.mCallback = callback;
	}
	
	
	
	
	
	public static interface Callback{
		public void onSuccess(Object object);
		public void onFailed();
	}


	@Override
	protected void onPostExecute(Object result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		if (mContext.getApplicationContext() != null){
			if (mCallback != null){
				if (result == null){
					mCallback.onFailed();
				}
				else{
					mCallback.onSuccess(result);
				}
			}
		}
	}



	@Override
	protected Object doInBackground(Void... params) {
		// TODO Auto-generated method stub
		Gson gson = new Gson();
		List<Currency> result = null;
		Log.e("GOMAN", "doinbackground");
		try {
			Type t = new TypeToken<List<Currency>>(){}.getType();
			result = gson.fromJson(mJson, t);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return result;
	}
}
