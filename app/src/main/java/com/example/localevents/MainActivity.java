package com.example.localevents;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ArrayList<Event> allEvents;
    private ArrayList<Event> shownEvents;
    private EventAdapter adapter;

    private Switch darkModeSwitch;
    private EditText searchEditText;
    private Button searchButton;
    private Button addEventButton;
    private Button clearAllButton;
    private ListView eventListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        SharedPreferences settings = getSharedPreferences("settings", MODE_PRIVATE);
        boolean darkMode = settings.getBoolean("darkMode", false);

        if (darkMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        searchEditText = findViewById(R.id.searchEditText);
        searchButton = findViewById(R.id.searchButton);
        addEventButton = findViewById(R.id.addEventButton);
        clearAllButton = findViewById(R.id.clearAllButton);
        darkModeSwitch = findViewById(R.id.darkModeSwitch);
        eventListView = findViewById(R.id.eventListView);

        darkModeSwitch.setChecked(darkMode);

        darkModeSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            SharedPreferences.Editor editor = settings.edit();
            editor.putBoolean("darkMode", isChecked);
            editor.apply();

            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            }
        });

        allEvents = new ArrayList<>();
        shownEvents = new ArrayList<>();

        loadEvents();

        shownEvents.addAll(allEvents);

        adapter = new EventAdapter(this, shownEvents, event -> deleteEvent(event));
        eventListView.setAdapter(adapter);

        searchButton.setOnClickListener(v -> filterEvents());

        clearAllButton.setOnClickListener(v -> clearAllEvents());

        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterEvents();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        addEventButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddEventActivity.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (adapter != null) {
            allEvents.clear();
            shownEvents.clear();

            loadEvents();

            filterEvents();
        }
    }

    private void loadEvents() {
        SharedPreferences prefs = getSharedPreferences("events", MODE_PRIVATE);

        if (!prefs.contains("count")) {
            createDefaultEvents();
            saveEvents();
            return;
        }

        int count = prefs.getInt("count", 0);

        for (int i = 0; i < count; i++) {
            String name = prefs.getString("name_" + i, "");
            String date = prefs.getString("date_" + i, "");
            String time = prefs.getString("time_" + i, "");
            String shortDescription = prefs.getString("short_" + i, "");
            String fullDescription = prefs.getString("full_" + i, "");
            String link = prefs.getString("link_" + i, "");

            allEvents.add(new Event(name, date, time, shortDescription, fullDescription, link));
        }
    }

    private void saveEvents() {
        SharedPreferences prefs = getSharedPreferences("events", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        editor.clear();
        editor.putInt("count", allEvents.size());

        for (int i = 0; i < allEvents.size(); i++) {
            Event event = allEvents.get(i);

            editor.putString("name_" + i, event.getName());
            editor.putString("date_" + i, event.getDate());
            editor.putString("time_" + i, event.getTime());
            editor.putString("short_" + i, event.getShortDescription());
            editor.putString("full_" + i, event.getFullDescription());
            editor.putString("link_" + i, event.getLink());
        }

        editor.apply();
    }

    private void createDefaultEvents() {
        allEvents.add(new Event(
                "Fællesspisning",
                "12. maj 2026",
                "18:00",
                "Kom til hyggelig fællesspisning.",
                "Vi holder fællesspisning i foreningshuset. Alle medlemmer er velkomne.",
                "https://www.google.com"
        ));

        allEvents.add(new Event(
                "Frivilligdag",
                "20. maj 2026",
                "10:00",
                "Hjælp med praktiske opgaver.",
                "På frivilligdagen hjælper vi med oprydning, maling og praktiske opgaver.",
                "https://www.google.com"
        ));
    }

    private void filterEvents() {
        String searchText = searchEditText.getText().toString().toLowerCase().trim();

        shownEvents.clear();

        for (Event event : allEvents) {
            String eventName = event.getName().toLowerCase();

            if (eventName.contains(searchText)) {
                shownEvents.add(event);
            }
        }

        adapter.notifyDataSetChanged();
    }

    private void deleteEvent(Event event) {
        allEvents.remove(event);
        saveEvents();
        filterEvents();
    }

    private void clearAllEvents() {
        new AlertDialog.Builder(this)
                .setTitle("Ryd alle begivenheder")
                .setMessage("Er du sikker på, du vil slette alle begivenheder?")
                .setPositiveButton("Ja", (dialog, which) -> {
                    allEvents.clear();
                    shownEvents.clear();
                    saveEvents();
                    adapter.notifyDataSetChanged();
                })
                .setNegativeButton("Nej", null)
                .show();
    }
}