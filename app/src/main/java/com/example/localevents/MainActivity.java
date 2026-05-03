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

// Hovedskærmen i appen, hvor brugeren kan se, søge, oprette og slette begivenheder
public class MainActivity extends AppCompatActivity {

    // allEvents indeholder alle begivenheder
    // shownEvents indeholder kun dem der vises efter søgning/filter
    private ArrayList<Event> allEvents;
    private ArrayList<Event> shownEvents;
    private EventAdapter adapter;

    // UI elementer
    private Switch darkModeSwitch;
    private EditText searchEditText;
    private Button searchButton;
    private Button addEventButton;
    private Button clearAllButton;
    private ListView eventListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // Henter gemt dark mode indstilling før layout vises
        SharedPreferences settings = getSharedPreferences("settings", MODE_PRIVATE);
        boolean darkMode = settings.getBoolean("darkMode", false);

        // Sætter appens tema til dark mode eller light mode
        if (darkMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Binder UI elementer til variabler
        searchEditText = findViewById(R.id.searchEditText);
        searchButton = findViewById(R.id.searchButton);
        addEventButton = findViewById(R.id.addEventButton);
        clearAllButton = findViewById(R.id.clearAllButton);
        darkModeSwitch = findViewById(R.id.darkModeSwitch);
        eventListView = findViewById(R.id.eventListView);

        // Viser om dark mode allerede er slået til
        darkModeSwitch.setChecked(darkMode);

        // Gemmer brugerens dark mode valg
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

        // Opretter listerne
        allEvents = new ArrayList<>();
        shownEvents = new ArrayList<>();

        // Henter begivenheder fra SharedPreferences
        loadEvents();

        // Viser alle events til at starte med
        shownEvents.addAll(allEvents);

        // Adapteren forbinder data med ListView
        adapter = new EventAdapter(this, shownEvents, event -> deleteEvent(event));
        eventListView.setAdapter(adapter);

        // Søgeknap filtrerer begivenheder
        searchButton.setOnClickListener(v -> filterEvents());

        // Knap der rydder alle begivenheder
        clearAllButton.setOnClickListener(v -> clearAllEvents());

        // Filtrerer automatisk mens brugeren skriver
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Bruges ikke, men skal være med pga. TextWatcher
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterEvents();
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Bruges ikke, men skal være med pga. TextWatcher
            }
        });

        // Åbner skærmen hvor man kan oprette ny begivenhed
        addEventButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddEventActivity.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Når brugeren kommer tilbage fra AddEventActivity, opdateres listen
        if (adapter != null) {
            allEvents.clear();
            shownEvents.clear();

            loadEvents();

            filterEvents();
        }
    }

    // Henter gemte begivenheder fra SharedPreferences
    private void loadEvents() {
        SharedPreferences prefs = getSharedPreferences("events", MODE_PRIVATE);

        // Hvis appen åbnes første gang, oprettes standard-events
        if (!prefs.contains("count")) {
            createDefaultEvents();
            saveEvents();
            return;
        }

        int count = prefs.getInt("count", 0);

        // Gennemgår alle gemte events og tilføjer dem til listen
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

    // Gemmer alle begivenheder i SharedPreferences
    private void saveEvents() {
        SharedPreferences prefs = getSharedPreferences("events", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        // Rydder gamle data, så listen gemmes korrekt igen
        editor.clear();

        // Gemmer antal events
        editor.putInt("count", allEvents.size());

        // Gemmer hvert event med unikke keys
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

    // Standardbegivenheder der vises første gang appen bruges
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

    // Filtrerer listen ud fra det brugeren skriver i søgefeltet
    private void filterEvents() {
        String searchText = searchEditText.getText().toString().toLowerCase().trim();

        shownEvents.clear();

        for (Event event : allEvents) {
            String eventName = event.getName().toLowerCase();

            if (eventName.contains(searchText)) {
                shownEvents.add(event);
            }
        }

        // Fortæller adapteren at listen er ændret
        adapter.notifyDataSetChanged();
    }

    // Sletter én valgt begivenhed
    private void deleteEvent(Event event) {
        allEvents.remove(event);
        saveEvents();
        filterEvents();
    }

    // Viser dialog og rydder alle begivenheder hvis brugeren siger ja
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