package com.ninis.tiv.data;

import com.parse.ParseGeoPoint;
import com.parse.ParseObject;

import java.util.Date;

/**
 * Created by gypark on 15. 8. 7..
 */
public class GeoData {
    private Date mDate;
    private ParseGeoPoint mParseGeoPoint;
    private String mCityName;

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date mDate) {
        this.mDate = mDate;
    }

    public ParseGeoPoint getParseGeoPoint() {
        return mParseGeoPoint;
    }

    public void setParseGeoPoint(ParseGeoPoint mParseGeoPoint) {
        this.mParseGeoPoint = mParseGeoPoint;
    }

    public String getCityName() {
        return mCityName;
    }

    public void setCityName(String mCityName) {
        this.mCityName = mCityName;
    }

    public void setData(ParseObject parseData) {
        this.mCityName = parseData.getString("City");
        this.mDate = parseData.getDate("Date");
        this.mParseGeoPoint = parseData.getParseGeoPoint("CurrentPosition");
    }
}
