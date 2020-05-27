package com.example.librarymanager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class HomeActivity extends AppCompatActivity {

    Button addBookButton, deleteBookButton, issuedBookButton, historyButton, contributorsButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        addBookButton = findViewById(R.id.buttonAddBook);
        deleteBookButton = findViewById(R.id.buttonDeleteBook);
        issuedBookButton = findViewById(R.id.buttonIssuedBook);
        historyButton = findViewById(R.id.buttonHistory);
        contributorsButton = findViewById(R.id.buttonContributors);

        addBookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, AddbookActivity.class);
                startActivity(intent);
            }
        });

        deleteBookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, DeletebookActivity.class);
                startActivity(intent);
            }
        });

        issuedBookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, IssuedbookActivity.class);
                startActivity(intent);
            }
        });

        historyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, HistoryActivity.class);
                startActivity(intent);
            }
        });

        contributorsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, ContributorsActivity.class);
                startActivity(intent);
            }
        });
    }
}
