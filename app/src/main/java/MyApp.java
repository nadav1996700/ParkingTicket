import android.app.Application;

import Utils.My_Firebase;
import Utils.Common_utils;
import Utils.My_images;

public class MyApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        My_Firebase firebase = My_Firebase.initHelper();
        My_images images = My_images.initHelper(this);
        Common_utils common_utils = Common_utils.initHelper(this);
    }
}
