package com.ninis.tiv;

import android.app.Application;

import com.ninis.tiv.data.GeoDataManager;
import com.parse.Parse;

/**
 * Created by gypark on 15. 8. 6..
 */
public class TravelerTivApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        AnalyticsTrackers.initialize(this);

        Parse.initialize(this, "XOuLLLjQedi12OJgcpoBBmJIQkrQoSNdtJ81CkyO", "PZz5tkHQh7hklynIA25hBWNEwx1rdA6eIOfAobRe");
    }
}
