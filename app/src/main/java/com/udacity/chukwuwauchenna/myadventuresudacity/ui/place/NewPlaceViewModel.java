package com.udacity.chukwuwauchenna.myadventuresudacity.ui.place;

import android.graphics.Bitmap;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.solver.widgets.Snapshot;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.udacity.chukwuwauchenna.myadventuresudacity.data.Place;
import com.udacity.chukwuwauchenna.myadventuresudacity.data.User;
import com.udacity.chukwuwauchenna.myadventuresudacity.util.State;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import static com.udacity.chukwuwauchenna.myadventuresudacity.util.Constants.PLACE_IMAGE_FIREBASE_STORAGE_REFERENCE;
import static com.udacity.chukwuwauchenna.myadventuresudacity.util.Constants.USERS_DATABASE_REFERENCE;

public class NewPlaceViewModel extends ViewModel {


    private String uid;
    private User user = new User();
    private DatabaseReference ref;
    private String imageUrl;

    private MutableLiveData<State> _state = new MutableLiveData<>();

    public NewPlaceViewModel() {

        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        ref = FirebaseDatabase.getInstance().getReference(USERS_DATABASE_REFERENCE).child(uid);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                user = snapshot.getValue(User.class);
                Log.d("TAG", "onDataChange: " + user.getJournals().size());
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void saveNewPlace(Place place, int journalPosition) {
        _state.setValue(State.LOADING);

        List<Place> placeList = user.getJournals().get(journalPosition).getPlaces();

        if (placeList == null) {
            placeList = new ArrayList<>();
        }
        place.setPhotoUrl(imageUrl);
        placeList.add(place);

        user.getJournals().get(journalPosition).setPlaces(placeList);
        ref.setValue(user).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                _state.setValue(State.SUCCESS);
            } else {
                _state.setValue(State.ERROR);
            }
        });
    }

    public void uploadPhoto(Bitmap mBitmap, String imagePath) {
        _state.setValue(State.LOADING);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        mBitmap.compress(Bitmap.CompressFormat.JPEG, 40, outputStream);
        byte[] data = outputStream.toByteArray();
        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
        StorageReference storageReference = firebaseStorage.getReference(PLACE_IMAGE_FIREBASE_STORAGE_REFERENCE + imagePath);

        UploadTask uploadTask = storageReference.putBytes(data);
        uploadTask.addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                // Upload successful
                // Get image download URL
                task.getResult().getStorage().getDownloadUrl().addOnCompleteListener(task1 -> {
                    if (task1.isSuccessful()) {
                        imageUrl = task1.getResult().toString();
                        _state.setValue(State.SUCCESS);

                    } else {
                        _state.setValue(State.ERROR);
                    }
                });
            } else {
                _state.setValue(State.ERROR);
            }
        });
    }
}
