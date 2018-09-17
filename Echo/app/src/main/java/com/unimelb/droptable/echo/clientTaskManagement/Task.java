package com.unimelb.droptable.echo.clientTaskManagement;

import java.io.Serializable;

public class Task implements Serializable {

    private String title;
    private String address;
    private String notes;
    private String category;
    private String subcategory;

    public Task() {
        // for firebase
    }

    public Task(String title, String address, String notes, String category, String subcategory) {
        this.title = title;
        this.address = address;
        this.notes = notes;
        this.category = category;
        this.subcategory = subcategory;
    }

    public String getSubcategory() {
        return subcategory;
    }

    public void setSubcategory(String subcategory) {
        this.subcategory = subcategory;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}