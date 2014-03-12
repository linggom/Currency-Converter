package com.antares.currency.client;

import java.util.List;

import com.antares.currency.model.AvailableCurrencyResponse;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Query;


public interface CurrencyService {

	/*
	 * News API
	 */
	@GET("/availablecurrencies")
	void getAvailableCurrency(Callback<List<AvailableCurrencyResponse.Currency>> callback);

	@GET("/")
	void convert(@Query("from") String from, @Query("to") String to, @Query("from_amount")Double number, Callback<AvailableCurrencyResponse.CurrencyResponse> callback);

	
	
}
