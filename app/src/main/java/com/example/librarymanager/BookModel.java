package com.example.librarymanager;

import java.util.Date;

public class BookModel {

    private String bookName;
    private String authorName;
    private String bookId;
    private Date issueDate;
    private String department;

    public BookModel(String bookName, String authorName, String bookId, Date issueDate, String department) {
        this.bookName = bookName;
        this.authorName = authorName;
        this.bookId = bookId;
        this.issueDate = issueDate;
        this.department = department;
    }

    public String getBookName() {
        return bookName;
    }

    public String getAuthorName() {
        return authorName;
    }

    public String getBookId() {
        return bookId;
    }

    public Date getIssueDate() {
        return issueDate;
    }

    public String getDepartment() {
        return department;
    }
}
