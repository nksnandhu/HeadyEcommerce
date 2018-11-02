package com.nkgroup.headyui.model;


import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
class Rankings {
    @PrimaryKey
    private final int id;
    private final String Rankings;

    public Rankings(int id, String rankings) {
       this. id = id;
        Rankings = rankings;
    }

    public int getRankingId() {
        return id;
    }

    public String getRankings() {
        return Rankings;
    }
}
