package com.example.a91;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    // Buttons for creating an advert and showing lost items
    private Button createAdvertButton;
    private Button showLostButton;
    private Button showOnMapButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize and set click listener for the create advert button
        createAdvertButton = findViewById(R.id.createAdvertButton);
        createAdvertButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to CreateAdvertActivity when create advert button is clicked
                Intent intent = new Intent(MainActivity.this, CreateAdvertActivity.class);
                startActivity(intent);
            }
        });

        // Initialize and set click listener for the show lost items button
        showLostButton = findViewById(R.id.showLostButton);
        showLostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to LANDFListActivity when show lost items button is clicked
                Intent intent = new Intent(MainActivity.this, LANDFListActivity.class);
                startActivity(intent);
            }
        });
        // Initialize and set click listener for the show all items on map button
        showOnMapButton = findViewById(R.id.showOnMapButton);
        showOnMapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MapActivity.class);
                intent.putExtra("mode", "view");
                startActivity(intent);
            }
        });
    }
}
