package com.example.localevents;

public class Event {
    private String name;
    private String date;
    private String time; // 👈 NY
    private String shortDescription;
    private String fullDescription;
    private String link;

    public Event(String name, String date, String time, String shortDescription, String fullDescription, String link) {
        this.name = name;
        this.date = date;
        this.time = time; // 👈 NY
        this.shortDescription = shortDescription;
        this.fullDescription = fullDescription;
        this.link = link;
    }

    public String getName() {
        return name;
    }

    public String getDate() {
        return date;
    }

    public String getTime() { // 👈 NY
        return time;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public String getFullDescription() {
        return fullDescription;
    }

    public String getLink() {
        return link;
    }
}