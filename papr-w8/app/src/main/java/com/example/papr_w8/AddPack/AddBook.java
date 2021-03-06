package com.example.papr_w8.AddPack;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.papr_w8.Host;
import com.example.papr_w8.R;
import com.example.papr_w8.ScanActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

/**
 * AddBook class handles adding a book from the addbook activity
 */
public class AddBook extends AppCompatActivity {

    public static final String TAG = "TAG";
    private Uri imageUri;
    private ImageButton addBookCover;
    private StorageReference storageReference;
    private String fileName;
    private EditText newBookTitle;
    private EditText newBookISBN;
    private EditText newBookAuthor;

    private final int ADD_COVER_REQUEST_CODE = 1;
    private final int SCAN_ISBN_REQUEST_CODE = 2;

    /**
     * onCreate starts the code for adding a book functionality
     * Contains the functionality for each of the buttons on the add book page
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_book);

        // find resources on the xml file and set them to variables for use
        newBookTitle = findViewById(R.id.new_title_editText);
        newBookISBN = findViewById(R.id.new_isbn_editText);
        newBookAuthor = findViewById(R.id.new_author_editText);
        Button cancel = findViewById(R.id.cancel_addbook_button);
        Button confirm = findViewById(R.id.confirm_addbook_button);
        Button scan = findViewById(R.id.scan_isbn);
        addBookCover = findViewById(R.id.imageButton);
        ImageView deleteBookCover = findViewById(R.id.delete_image_ab);
        final int add_id = getResources().getIdentifier("@android:drawable/ic_menu_add", null, null);

        // firebase set up allows for communicating between app and firebase
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        final FirebaseFirestore fbDB = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference("images");
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("images");
        FirebaseUser user = firebaseAuth.getInstance().getCurrentUser();
        final String email = user.getEmail();

        // functionality for scan button
        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddBook.this, ScanActivity.class);
                startActivityForResult(intent, SCAN_ISBN_REQUEST_CODE);
            }
        });

        // functionality for addBookcover button
        addBookCover.setOnClickListener(new View.OnClickListener() {  // onClickListener for when the user clicks on the add book cover image buttor
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddBook.this, AddBookCoverActivity.class);
                startActivityForResult(intent, ADD_COVER_REQUEST_CODE);
            }
        });

        // functionality for deleteBookCover button
        deleteBookCover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fileName = "default_book.png";
                addBookCover.setImageResource(add_id);
                imageUri = null;
            }
        });

        // functionality for confirm button
        confirm.setOnClickListener(new View.OnClickListener() {  // onClickListener for when the user clicks on the confirm button to add a book
            @Override
            public void onClick(View view) {
                String title = newBookTitle.getText().toString();
                String author = newBookAuthor.getText().toString();
                String ISBN = newBookISBN.getText().toString();

                if (title.isEmpty()) { //checks for valid title entry
                    newBookTitle.setError("Please enter title");
                    newBookTitle.requestFocus();
                } else if (author.isEmpty()) { //checks for valid author entry
                    newBookAuthor.setError("Please enter author's name");
                    newBookAuthor.requestFocus();
                } else if (ISBN.isEmpty()) { //checks for valid ISBN entry
                    newBookISBN.setError("Please enter valid ISBN");
                    newBookISBN.requestFocus();
                } else {
                    // if no image cover uploaded then set to default
                    if (imageUri == null) {
                        fileName = "default_book.png";
                    }
                    // create book HashMap object that will be uploaded to firebase
                    final Map<String, Object> book = new HashMap<>();
                    book.put("Title", title);
                    book.put("Author", author);
                    book.put("ISBN", ISBN);
                    book.put("Status", "Available");
                    book.put("Book Cover", fileName);
                    book.put("Owner", email);

                    // Implementation for adding book details to the firestore collection
                    fbDB.collection("Users").document(email).collection("Books Owned")
                            .add(book)
                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    fbDB.collection("Books").document(documentReference.getId()) //adds book to "Books" collections too
                                            .set(book);
                                    Toast.makeText(AddBook.this, "Book Added", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(AddBook.this, "Book Add Failed", Toast.LENGTH_SHORT).show();
                                }
                            });
                    //uploadCover();
                    Intent intent = new Intent(AddBook.this, Host.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {  // onClickListener for cancel button if the user wants to cancel adding a book
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    /**
     * onActivityResult receives the book cover after user chooses and image from their device
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADD_COVER_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                String imageUriString = data.getStringExtra("coverUri");
                imageUri = Uri.parse(imageUriString);
                Picasso.get().load(imageUri).into(addBookCover);
                uploadCover();
            }
        }else if (requestCode == SCAN_ISBN_REQUEST_CODE){
            if (resultCode == RESULT_OK){
                String isbn = data.getStringExtra("ISBN");
                newBookISBN.setText(isbn);
            }
        }

    }

    /**
     * Gets file extension from the image uri
     * @param uri
     * @return
     */
    private String getFileExt(Uri uri) {  // gets file extension of image
        ContentResolver cr = getContentResolver();
        MimeTypeMap mtp = MimeTypeMap.getSingleton();
        return mtp.getExtensionFromMimeType(cr.getType(uri));
    }

    /**
     * Handles uploading the cover to firebase storage
     * Sets a filename for the image and uplaods to firebase
     */
    private void uploadCover() {
        if (imageUri != null) {
            fileName = System.currentTimeMillis() + "." + getFileExt(imageUri);
            StorageReference fileRef = storageReference.child(fileName); //sets the filename of the image
            fileRef.putFile(imageUri)  //uploads the image to firebase using imageUri
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                            Toast.makeText(AddBook.this, "Cover Uploaded", Toast.LENGTH_SHORT).show();
                            Log.d("CoverDEBUG", "Cover Uploaded");

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
//                            Toast.makeText(AddBook.this, "Cover Upload Failed", Toast.LENGTH_SHORT).show();
                            Log.d("CoverDEBUG", "Cover Upload Failed");
                        }
                    });
        } else {
            fileName = "default_book.png";
            Toast.makeText(this, "No book cover selected", Toast.LENGTH_SHORT).show();
        }
    }
}