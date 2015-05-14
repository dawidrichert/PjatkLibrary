package main.java.com.dawidrichert.database.model;

public class Book {

    private int id = -1;
    private String name;
    private String author;
    private String publisher;
    private String publicationYear;

    public Book() { }

    public Book(String name, String author, String publisher, String publicationYear) {
        this.name = name;
        this.author = author;
        this.publisher = publisher;
        this.publicationYear = publicationYear;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getPublicationYear() {
        return publicationYear;
    }

    public void setPublicationYear(String publicationYear) {
        this.publicationYear = publicationYear;
    }

    public boolean isNew() {
        return -1 == id;
    }
}
