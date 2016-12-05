package com.example.hrjoshi.locreader;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Iterator;

import static com.example.hrjoshi.locreader.LocFragment.Locationresults;
import static com.example.hrjoshi.locreader.MainActivity.location;

public class MapsLocation extends FragmentActivity implements OnMapReadyCallback
       // GoogleApiClient.ConnectionCallbacks,
       // GoogleApiClient.OnConnectionFailedListener,
       // LocationListener
       {
    //public static Location location;
    private GoogleMap mMap;
    private String TAG = "Location on Map";
    public LatLng latLng;
    private static int READ_LOCATION_PERMISSION = 1;
    GoogleApiClient mGoogleApiClient;
    LocationRequest mLocationRequest;
    private int UPDATE_INTERVAL = 1000*20;
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps_location);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    /*
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (shouldShowRequestPermissionRationale(android.Manifest.permission.ACCESS_FINE_LOCATION)) {
                Toast.makeText(this, "Oops! We need permission to get access to the location :(", Toast.LENGTH_SHORT).show();
            }
            requestPermissions(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, READ_LOCATION_PERMISSION);
        }

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        if(Locationresults!=null){
            Log.v(TAG,"Location list is not null");
    */
        }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == READ_LOCATION_PERMISSION) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.v(TAG, "Permission granted");
            } else {
                if (shouldShowRequestPermissionRationale(android.Manifest.permission.ACCESS_FINE_LOCATION)) {
                    Toast.makeText(this, "Please grant permissions for access to the location! :|", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Permissions denied", Toast.LENGTH_SHORT).show();
                }
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        Log.v(TAG,"Entering map!");
        if (location!=null){
            LatLng currentLatLng = new LatLng(location.getLatitude(), location.getLongitude());
            mMap.addMarker(new MarkerOptions()
                    .position(currentLatLng)
                    .title("Your current location"));
            Log.v(TAG, "LatLong is " + latLng);
        }
        double xLat = 47.606209;
        //double xLong = location.getLongitude();
        double xLong= -122.332071;
        latLng = new LatLng(location.getLatitude(),location.getLongitude());
        mMap.addMarker(new MarkerOptions()
                .position(latLng)
                .title("Your current location"));
        Iterator<LatLng> locationIterator = Locationresults.iterator();
        while (locationIterator.hasNext()){
            Log.v(TAG,locationIterator.next().toString());
            mMap.addMarker((new MarkerOptions()
                .position(locationIterator.next())
                .title("This is a new location")));
        }

        CameraUpdate center = CameraUpdateFactory.newLatLng(latLng);
        CameraUpdate zoom = CameraUpdateFactory.zoomTo(16);
        googleMap.moveCamera(center);
        googleMap.animateCamera(zoom);
    }

  /*  @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.v(TAG, "Location Services Connected");
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (shouldShowRequestPermissionRationale(android.Manifest.permission.ACCESS_FINE_LOCATION)) {
                Toast.makeText(this, "Oops! We need permission to get access to the location :(", Toast.LENGTH_SHORT).show();
            }
            requestPermissions(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, READ_LOCATION_PERMISSION);
        }
        location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if(location == null){
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,mLocationRequest,this);
            Log.v(TAG,"Location is "+location);
        }else{
            Log.v(TAG,"Location abc is "+location);
            handleNewLocation(location);
        }
    }

    public void handleNewLocation(Location location){
        //Log.i(TAG,location.toString());
        double currentLat = location.getLatitude();
        currentLat = 47.606209;
        double currentLong = location.getLongitude();
        currentLong = -122.332071;
        latLng = new LatLng(currentLat,currentLong);
        Log.v(TAG,"Your current location is " + latLng.toString());
        MarkerOptions options=new MarkerOptions().position(latLng).title("You are here");
        mMap.addMarker(new MarkerOptions()
                .position(latLng)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                .title("Your current location"))
                ;
    }
    @Override
    public void onConnectionSuspended(int i) {
    }
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    }
    @Override
    public void onLocationChanged(Location location) {
        Log.i(TAG,"Location changed to " + location.toString());
        handleNewLocation(location);
    }*/
}
