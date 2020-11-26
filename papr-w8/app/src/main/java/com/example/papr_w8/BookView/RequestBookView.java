package com.example.papr_w8.BookView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.papr_w8.AddPack.AddBook;
import com.example.papr_w8.Book;
import com.example.papr_w8.Host;
import com.example.papr_w8.MainActivity;
import com.example.papr_w8.R;
import com.example.papr_w8.ShelfPack.BooksOwned;
import com.example.papr_w8.ShelfPack.Shelves;
import com.example.papr_w8.SignUpActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * This is a Fragment that displays the view of a Book Description that is Owned providing
 * options for the Owner to Edit, View Requests, or Delete Book.
 */
public class RequestBookView extends BookBase {

    private String fileName;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseUser user = firebaseAuth.getInstance().getCurrentUser();
    private FirebaseFirestore fbDB = FirebaseFirestore.getInstance();
    private String userEmail = user.getEmail();
    private String userId = user.getUid();

//    public static RequestBookView newInstance(Book book) {
//        Bundle args = new Bundle();
//        args.putSerializable("book", book);
//        Log.d("CREATION", "Book list was clicked");
//
//        RequestBookView fragment  = new RequestBookView();
//        fragment.setArguments(args);
//        return fragment;
//    }



    @Override
    public void onCreate( Bundle savedInstanceState ){
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public void provideYourFragmentView(final View baseView, ViewGroup container){


        Button requestBookButton;
        Button cancelRequestButton;

        setRetainInstance(true);
        ViewStub stub = baseView.findViewById(R.id.child_fragment_here);
        stub.setLayoutResource(R.layout.fragment_book_request_view);
        stub.inflate();
        final Bundle bundle = getArguments();
        final Book book = (Book) bundle.getSerializable("bookSelected");
        assert book != null;
        final String title = book.getTitle();
        final String author = book.getAuthor();
        final String ISBN = book.getISBN();
        final String owner = book.getOwner();
        final String id = book.getId();

        requestBookButton = (Button) baseView.findViewById(R.id.request_book_button);
        cancelRequestButton = (Button) baseView.findViewById(R.id.cancel_request);


        requestBookButton.setOnClickListener(new View.OnClickListener() {  // onClickListener for when the user clicks on the confirm button to add a book
            @Override
            public void onClick(View view) {

                Map<String, Object> book = new HashMap<>();
                book.put("Title", title);
                book.put("Author", author);
                book.put("ISBN", ISBN);
                book.put("Status", "Requested");
                book.put("Book Cover", fileName);
                book.put("Owner", owner);

                Map<String, Object> user = new HashMap<>();
                user.put("email", userEmail);

                // Add Book to users awaiting approval collection
                fbDB.collection("Users")
                        .document(userEmail)
                        .collection("Awaiting Approval")
                        .document(id)
                        .set(book);
                fbDB.collection("Users")
                        .document(owner)
                        .collection("Books Owned")
                        .document(id)
                        .collection("Requested")
                        .document(userId)
                        .set(user);
                fbDB.collection("Users")
                        .document(owner)
                        .collection("Books Owned")
                        .document(id)
                        .set(book);

                Intent intent = new Intent(getActivity(), Host.class);
                startActivity(intent);
                // Add Book to Owners Books Requested collection




                }


        });
//        cancelRequestButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(getActivity(), Host.class);
//                startActivity(intent);
//            });

    };

}