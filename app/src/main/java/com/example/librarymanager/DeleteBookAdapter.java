package com.example.librarymanager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.Date;
import java.util.List;

public class DeleteBookAdapter extends RecyclerView.Adapter<DeleteBookAdapter.ViewHolder> {

    private List<BookModel> bookModelList;

    public DeleteBookAdapter(List<BookModel> bookModelList) {
        this.bookModelList = bookModelList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.delete_book, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String BookName = bookModelList.get(position).getBookName();
        String AuthorName = bookModelList.get(position).getAuthorName();
        String BookId = bookModelList.get(position).getBookId();
        Date IssueDate = bookModelList.get(position).getIssueDate();
        String Department = bookModelList.get(position).getDepartment();

        ViewHolder.setData(BookName, AuthorName, BookId, IssueDate, Department);
    }

    @Override
    public int getItemCount() {
        return bookModelList.size();
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

        private static TextView nameOfBook;
        private static TextView nameOfAuthor;
        private static TextView id;
        private static TextView dateOfIssue;
        private static TextView dept;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            nameOfBook = itemView.findViewById(R.id.textBookName);
            nameOfAuthor = itemView.findViewById(R.id.textAuthorName);
            id = itemView.findViewById(R.id.textBookId);
            dateOfIssue = itemView.findViewById(R.id.textIssueDate);
            dept = itemView.findViewById(R.id.textDepartment);

        }

        public static void setData(String bookName, String authorName, String bookId, Date issueDate, String department) {

            nameOfBook.setText(bookName);
            nameOfAuthor.setText(authorName);
            id.setText(bookId);
            dateOfIssue.setText((CharSequence) issueDate);
            dept.setText(department);

        }
    }
}
