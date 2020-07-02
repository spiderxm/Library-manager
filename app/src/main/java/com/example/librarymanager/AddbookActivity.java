package com.example.librarymanager;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.Date;

public class AddbookActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{

    private RequestQueue requestQueue;
    Button back, addBook;
    EditText bookName, authorName, bookId, department;
    TextView issueDate;
    FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addbook);

        firebaseAuth = FirebaseAuth.getInstance();
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

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                    FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                    if(firebaseUser != null)
                    {
                        uid = firebaseUser.getUid();
                        Log.e("Add book", uid);
                    }
                    else
                    {
                        Log.e("Add book", "Error occurred");
                    }
            }
        };

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
                    if(dateIssue == null)
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

                if(!(nameBook.isEmpty()) && !(nameAuthor.isEmpty()) && !(idBook.isEmpty()) && !(dateIssue.isEmpty()) && !(dept.isEmpty()))
                {
                    String data = "{" +
                            "\"book_id\"" + ":" + "\"" + idBook + "\"," +
                            "\"user_id\"" + ":" + "\"" + uid + "\"," +
                            "\"book_name\"" + ":" + "\"" + nameBook + "\"," +
                            "\"status\"" + ":" + "\"" + "issued" + "\"," +
                            "\"issue_date\"" + ":" + "\"" + dateIssue + "\"," +
                            "\"author_name\"" + ":" + "\"" + nameAuthor + "\"," +
                            "\"department\"" + ":" + "\"" + dept + "\"" + "}";
                    addData(data);
                }
            }

            private void addData(final String data) {
                final String details = data;
                String url = "https://librarymanagerbackend.herokuapp.com/add_book";
                requestQueue = Volley.newRequestQueue(getApplicationContext());
                StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            Toast.makeText(getApplicationContext(), jsonObject.toString(), Toast.LENGTH_SHORT).show();
                            Log.e("Post volley", response);
                        } catch (JSONException e) {
                            Toast.makeText(getApplicationContext(), "Server Error", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
            }) {
                    @Override
                    public String getBodyContentType() {
                        return "application/json; charset=utf-8";
                    }

                    @Override
                    public byte[] getBody() throws AuthFailureError {
                        try{
                            return details == null ? null : details.getBytes("utf-8");
                        } catch (UnsupportedEncodingException e) {
                            Log.e("post failure", "Unsupported Encoding while trying to get the bytes");
                            return null;
                        }
                    }
                };
                requestQueue.add(stringRequest);
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
        String date = year + "-" + month + "-" + dayOfMonth;
        issueDate.setText(date);
    }

    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(authStateListener);
    }
}