package com.udacity.chukwuwauchenna.myadventuresudacity.ui.journal;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.udacity.chukwuwauchenna.myadventuresudacity.data.Journal;
import com.udacity.chukwuwauchenna.myadventuresudacity.data.User;
import com.udacity.chukwuwauchenna.myadventuresudacity.util.State;

import java.util.List;

import static com.udacity.chukwuwauchenna.myadventuresudacity.util.Constants.USERS_DATABASE_REFERENCE;


public class JournalViewModel extends ViewModel {

    private DatabaseReference mReference;
    private User mUser;
    private MutableLiveData<State> _loadState = new MutableLiveData<>();
    private MutableLiveData<List<Journal>> _marches = new MutableLiveData<>();

    LiveData<State> loadState() {
        return _loadState;
    }

    LiveData<List<Journal>> journalList() {
        return _marches;
    }

    public  JournalViewModel(){
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            String uid = FirebaseAuth.getInstance().getUid();
            mReference = FirebaseDatabase.getInstance().getReference(USERS_DATABASE_REFERENCE).child(uid);
            getJournalsFromFirebase();
        }
    }




    private void getJournalsFromFirebase(){
//        List<Journal> journals = new ArrayList<>();

        _loadState.setValue(State.LOADING);

        mReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mUser = snapshot.getValue(User.class);
//                Log.d("TAG", "onDataChange: " + mUser.getName());
                assert mUser != null;
                _loadState.setValue(State.SUCCESS);
                _marches.setValue(mUser.getJournals());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("TAG", error.getMessage()); //Don't ignore errors!
                _loadState.setValue(State.ERROR);
            }
        });
    }

}
