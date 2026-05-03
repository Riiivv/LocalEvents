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

public class EventAdapter extends BaseAdapter {

    public interface OnEventDeleteListener {
        void onDelete(Event event);
    }

    private Context context;
    private ArrayList<Event> events;
    private OnEventDeleteListener deleteListener;

    public EventAdapter(Context context, ArrayList<Event> events, OnEventDeleteListener deleteListener) {
        this.context = context;
        this.events = events;
        this.deleteListener = deleteListener;
    }

    @Override
    public int getCount() {
        return events.size();
    }

    @Override
    public Object getItem(int position) {
        return events.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Event event = events.get(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.event_item, parent, false);
        }

        TextView nameTextView = convertView.findViewById(R.id.eventNameTextView);
        TextView dateTextView = convertView.findViewById(R.id.eventDateTextView);
        TextView timeTextView = convertView.findViewById(R.id.eventTimeTextView);
        TextView shortDescriptionTextView = convertView.findViewById(R.id.eventShortDescriptionTextView);

        Button detailsButton = convertView.findViewById(R.id.detailsButton);
        Button signupButton = convertView.findViewById(R.id.signupButton);
        Button deleteButton = convertView.findViewById(R.id.deleteButton);

        nameTextView.setText(event.getName());
        dateTextView.setText("Dato: " + event.getDate());
        timeTextView.setText("Tid: " + event.getTime());
        shortDescriptionTextView.setText(event.getShortDescription());

        detailsButton.setOnClickListener(v -> {
            Intent intent = new Intent(context, DetailActivity.class);
            intent.putExtra("name", event.getName());
            intent.putExtra("date", event.getDate());
            intent.putExtra("time", event.getTime());
            intent.putExtra("description", event.getFullDescription());
            intent.putExtra("link", event.getLink());
            context.startActivity(intent);
        });

        signupButton.setOnClickListener(v -> {
            new AlertDialog.Builder(context)
                    .setTitle("Tilmelding")
                    .setMessage("Er du sikker på, du vil tilmelde dig " + event.getName() + "?")
                    .setPositiveButton("Ja", (dialog, which) -> {
                        new AlertDialog.Builder(context)
                                .setTitle("Tilmeldt")
                                .setMessage("Du er nu tilmeldt " + event.getName())
                                .setPositiveButton("OK", null)
                                .show();
                    })
                    .setNegativeButton("Nej", null)
                    .show();
        });

        deleteButton.setOnClickListener(v -> {
            new AlertDialog.Builder(context)
                    .setTitle("Slet begivenhed")
                    .setMessage("Vil du slette " + event.getName() + "?")
                    .setPositiveButton("Ja", (dialog, which) -> deleteListener.onDelete(event))
                    .setNegativeButton("Nej", null)
                    .show();
        });

        return convertView;
    }
}