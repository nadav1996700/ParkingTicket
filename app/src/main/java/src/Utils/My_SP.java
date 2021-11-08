package src.Utils;

import android.content.Context;
import android.content.SharedPreferences;

public class My_SP {
    private static My_SP instance;
    private SharedPreferences prefs;

    public static My_SP getInstance() {
        return instance;
    }

    private My_SP(Context context) {
        prefs = context.getApplicationContext().getSharedPreferences("App_SP_DB", Context.MODE_PRIVATE);
    }

    public static My_SP initHelper(Context context) {
        if (instance == null)
            instance = new My_SP(context);
        return instance;
    }

    // save String object to SharedPreferences
    public void saveString(String data, String key) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(key, data);
        editor.apply();
    }

    // load string from SharedPreferences
    public String loadData(String key) {
        return prefs.getString(key, null);
    }
}
