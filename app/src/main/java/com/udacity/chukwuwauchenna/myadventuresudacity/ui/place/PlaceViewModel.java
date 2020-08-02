package com.udacity.chukwuwauchenna.myadventuresudacity.ui.place;

import android.app.Application;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.udacity.chukwuwauchenna.myadventuresudacity.data.Journal;
import com.udacity.chukwuwauchenna.myadventuresudacity.data.Place;
import com.udacity.chukwuwauchenna.myadventuresudacity.data.User;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;
import static com.udacity.chukwuwauchenna.myadventuresudacity.util.Constants.NAME_PREF;
import static com.udacity.chukwuwauchenna.myadventuresudacity.util.Constants.PLACES_PREF;
import static com.udacity.chukwuwauchenna.myadventuresudacity.util.Constants.USERS_DATABASE_REFERENCE;
import static com.udacity.chukwuwauchenna.myadventuresudacity.util.Constants.WIDGET_PREF;


public class PlaceViewModel extends AndroidViewModel {

    private User user;
    private String uid;
    private DatabaseReference ref;
    private Application application;
    private MutableLiveData<Journal> _journal = new MutableLiveData<>();
    private MutableLiveData<Place> _places = new MutableLiveData<>();



    public PlaceViewModel(@NonNull Application application) {
        super(application);
        this.application = application;

        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        ref = FirebaseDatabase.getInstance().getReference(USERS_DATABASE_REFERENCE).child(uid);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                user = snapshot.getValue(User.class);

                Log.d("TAG", "saveNewPlace: " + user.getName());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void setPlace(Place place){
        _places.setValue(place);
    }

    public LiveData<Place>  getPlace(){
        return _places;
    }

    public LiveData<Journal> getJournal(){

        return _journal;

    }

    public void setJournal(Journal journal){
       _journal.setValue(journal);
    }


    public void addToPrefsForWidget(Journal journal) {
        SharedPreferences preferences = application.getSharedPreferences(WIDGET_PREF, MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        Gson gson = new Gson();
        String places = gson.toJson(journal.getPlaces());
        Log.d("TAG", "addToPrefsForWidget: " + places);
        editor.putString(PLACES_PREF, places);
        editor.putString(NAME_PREF, journal.getNameJournal());
        editor.apply();

    }
}
