package com.nkgroup.headyui.dao;


import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.nkgroup.headyui.model.Variants;

import java.util.List;

@Dao
public interface VariantsDao {

    @Query("SELECT * FROM variants where productId = :productId")
    LiveData<List<Variants>> loadVariant(int productId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Variants> variants);


}
