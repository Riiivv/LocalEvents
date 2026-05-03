package com.example.localevents;

// Modelklasse der repræsenterer en begivenhed
public class Event {

    // Felter (data) for en begivenhed
    private String name;              // Navn på begivenheden
    private String date;              // Dato
    private String time;              // Tidspunkt
    private String shortDescription;  // Kort beskrivelse (vises i liste)
    private String fullDescription;   // Fuld beskrivelse (vises i detaljer)
    private String link;              // Link til ekstern side

    // Konstruktør der opretter en ny Event med alle værdier
    public Event(String name, String date, String time, String shortDescription, String fullDescription, String link) {
        this.name = name;
        this.date = date;
        this.time = time;
        this.shortDescription = shortDescription;
        this.fullDescription = fullDescription;
        this.link = link;
    }

    // Getter-metoder bruges til at hente data fra objektet

    public String getName() {
        return name;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
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