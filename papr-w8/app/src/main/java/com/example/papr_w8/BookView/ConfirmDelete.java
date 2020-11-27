package com.example.papr_w8.BookView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import com.example.papr_w8.Host;
import com.example.papr_w8.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class ConfirmDelete extends Fragment {
    Button deleteConfirm;
    Button cancel;
    public ConfirmDelete (){}
    private String fileName;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseUser user = firebaseAuth.getInstance().getCurrentUser();
    private FirebaseFirestore fbDB = FirebaseFirestore.getInstance();
    private String email = user.getEmail();

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {
        final String bookID = getArguments().getString("Book ID");

        final View view = inflater.inflate(R.layout.fragment_confirm_delete, container, false);
        deleteConfirm = (Button) view.findViewById(R.id.confirm_deletion);
        cancel = (Button) view.findViewById(R.id.cancel_deletion);


        // ASSERT BOOK AVAILABLE |||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||
        deleteConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fbDB.collection("Users").document(email).collection("Books Owned").document(bookID).delete();

                fbDB.collection("Books").document(bookID).delete();


                Intent intent = new Intent(getActivity(), Host.class);
                startActivity(intent);

            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), Host.class);
                startActivity(intent);

            }
        });


        return view;


    }}