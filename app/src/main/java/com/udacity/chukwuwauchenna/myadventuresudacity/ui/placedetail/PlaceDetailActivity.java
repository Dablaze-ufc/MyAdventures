package com.udacity.chukwuwauchenna.myadventuresudacity.ui.placedetail;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.google.android.material.appbar.MaterialToolbar;
import com.udacity.chukwuwauchenna.myadventuresudacity.R;
import com.udacity.chukwuwauchenna.myadventuresudacity.ui.place.PlaceViewModel;


public class PlaceDetailActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        PlaceViewModel mViewModel = new ViewModelProvider(this).get(PlaceViewModel.class);

        setContentView(R.layout.activity_place_detail);

        TextView funFactText, dateText;
        ImageView placeImage;
        MaterialToolbar toolbar;
        funFactText = findViewById(R.id.text_funFact);
        dateText = findViewById(R.id.text_date);
        placeImage = findViewById(R.id.image_place);
        toolbar = findViewById(R.id.details_toolbar);

        setSupportActionBar(toolbar);


            mViewModel.getPlace().observe(this, place -> {
                funFactText.setText(place.getFunFact());
                dateText.setText(place.getTime());
                toolbar.setTitle(place.getNamePlace());
                Glide.with(this)
                        .load(place.getPhotoUrl())
                        .placeholder(R.drawable.loading_animation)
                        .error(R.drawable.ic_broken_image_24dp)
                        .into(placeImage);
            });


    }
}