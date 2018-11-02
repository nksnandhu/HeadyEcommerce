package com.nkgroup.headyui.Viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.nkgroup.headyui.db.AppDataBase;
import com.nkgroup.headyui.model.Products;
import com.nkgroup.headyui.dao.ProductsDao;

import java.util.List;

public class ProductViewModel extends AndroidViewModel {
    private final LiveData<List<Products>> mObservableProducts;
    private ProductViewModel(@NonNull Application application, final int categoryId) {
        super(application);
        AppDataBase mDatabase = AppDataBase.getDatabase(application);
        ProductsDao productDao= mDatabase.productDao();
        mObservableProducts =productDao.loadProducts(categoryId);
    }

    public LiveData<List<Products>> getProduct() {
        return mObservableProducts;
    }

   /* public LiveData<List<Products>> getProduct(final int productId) {
        return mDatabase.productDao().loadProducts(productId);
    }*/


    public static class Factory extends ViewModelProvider.NewInstanceFactory {
        @NonNull
        private final Application mApplication;
        private final int sCategoryId;
        public Factory(@NonNull Application application, int categoryId) {
            mApplication = application;
            sCategoryId = categoryId;
        }
        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            //noinspection unchecked
            return (T) new ProductViewModel(mApplication,  sCategoryId);
        }
    }
}
