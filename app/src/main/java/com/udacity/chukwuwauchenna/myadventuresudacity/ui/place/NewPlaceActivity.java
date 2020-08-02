package com.udacity.chukwuwauchenna.myadventuresudacity.ui.place;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.udacity.chukwuwauchenna.myadventuresudacity.R;
import com.udacity.chukwuwauchenna.myadventuresudacity.data.Place;
import com.udacity.chukwuwauchenna.myadventuresudacity.databinding.ActivityNewPlaceBinding;

import java.util.Calendar;

import static com.udacity.chukwuwauchenna.myadventuresudacity.util.Constants.JOURNAL_POSITION;
import static com.udacity.chukwuwauchenna.myadventuresudacity.util.Constants.PLACE_IMAGE_REQUEST_CODE;


public class NewPlaceActivity extends AppCompatActivity {

    private ActivityNewPlaceBinding binding;
    private NewPlaceViewModel viewModel;
    private String mDateVisited;
    private DatePickerDialog.OnDateSetListener mDateDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_new_place);
        viewModel = new ViewModelProvider(this).get(NewPlaceViewModel.class);

        Calendar todaysDate = Calendar.getInstance();
        CharSequence date = DateFormat.format("MMM d, yyyy", todaysDate);
       binding.dateVisitedEt.setText(date);


        binding.imageAdd.setOnClickListener(v -> {
            openGallery();
        });



        binding.dateVisitedEt.setOnClickListener(v -> {
            Calendar cal = Calendar.getInstance();
            int year = cal.get(Calendar.YEAR);
            int month = cal.get(Calendar.MONTH);
            int day = cal.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog pickerDialog = new DatePickerDialog(this
                    , mDateDialog,
                    year, month, day);
            pickerDialog.show();
        });

        mDateDialog = (view, year, month, dayOfMonth) -> {
            Calendar calendar1 = Calendar.getInstance();
            calendar1.set(Calendar.YEAR, year);
            calendar1.set(Calendar.MONTH, month);
            calendar1.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            CharSequence dateVisited = DateFormat.format("MMM d, yyyy", calendar1);
            mDateVisited = dateVisited.toString();
            binding.dateVisitedEt.setText(dateVisited);
        };



        binding.buttonCreatePlace.setOnClickListener(v -> {
           String funFact =  binding.funFactEt.getText().toString().trim();
           String name = binding.nameEt.getText().toString().trim();

            if(!funFact.isEmpty()  && !name.isEmpty() && mDateVisited != null){
                Place place = new Place();
                place.setFunFact(funFact);
                place.setNamePlace(name);
                place.setTime(mDateVisited);

                if (getIntent() != null){
                    int journalPosition = getIntent().getIntExtra(JOURNAL_POSITION, -1);
                    Log.d("TAG", "onCreate: " + journalPosition);
                viewModel.saveNewPlace(place,journalPosition);
            }
                finish();
            }else {
                Toast.makeText(this, "Please  fill in all fields", Toast.LENGTH_SHORT).show();
            }


        });
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, PLACE_IMAGE_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK && requestCode == PLACE_IMAGE_REQUEST_CODE) {
            Uri imageUri = data.getData();
            if (imageUri != null) {
                try {
                    String imagePath = imageUri.getLastPathSegment();
                    Bitmap mBitmap;
                    if (Build.VERSION.SDK_INT >= 29) {
                        ImageDecoder.Source source = ImageDecoder.createSource(this.getContentResolver(), imageUri);
                        mBitmap = ImageDecoder.decodeBitmap(source);


                    } else {
                        mBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);

                    }
                    viewModel.uploadPhoto(mBitmap, imagePath);
                    // Load image using Glide
                    Glide.with(this)
                            .load(mBitmap)
                            .placeholder(R.drawable.loading_animation)
                            .error(R.drawable.ic_broken_image_24dp)
                            .into(binding.imagePreview);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(this, "No Image Selected", Toast.LENGTH_SHORT).show();

            }
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}