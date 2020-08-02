package com.udacity.chukwuwauchenna.myadventuresudacity.ui.placedetail;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.appbar.MaterialToolbar;
import com.bumptech.glide.Glide;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.udacity.chukwuwauchenna.myadventuresudacity.R;
import com.udacity.chukwuwauchenna.myadventuresudacity.data.Place;
import com.udacity.chukwuwauchenna.myadventuresudacity.data.User;
import com.udacity.chukwuwauchenna.myadventuresudacity.ui.place.PlaceViewModel;

import java.util.List;

import static com.udacity.chukwuwauchenna.myadventuresudacity.util.Constants.JOURNAL_POSITION_TO_DETAIL_ACTIVITY;
import static com.udacity.chukwuwauchenna.myadventuresudacity.util.Constants.PLACE_ACTIVITY_TO_DETAIL_ACTIVITY_INTENT;
import static com.udacity.chukwuwauchenna.myadventuresudacity.util.Constants.USERS_DATABASE_REFERENCE;


public class PlaceDetailActivity extends AppCompatActivity {

    private User user;
    private int placePosition;
    private int journalPosition;
    private String uid;
    private DatabaseReference ref;
    private PlaceViewModel mViewModel;

    public PlaceDetailActivity(){


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mViewModel = new ViewModelProvider(this).get(PlaceViewModel.class);

        setContentView(R.layout.activity_place_detail);




        TextView funFactText, dateText;
        ImageView placeImage;
        MaterialToolbar toolbar;
        funFactText = findViewById(R.id.text_funFact);
        dateText = findViewById(R.id.text_date);
        placeImage = findViewById(R.id.image_place);
        toolbar = findViewById(R.id.details_toolbar);

        setSupportActionBar(toolbar);

        if (getIntent() != null){
//            mViewModel.getPlace().observe(this, placeList -> {
                placePosition = getIntent().getIntExtra(PLACE_ACTIVITY_TO_DETAIL_ACTIVITY_INTENT, -1);
                journalPosition = getIntent().getIntExtra(JOURNAL_POSITION_TO_DETAIL_ACTIVITY, -1);

//                Log.d("TAG", "saveNewPlace: " + user.getName());
                Place place = (Place) getIntent().getSerializableExtra("PLACE");
//                Place place  = placeList.get(placePosition);
                funFactText.setText(place.getFunFact());
                dateText.setText(place.getTime());
                toolbar.setTitle(place.getNamePlace());
                Glide.with(this)
                        .load(place.getPhotoUrl())
                        .placeholder(R.drawable.loading_animation)
                        .error(R.drawable.ic_broken_image_24dp)
                        .into(placeImage);
//            });

        }
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_detail, menu);
//        return true;
//
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        if (item.getItemId() == R.id.item_delete) {
//            showDeleteDialog();
//            return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }

    private void showDeleteDialog() {
        new MaterialAlertDialogBuilder(this)
                .setTitle("Delete!")
                .setMessage("Are you sure you want to Delete this Item?")
                .setPositiveButton("Yes", (dialog, which) -> delete())
                .setNegativeButton("No", (dialog, which) -> dialog.dismiss())
                .create()
                .show();
    }

    private void delete() {
        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Log.d("TAG", "delete: " + uid);
        ref = FirebaseDatabase.getInstance().getReference(USERS_DATABASE_REFERENCE).child(uid);
        Log.d("reference", "delete: " + ref.toString());

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                user = snapshot.getValue(User.class);

                Log.d("TAG", "delete: " + user.getName());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("TAG", "onCancelled: " + error.getMessage());

            }
        });
        List<Place> places;
         places = user.getJournals().get(journalPosition).getPlaces();
         places.remove(placePosition);
         user.getJournals().get(journalPosition).setPlaces(places);
         ref.setValue(user).addOnCompleteListener(task -> {
             if (task.isSuccessful()){
                 finish();
             }else {
                 Toast.makeText(this, "Looks Like  Something went wrong try again later", Toast.LENGTH_SHORT).show();
             }
         });

    }
}