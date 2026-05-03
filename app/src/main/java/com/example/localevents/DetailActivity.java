package com.example.localevents;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

// Activity der viser detaljer for en valgt begivenhed
public class DetailActivity extends AppCompatActivity {

    // Gemmer linket så det kan bruges til browser
    private String link;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        // Binder UI elementer til variabler
        TextView nameTextView = findViewById(R.id.detailNameTextView);
        TextView dateTextView = findViewById(R.id.detailDateTextView);
        TextView timeTextView = findViewById(R.id.detailTimeTextView);
        TextView descriptionTextView = findViewById(R.id.detailDescriptionTextView);

        Button openBrowserButton = findViewById(R.id.openBrowserButton);
        Button backButton = findViewById(R.id.backButton);

        // Henter data fra Intent (sendt fra MainActivity/EventAdapter)
        Intent intent = getIntent();

        String name = intent.getStringExtra("name");
        String date = intent.getStringExtra("date");
        String time = intent.getStringExtra("time");
        String description = intent.getStringExtra("description");
        link = intent.getStringExtra("link");

        // Sætter data ind i UI
        nameTextView.setText(name);
        dateTextView.setText("Dato: " + date);
        timeTextView.setText("Tid: " + time);
        descriptionTextView.setText(description);

        // Åbner link i browser via Intent
        openBrowserButton.setOnClickListener(v -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
            startActivity(browserIntent);
        });

        // Lukker activity og går tilbage til listen
        backButton.setOnClickListener(v -> finish());
    }
}