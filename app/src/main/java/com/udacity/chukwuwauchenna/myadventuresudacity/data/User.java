package com.udacity.chukwuwauchenna.myadventuresudacity.data;

import java.io.Serializable;
import java.util.List;

/**
 * Created by ChukwuwaUchenna
 */
public class User implements Serializable {
    private String userId;
    private String name;
    private String profilePhotoUrl;
    private String email;
    private List<Journal> journals;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfilePhotoUrl() {
        return profilePhotoUrl;
    }

    public void setProfilePhotoUrl(String profilePhotoUrl) {
        this.profilePhotoUrl = profilePhotoUrl;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Journal> getJournals() {
        return journals;
    }

    public void setJournals(List<Journal> journals) {
        this.journals = journals;
    }
}
