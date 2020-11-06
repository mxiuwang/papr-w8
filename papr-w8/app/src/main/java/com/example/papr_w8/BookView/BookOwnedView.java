package com.example.papr_w8.BookView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.papr_w8.Book;
import com.example.papr_w8.R;

public class BookOwnedView extends Fragment {

    public BookOwnedView() {
    }

    private Button buttonViewApprove;
    private Button buttonEditDescription;
    private Button buttonDeleteBook;

    private TextView textViewTitle;
    private TextView textViewAuthor;
    private TextView textViewISBN;
    private TextView textViewStatus;

    private ImageView imageViewDefault;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_book_owned, container, false);

        buttonViewApprove = (Button) view.findViewById(R.id.viewapproveButton);
        buttonEditDescription = (Button) view.findViewById(R.id.editdescriptionButton);
        buttonDeleteBook = (Button) view.findViewById(R.id.deleteButton);

        textViewTitle = view.findViewById(R.id.titleEditText);
        textViewAuthor = view.findViewById(R.id.authorEditText);
        textViewISBN = view.findViewById(R.id.isbnEditText);
        textViewStatus = view.findViewById(R.id.statusEditText);

        Bundle bundle = this.getArguments();
        Book book = (Book) bundle.getSerializable("bookSelected");

        textViewTitle.setText(book.getTitle());
        textViewAuthor.setText(book.getAuthor());
        textViewISBN.setText(book.getISBN());
        textViewStatus.setText(book.getStatus());

        buttonViewApprove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        buttonEditDescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        buttonDeleteBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        return view;
    }
}