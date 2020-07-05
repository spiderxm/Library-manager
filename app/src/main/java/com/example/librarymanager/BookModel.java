package com.example.librarymanager;

import java.util.Date;

public class BookModel {

    private String bookId;
    private String bookName;
    private String authorName;
    private String issueDate;
    private String department;

    public BookModel(String bookId, String bookName, String authorName, String issueDate, String department) {
        this.bookId = bookId;
        this.bookName = bookName;
        this.authorName = authorName;
        this.issueDate = issueDate;
        this.department = department;
    }

    public String getBookId() {
        return bookId;
    }

    public String getBookName() {
        return bookName;
    }

    public String getAuthorName() {
        return authorName;
    }

    public String getIssueDate() {
        return issueDate;
    }

    public String getDepartment() {
        return department;
    }
}