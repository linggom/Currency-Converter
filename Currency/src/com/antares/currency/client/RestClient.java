package com.antares.currency.client;

import com.antares.currency.Constant.AppConstant;

import retrofit.RequestInterceptor;
import retrofit.RestAdapter;

public class RestClient {

	public static final String BASE_URL = "https://currencyconverter.p.mashape.com";
	
	private static CurrencyService mNewsService;
	
	public static CurrencyService getCurrencyService() {
		if(null == mNewsService) {
			
			RestAdapter.Builder adapterBuilder = new RestAdapter.Builder().setRequestInterceptor(new RequestInterceptor() {
				
				@Override
				public void intercept(RequestFacade request) {
					request.addHeader(AppConstant.KEY_HEADER, AppConstant.VALUE_HEADER);
					
				}
			});
			adapterBuilder .setEndpoint(BASE_URL);
			RestAdapter mAdapter = adapterBuilder .build();
			mNewsService = mAdapter.create(CurrencyService.class);
		}
		return mNewsService;
	}
}
