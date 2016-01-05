package com.ninis.tiv;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.ninis.tiv.data.GeoData;
import com.ninis.tiv.data.GeoDataManager;

import java.util.LinkedList;

/**
 * Created by gypark on 15. 8. 4..
 */
public class MapviewFragment extends Fragment {

    private View mRootView;
    private GoogleMap mMap; // Might be null if Google Play services APK is not available.

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_mapview, container, false);

//        setUpMapIfNeeded();

        return mRootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }


    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #mMap} is not null.
     * <p/>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p/>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map))
                    .getMap();
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p/>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    public void setUpMap() {
        if (mMap == null)
            return;

        try {
            // Marker, Line 생성
            LinkedList<GeoData> itemList = GeoDataManager.getInstance().getGeoDataList();
            PolylineOptions polylineOptions = new PolylineOptions();
            polylineOptions.color(Color.RED);
            for (int i = 0; i < itemList.size(); ++i) {
                GeoData itemData = itemList.get(i);
                LatLng latLng = new LatLng(itemData.getParseGeoPoint().getLatitude(), itemData.getParseGeoPoint().getLongitude());

                // Marker
                MarkerOptions itemMarker = new MarkerOptions();
                itemMarker.title(itemData.getCityName());
                itemMarker.position(latLng);

                Marker marker = mMap.addMarker(itemMarker);

                // Line
                polylineOptions.add(latLng);

                // set last Position
                if( i == itemList.size()-1 ) {
                    CameraUpdate center = CameraUpdateFactory.newLatLng(latLng);
                    CameraUpdate zoom = CameraUpdateFactory.zoomTo(5);

                    mMap.moveCamera(center);
                    mMap.animateCamera(zoom);

                    marker.setSnippet("Tiv is here!!");
                    marker.showInfoWindow();
                }
            }

            Polyline polyline = mMap.addPolyline(polylineOptions);


        } catch (Exception e) {

        }
    }
}
