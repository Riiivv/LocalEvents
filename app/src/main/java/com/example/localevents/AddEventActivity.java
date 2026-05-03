package com.example.localevents;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

// Activity til at oprette en ny begivenhed
public class AddEventActivity extends AppCompatActivity {

    // Inputfelter til brugerens data
    private EditText nameEditText;
    private EditText dateEditText;
    private EditText timeEditText;
    private EditText shortEditText;
    private EditText fullEditText;
    private EditText linkEditText;

    // Knapper til at gemme eller annullere
    private Button saveEventButton;
    private Button cancelButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        // Binder UI elementer til variabler
        nameEditText = findViewById(R.id.nameEditText);
        dateEditText = findViewById(R.id.dateEditText);
        timeEditText = findViewById(R.id.timeEditText);
        shortEditText = findViewById(R.id.shortEditText);
        fullEditText = findViewById(R.id.fullEditText);
        linkEditText = findViewById(R.id.linkEditText);

        saveEventButton = findViewById(R.id.saveEventButton);
        cancelButton = findViewById(R.id.cancelButton);

        // Når brugeren trykker "Gem"
        saveEventButton.setOnClickListener(v -> saveEvent());

        // Når brugeren trykker "Annuller"
        cancelButton.setOnClickListener(v -> finish());

        // Åbner kalender når man trykker på datofeltet
        dateEditText.setOnClickListener(v -> showDatePicker());

        // Åbner ur når man trykker på tidsfeltet
        timeEditText.setOnClickListener(v -> showTimePicker());
    }

    // Gemmer begivenheden i SharedPreferences
    private void saveEvent() {
        // Henter input fra felterne
        String name = nameEditText.getText().toString();
        String date = dateEditText.getText().toString();
        String time = timeEditText.getText().toString();
        String shortDescription = shortEditText.getText().toString();
        String fullDescription = fullEditText.getText().toString();
        String link = linkEditText.getText().toString();

        // Tjekker at de vigtigste felter er udfyldt
        if (name.isEmpty() || date.isEmpty() || time.isEmpty()) {
            Toast.makeText(this, "Udfyld mindst navn, dato og tid", Toast.LENGTH_SHORT).show();
            return;
        }

        // Standard link hvis brugeren ikke skriver noget
        if (link.isEmpty()) {
            link = "https://www.google.com";
        }

        // Henter SharedPreferences (lokal lagring)
        SharedPreferences prefs = getSharedPreferences("events", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        // Finder næste ledige plads i listen
        int count = prefs.getInt("count", 0);

        // Gemmer data med unikke keys
        editor.putString("name_" + count, name);
        editor.putString("date_" + count, date);
        editor.putString("time_" + count, time);
        editor.putString("short_" + count, shortDescription);
        editor.putString("full_" + count, fullDescription);
        editor.putString("link_" + count, link);

        // Opdaterer antal events
        editor.putInt("count", count + 1);

        // Gemmer ændringer
        editor.apply();

        // Giver feedback til brugeren
        Toast.makeText(this, "Begivenhed gemt", Toast.LENGTH_SHORT).show();

        // Lukker activity og går tilbage
        finish();
    }

    // Viser en kalender hvor brugeren kan vælge dato
    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    // Formatterer dato i dansk format
                    String date = selectedDay + ". " + getMonthName(selectedMonth) + " " + selectedYear;
                    dateEditText.setText(date);
                },
                year,
                month,
                day
        );

        datePickerDialog.show();
    }

    // Viser en time picker hvor brugeren kan vælge tidspunkt
    private void showTimePicker() {
        Calendar calendar = Calendar.getInstance();

        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(
                this,
                (view, selectedHour, selectedMinute) -> {
                    // Formatterer tid til fx 18:00
                    String time = String.format("%02d:%02d", selectedHour, selectedMinute);
                    timeEditText.setText(time);
                },
                hour,
                minute,
                true // 24-timers format
        );

        timePickerDialog.show();
    }

    // Konverterer månedstal til dansk navn
    private String getMonthName(int month) {
        String[] months = {
                "januar", "februar", "marts", "april", "maj", "juni",
                "juli", "august", "september", "oktober", "november", "december"
        };

        return months[month];
    }
}