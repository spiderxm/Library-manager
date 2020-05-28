package com.example.librarymanager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Date;

public class AddbookActivity extends AppCompatActivity {

    Button back, addBook;
    EditText bookName, authorName, bookId, issueDate, department;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addbook);

        back = findViewById(R.id.buttonAddBack);
        addBook = findViewById(R.id.buttonAddBook);
        bookName = findViewById(R.id.editTextBookName);
        authorName = findViewById(R.id.editTextAuthorName);
        bookId = findViewById(R.id.editTextBookId);
        issueDate = findViewById(R.id.editTextIssueDate);
        department = findViewById(R.id.editTextDepartment);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddbookActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });

        addBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String nameBook = bookName.getText().toString();
                String nameAuthor = authorName.getText().toString();
                String idBook = bookId.getText().toString();
                String dateIssue = issueDate.getText().toString();
                String dept = department.getText().toString();

                if(nameBook.isEmpty() && nameAuthor.isEmpty() && idBook.isEmpty() && dateIssue.isEmpty() && dept.isEmpty())
                {
                    Toast.makeText(getApplicationContext(), "Please fill the details", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    if(nameBook.isEmpty())
                    {
                        bookName.setError("Please enter Book Name");
                        bookName.requestFocus();
                    }
                    if(nameAuthor.isEmpty())
                    {
                        authorName.setError("Please enter Author Name");
                        authorName.requestFocus();
                    }
                    if(idBook.isEmpty())
                    {
                        bookId.setError("Please enter Book ID");
                        bookId.requestFocus();
                    }
                    if(dateIssue.isEmpty())
                    {
                        issueDate.setError("Please enter Issue Date");
                        issueDate.requestFocus();
                    }
                    if(dept.isEmpty())
                    {
                        department.setError("Please enter Department");
                        department.requestFocus();
                    }
                }
            }
        });

    }
}
