package com.example.librarymanager;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
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

import org.joda.time.Period;
import org.joda.time.PeriodType;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static com.example.librarymanager.App.CHANNEL_ID;

public class IssuedbookActivity extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private NotificationManagerCompat notificationManagerCompat;
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
        notificationManagerCompat = NotificationManagerCompat.from(this);

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
                                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                                    String date = details.getString("issue_date").substring(5, 16);
                                    String dept = details.getString("department").toLowerCase();
                                    String bookDetails = "Book Id: " + details.getString("book_id") + "\n" +
                                            "Book Name: " + details.getString("book_name") + "\n" +
                                            "Author Name: " + details.getString("author_name") + "\n" +
                                            "Issue Date: " + details.getString("issue_date").substring(0, 16) + "\n" +
                                            "Department: " + details.getString("department");
                                    String dayofmonth = date.substring(0, 2);
                                    String month = date.substring(3, 6);
                                    String year = date.substring(7);
                                    switch (month.toLowerCase())
                                    {
                                        case "jan": month = "01";
                                        break;
                                        case "feb": month = "02";
                                        break;
                                        case "mar": month = "03";
                                        break;
                                        case "apr": month = "04";
                                        break;
                                        case "may": month = "05";
                                        break;
                                        case "jun": month = "06";
                                        break;
                                        case "jul": month = "07";
                                        break;
                                        case "aug": month = "08";
                                        break;
                                        case "sep": month = "09";
                                        break;
                                        case "oct": month = "10";
                                        break;
                                        case "nov": month = "11";
                                        break;
                                        case "dec": month = "12";
                                        break;
                                    }
                                    date = dayofmonth + "/" + month + "/" + year;
//                                    Log.e("Issue day of month", dayofmonth);
//                                    Log.e("Issue Month", month);
//                                    Log.e("Issue Year", year);
                                    Log.e("Issue Date", date);
                                    String currentDate = simpleDateFormat.format(Calendar.getInstance().getTime());
                                    Date date1 = simpleDateFormat.parse(date);
                                    Date date2 = simpleDateFormat.parse(currentDate);
                                    long startDate = date1.getTime();
                                    long endDate = date2.getTime();
                                    if(startDate <= endDate)
                                    {
                                        Period period = new Period(startDate, endDate, PeriodType.yearDayTime());
                                        int days = period.getDays() + period.getMonths() * 30 + period.getYears() * 365;
                                        Log.e("diff", String.valueOf(days));
                                        if(days >= 25  && dept.equals("library"))
                                        {
                                            Intent activityIntent = new Intent(getApplicationContext(), IssuedbookActivity.class);
                                            PendingIntent contentIntent = PendingIntent.getActivity(getApplicationContext(), 0, activityIntent, 0);
                                            int dif = 30 - days;
                                            if(dif == 1)
                                            {
                                                bookDetails += "\n\n" + "Return this book within a day";
                                            }
                                            else if(dif == 0)
                                            {
                                                bookDetails += "\n\n" + "Return this book today!";
                                            }
                                            else if(dif > 1)
                                            {
                                                bookDetails += "\n\n" + "Return this book within " + dif + " days";
                                            }
                                            else
                                            {
                                                bookDetails += "\n\n" + "Return this book as soon as possible";
                                            }
                                            Notification notification = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
                                                    .setSmallIcon(R.drawable.icon)
                                                    .setContentTitle("Return Book")
                                                    .setContentText( details.getString("book_name"))
                                                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                                                    .setContentIntent(contentIntent)
                                                    .setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000})
                                                    .setLights(Color.RED, 3000, 3000)
                                                    .setGroup("Notifications")
                                                    .setStyle(new NotificationCompat.BigTextStyle()
                                                    .bigText(bookDetails))
                                                    .build();

                                            notificationManagerCompat.notify(1, notification);
                                        }

                                        if(days >= 175 && dept.equals("book bank"))
                                        {
                                            Intent activityIntent = new Intent(getApplicationContext(), IssuedbookActivity.class);
                                            PendingIntent contentIntent = PendingIntent.getActivity(getApplicationContext(), 0, activityIntent, 0);
                                            int dif = 180 - days;
                                            if(dif == 1)
                                            {
                                                bookDetails += "\n\n" + "Return this book within a day";
                                            }
                                            else if(dif == 0)
                                            {
                                                bookDetails += "\n\n" + "Return this book today!";
                                            }
                                            else if(dif > 1)
                                            {
                                                bookDetails += "\n\n" + "Return this book within " + dif + " days";
                                            }
                                            else
                                            {
                                                bookDetails += "\n\n" + "Return this book as soon as possible";
                                            }
                                            Notification notification = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
                                                    .setSmallIcon(R.drawable.icon)
                                                    .setContentTitle("Return Book")
                                                    .setContentText( details.getString("book_name"))
                                                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                                                    .setContentIntent(contentIntent)
                                                    .setGroup("Notifications")
                                                    .setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000})
                                                    .setLights(Color.RED, 3000, 3000)
                                                    .setStyle(new NotificationCompat.BigTextStyle()
                                                            .bigText(bookDetails))
                                                    .build();

                                            notificationManagerCompat.notify(1, notification);
                                        }
                                    }
//                                    Toast.makeText(getApplicationContext(), currentDate, Toast.LENGTH_SHORT).show();
                                }
                                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
                                linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
                                issuedRecyclerView.setLayoutManager(linearLayoutManager);
                                issuedBookAdapter = new BookAdapter(issuedBookList, getApplicationContext());
                                issuedRecyclerView.setAdapter(issuedBookAdapter);

                            } catch (JSONException | ParseException e) {
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
