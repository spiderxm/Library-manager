package com.example.librarymanager;

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

public class DeletebookActivity extends AppCompatActivity {

    Button back;
    FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    RecyclerView recyclerViewDelete;
    ArrayList<BookModel> arrayListBooks;
    BookAdapter adapterDeleteBook;
    String uid, urlAllBooks, urlDelete;
    RequestQueue requestQueueBook, requestQueueDelete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deletebook);

        firebaseAuth = FirebaseAuth.getInstance();
        requestQueueBook = Volley.newRequestQueue(this);
        requestQueueDelete = Volley.newRequestQueue(this);
        recyclerViewDelete = findViewById(R.id.recyclerViewDelete);
        back = findViewById(R.id.buttonDelBack);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DeletebookActivity.this, HomeActivity.class);
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
                    urlAllBooks = "https://librarymanagerbackend.herokuapp.com/issued_books/" + uid;
                    StringRequest stringRequest1 = new StringRequest(Request.Method.GET, urlAllBooks, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            arrayListBooks = new ArrayList<>();
                            try {
                                JSONArray jsonArrayBooks = new JSONArray(response);
                                for(int i = 0; i < jsonArrayBooks.length(); i++)
                                {
                                    JSONObject data = jsonArrayBooks.getJSONObject(i);
                                    Log.e("Delete book", data.toString());
                                    arrayListBooks.add(new BookModel(data.getString("book_id"), data.getString("book_name"), data.getString("author_name"), data.getString("issue_date").substring(0, 16), data.getString("department")));
                                }
                                LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                                layoutManager.setOrientation(RecyclerView.VERTICAL);
                                recyclerViewDelete.setLayoutManager(layoutManager);
                                adapterDeleteBook = new BookAdapter(arrayListBooks, getApplicationContext());
                                recyclerViewDelete.setAdapter(adapterDeleteBook);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Toast.makeText(getApplicationContext(),"Error Occurred", Toast.LENGTH_SHORT).show();
                                }
                            });
                    requestQueueBook.add(stringRequest1);
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
