package com.example.librarymanager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class IssuedbookActivity extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    Button back;
    RequestQueue queue;
    RecyclerView issuedRecyclerView;
    ArrayList<BookModel> issuedBookList;
    BookAdapter issuedBookAdapter;
    String uid, bookUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_issuedbook);

        issuedRecyclerView = findViewById(R.id.recyclerViewIssuedBooks);
        queue = Volley.newRequestQueue(this);
        firebaseAuth = FirebaseAuth.getInstance();
        back = findViewById(R.id.buttonIssuesBack);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(IssuedbookActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                if(firebaseUser != null)
                {
                    uid = firebaseUser.getUid();
                    bookUrl = "https://librarymanagerbackend.herokuapp.com/issued_books/" + uid;
                    StringRequest stringRequest = new StringRequest(Request.Method.GET, bookUrl, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            issuedBookList = new ArrayList<>();
                            try {
                                JSONArray array = new JSONArray(response);
                                for(int i = 0; i < array.length(); i++)
                                {
                                    JSONObject details = array.getJSONObject(i);
                                    Log.e("Issued Books", details.toString());
                                    issuedBookList.add(new BookModel(details.getString("book_id"), details.getString("book_name"), details.getString("author_name"), details.getString("issue_date").substring(0, 16), details.getString("department")));
                                }
                                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
                                linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
                                issuedRecyclerView.setLayoutManager(linearLayoutManager);
                                issuedBookAdapter = new BookAdapter(issuedBookList, getApplicationContext());
                                issuedRecyclerView.setAdapter(issuedBookAdapter);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                    queue.add(stringRequest);
                }
                else
                {
                    Log.e("Issued Book Activity", "error Occurred");
                }
            }
        };

    }

    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(authStateListener);
    }
}
