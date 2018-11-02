package com.nkgroup.headyui.ui;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.nkgroup.headyui.db.AppDataBase;
import com.nkgroup.headyui.R;
import com.nkgroup.headyui.model.Category;
import com.nkgroup.headyui.model.Products;
import com.nkgroup.headyui.model.SortName;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    public static final ArrayList<SortName> mListSort = new ArrayList<>();
    @SuppressLint("StaticFieldLeak")
    public static MaterialSearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar myToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        if (savedInstanceState == null) {
            CategoryFragment fragment = new CategoryFragment();
            getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, fragment, "ProductListViewModel").commit();
            getSearch();
        }
    }

    private void getSearch() {
        searchView = findViewById(R.id.search_view);

        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {


            @Override
            public boolean onQueryTextSubmit(String query) {
                AppDataBase appDatabase = AppDataBase.getDatabase(MainActivity.this);
                new insertAsyncTask(appDatabase, query).execute();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //Do some magic
                return false;
            }
        });

        searchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {
                //Do some magic
                searchView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onSearchViewClosed() {
                //Do some magic
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        searchView.setMenuItem(item);
        return true;
    }

    @SuppressLint("StaticFieldLeak")
    private class insertAsyncTask extends AsyncTask<Void, Void, Integer> implements com.nkgroup.headyui.ui.insertAsyncTask {
        private final AppDataBase db;
        String str;
        int id = 0;

        double price = 0;
        String color = "";

        insertAsyncTask(AppDataBase appDatabase, String query) {
            db = appDatabase;
            str = query;
        }

        @Override
        protected Integer doInBackground(final Void... params) {
            Category category = db.categoryDao().getSingleCategory(str);
            if (category != null) {
                id = category.getId();
                str = category.getCategoryName();
                return 1;
            } else {
                Products products = db.productDao().getSingleProduct(str);
                if (products != null) {
                    id = products.getId();
                    str = products.getProductName();
                    color = products.getDisplayColor();
                    price = products.getLowPrice();
                    return 2;
                }
            }
            return 0;
        }


        @Override
        protected void onPostExecute(Integer response) {
            super.onPostExecute(response);
            if (response == 1) {
                ProductListFragment fragment = new ProductListFragment();
                Bundle bundle = new Bundle();
                bundle.putInt("Id", id);
                bundle.putString("name", str);
                fragment.setArguments(bundle);
                assert getFragmentManager() != null;
                getSupportFragmentManager()
                        .beginTransaction()
                        .addToBackStack("category")
                        .replace(R.id.fragment_container,
                                fragment, null).commit();
            } else if (response == 2) {
                ProductViewFragment fragment = new ProductViewFragment();
                Bundle bundle = new Bundle();
                bundle.putInt("Id", id);
                bundle.putString("name", str);
                bundle.putString("color", color);
                bundle.putDouble("price", price);
                fragment.setArguments(bundle);
                assert getFragmentManager() != null;
                getSupportFragmentManager()
                        .beginTransaction()
                        .addToBackStack("product")
                        .replace(R.id.fragment_container,
                                fragment, null).commit();
            } else {
                Toast.makeText(MainActivity.this, "No Data Found", Toast.LENGTH_SHORT).show();
            }


        }

    }






   /* boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            finish();
            return;
        }
        if (searchView.isSearchOpen()) {
            searchView.closeSearch();
        }
        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }*/
}
