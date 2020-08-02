package com.udacity.chukwuwauchenna.myadventuresudacity.ui.journal;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.udacity.chukwuwauchenna.myadventuresudacity.R;
import com.udacity.chukwuwauchenna.myadventuresudacity.data.Journal;
import com.udacity.chukwuwauchenna.myadventuresudacity.data.Place;
import com.udacity.chukwuwauchenna.myadventuresudacity.data.User;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static android.app.Activity.RESULT_OK;
import static com.udacity.chukwuwauchenna.myadventuresudacity.util.Constants.JOURNAL_IMAGE_FIREBASE_STORAGE_REFERENCE;
import static com.udacity.chukwuwauchenna.myadventuresudacity.util.Constants.JOURNAL_IMAGE_REQUEST_CODE;
import static com.udacity.chukwuwauchenna.myadventuresudacity.util.Constants.USERS_DATABASE_REFERENCE;

public class NewJournalDialog extends DialogFragment {

    ImageView journalImage, journalImagePreview;
    TextInputEditText journalName;
    MaterialButton btnCreate;
    private String imagePath;
    private Bitmap mBitmap;
    private String uid;
    private User user;
    private DatabaseReference ref;
    private String imageUrl;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.new_journal_layout, container);
        uid = FirebaseAuth.getInstance().getUid();
        ref = FirebaseDatabase.getInstance().getReference(USERS_DATABASE_REFERENCE);
        ref.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                user = snapshot.getValue(User.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        journalImage = view.findViewById(R.id.image_add_journal);
        journalImagePreview = view.findViewById(R.id.image_journal_preview);
        journalName = view.findViewById(R.id.editTextJournalName);
        btnCreate = view.findViewById(R.id.button_create_journal);

        Objects.requireNonNull(getDialog()).setTitle("Create new Sights");

        btnCreate.setOnClickListener(v -> {

            String name = journalName.getText().toString();

            if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(imageUrl)) {
                Journal journal = new Journal();
                List<Place> places = new ArrayList<>();
                journal.setNameJournal(name);
                journal.setPhotoUrl(imageUrl);
                journal.setPlaces(places);
                createNewJournal(journal);

                dismiss();
            } else {
                Toast.makeText(requireContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show();
            }

        });

        journalImage.setOnClickListener(v -> {
            openGallery();
        });


    }

    private void uploadPhoto() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        mBitmap.compress(Bitmap.CompressFormat.JPEG, 40, outputStream);
        byte[] data = outputStream.toByteArray();
        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
        StorageReference storageReference = firebaseStorage.getReference(JOURNAL_IMAGE_FIREBASE_STORAGE_REFERENCE + imagePath);

        UploadTask uploadTask = storageReference.putBytes(data);
        uploadTask.addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                // Upload successful
                // Get image download URL
                task.getResult().getStorage().getDownloadUrl().addOnCompleteListener(task1 -> {
                    if (task1.isSuccessful()) {
                        imageUrl = task1.getResult().toString();

                    } else {
                        Toast.makeText(requireActivity(), task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                Toast.makeText(requireActivity(), task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void createNewJournal(Journal journal) {
        List<Journal> journalList;
        journalList = user.getJournals();
        if (journalList == null) {
            journalList = new ArrayList<>();
        }
        journalList.add(journal);
        user.setJournals(journalList);

        ref.child(uid).setValue(user).addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                Toast.makeText(getActivity(), task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
            dismiss();
        });
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, JOURNAL_IMAGE_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK && requestCode == JOURNAL_IMAGE_REQUEST_CODE) {
            Uri imageUri = data.getData();
            if (imageUri != null) {
                try {
                    imagePath = imageUri.getLastPathSegment();
                    if (Build.VERSION.SDK_INT >= 29) {
                        ImageDecoder.Source source = ImageDecoder.createSource(requireActivity().getContentResolver(), imageUri);
                        mBitmap = ImageDecoder.decodeBitmap(source);


                    } else {
                        mBitmap = MediaStore.Images.Media.getBitmap(requireActivity().getContentResolver(), imageUri);

                    }
                    uploadPhoto();
                    // Load image using Glide
                    Glide.with(requireContext())
                            .load(mBitmap)
                            .placeholder(R.drawable.loading_animation)
                            .error(R.drawable.ic_broken_image_24dp)
                            .into(journalImagePreview);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(requireActivity(), "No Image Selected", Toast.LENGTH_SHORT).show();

            }
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
