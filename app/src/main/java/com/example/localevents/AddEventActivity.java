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

public class AddEventActivity extends AppCompatActivity {

    private EditText nameEditText;
    private EditText dateEditText;
    private EditText timeEditText;
    private EditText shortEditText;
    private EditText fullEditText;
    private EditText linkEditText;

    private Button saveEventButton;
    private Button cancelButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        nameEditText = findViewById(R.id.nameEditText);
        dateEditText = findViewById(R.id.dateEditText);
        timeEditText = findViewById(R.id.timeEditText);
        shortEditText = findViewById(R.id.shortEditText);
        fullEditText = findViewById(R.id.fullEditText);
        linkEditText = findViewById(R.id.linkEditText);

        saveEventButton = findViewById(R.id.saveEventButton);
        cancelButton = findViewById(R.id.cancelButton);

        saveEventButton.setOnClickListener(v -> saveEvent());
        cancelButton.setOnClickListener(v -> finish());

        // Åbner kalender når man trykker på datofeltet
        dateEditText.setOnClickListener(v -> showDatePicker());

        // Åbner ur når man trykker på tidsfeltet
        timeEditText.setOnClickListener(v -> showTimePicker());
    }

    private void saveEvent() {
        String name = nameEditText.getText().toString();
        String date = dateEditText.getText().toString();
        String time = timeEditText.getText().toString();
        String shortDescription = shortEditText.getText().toString();
        String fullDescription = fullEditText.getText().toString();
        String link = linkEditText.getText().toString();

        if (name.isEmpty() || date.isEmpty() || time.isEmpty()) {
            Toast.makeText(this, "Udfyld mindst navn, dato og tid", Toast.LENGTH_SHORT).show();
            return;
        }

        if (link.isEmpty()) {
            link = "https://www.google.com";
        }

        SharedPreferences prefs = getSharedPreferences("events", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        int count = prefs.getInt("count", 0);

        editor.putString("name_" + count, name);
        editor.putString("date_" + count, date);
        editor.putString("time_" + count, time);
        editor.putString("short_" + count, shortDescription);
        editor.putString("full_" + count, fullDescription);
        editor.putString("link_" + count, link);
        editor.putInt("count", count + 1);

        editor.apply();

        Toast.makeText(this, "Begivenhed gemt", Toast.LENGTH_SHORT).show();

        finish();
    }

    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    String date = selectedDay + ". " + getMonthName(selectedMonth) + " " + selectedYear;
                    dateEditText.setText(date);
                },
                year,
                month,
                day
        );

        datePickerDialog.show();
    }

    private void showTimePicker() {
        Calendar calendar = Calendar.getInstance();

        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(
                this,
                (view, selectedHour, selectedMinute) -> {
                    String time = String.format("%02d:%02d", selectedHour, selectedMinute);
                    timeEditText.setText(time);
                },
                hour,
                minute,
                true
        );

        timePickerDialog.show();
    }

    private String getMonthName(int month) {
        String[] months = {
                "januar", "februar", "marts", "april", "maj", "juni",
                "juli", "august", "september", "oktober", "november", "december"
        };

        return months[month];
    }
}