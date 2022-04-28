package src;

import android.app.Application;

import src.Utils.Common_utils;
import src.Utils.My_Firebase;
import src.Utils.My_SP;
import src.Utils.My_images;

public class MyApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        My_Firebase firebase = My_Firebase.initHelper();
        My_images images = My_images.initHelper(this);
        Common_utils common_utils = Common_utils.initHelper(this);
        My_SP my_sp = My_SP.initHelper(this);
    }
}
