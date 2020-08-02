package com.udacity.chukwuwauchenna.myadventuresudacity.data;

import java.io.Serializable;
import java.util.List;

public class Journal implements Serializable {

    private String nameJournal;
    private String photoUrl;
    private List<Place> places;

    public String getNameJournal() {
        return nameJournal;
    }

    public void setNameJournal(String nameJournal) {
        this.nameJournal = nameJournal;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public List<Place> getPlaces() {
        return places;
    }

    public void setPlaces(List<Place> places) {
        this.places = places;
    }
}
