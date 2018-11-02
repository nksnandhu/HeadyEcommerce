package com.nkgroup.headyui.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.nkgroup.headyui.model.Products;

import java.util.List;
@Dao
public interface ProductsDao {

    @Query("SELECT * FROM sub_category where categoryId = :categoryId")
    LiveData<List<Products>> loadProducts(int categoryId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Products> products);

    @Query("SELECT * FROM sub_category")
    LiveData<List<Products>> getAllProducts();

    @Query("SELECT  productName FROM sub_category")
    List<String> getSearchSuggessionProduct();

    @Query("select * from sub_category where productName = :productName")
    Products getSingleProduct(String  productName);



   /* @Query("SELECT * FROM sub_category ")
    LiveData<List<String>> getSearchSuggession();*/




}
