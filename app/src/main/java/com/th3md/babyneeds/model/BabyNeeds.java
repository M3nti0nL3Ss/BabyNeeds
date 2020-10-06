package com.th3md.babyneeds.model;

public class BabyNeeds {
    private int id;

    private String title;
    private int qty;
    private String color;
    private int size;
    private String dateAdded;

    public BabyNeeds(){}

    public BabyNeeds(int id, String title, int qty, String color, int size, String dateAdded) {
        this.id = id;
        this.title = title;
        this.qty = qty;
        this.color = color;
        this.size = size;
        this.dateAdded = dateAdded;
    }

    public BabyNeeds(String title, int qty, String color, int size) {
        this.id = id;
        this.title = title;
        this.qty = qty;
        this.color = color;
        this.size = size;
    }

    public BabyNeeds(String title, int qty, String color, int size,String dateAdded) {
        this.title = title;
        this.qty = qty;
        this.color = color;
        this.size = size;
        this.dateAdded = dateAdded;
    }

    public String getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(String dateAdded) {
        this.dateAdded = dateAdded;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
}
