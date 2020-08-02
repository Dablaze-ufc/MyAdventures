package com.udacity.chukwuwauchenna.myadventuresudacity.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.udacity.chukwuwauchenna.myadventuresudacity.R;
import com.udacity.chukwuwauchenna.myadventuresudacity.data.Place;

import java.util.List;


public class PlaceAdapter extends RecyclerView.Adapter<PlaceAdapter.PlaceViewHolder> {
    private List<Place> placeList;
    private OnPlaceItemClickListener clickListener;

    public PlaceAdapter(List<Place> placeList, OnPlaceItemClickListener clickListener) {
        this.placeList = placeList;
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public PlaceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new PlaceViewHolder(inflater.inflate(R.layout.place_list_item, parent, false));

    }

    @Override
    public void onBindViewHolder(@NonNull PlaceViewHolder holder, int position) {
        Place place = placeList.get(position);
        holder.bind(place, clickListener);
    }

    @Override
    public int getItemCount() {
        if (placeList == null) {
            return 0;
        } else {
            return placeList.size();
        }
    }

    static class PlaceViewHolder extends RecyclerView.ViewHolder {
        public PlaceViewHolder(@NonNull View itemView) {
            super(itemView);
        }

         void bind(final Place place, final OnPlaceItemClickListener clickListener) {

             ImageView imageView = itemView.findViewById(R.id.imageView_place);

             Glide.with(imageView.getContext())
                     .load(place.getPhotoUrl())
                     .placeholder(R.drawable.loading_animation)
                     .error(R.drawable.ic_broken_image_24dp)
                     .into(imageView);

             itemView.setOnClickListener(v -> clickListener.onItemClick(place,getAdapterPosition(), imageView));
        }
    }

    public interface OnPlaceItemClickListener{
        void onItemClick(Place place, int position, ImageView imageView);

    }
}
