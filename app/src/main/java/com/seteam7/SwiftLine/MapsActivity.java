package com.seteam7.SwiftLine;

import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.widget.ImageButton;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.seteam7.SwiftLine.databinding.ActivityMapsBinding;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap map;
    private ActivityMapsBinding binding;
    private List<Marker> markers;
    private ImageButton refresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        WifiManager wifiMgr = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
        WifiInfo wifiInfo = wifiMgr.getConnectionInfo();
        DatabaseCtl.setupDatabaseCtl(wifiInfo.getMacAddress(), getApplicationContext());

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        refresh = (ImageButton) findViewById(R.id.RefreshButton);

        refresh.setOnClickListener(v -> {
            DatabaseCtl.setLocations(this, map);
            DatabaseCtl.updateLocations();
        });
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;

        final String indyPlaceID = "ChIJA2p5p_9Qa4gRfOq5QPadjtY";

        LatLng indy = new LatLng(39.76838, -86.15804);
        map.moveCamera(CameraUpdateFactory.newLatLng(indy));
        map.moveCamera(CameraUpdateFactory.zoomTo(11));

        DatabaseCtl.setLocations(this, map);
        map.setOnMarkerClickListener((marker) -> {
            InfoTuple info = (InfoTuple) marker.getTag();
            Intent intent = new Intent(MapsActivity.this, RestaurantActivity.class);
            intent.putExtra("id", info.id);
            intent.putExtra("ratio", info.ratio);
            intent.putExtra("waitTime", info.waitTime);
            startActivity(intent);
            return true;
        });
    }

    public void placeLocation(String loc, double ratio, int waitTime, GoogleMap nMap) {
        if (markers == null)
            markers = new ArrayList<Marker>();
        final List<Place.Field> placeFields = Arrays.asList(Place.Field.ID, Place.Field.LAT_LNG);
        if (loc == null) {
            return;
        }
        final FetchPlaceRequest request = FetchPlaceRequest.newInstance(loc, placeFields);
        for (Marker marker : markers) {
            if (((String) marker.getTag()).equals(loc)) {
                marker.setIcon(DatabaseCtl.getCorrectIconMap(ratio, this));
                return;
            }
        }
        DatabaseCtl.placesClient.fetchPlace(request).addOnSuccessListener((response) -> {
            Place place = response.getPlace();
            MarkerOptions newMarker = new MarkerOptions().position(place.getLatLng()).icon(DatabaseCtl.getCorrectIconMap(ratio, this));
            Log.d("PLACING", place.getId());
            Marker newMark = nMap.addMarker(newMarker);
            newMark.setTag(new InfoTuple(place.getId(), ratio, waitTime));
            markers.add(newMark);
        }).addOnFailureListener((exception) -> {
            if (exception instanceof ApiException) {
                final ApiException apiException = (ApiException) exception;
                Log.e("PLACING", "Place not found: " + exception.getMessage());
                final int statusCode = apiException.getStatusCode();
            }
        });
    }


    private class InfoTuple {
        public String id;
        public double ratio;
        public int waitTime;
        public InfoTuple (String id, double ratio, int waitTime) {
            this.id = id;
            this.ratio = ratio;
            this.waitTime = waitTime;
        }
    }
}