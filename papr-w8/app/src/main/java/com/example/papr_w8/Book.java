package com.example.papr_w8;


import java.io.Serializable;

/**
 * This is a class that describes an instance of a Book object
 */
public class Book implements Serializable {
    private String title;
    private String author;
    private String ISBN;
    private String status;
    private String cover;
    private String owner;
    private String location;
    private String id;




    /**
     * Constructor to initialize a Book with a cover provided
     * @param title
     *      This is a title string  to be set
     * @param author
     *      This is an author string to be set
     * @param ISBN
     *      This is a 13-character book ISBN string to be set
     * @param status
     *      This is the current status of a book to be set
     * @param cover
     *      This is a filename string to be set for retrieving an image from Firestore
     *
     */
    public Book(String title, String author, String ISBN, String status, String cover, String owner) {

        this.title = title;
        this.author = author;
        this.ISBN = ISBN;
        this.status = status;
        this.cover = cover;
        this.owner = owner;
        this.id = "";
    }

    /**
     * Constructor to initialize a Book with a cover provided
     * @param title
     *      This is a title string  to be set
     * @param author
     *      This is an author string to be set
     * @param ISBN
     *      This is a 13-character book ISBN string to be set
     * @param status
     *      This is the current status of a book to be set
     * @param cover
     *      This is a filename string to be set for retrieving an image from Firestore
     *
     */
    public Book(String title, String author, String ISBN, String status, String cover) {

        this.title = title;
        this.author = author;
        this.ISBN = ISBN;
        this.status = status;
        this.cover = cover;
    }

    /**
     * Constructor to initialize a Book without a cover provided
     * @param title
     *      This is a title string  to be set
     * @param author
     *      This is an author string to be set
     * @param ISBN
     *      This is a 13-character book ISBN string to be set
     * @param status
     *      This is the current status of a book to be set
     */
    public Book(String title, String author, String ISBN, String status) {

        this.title = title;
        this.author = author;
        this.ISBN = ISBN;
        this.status = status;
        this.cover = "default_book.png";
    }

    public void setId( String Id ){ this.id = Id; }

    public String getId(){ return this.id; }

    public void setLocation( String location ){ this.location = location; }

    public String getLocation(){ return this.location; }

    public void setOwner( String owner ){ this.owner = owner; }

    public String getOwner(){ return this.owner; }

    /**
     * This returns the title of a Book
     * @return
     *      Return the title of a Book
     */
    public String getTitle() {
        return this.title;
    }

    /**
     * This sets a new title of a Book
     * @param title
     *      The new title of a book
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * This returns the author of a Book
     * @return
     *      Return the author of a Book
     */
    public String getAuthor() {
        return this.author;
    }

    /**
     * This sets a new author of a Book
     * @param author
     *      The new author of a book
     */
    public void setAuthor(String author) {
        this.author = author;
    }

    /**
     * This returns the ISBN of a Book
     * @return
     *      Return the ISBN of a Book
     */
    public String getISBN() {
        return this.ISBN;
    }

    /**
     * This sets a new ISBN of a Book
     * @param ISBN
     *      The new ISBN of a Book
     */
    public void setISBN(String ISBN) {
        this.ISBN = ISBN;
    }

    /**
     * This returns the status of a Book
     * @return
     *      Return the status of a Book
     */
    public String getStatus() {
        return this.status;
    }

    /**
     * This sets the new status of a Book
     * @param status
     *      The new status of a Book
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * This returns the cover of a Book
     * @return
     *      Return the cover of a Book
     */
    public String getCover() {
        return this.cover;
    }

    /**
     * This sets the new cover of a Book
     * @param cover
     *      The new cover of a Book
     */
    public void setCover(String cover) {
        this.cover = cover;
    }
}