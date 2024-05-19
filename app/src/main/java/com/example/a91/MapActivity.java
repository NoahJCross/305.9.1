package com.example.a91;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;

import java.util.Arrays;
import java.util.List;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap gMap;
    private SupportMapFragment mapFragment;
    private LostItemsDbHandler lostItemsDbHandler;
    private PlacesClient placesClient;
    private ActivityResultLauncher<Intent> autocompleteLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_fragment);

        // Register the activity result launcher for handling place picker results
        autocompleteLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    Intent data = result.getData();
                    if(data != null ){
                        if (result.getResultCode() == RESULT_OK) {
                            // Handle the selected place
                            Place place = Autocomplete.getPlaceFromIntent(data);
                            handleSelectedPlace(place);
                        } else if (result.getResultCode() == AutocompleteActivity.RESULT_ERROR) {
                            // Handle the error
                            Status status = Autocomplete.getStatusFromIntent(data);
                            Toast.makeText(this, "Error: " + status.getStatusMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );

        // Initialize the database handler
        lostItemsDbHandler = new LostItemsDbHandler(this);

        // Set up the map fragment and initialize the Places API
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapFragment);
        mapFragment.getMapAsync(this);
        Places.initialize(getApplicationContext(), getString(R.string.google_maps_key));
        placesClient = Places.createClient(this);
    }

    // Start the place picker activity
    private void startPlacePicker() {
        List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG);
        Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields).build(this);
        autocompleteLauncher.launch(intent);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        gMap = googleMap;

        // Get the mode from the intent to determine what to display on the map
        String mode = getIntent().getStringExtra("mode");
        if (mode != null && mode.equals("create")) {
            // Start the place picker if mode is "create"
            startPlacePicker();
        } else if(mode != null && mode.equals("view")) {
            // Load and display markers from the database if mode is "view"
            List<String> markerLocations = lostItemsDbHandler.getAllLocations();
            for (String location : markerLocations) {
                String[] latLng = location.split(",");
                double latitude = Double.parseDouble(latLng[0]);
                double longitude = Double.parseDouble(latLng[1]);
                LatLng latLngMarker = new LatLng(latitude, longitude);
                googleMap.addMarker(new MarkerOptions().position(latLngMarker));
            }
        } else {
            // Handle the case when mode is neither "create" nor "view"
            String locationString = getIntent().getStringExtra("location");
            if(locationString != null){
                String[] latLng = locationString.split(",");
                double latitude = Double.parseDouble(latLng[0]);
                double longitude = Double.parseDouble(latLng[1]);
                LatLng location = new LatLng(latitude, longitude);
                googleMap.addMarker(new MarkerOptions().position(location));
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 12));
            }
        }
    }

    // Handle the selected place from the place picker
    private void handleSelectedPlace(Place place) {
        LatLng latLng = place.getLatLng();
        if(latLng != null) {
            gMap.addMarker(new MarkerOptions().position(latLng));
            String location = latLng.latitude + "," + latLng.longitude;

            // Prepare the result intent to send back to the previous activity
            Intent resultIntent = new Intent();
            resultIntent.putExtra("location", location);
            setResult(Activity.RESULT_OK, resultIntent);
            finish();
        }
    }
}
