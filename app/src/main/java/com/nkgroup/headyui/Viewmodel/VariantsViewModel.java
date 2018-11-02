package com.nkgroup.headyui.Viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.nkgroup.headyui.db.AppDataBase;
import com.nkgroup.headyui.model.Variants;
import com.nkgroup.headyui.dao.VariantsDao;

import java.util.List;

public class VariantsViewModel extends AndroidViewModel {
    private final LiveData<List<Variants>> mObservableProducts;
    private VariantsViewModel(@NonNull Application application,int productId) {
        super(application);
        AppDataBase mDatabase = AppDataBase.getDatabase(application);
        VariantsDao variantsDao= mDatabase.variantsDao();
        mObservableProducts =variantsDao.loadVariant(productId);
    }
    public LiveData<List<Variants>> getProduct() {
        return mObservableProducts;
    }
   /* public LiveData<List<Variants>> getProduct(final int productId) {
        return mDatabase.variantsDao().loadVariant(productId);
    }*/

    public static class Factory extends ViewModelProvider.NewInstanceFactory {
        @NonNull
        private final Application mApplication;
        private final int mProductId;
        public Factory(@NonNull Application application, int productId) {
            mApplication = application;
            mProductId = productId;
        }
        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            //noinspection unchecked
            return (T) new VariantsViewModel(mApplication,  mProductId);
        }
    }


}
