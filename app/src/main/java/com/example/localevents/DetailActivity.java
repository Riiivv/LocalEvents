package com.example.localevents;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class DetailActivity extends AppCompatActivity {

    private String link;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        TextView nameTextView = findViewById(R.id.detailNameTextView);
        TextView dateTextView = findViewById(R.id.detailDateTextView);
        TextView timeTextView = findViewById(R.id.detailTimeTextView);
        TextView descriptionTextView = findViewById(R.id.detailDescriptionTextView);

        Button openBrowserButton = findViewById(R.id.openBrowserButton);
        Button backButton = findViewById(R.id.backButton);

        Intent intent = getIntent();

        String name = intent.getStringExtra("name");
        String date = intent.getStringExtra("date");
        String time = intent.getStringExtra("time");
        String description = intent.getStringExtra("description");
        link = intent.getStringExtra("link");

        nameTextView.setText(name);
        dateTextView.setText("Dato: " + date);
        timeTextView.setText("Tid: " + time);
        descriptionTextView.setText(description);

        openBrowserButton.setOnClickListener(v -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
            startActivity(browserIntent);
        });

        backButton.setOnClickListener(v -> finish());
    }
}