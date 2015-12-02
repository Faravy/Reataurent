package nerdcastle.faravy.info;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

public class SessionManager {
	// LogCat tag
	private static String TAG = SessionManager.class.getSimpleName();

	// Shared Preferences
	SharedPreferences pref;

	Editor editor;
	Context context;

	// Shared pref mode
	int PRIVATE_MODE = 0;

	// Shared preferences file name
	private static final String PREF_NAME = "Restaurent";
	
	private static final String KEY_IS_LOGGEDIN = "isLoggedIn";

	public SessionManager(Context context) {
		this.context = context;
		pref = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
		editor = pref.edit();
	}

	public void setLogin(boolean isLoggedIn) {

		editor.putBoolean(KEY_IS_LOGGEDIN, isLoggedIn);

		// commit changes
		editor.commit();

		Log.d(TAG, "User login session modified!");
	}
	
	public boolean isLoggedIn(){
		return pref.getBoolean(KEY_IS_LOGGEDIN, false);
	}
	
	public void setUserData(String userData) {
        // Storing data in pref
        editor.putString("userData", userData);
        editor.commit();
    }

    public String getUserData() {
        return pref.getString("userData", null);
    }
}
