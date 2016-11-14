package com.example.map;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.location.LocationListener;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.*;


import java.util.ArrayList;
import java.util.List;


import static com.example.map.R.id.map;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    private static final String TAG = "MapsActivity";
    public GoogleMap mMap;
    private ArrayList<LatLng> points;
    Polyline line;
    Marker now;
    double lat1;
    double lon1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        points = new ArrayList<LatLng>();
        setContentView(R.layout.activity_maps);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(map);
        mapFragment.getMapAsync(this);

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (!mMap.isMyLocationEnabled())
            mMap.setMyLocationEnabled(true);

        LocationManager lm = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        Location myLocation = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);


        if (myLocation == null) {
            Criteria criteria = new Criteria();
            criteria.setAccuracy(Criteria.ACCURACY_COARSE);
            String provider = lm.getBestProvider(criteria, true);
            myLocation = lm.getLastKnownLocation(provider);
        }

        if (myLocation != null) {
            LatLng userLocation = new LatLng(myLocation.getLatitude(), myLocation.getLongitude());

            lat1=myLocation.getLatitude();
            lon1=myLocation.getLongitude();

            mMap.addMarker(new MarkerOptions()
                    .position(userLocation)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
                    .title("Welcome ")
                    .snippet("Latitude:"+lat1+",Longitude:"+lon1)
            );

            Log.v(TAG, "Lat1=" + lat1);
            Log.v(TAG, "Long1=" + lon1);

            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 20), 1500, null);


            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, new LocationListener() {
                @Override
                public void onLocationChanged(Location myLocation) {

                    // Getting latitude of the current location
                    double latitude = myLocation.getLatitude();

                    // Getting longitude of the current location
                    double longitude = myLocation.getLongitude();

                    // Creating a LatLng object for the current location
                    LatLng latLng = new LatLng(latitude, longitude);

                    //Adding new marker
                    now = mMap.addMarker(new MarkerOptions()
                            .icon(BitmapDescriptorFactory
                            .defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                            .position(latLng).title("New")
                            .snippet("Latitude:"+lat1+",Longitude:"+lon1)
                    );

                    // Showing the current location in Google Map
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));

                    // Zoom in the Google Map
                    mMap.animateCamera(CameraUpdateFactory.zoomTo(20));

                    //Draw polyline
                    drawPolygon(latitude, longitude);


                }

                @Override
                public void onProviderDisabled(String provider) {
                    // TODO Auto-generated method stub
                }
                @Override
                public void onProviderEnabled(String provider) {
                    // TODO Auto-generated method stub
                }
                @Override
                public void onStatusChanged(String provider, int status,
                                            Bundle extras) {
                    // TODO Auto-generated method stub
                }
            });

        }


        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }


        mMap.getUiSettings().setZoomControlsEnabled(true);
    }

     private void drawPolygon( double latitude, double longitude) {

         List<LatLng> polygon = new ArrayList<>();
            //old lat and long
             polygon.add(new LatLng(lat1, lon1));
            //new lat and long
             polygon.add(new LatLng(latitude,longitude));

            //global edge lat and long
            // polygon.add(new LatLng(12.917588955680403,77.50054121017456));

             mMap.addPolygon(new PolygonOptions()
                     .addAll(polygon)
                     .strokeColor(Color.CYAN)
                     .strokeWidth(15)
                     .fillColor(Color.CYAN)
                     );

         lat1=latitude;
         lon1=longitude;
     }

}
