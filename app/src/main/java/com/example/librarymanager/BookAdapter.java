package com.example.librarymanager;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Response;

import java.util.List;
public class BookAdapter extends RecyclerView.Adapter<BookAdapter.ViewHolder> {

    private static List<BookModel> bookModelList;
    private static Context context;

    public BookAdapter(List<BookModel> bookModelList, Context context) {
        this.bookModelList = bookModelList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.booksitem, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String id = bookModelList.get(position).getBookId();
        String bName = bookModelList.get(position).getBookName();
        String aName = bookModelList.get(position).getAuthorName();
        String date = bookModelList.get(position).getIssueDate();
        String dep = bookModelList.get(position).getDepartment();
        
        holder.setData(id, bName,aName, date, dep);
    }

    @Override
    public int getItemCount() {
        return bookModelList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
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

        public void setData(String id, String bName, String aName, String date, String dep) {

            IdOfBook.setText("Book ID : " + id);
            NameOfBook.setText("Book Name : "+ bName);
            NameOfAuthor.setText("Author Name : " + aName);
            DateOfIssue.setText("Issue Date : " + date);
            Department.setText("Department : " + dep);
        }
    }
}
