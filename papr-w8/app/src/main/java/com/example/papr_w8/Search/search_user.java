package com.example.papr_w8.Search;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.papr_w8.R;
import com.example.papr_w8.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class search_user extends Fragment {

    ListView userList;
    ArrayAdapter<User> userAdapter;

    ArrayList<String> userNames = new ArrayList<String>();
    ArrayList<String> userEmails = new ArrayList<String>();
    ArrayList<String> userPasswords = new ArrayList<String>();
    ArrayList<String> userAddresses = new ArrayList<String>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_user, container, false);

        userList = view.findViewById(R.id.user_list);
//        userDataList = new ArrayList<>();
        final Task<QuerySnapshot> userDoc = FirebaseFirestore.getInstance().collection("Users")
            .get()
            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    ArrayList<User> userDataList = new ArrayList<>();
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            userDataList.add(new User(document.getString("name"), document.getString("password"), document.getString("email"), document.getString("address")));
                            userAdapter = new UserDisplayList(getContext(), userDataList); // userDataList is an array of users
                            userList.setAdapter(userAdapter);
                            Log.d("TAG", document.getId() + " => " + document.getData());
                        }
                    } else {
                        Log.d("TAG", "Error getting documents: ", task.getException());
                    }
                }
            });

        // Inflate the layout for this fragment
        return view;
    }

}