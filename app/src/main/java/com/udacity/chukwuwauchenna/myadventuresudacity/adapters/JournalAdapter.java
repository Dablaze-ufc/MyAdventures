package com.udacity.chukwuwauchenna.myadventuresudacity.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.udacity.chukwuwauchenna.myadventuresudacity.R;
import com.udacity.chukwuwauchenna.myadventuresudacity.data.Journal;

import java.util.List;


public class JournalAdapter extends RecyclerView.Adapter<JournalAdapter.JournalViewHolder> {

    private List<Journal> journalList;
    private OnJournalItemClickListener onItemClickListener;

    public JournalAdapter(List<Journal> journalList, OnJournalItemClickListener onItemClickListener) {
        this.journalList = journalList;
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public JournalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new JournalViewHolder(inflater.inflate(R.layout.journal_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull JournalViewHolder holder, int position) {
        Journal journal = journalList.get(position);
        holder.bind(journal, onItemClickListener);
    }

    @Override
    public int getItemCount() {
        if (journalList == null) {
            return 0;
        } else {
            return journalList.size();
        }
    }

    static class JournalViewHolder extends RecyclerView.ViewHolder {
        JournalViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        void bind(final Journal journal, final OnJournalItemClickListener onItemClickListener) {
            ImageView imageView = itemView.findViewById(R.id.image_journal);
            TextView journalName = itemView.findViewById(R.id.text_journal_tittle);

            journalName.setText(journal.getNameJournal());

            Glide.with(imageView.getContext())
                    .load(journal.getPhotoUrl())
                    .placeholder(R.drawable.loading_animation)
                    .error(R.drawable.ic_broken_image_24dp)
                    .into(imageView);

            itemView.setOnClickListener(v -> onItemClickListener.onItemClick(journal, getAdapterPosition()));
        }
    }

    public interface OnJournalItemClickListener {
        void onItemClick(Journal journal, int position);
    }
}
