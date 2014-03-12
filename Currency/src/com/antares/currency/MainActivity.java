package com.antares.currency;

import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.antares.currency.client.CurrencyService;
import com.antares.currency.client.ParsingJSONTask;
import com.antares.currency.client.RestClient;
import com.antares.currency.model.AvailableCurrencyResponse;
import com.antares.currency.model.AvailableCurrencyResponse.Currency;
import com.antares.currency.model.AvailableCurrencyResponse.CurrencyResponse;
import com.antares.currency.preference.PreferenceConstant;
import com.antares.currency.preference.PreferenceManager;
import com.google.gson.Gson;

public class MainActivity extends Activity implements OnClickListener {

	private ProgressBar pbLoading;
	private LinearLayout layoutCurrency;
	private Spinner spinnerSource;
	private Spinner spinnerDestination;
	private LinearLayout layoutCustom;
	private TextView textMessage;
	private Button btnReload;
	private List<Currency> listCurrencies;
	private Button btnConvert;
	private final int btnConvertId = R.id.btnConvert;
	private final int btnReloadId = R.id.btnReload;
	private TextView tvResult;
	private EditText etFrom;
	private ProgressBar pbLoadingConvert;
	private String json;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		getActionBar().setBackgroundDrawable(new ColorDrawable(0xff3fc2d7));

		pbLoading = (ProgressBar)findViewById(R.id.pbLoading);
		layoutCurrency = (LinearLayout)findViewById(R.id.layoutCurrency);
		spinnerSource = (Spinner)findViewById(R.id.spinnerSource);
		spinnerDestination = (Spinner)findViewById(R.id.spinnerDestination);
		layoutCustom = (LinearLayout)findViewById(R.id.layoutCustom);
		textMessage = (TextView)findViewById(R.id.tvMessage);
		btnReload = (Button)findViewById(btnReloadId);
		btnConvert = (Button)findViewById(btnConvertId);
		tvResult = (TextView)findViewById(R.id.tvResult);
		etFrom = (EditText)findViewById(R.id.etFrom);
		
