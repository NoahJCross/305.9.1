package com.example.a91;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class LANDFViewAdapter extends RecyclerView.Adapter<LANDFViewAdapter.ViewHolder> {

    // List to store lost items
    private List<LostItem> listItems;
    // Context for the adapter
    private Context context;

    // Constructor to initialize the adapter with a list of lost items and context
    public LANDFViewAdapter(List<LostItem> lostItems, Context context){
        this.listItems = lostItems;
        this.context = context;
    }

    // Create new views (invoked by the layout manager)
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the layout for a single item
        View itemView = LayoutInflater.from(context).inflate(R.layout.lost_and_found_item, parent, false);
        return new ViewHolder(itemView);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Set text for the lost item name TextView
        String lOrF = listItems.get(position).getFound() == 0 ? "Lost: " : "Found: ";
        holder.lostItemNameTextView.setText(lOrF + listItems.get(position).getLostItemName());

        // Set click listener for the lost item name TextView to navigate to item details activity
        holder.lostItemNameTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ItemDetailsActivity.class);
                long itemId = listItems.get(holder.getAdapterPosition()).getId();
                intent.putExtra("item_id", itemId);
                context.startActivity(intent);
                ((Activity)context).finish();
            }
        });
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return listItems.size();
    }

    // ViewHolder class to hold references to the views for each data item
    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView lostItemNameTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            lostItemNameTextView = itemView.findViewById(R.id.lostItemNameTextView);
        }
    }
}
