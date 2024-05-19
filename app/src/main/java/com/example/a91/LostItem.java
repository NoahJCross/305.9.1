package com.example.a91;
public class LostItem {
    // Properties of a lost item
    private long id;
    private String lostItemName;
    private String phoneNumber;
    private String description;
    private String date;
    private String location;
    private int found;

    // Constructors
    public LostItem() {
    }

    public LostItem(String lostItemName, String phoneNumber, String description, String dateLost, String location, int found) {
        this.lostItemName = lostItemName;
        this.phoneNumber = phoneNumber;
        this.description = description;
        this.date = dateLost;
        this.location = location;
        this.found = found;
    }

    // Getters and setters for the properties
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getLostItemName() {
        return lostItemName;
    }

    public void setLostItemName(String lostItemName) {
        this.lostItemName = lostItemName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getFound() {
        return found;
    }

    public void setFound(int found) {
        this.found = found;
    }
}
