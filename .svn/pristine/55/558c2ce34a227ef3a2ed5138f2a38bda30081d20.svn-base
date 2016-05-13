package jhunplay.com.fanjie.tool;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class Msharepreference {

	public String FILENAME = "setting";
	public Context context;
	SharedPreferences sharedPreferences;

	public Msharepreference(Context context) {
		this.context = context;
		this.FILENAME = "setting";
		sharedPreferences = context.getSharedPreferences(FILENAME, Activity.MODE_PRIVATE);
	}

	public Msharepreference(Context context, String filename) {

		this.context = context;
		this.FILENAME = filename;
		if (filename == null || filename.equals("null") || filename.equals("")) {
			sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
		} else {
			sharedPreferences = context.getSharedPreferences(FILENAME, Activity.MODE_PRIVATE);
		}
	}

	public void setStringProperty(String key, String value) {
		try {
			SharedPreferences.Editor editor = sharedPreferences.edit();
			editor.putString(key, value);
			editor.commit();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	public String getStringProperty(String key) {
		try {
			return sharedPreferences.getString(key, "");
		} catch (Exception e) {
			return null;
		}
	}

	public void setIntProperty(String key, int value) {
		try {
			SharedPreferences.Editor editor = sharedPreferences.edit();
			editor.putInt(key, value);
			editor.commit();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	public int getIntProperty(String key) {
		try {
			return sharedPreferences.getInt(key, 0);
		} catch (Exception e) {
			return 0;
		}
	}

	public void setBooleanProperty(String key, Boolean value) {
		try {
			SharedPreferences.Editor editor = sharedPreferences.edit();
			editor.putBoolean(key, value);
			editor.commit();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	public Boolean getBooleanProperty(String key) {
		try {
			return sharedPreferences.getBoolean(key, false);
		} catch (Exception e) {
			return false;
		}
	}

	public  void clearseasonkey(){
		try {
			SharedPreferences.Editor editor = sharedPreferences.edit();
			editor.remove("sessiokey");
			editor.remove("user_id");
			editor.commit();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void clearProperty() {
		try {
			SharedPreferences.Editor editor = sharedPreferences.edit();
			editor.clear();
			editor.commit();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
}
