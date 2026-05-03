package com.example.localevents;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

// Adapter der forbinder Event-data med ListView (viser hver begivenhed i listen)
public class EventAdapter extends BaseAdapter {

    // Interface bruges til at sende "slet event" tilbage til MainActivity
    public interface OnEventDeleteListener {
        void onDelete(Event event);
    }

    private Context context;
    private ArrayList<Event> events;
    private OnEventDeleteListener deleteListener;

    // Konstruktør der modtager data + listener til sletning
    public EventAdapter(Context context, ArrayList<Event> events, OnEventDeleteListener deleteListener) {
        this.context = context;
        this.events = events;
        this.deleteListener = deleteListener;
    }

    // Returnerer antal elementer i listen
    @Override
    public int getCount() {
        return events.size();
    }

    // Returnerer et specifikt element
    @Override
    public Object getItem(int position) {
        return events.get(position);
    }

    // Returnerer ID (her bare position)
    @Override
    public long getItemId(int position) {
        return position;
    }

    // Denne metode laver hver række (view) i ListView
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // Henter event for den aktuelle position
        Event event = events.get(position);

        // Hvis view ikke eksisterer, opretter vi det
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.event_item, parent, false);
        }

        // Finder UI elementer i layoutet
        TextView nameTextView = convertView.findViewById(R.id.eventNameTextView);
        TextView dateTextView = convertView.findViewById(R.id.eventDateTextView);
        TextView timeTextView = convertView.findViewById(R.id.eventTimeTextView);
        TextView shortDescriptionTextView = convertView.findViewById(R.id.eventShortDescriptionTextView);

        Button detailsButton = convertView.findViewById(R.id.detailsButton);
        Button signupButton = convertView.findViewById(R.id.signupButton);
        Button deleteButton = convertView.findViewById(R.id.deleteButton);

        // Sætter data ind i UI
        nameTextView.setText(event.getName());
        dateTextView.setText("Dato: " + event.getDate());
        timeTextView.setText("Tid: " + event.getTime());
        shortDescriptionTextView.setText(event.getShortDescription());

        // Når brugeren trykker "Detaljer"
        detailsButton.setOnClickListener(v -> {
            // Starter DetailActivity og sender data med Intent
            Intent intent = new Intent(context, DetailActivity.class);
            intent.putExtra("name", event.getName());
            intent.putExtra("date", event.getDate());
            intent.putExtra("time", event.getTime());
            intent.putExtra("description", event.getFullDescription());
            intent.putExtra("link", event.getLink());
            context.startActivity(intent);
        });

        // Når brugeren trykker "Tilmeld"
        signupButton.setOnClickListener(v -> {
            // Viser en bekræftelses-dialog
            new AlertDialog.Builder(context)
                    .setTitle("Tilmelding")
                    .setMessage("Er du sikker på, du vil tilmelde dig " + event.getName() + "?")
                    .setPositiveButton("Ja", (dialog, which) -> {
                        // Hvis ja → vis en bekræftelse
                        new AlertDialog.Builder(context)
                                .setTitle("Tilmeldt")
                                .setMessage("Du er nu tilmeldt " + event.getName())
                                .setPositiveButton("OK", null)
                                .show();
                    })
                    .setNegativeButton("Nej", null)
                    .show();
        });

        // Når brugeren trykker "Slet"
        deleteButton.setOnClickListener(v -> {
            new AlertDialog.Builder(context)
                    .setTitle("Slet begivenhed")
                    .setMessage("Vil du slette " + event.getName() + "?")
                    // Kalder listener (MainActivity håndterer selve sletningen)
                    .setPositiveButton("Ja", (dialog, which) -> deleteListener.onDelete(event))
                    .setNegativeButton("Nej", null)
                    .show();
        });

        return convertView;
    }
}