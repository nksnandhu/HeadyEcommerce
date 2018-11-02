package com.nkgroup.headyui.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "category")
public class Category {
    @PrimaryKey
    private int id;
    private final String categoryName;

    public Category(int id, String categoryName) {
        this.categoryName = categoryName;
        this.id = id;
    }


    public int getId() {
        return id;
    }

    /*public void setId(int id) {
        this.id = id;
    }
*/


    public String getCategoryName() {
        return categoryName;
    }
}
