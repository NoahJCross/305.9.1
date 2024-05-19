package com.example.a91;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class ItemDetailsActivity extends AppCompatActivity {

    // UI elements
    private TextView itemNameTextView;
    private TextView dateTextView;
    private Button locationButton;
    private Button removeButton;
    private LostItemsDbHandler lostItemsDbHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_details);

        // Retrieve item ID from intent
        long itemId = getIntent().getLongExtra("item_id", 0);

        // Initialize database handler
        lostItemsDbHandler = new LostItemsDbHandler(this);

        // Retrieve lost item from database using the item ID
        LostItem lostItem = lostItemsDbHandler.getLostItemById(itemId);

        // Initialize UI elements
        itemNameTextView = findViewById(R.id.itemNameTextView);
        dateTextView = findViewById(R.id.dateTextView);


        // Set text for item name, date, and location TextViews
        String lOrF = lostItem.getFound() == 0 ? "Lost " : "Found ";
        itemNameTextView.setText(lOrF + lostItem.getLostItemName());
        String date = getFormattedDate(lostItem.getDate());
        dateTextView.setText(lOrF + date);

        locationButton = findViewById(R.id.locationButton);
        locationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ItemDetailsActivity.this, MapActivity.class);
                intent.putExtra("location", lostItem.getLocation());
                startActivity(intent);
            }
        });

        // Initialize and handle click for the remove button
        removeButton = findViewById(R.id.removeButton);
        removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Delete the lost item from the database
                lostItemsDbHandler.deleteLostItem(lostItem.getId());
                // Redirect to the list activity
                Intent intent = new Intent(ItemDetailsActivity.this, LANDFListActivity.class);
                startActivity(intent);
            }
        });
    }

    // Method to get formatted date string (e.g., Today, Yesterday, or X days ago)
    private String getFormattedDate(String date) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        Date lostDate = null;
        try {
            lostDate = sdf.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Calendar currentDate = Calendar.getInstance();
        currentDate.setTime(new Date());

        long diffInMillis = currentDate.getTimeInMillis() - lostDate.getTime();
        long daysDiff = TimeUnit.MILLISECONDS.toDays(diffInMillis);

        if (daysDiff == 0) {
            return "Today";
        } else if (daysDiff == 1) {
            return "Yesterday";
        } else {
            return daysDiff + " days ago";
        }
    }
}
