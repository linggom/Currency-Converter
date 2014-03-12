package com.antares.currency.preference;

public class PreferenceManager {

	public static void putInt(String key, Integer value) {
		AppPreference.getInstance().getAppPreferences().edit().putInt(key, value).commit();
	}

	public static void putString(String key, String value) {
		AppPreference.getInstance().getAppPreferences().edit().putString(key, value).commit();
	}

	public static void putBoolean(String key, Boolean value) {
		AppPreference.getInstance().getAppPreferences().edit().putBoolean(key, value).commit();
	}

	public static void putLong(String key, Long value) {
		AppPreference.getInstance().getAppPreferences().edit().putLong(key, value).commit();
	}

	public static int getInt(String key) {
		if (key == null) {
			return 0;
		}
		return AppPreference.getInstance().getAppPreferences().getInt(key, 0);
	}

	public static String getString(String key) {
		if (key == null) {
			return null;
		}
		return AppPreference.getInstance().getAppPreferences().getString(key, null);
	}

	public static Boolean getBoolean(String key) {
		if (key == null) {
			return false;
		}
		return AppPreference.getInstance().getAppPreferences().getBoolean(key, false);
	}
	public static Boolean getBoolean(String key, boolean defaultValue) {
		if (key == null) {
			return false;
		}
		return AppPreference.getInstance().getAppPreferences().getBoolean(key, defaultValue);
	}

	public static Long getLong(String key) {
		if (key == null) {
			return 0L;
		}
		return AppPreference.getInstance().getAppPreferences().getLong(key, 0L);
	}



}
