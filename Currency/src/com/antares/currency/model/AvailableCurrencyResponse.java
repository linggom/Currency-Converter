package com.antares.currency.model;


public class AvailableCurrencyResponse {

	public CurrencyItem listOfCurrencies;

	public class CurrencyItem{
		public Currency currency;
	}

	public class Currency{
		public String id;
		public String description;

		@Override
		public String toString() {
			// TODO Auto-generated method stub
			return "(" + id + ") " + description;
		}
	}

	public class CurrencyResponse{
		public String from;
		public String to;
		public String from_amount;
		public String to_amount;	
	}
}

