package com.nkgroup.headyui.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;

import com.nkgroup.headyui.model.Category;
import com.nkgroup.headyui.dao.CategoryDao;
import com.nkgroup.headyui.model.Products;
import com.nkgroup.headyui.dao.ProductsDao;
import com.nkgroup.headyui.model.Variants;
import com.nkgroup.headyui.dao.VariantsDao;

import java.util.List;

@Database(entities = {Category.class, Products.class, Variants.class}, version = 1)
abstract public class AppDataBase extends RoomDatabase {

    private static final String DATABASE_NAME = "ListDb";
    private static AppDataBase INSTANCE;

    public abstract CategoryDao categoryDao();

    public abstract ProductsDao productDao();

    public abstract VariantsDao variantsDao();


    public static AppDataBase getDatabase(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(), AppDataBase.class, DATABASE_NAME).build();
        }
        return INSTANCE;
    }

    public static void buildDateBase(Context appContext, AppExecutors executors, List<Category> categories, List<Products> products, List<Variants> variants) {
        executors.diskIO().execute(() -> {
            addDelay();
            AppDataBase database = AppDataBase.getDatabase(appContext);
            insertData(database, categories, products, variants);
        });

    }


    private static void addDelay() {
        try {
            Thread.sleep(4000);
        } catch (InterruptedException ignored) {
        }
    }

    private static void insertData(final AppDataBase database, final List<Category> listCategory,
                                  final List<Products> listProducts, List<Variants> listVariants) {
        database.runInTransaction(() -> {
            database.categoryDao().insertAll(listCategory);
            database.productDao().insertAll(listProducts);
            database.variantsDao().insertAll(listVariants);
        });
    }


}
