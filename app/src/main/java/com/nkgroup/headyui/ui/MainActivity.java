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
import com.nkgroup.headyui.db.AppExecutors;
import com.nkgroup.headyui.model.Category;
import com.nkgroup.headyui.model.Products;
import com.nkgroup.headyui.model.SortItem;
import com.nkgroup.headyui.model.SortName;
import com.nkgroup.headyui.model.Variants;
import com.nkgroup.headyui.service.VolleyService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static com.nkgroup.headyui.db.AppDataBase.buildDateBase;

public class MainActivity extends AppCompatActivity implements VolleyService.ResponseListener {

    public static final ArrayList<SortName> mListSort = new ArrayList<>();
    @SuppressLint("StaticFieldLeak")
    public static MaterialSearchView searchView;
    private VolleyService volleyService;
    private AppExecutors mAppExecutors;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar myToolbar = findViewById(R.id.toolbar);
        volleyService = new VolleyService(this);
        mAppExecutors = new AppExecutors();
        setSupportActionBar(myToolbar);
        if (savedInstanceState == null) {
            CategoryFragment fragment = new CategoryFragment();
            getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, fragment, "ProductListViewModel").commit();
            getSearch();
            getCategory();
        }
    }

    private void getCategory() {
        volleyService.getObjectResponse();
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
    private class insertAsyncTask extends AsyncTask<Void, Void, Integer> {
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
            if (response != 0) {
                Bundle bundle = new Bundle();
                bundle.putInt("Id", id);
                bundle.putString("name", str);
                if (response == 1) {
                    ProductListFragment fragment = new ProductListFragment();
                    fragment.setArguments(bundle);
                    assert getFragmentManager() != null;
                    getSupportFragmentManager().beginTransaction().addToBackStack("category")
                            .replace(R.id.fragment_container, fragment, null).commit();
                } else if (response == 2) {
                    ProductViewFragment fragment = new ProductViewFragment();
                    bundle.putString("color", color);
                    bundle.putDouble("price", price);
                    fragment.setArguments(bundle);
                    assert getFragmentManager() != null;
                    getSupportFragmentManager().beginTransaction().addToBackStack("product")
                            .replace(R.id.fragment_container, fragment, null).commit();
                }
            } else {
                Toast.makeText(MainActivity.this, "No Data Found", Toast.LENGTH_SHORT).show();
            }


        }

    }


    @Override
    public void onObjectListener(JSONObject jsonObject) {
        List<Category> mListCategory = new ArrayList<>();
        List<Products> mListProduct = new ArrayList<>();
        List<Variants> mListVariant = new ArrayList<>();
        ArrayList<SortItem> mListMostViewedProducts;
        try {
            JSONArray jAryCategory = jsonObject.getJSONArray("categories");
            for (int posCategory = 0; posCategory < jAryCategory.length(); posCategory++) {
                JSONObject jObjCategoryItem = jAryCategory.getJSONObject(posCategory);
                int categoryId = jObjCategoryItem.getInt("id");
                String categoryName = jObjCategoryItem.getString("name");
                mListCategory.add(new Category(categoryId, categoryName));
                JSONArray jAryProducts = jObjCategoryItem.getJSONArray("products");
                for (int posProduct = 0; posProduct < jAryProducts.length(); posProduct++) {
                    JSONObject jObjProductItem = jAryProducts.getJSONObject(posProduct);
                    int productId = jObjProductItem.getInt("id");
                    String productNae = jObjProductItem.getString("name");
                    String ProductAddedDate = jObjProductItem.getString("date_added");
                    JSONArray jAryVariants = jObjProductItem.getJSONArray("variants");
                    double displayPrice = 0;
                    String currentDisplayColor = "";
                    for (int postVariants = 0; postVariants < jAryVariants.length(); postVariants++) {
                        JSONObject jObjVariantsItem = jAryVariants.getJSONObject(postVariants);
                        int variantId = jObjVariantsItem.getInt("id");
                        String variantColor = jObjVariantsItem.getString("color");
                        String variantSize = jObjVariantsItem.getString("size");
                        double variantPrice = jObjVariantsItem.getDouble("price");
                        mListVariant.add(new Variants(variantId, productId, variantColor, variantSize, variantPrice));
                        if (postVariants == 0) {
                            displayPrice = variantPrice;//low price  & color will display
                            currentDisplayColor = variantColor;
                        } else if (displayPrice > variantPrice) {
                            displayPrice = variantPrice;
                            currentDisplayColor = variantColor;
                        }
                    }

                    mListProduct.add(new Products(productId, categoryId, productNae, ProductAddedDate, displayPrice, currentDisplayColor));


                   /* JSONObject jObjTaxItem = jObjProductItem.getJSONObject("tax");
                    String name = jObjTaxItem.getString("name");
                    String value = jObjTaxItem.getString("value");*/
                }
              /*  JSONArray jAryChildCategories = jObjCategoryItem.getJSONArray("child_categories");
                for (int posChildCategories = 0; posChildCategories < jAryChildCategories.length(); posChildCategories++) {
                  //  String sChildCategories = jAryChildCategories.optString(posChildCategories);
                }*/
            }
            JSONArray jAryRankings = jsonObject.getJSONArray("rankings");
            for (int postRanding = 0; postRanding < jAryRankings.length(); postRanding++) {
                JSONObject jObjRankingItem = jAryRankings.getJSONObject(postRanding);
                mListMostViewedProducts = new ArrayList<>();
                JSONArray jAryRankingProduct = jObjRankingItem.getJSONArray("products");
                for (int postProduct = 0; postProduct < jAryRankingProduct.length(); postProduct++) {
                    JSONObject jObjRankingProduct = jAryRankingProduct.getJSONObject(postProduct);
                    Iterator<String> keys = jObjRankingProduct.keys();
                    int count = 0;
                    int id = jObjRankingProduct.getInt("id");
                    while (keys.hasNext()) {
                        String keyValue = keys.next();
                        if (!keyValue.equals("id")) {
                            count = jObjRankingProduct.getInt(keyValue);
                        }
                    }
                    mListMostViewedProducts.add(new SortItem(id, count));
                }
                mListSort.add(new SortName(jObjRankingItem.getString("ranking"), mListMostViewedProducts));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        buildDateBase(this, mAppExecutors, mListCategory, mListProduct, mListVariant);
        //  ShowData(viewModel);
    }
}
