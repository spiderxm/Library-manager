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

public class HistoryActivity extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener stateListener;
    Button back;
    String uid, url;
    RequestQueue requestQueue;
    RecyclerView recyclerViewBooks;
    ArrayList<BookModel> bookModelArrayList;
    BookAdapter bookAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        recyclerViewBooks = findViewById(R.id.recyclerViewAllBook);
        requestQueue = Volley.newRequestQueue(this);
        firebaseAuth = FirebaseAuth.getInstance();
        back = findViewById(R.id.buttonHisBack);
//        final String Url;

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HistoryActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });

        stateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                if(firebaseUser != null)
                {
                    uid = firebaseUser.getUid();
                    url = "https://librarymanagerbackend.herokuapp.com/books/" + uid;
                    Log.e("History", uid);
                    StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            bookModelArrayList = new ArrayList<>();
                            try {
                                JSONArray jsonArray = new JSONArray(response);
                                for(int i = 0; i < jsonArray.length(); i++)
                                {
                                    JSONObject data = jsonArray.getJSONObject(i);
                                    Log.e("JSONObject", data.toString());
                                    bookModelArrayList.add(new BookModel(data.getString("book_id"), data.getString("book_name"), data.getString("author_name"), data.getString("issue_date").substring(0, 16), data.getString("department")));
                                }
//                                Toast.makeText(getApplicationContext(), bookModelArrayList.toString(), Toast.LENGTH_SHORT).show();
                                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
                                linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
                                recyclerViewBooks.setLayoutManager(linearLayoutManager);
                                bookAdapter = new BookAdapter(bookModelArrayList, getApplicationContext());
                                recyclerViewBooks.setAdapter(bookAdapter);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }, new Response.ErrorListener() {
                             @Override
                             public void onErrorResponse(VolleyError error) {
                                 Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                             }
                         });
                        requestQueue.add(stringRequest);
                }
                else
                {
                    Log.e("History", "Error Occurred");
                }
            }
        };
    }

    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(stateListener);
    }
}