package com.example.hrjoshi.locreader;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Iterator;

import static com.example.hrjoshi.locreader.LocFragment.Locationresults;
import static com.example.hrjoshi.locreader.MainActivity.location;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private String TAG = "MapsActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps_location);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        Log.v(TAG, "Entering map");
        LatLng currentLatLng=null;
        // Add a marker in Sydney and move the camera
        if (location!=null){
            currentLatLng = new LatLng(location.getLatitude(), location.getLongitude());
            //Log.v(TAG,"Latlong is "+currentLatLng);
            mMap.addMarker(new MarkerOptions()
                    .position(currentLatLng)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                    .title("Your current location"));
            //Log.v(TAG, "Printing location " + location.toString());
            CameraUpdate center = CameraUpdateFactory.newLatLng(currentLatLng);
            CameraUpdate zoom = CameraUpdateFactory.zoomTo(16);
            googleMap.moveCamera(center);
            googleMap.animateCamera(zoom);

            Iterator<LatLng> locationIterator = Locationresults.iterator();
            while (locationIterator.hasNext()){
                Log.v(TAG,locationIterator.next().toString());
                mMap.addMarker((new MarkerOptions()
                        .position(locationIterator.next())
                        .title("This is a new location")));

            }
        }

    }
}
