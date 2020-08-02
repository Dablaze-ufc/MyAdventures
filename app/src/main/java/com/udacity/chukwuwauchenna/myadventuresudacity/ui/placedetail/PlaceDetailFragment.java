package com.udacity.chukwuwauchenna.myadventuresudacity.ui.placedetail;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.google.android.material.appbar.MaterialToolbar;
import com.udacity.chukwuwauchenna.myadventuresudacity.R;
import com.udacity.chukwuwauchenna.myadventuresudacity.data.Place;
import com.udacity.chukwuwauchenna.myadventuresudacity.ui.place.PlaceViewModel;

import static com.udacity.chukwuwauchenna.myadventuresudacity.util.Constants.JOURNAL_POSITION_TO_DETAIL_ACTIVITY;
import static com.udacity.chukwuwauchenna.myadventuresudacity.util.Constants.PLACE_ACTIVITY_TO_DETAIL_ACTIVITY_INTENT;


public class PlaceDetailFragment extends Fragment {

    private PlaceViewModel mViewModel;
    private TextView funFactText, dateText;
    private ImageView placeImage;
    private MaterialToolbar toolbar;



    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.place_detail_fragment, container, false);
        mViewModel = new ViewModelProvider(this).get(PlaceViewModel.class);

        funFactText = view.findViewById(R.id.text_funFact);
        dateText = view.findViewById(R.id.text_date);
        placeImage = view.findViewById(R.id.image_place);
        toolbar = view.findViewById(R.id.details_toolbar);
        setHasOptionsMenu(true);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);





            mViewModel.getPlace().observe(getViewLifecycleOwner(), place -> {
//            placePosition = getIntent().getIntExtra(PLACE_ACTIVITY_TO_DETAIL_ACTIVITY_INTENT, -1);
//            journalPosition = getIntent().getIntExtra(JOURNAL_POSITION_TO_DETAIL_ACTIVITY, -1);

//                Log.d("TAG", "saveNewPlace: " + user.getName());
//            Place place = (Place) getIntent().getSerializableExtra("PLACE");
//                Place place  = placeList.get(placePosition);
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
