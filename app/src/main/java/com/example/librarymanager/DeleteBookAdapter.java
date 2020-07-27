package com.example.librarymanager;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;


public class DeleteBookAdapter extends RecyclerView.Adapter<DeleteBookAdapter.ViewHolder> {

    private static List<BookModel> modelList;
    private static Context context;
    private static String idUser;

    public DeleteBookAdapter(List<BookModel> modelList, Context context, String idUser) {
        this.modelList = modelList;
        this.context = context;
        this.idUser = idUser;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.booksitem, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        String id = modelList.get(position).getBookId();
        String bName = modelList.get(position).getBookName();
        String aName = modelList.get(position).getAuthorName();
        String date = modelList.get(position).getIssueDate();
        String dep = modelList.get(position).getDepartment();

        holder.displayData(id, bName,aName, date, dep);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BookModel bookModel = modelList.get(position);
                String bookId = bookModel.getBookId();

                String urlDelete = "http://librarymanagerbackend.herokuapp.com/deletebook/" + idUser + "/" + bookId;
                final String data = "{" +
                        "\"user_id\"" + "\"" + idUser + "\"," +
                        "\"book_id\"" + "\"" + bookId + "\"" + "}";
                RequestQueue requestQueueDelete = Volley.newRequestQueue(context);
                StringRequest stringRequestDelete = new StringRequest(Request.Method.DELETE, urlDelete,
                        new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            Log.e("Delete Book", jsonObject.toString());
                            Toast.makeText(context, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(context, HomeActivity.class);
                            context.startActivity(intent);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {
                    @Override
                    public byte[] getBody() throws AuthFailureError {
                        try{
                            return data == null ? null : data.getBytes("utf-8");
                        } catch (UnsupportedEncodingException e) {
                            Log.e("post failure", "Unsupported Encoding while trying to get the bytes");
                            return null;
                        }
                    }
                };
                requestQueueDelete.add(stringRequestDelete);
            }
        });
    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private static TextView IdOfBook;
        private static TextView NameOfBook;
        private static TextView NameOfAuthor;
        private static TextView DateOfIssue;
        private static TextView Department;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            IdOfBook = itemView.findViewById(R.id.textViewBID);
            NameOfBook = itemView.findViewById(R.id.textViewBName);
            NameOfAuthor = itemView.findViewById(R.id.textViewAName);
            DateOfIssue = itemView.findViewById(R.id.textViewIDate);
            Department = itemView.findViewById(R.id.textDept);
        }

        public void displayData(String id, String bName, String aName, String date, String dep) {
            IdOfBook.setText("Book ID : " + id);
            NameOfBook.setText("Book Name : "+ bName);
            NameOfAuthor.setText("Author Name : " + aName);
            DateOfIssue.setText("Issue Date : " + date);
            Department.setText("Department : " + dep);
        }
    }
}
