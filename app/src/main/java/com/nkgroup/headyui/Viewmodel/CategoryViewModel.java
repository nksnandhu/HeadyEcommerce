package com.nkgroup.headyui.Viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;


import com.nkgroup.headyui.db.AppDataBase;
import com.nkgroup.headyui.model.Category;
import com.nkgroup.headyui.model.Products;

import java.util.List;

public class CategoryViewModel extends AndroidViewModel {

    private final MediatorLiveData<List<Category>> mObservableCategory;

    public CategoryViewModel(Application application) {
        super(application);
        mObservableCategory = new MediatorLiveData<>();
        MediatorLiveData<List<Products>> mObservableProducts = new MediatorLiveData<>();
        AppDataBase mDatabase = AppDataBase.getDatabase(application);
        LiveData<List<Category>> category = mDatabase.categoryDao().getAllCategory();
        LiveData<List<Products>> products = mDatabase.productDao().getAllProducts();
        mObservableCategory.addSource(category, mObservableCategory::setValue);
        mObservableProducts.addSource(products, mObservableProducts::setValue);
    }

    /*public LiveData<List<String>> getCategorySuggession() {
        return mObservableSuggestionCategory;
    }

    public LiveData<List<String>> getProductSuggession() {
        return mObservableSuggestionProduct;
    }*/


    public LiveData<List<Category>> getCategory() {
        return mObservableCategory;
    }


    //private final MutableLiveData<Category> selectedCategory = new MutableLiveData<>();

}
