package com.nkgroup.headyui.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.nkgroup.headyui.model.Category;

import java.util.List;

@Dao
public interface CategoryDao {
    @Query("SELECT * FROM category")
    LiveData<List<Category>> getAllCategory();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Category> products);

   /* @Query("select * from category where id = :categoryId")
    LiveData<Category> getCategory(int categoryId);*/


    @Query("SELECT categoryName FROM category")
    List<String> getSearchSuggessionCategory();

    /*@Query("select id from category where categoryName = :categoryName")
    Integer getCategoryId(String categoryName);*/

    @Query("select * from category where categoryName = :categoryName")
    Category getSingleCategory(String  categoryName);


}
