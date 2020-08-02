package com.udacity.chukwuwauchenna.myadventuresudacity.ui.place;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.udacity.chukwuwauchenna.myadventuresudacity.R;
import com.udacity.chukwuwauchenna.myadventuresudacity.adapters.PlaceAdapter;
import com.udacity.chukwuwauchenna.myadventuresudacity.data.Journal;
import com.udacity.chukwuwauchenna.myadventuresudacity.data.Place;

import static com.udacity.chukwuwauchenna.myadventuresudacity.util.Constants.JOURNAL_ACTIVITY_TO_PLACE_ACTIVITY_INTENT;
import static com.udacity.chukwuwauchenna.myadventuresudacity.util.Constants.JOURNAL_POSITION;


public class PlaceFragment extends Fragment implements PlaceAdapter.OnPlaceItemClickListener {

    private PlaceViewModel mViewModel;
    private RecyclerView recyclerView;
    private FloatingActionButton fab;
    private Toolbar mToolbar;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.place_fragment, container, false);
        mViewModel = new ViewModelProvider(this).get(PlaceViewModel.class);
        recyclerView = view.findViewById(R.id.recycler_place);
        fab = view.findViewById(R.id.fab_place);
        mToolbar = view.findViewById(R.id.toolbar_places);


        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        if (getActivity().getIntent() != null) {
            Journal journal = (Journal) getActivity().getIntent().getSerializableExtra(JOURNAL_ACTIVITY_TO_PLACE_ACTIVITY_INTENT);
            mViewModel.setJournal(journal);

            recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, RecyclerView.VERTICAL));

            mViewModel.getJournal().observe(getViewLifecycleOwner(), journal1 -> {
                PlaceAdapter adapter = new PlaceAdapter(journal.getPlaces(), this);
                recyclerView.setAdapter(adapter);
            });

            fab.setOnClickListener(view1 -> {
                if (getActivity().getIntent() != null) {
                    int journalPosition = getActivity().getIntent().getIntExtra(JOURNAL_POSITION, -1);
                    startActivity(new Intent(getActivity(), NewPlaceActivity.class).putExtra(JOURNAL_ACTIVITY_TO_PLACE_ACTIVITY_INTENT, journalPosition));
                }
            });
        }
    }


    @Override
    public void onItemClick(Place place, int position, ImageView imageView) {
        mViewModel.setPlace(place);

    }
}