		pbLoadingConvert = (ProgressBar)findViewById(R.id.pbLoadingConvert);
		btnConvert.setOnClickListener(this);
		spinnerSource.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				PreferenceManager.putInt(PreferenceConstant.PREF_FROM_CURRENCY, arg2);
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}
		});

		spinnerDestination.setOnItemSelectedListener(new OnItemSelectedListener() {


			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				PreferenceManager.putInt(PreferenceConstant.PREF_TO_CURRENCY, arg2);

			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}
		});

		tvResult.setOnLongClickListener(new OnLongClickListener() {

			@Override
			public boolean onLongClick(View v) {
				// TODO Auto-generated method stub
				String value = ((TextView)v).getText().toString();
				android.content.ClipboardManager clipboard = (android.content.ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
				android.content.ClipData clip = android.content.ClipData.newPlainText("Currency", value);
				clipboard.setPrimaryClip(clip);
				Toast.makeText(MainActivity.this, "Copied to clipboard", Toast.LENGTH_LONG).show();
				return false;
			}
		});
		json = PreferenceManager.getString(PreferenceConstant.PREF_AVAILABLE_CURRENCY);
		if (json != null){
			loadFromPreferenceCurrency();
		}
		else{
			getAvailableCurrency();
		}

	}

	private void loadFromPreferenceCurrency() {
		layoutCurrency.setVisibility(View.GONE);
		pbLoading.setVisibility(View.VISIBLE);
		textMessage.setVisibility(View.GONE);
		btnReload.setVisibility(View.GONE);
		btnConvert.setVisibility(View.GONE);
		layoutCustom.setVisibility(View.VISIBLE);
		tvResult.setVisibility(View.GONE);
		etFrom.setVisibility(View.GONE);
		pbLoadingConvert.setVisibility(View.GONE);
		Log.e("GOMAN", "loadFromPreferenceCurrency");
		ParsingJSONTask.parsing(this, json, new com.antares.currency.client.ParsingJSONTask.Callback() {

			@SuppressWarnings("unchecked")
			@Override
			public void onSuccess(Object object) {
				// TODO Auto-generated method stub
				listCurrencies = (List<Currency>) object;
				ArrayAdapter<Currency> adapter = new ArrayAdapter<Currency>(MainActivity.this, R.layout.item_currency, R.id.tvItem, listCurrencies);
				int sel = PreferenceManager.getInt(PreferenceConstant.PREF_FROM_CURRENCY);;
				int sel2 = PreferenceManager.getInt(PreferenceConstant.PREF_TO_CURRENCY);
				if (sel == 0){
					for (int i = 0, b = listCurrencies.size(); i < b; i++) {
						if (listCurrencies.get(i).toString().contains("USD")){
							sel = i;
							break;
						}
					}
				}
				
				spinnerSource.setAdapter(adapter);
				spinnerDestination.setAdapter(adapter);
					
				spinnerSource.setSelection(sel);
				spinnerDestination.setSelection(sel2);
				
				layoutCustom.setVisibility(View.GONE);
				pbLoading.setVisibility(View.GONE);
				btnReload.setVisibility(View.GONE); 
				textMessage.setVisibility(View.GONE);
				layoutCurrency.setVisibility(View.VISIBLE);
				btnConvert.setVisibility(View.VISIBLE);
				etFrom.setVisibility(View.VISIBLE);
				etFrom.requestFocus();
			}

			@Override
			public void onFailed() {
				Log.e("GOMAN", "1 failed");
			}
		});

	}

	public void convert(final String source, final String destination, final double number){
		tvResult.setVisibility(View.GONE);
		CurrencyService mService = RestClient.getCurrencyService();
		pbLoadingConvert.setVisibility(View.VISIBLE);

		mService.convert(source, destination, number, new Callback<AvailableCurrencyResponse.CurrencyResponse>() {

			@Override
			public void success(CurrencyResponse arg0, Response arg1) {
				// TODO Auto-generated method stub
				tvResult.setText(destination + " " +  arg0.to_amount);
				pbLoadingConvert.setVisibility(View.GONE);
				tvResult.setVisibility(View.VISIBLE);
			}

			@Override
			public void failure(RetrofitError arg0) {
				tvResult.setVisibility(View.VISIBLE);
				pbLoadingConvert.setVisibility(View.GONE);
				tvResult.setText("Error !");

			}

		});

	}



	public void getAvailableCurrency(){
		layoutCurrency.setVisibility(View.GONE);
		pbLoading.setVisibility(View.VISIBLE);
		textMessage.setVisibility(View.GONE);
		btnReload.setVisibility(View.GONE);
		btnConvert.setVisibility(View.GONE);
		layoutCustom.setVisibility(View.VISIBLE);
		tvResult.setVisibility(View.GONE);
		etFrom.setVisibility(View.GONE);
		pbLoadingConvert.setVisibility(View.GONE);

		CurrencyService mService = RestClient.getCurrencyService();
		mService.getAvailableCurrency(new Callback<List<Currency>>() {

			@Override
			public void success(List<Currency> result, Response arg1) {
				listCurrencies = result;
				PreferenceManager.putString(PreferenceConstant.PREF_AVAILABLE_CURRENCY, new Gson().toJson(result));
				ArrayAdapter<Currency> adapter = new ArrayAdapter<Currency>(MainActivity.this, android.R.layout.simple_list_item_1, android.R.id.text1, result);
				int sel = PreferenceManager.getInt(PreferenceConstant.PREF_FROM_CURRENCY);;
				int sel2 = PreferenceManager.getInt(PreferenceConstant.PREF_TO_CURRENCY);
				if (sel == 0){
					for (int i = 0, b = listCurrencies.size(); i < b; i++) {
						if (listCurrencies.get(i).toString().contains("USD")){
							sel = i;
							break;
						}
					}
				}
				
				spinnerSource.setAdapter(adapter);
				spinnerDestination.setAdapter(adapter);
				
				spinnerSource.setSelection(sel);
				spinnerDestination.setSelection(sel2);
				
				
				layoutCustom.setVisibility(View.GONE);
				pbLoading.setVisibility(View.GONE);
				btnReload.setVisibility(View.GONE);
				textMessage.setVisibility(View.GONE);
				layoutCurrency.setVisibility(View.VISIBLE);
				btnConvert.setVisibility(View.VISIBLE);
				etFrom.setVisibility(View.VISIBLE);
				etFrom.requestFocus();

			}

			@Override
			public void failure(RetrofitError arg0) {
				Log.e("GOMAN", "2 failed");
				layoutCustom.setVisibility(View.VISIBLE);
				layoutCurrency.setVisibility(View.GONE);
				pbLoading.setVisibility(View.GONE);
				textMessage.setVisibility(View.VISIBLE);
				btnReload.setVisibility(View.VISIBLE);
				etFrom.setVisibility(View.GONE);
			}
		});
	}


	public Double getValue(EditText text){
		if (text == null)return 0.0;
		try {
			return Double.parseDouble(text.getText().toString());
		} catch (Exception e) {
			return 0.0;
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		switch (v.getId()) {
		case btnReloadId:
			getAvailableCurrency();
			break;
		case btnConvertId:

			int indexsource = spinnerSource.getSelectedItemPosition();
			int indexdest = spinnerDestination.getSelectedItemPosition();
			Currency source = listCurrencies.get(indexsource);
			Currency destination = listCurrencies.get(indexdest);
			double from = getValue(etFrom);
			if (from > 0.0){
				convert(source.id, destination.id, from);
			}
			break;
		default:
			break;
		}
	}

}
