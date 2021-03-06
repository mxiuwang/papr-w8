package com.example.papr_w8.Adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.papr_w8.Book;
import com.example.papr_w8.BookView.RequestConfirmView;
import com.example.papr_w8.R;
import com.example.papr_w8.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class UserRequestsDisplayList extends ArrayAdapter<User> {
    private Context context;
    private ArrayList<User> users;
    private Book book;
    private Boolean isAccepted;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseUser fbUser = firebaseAuth.getInstance().getCurrentUser();
    private FirebaseFirestore fbDB = FirebaseFirestore.getInstance();
    private String user_name;


    public UserRequestsDisplayList(Context context, ArrayList<User> users, Book book) { // items is an array of all the default items
        super(context,0,users);
        this.context = context;
        this.users = users; // UserDataList, or ArrayList of User objects
        this.book = book;
    }

    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;

        if (view == null){
            view = LayoutInflater.from(context).inflate(R.layout.user_requests_display_list_item,parent,false);
        }

        final User user = users.get(position);
        Log.d("BOOK ID", book.getId());

        TextView userName = (TextView) view.findViewById(R.id.user_name_text);
        Button acceptRequest = (Button) view.findViewById(R.id.accept_request);
        Button declineRequest = (Button) view.findViewById(R.id.decline_request);

        fbDB.collection("Users")
                .document(fbUser.getEmail())
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        user_name = documentSnapshot.getString("name");
                    }
                });

        userName.setText(user.getEmail());

        acceptRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), RequestConfirmView.class);
                intent.putExtra("book", book);
                intent.putExtra("borrower", user.getEmail());
                Log.d("BORROWER_EMAIL", user.getEmail());
                context.startActivity(intent);
            }
        });

        declineRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                fbDB.collection("Users")
                        .document(fbUser.getEmail())
                        .collection("Books Owned")
                        .document(book.getId())
                        .collection("Requested")
                        .document(users.get(position).getEmail())
                        .delete();

                fbDB.collection("Users")
                        .document(users.get(position).getEmail())
                        .collection("Awaiting Approval")
                        .document(book.getId())
                        .delete();

                users.remove(position);
                notifyDataSetChanged();
                notifyUser(book.getId(), users.get(position).getEmail(), fbUser.getEmail(), user_name, book.getTitle());
            }
        });

        return view;
    }

    /**
     * Notifies the user that their request has been declined
     * @param bookId
     * @param user_email
     * @param owner_email
     * @param user_name
     * @param book_title
     */
    public void notifyUser(String bookId, String user_email, String owner_email, String user_name, String book_title){
        Map<String, Object> notification = new HashMap<>();
        notification.put("Sender", owner_email);
        notification.put("Name", user_name);
        notification.put("Type", "declined");
        notification.put("Book Title", book_title);
        notification.put("Book Id", bookId);

        fbDB.collection("Users")
                .document(user_email)
                .collection("Notifications")
                .document()
                .set(notification);
    }
}
