package com.example.a91;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class LANDFListActivity extends AppCompatActivity {

    // UI elements
    private List<LostItem> lostItems;
    private RecyclerView lAndFRecyclerView;
    private LostItemsDbHandler lostItemsDbHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lost_and_found);

        // Initialize database handler
        lostItemsDbHandler = new LostItemsDbHandler(this);

        // Retrieve all lost items from the database
        lostItems = lostItemsDbHandler.getLostItems();

        // Initialize RecyclerView
        lAndFRecyclerView = findViewById(R.id.lAndFRecyclerView);
        LANDFViewAdapter adapter = new LANDFViewAdapter(lostItems, this);
        lAndFRecyclerView.setAdapter(adapter);

        // Set layout manager for RecyclerView
        lAndFRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}