package com.example.librarymanager;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class AddbookActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{

    Button back, addBook;
    EditText bookName, authorName, bookId, department;
    TextView issueDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addbook);

        back = findViewById(R.id.buttonAddBack);
        addBook = findViewById(R.id.buttonAddBook);
        bookName = findViewById(R.id.editTextBookName);
        authorName = findViewById(R.id.editTextAuthorName);
        bookId = findViewById(R.id.editTextBookId);
        issueDate = findViewById(R.id.textViewIssueDate);
        department = findViewById(R.id.editTextDepartment);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddbookActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });

        issueDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
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

    private void showDatePickerDialog()
    {
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, (DatePickerDialog.OnDateSetListener) this, Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        month += 1;    // month + 1 because here january is represented by 0, february by 1 and so on
        String date = dayOfMonth + "/" + month + "/" + year;
        issueDate.setText(date);
    }
}