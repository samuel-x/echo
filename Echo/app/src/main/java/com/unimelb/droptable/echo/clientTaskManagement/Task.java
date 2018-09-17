package com.unimelb.droptable.echo.clientTaskManagement;

public class Task {

    public String title;
    public String address;
    public String notes;

    public Task(String title, String address, String notes) {
        this.title = title;
        this.address = address;
        this.notes = notes;
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
}
