package com.example.a91;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Calendar;
import java.util.Locale;

public class CreateAdvertActivity extends AppCompatActivity {

    // Declare UI elements
    private CheckBox lostCheckBox;
    private CheckBox foundCheckBox;
    private TextView createItemName;
    private TextView createPhoneNumber;
    private TextView createDescription;
    private TextView createDate;
    private Button createLocationButton;
    private Button saveButton;
    private int found = -1; // Flag to determine if item is lost or found
    private LostItemsDbHandler lostItemsDbHandler;
    private String location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_lost_item);

        lostItemsDbHandler = new LostItemsDbHandler(this); // Initialize database handler

        // Initialize UI elements
        lostCheckBox = findViewById(R.id.lostCheckBox);
        foundCheckBox = findViewById(R.id.foundCheckBox);
        createItemName = findViewById(R.id.createItemName);
        createPhoneNumber = findViewById(R.id.createPhoneNumber);
        createDescription = findViewById(R.id.createDescription);
        createDate = findViewById(R.id.createDate);
        createLocationButton = findViewById(R.id.createLocationButton);
        saveButton = findViewById(R.id.saveButton);


        ActivityResultLauncher<Intent> mapActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null && data.hasExtra("location")) {
                            location = data.getStringExtra("location");
                            if (location != null) {
                                createLocationButton.setText("Coordinates: " + location);
                            }
                        }
                    }
                });


        // Handle clicks on the "Lost" checkbox
        lostCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                found = 0; // Item is marked as lost
                foundCheckBox.setChecked(false); // Uncheck "Found" checkbox
            }
        });

        createLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreateAdvertActivity.this, MapActivity.class);
                intent.putExtra("mode", "create");
                mapActivityResultLauncher.launch(intent);
            }
        });

        // Handle clicks on the "Found" checkbox
        foundCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                found = 1; // Item is marked as found
                lostCheckBox.setChecked(false); // Uncheck "Lost" checkbox
            }
        });

        // Handle clicks on the date field to select a date
        createDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show date picker dialog
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(CreateAdvertActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDayOfMonth) {
                        // Format and display selected date
                        String formattedDate = String.format(Locale.getDefault(), "%02d/%02d/%04d", selectedDayOfMonth, selectedMonth + 1, selectedYear);
                        createDate.setText(formattedDate);
                    }
                }, year, month, dayOfMonth);

                datePickerDialog.show();
            }
        });

        // Handle click on the save button to save the item
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveItem(); // Save the item to the database
            }
        });
    }

    // Method to save the item to the database
    private void saveItem() {
        // Get user input from UI elements
        String itemName = createItemName.getText().toString();
        String phoneNumber = createPhoneNumber.getText().toString();
        String description = createDescription.getText().toString();
        String date = createDate.getText().toString();
        // Check if any field is empty or if the item type is not selected
        if (itemName.isEmpty() || phoneNumber.isEmpty() || description.isEmpty() || date.isEmpty() || location == null || found == -1) {
            // Display a toast message prompting the user to fill in all fields and select item type
            Toast.makeText(this, "Please fill in all fields and select Lost or Found.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create a LostItem object with the provided information
        LostItem item = new LostItem(itemName, phoneNumber, description, date, location, found);

        // Add the item to the database
        lostItemsDbHandler.addLostItem(item);

        // Finish the activity
        finish();
    }
}
