package com.udacity.chukwuwauchenna.myadventuresudacity.ui.journal;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.google.firebase.auth.FirebaseAuth;
import com.udacity.chukwuwauchenna.myadventuresudacity.R;
import com.udacity.chukwuwauchenna.myadventuresudacity.adapters.JournalAdapter;
import com.udacity.chukwuwauchenna.myadventuresudacity.data.Journal;
import com.udacity.chukwuwauchenna.myadventuresudacity.ui.place.PlaceActivity;
import com.udacity.chukwuwauchenna.myadventuresudacity.ui.settings.SettingsActivity;

import static com.udacity.chukwuwauchenna.myadventuresudacity.util.Constants.JOURNAL_ACTIVITY_TO_PLACE_ACTIVITY_INTENT;
import static com.udacity.chukwuwauchenna.myadventuresudacity.util.Constants.JOURNAL_POSITION;

public class JournalActivity extends AppCompatActivity implements JournalAdapter.OnJournalItemClickListener {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_journal);

        JournalViewModel mViewModel = new ViewModelProvider(this).get(JournalViewModel.class);



        RecyclerView recyclerView = findViewById(R.id.recycler_journal);
        ProgressBar progressBar = findViewById(R.id.progressBar_journal);
        Toolbar mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);


        int orientation = getResources().getConfiguration().orientation;
        if (getResources().getBoolean(R.bool.isTablet)) {
            if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                recyclerView.setLayoutManager(new StaggeredGridLayoutManager(calculateNoOfColumns(this), RecyclerView.VERTICAL));
            }else {
                recyclerView.setLayoutManager(new StaggeredGridLayoutManager(calculateNoOfColumns(this) - 1, RecyclerView.VERTICAL));

            }
        }else {
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        }

        mViewModel.journalList().observe(this, journals -> {
            JournalAdapter adapter = new JournalAdapter(journals, JournalActivity.this);
            recyclerView.setAdapter(adapter);
        });

        mViewModel.loadState().observe(this, states -> {
            switch (states) {
                case LOADING: {
                    showProgressBar(progressBar);
                }
                case SUCCESS: {
                    hideProgressBar(progressBar);
                }
                case ERROR: {
                    hideProgressBar(progressBar);
                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_journal, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.item_settings:
                settings();
                return true;

            case R.id.item_add_journal:
                showJournalDialog();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void showJournalDialog() {
        NewJournalDialog dialog = new NewJournalDialog();
        FragmentManager fm = getSupportFragmentManager();

        dialog.show(fm,"new_journal_dialog");
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void settings() {
        startActivity(new Intent(this, SettingsActivity.class));
    }

    @Override
    public void onItemClick(Journal journal, int position) {
        Log.d("TAG", "onCreate: " + position);
        startActivity(new Intent(this, PlaceActivity.class).putExtra(JOURNAL_ACTIVITY_TO_PLACE_ACTIVITY_INTENT, journal).putExtra(JOURNAL_POSITION,position));
    }

    private int calculateNoOfColumns(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        int scalingFactor = 200;
        int noOfColumns = (int) (dpWidth / scalingFactor);
        if (noOfColumns < 2)
            noOfColumns = 2;
        return noOfColumns;
    }
    private void hideProgressBar(ProgressBar progressBar) {
        progressBar.setVisibility(View.GONE);
    }

    private void showProgressBar(ProgressBar progressBar) {
        progressBar.setVisibility(View.VISIBLE);
    }
}