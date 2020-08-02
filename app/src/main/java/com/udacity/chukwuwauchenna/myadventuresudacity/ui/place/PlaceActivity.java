package com.udacity.chukwuwauchenna.myadventuresudacity.ui.place;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityOptionsCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.udacity.chukwuwauchenna.myadventuresudacity.R;
import com.udacity.chukwuwauchenna.myadventuresudacity.adapters.PlaceAdapter;
import com.udacity.chukwuwauchenna.myadventuresudacity.data.Journal;
import com.udacity.chukwuwauchenna.myadventuresudacity.data.Place;
import com.udacity.chukwuwauchenna.myadventuresudacity.ui.placedetail.PlaceDetailActivity;
import com.udacity.chukwuwauchenna.myadventuresudacity.widget.AdventureWidget;

import static com.udacity.chukwuwauchenna.myadventuresudacity.util.Constants.JOURNAL_ACTIVITY_TO_PLACE_ACTIVITY_INTENT;
import static com.udacity.chukwuwauchenna.myadventuresudacity.util.Constants.JOURNAL_POSITION;
import static com.udacity.chukwuwauchenna.myadventuresudacity.util.Constants.JOURNAL_POSITION_TO_DETAIL_ACTIVITY;
import static com.udacity.chukwuwauchenna.myadventuresudacity.util.Constants.PLACE_ACTIVITY_TO_DETAIL_ACTIVITY_INTENT;

public class PlaceActivity extends AppCompatActivity implements PlaceAdapter.OnPlaceItemClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place);
        RecyclerView recyclerView = findViewById(R.id.recycler_place);
        FloatingActionButton fab = findViewById(R.id.fab_place);
        Toolbar mToolbar = findViewById(R.id.toolbar_places);
        setSupportActionBar(mToolbar);

        PlaceViewModel mViewModel = new ViewModelProvider(this).get(PlaceViewModel.class);

        if (getIntent() != null){
            Journal journal = (Journal) getIntent().getSerializableExtra(JOURNAL_ACTIVITY_TO_PLACE_ACTIVITY_INTENT);
            mViewModel.setJournal(journal);
            mViewModel.addToPrefsForWidget(journal);

        }

        AdventureWidget.updateWidget(this);

        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, RecyclerView.VERTICAL));

        mViewModel.getJournal().observe(this, journal -> {
            PlaceAdapter adapter = new PlaceAdapter(journal.getPlaces(), this);
            recyclerView.setAdapter(adapter);
        });

        fab.setOnClickListener(view ->{  if (getIntent() != null){
            int journalPosition = getIntent().getIntExtra(JOURNAL_POSITION, -1);
            Log.d("TAG", "onCreate: " + journalPosition);

            startActivity(new Intent(this, NewPlaceActivity.class).putExtra(JOURNAL_POSITION_TO_DETAIL_ACTIVITY, journalPosition));
        }});

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onItemClick(Place place, int position, ImageView imageView) {
        Bundle bundle = ActivityOptionsCompat.makeSceneTransitionAnimation(
                PlaceActivity.this,
                imageView,
                imageView.getTransitionName()
        ).toBundle();
        if (getIntent() != null){
            int journalPosition = getIntent().getIntExtra(JOURNAL_POSITION, -1);
        startActivity(new Intent(this, PlaceDetailActivity.class).putExtra(PLACE_ACTIVITY_TO_DETAIL_ACTIVITY_INTENT, position).putExtra("PLACE", place).putExtra(JOURNAL_ACTIVITY_TO_PLACE_ACTIVITY_INTENT, journalPosition), bundle);
}
    }
}