package com.ninis.tiv.data;

import android.util.Log;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by gypark on 15. 8. 7..
 */
public class GeoDataManager {
    private static GeoDataManager ourInstance = new GeoDataManager();

    public static GeoDataManager getInstance() {
        return ourInstance;
    }

    private LinkedList<GeoData> mListGeoDatas;

    private GeoDataManager() {
        mListGeoDatas = new LinkedList<>();
    }

    public interface OnGeoDataLoadComplete {
        void onLoadComplete();
    }
    private OnGeoDataLoadComplete mOnGeoDataLoadComplete;

    public void setOnGeoDataLoadComplete(OnGeoDataLoadComplete listener) {
        mOnGeoDataLoadComplete = listener;
    }

    public void getGeoData() {
        try {
            ParseQuery<ParseObject> query = ParseQuery.getQuery("TivLocation");
            query.orderByAscending("Date");
            query.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> list, ParseException e) {
                    for( int i = 0; i < list.size(); ++i ) {
                        ParseObject item = list.get(i);
                        GeoData geoData = new GeoData();
                        geoData.setData(item);

                        mListGeoDatas.add(geoData);
                    }

                    if( mOnGeoDataLoadComplete != null )
                        mOnGeoDataLoadComplete.onLoadComplete();
                }
            });
        } catch (Exception e) {

        }
    }

    public LinkedList<GeoData> getGeoDataList() {
        return mListGeoDatas;
    }
}
