package com.nkgroup.headyui.ui;


import android.arch.lifecycle.ViewModelProviders;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nkgroup.headyui.R;
import com.nkgroup.headyui.Viewmodel.CategoryViewModel;
import com.nkgroup.headyui.adapter.CategoryListAdapter;
import com.nkgroup.headyui.db.AppDataBase;
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
import static com.nkgroup.headyui.ui.MainActivity.mListSort;
import static com.nkgroup.headyui.ui.MainActivity.searchView;


public class CategoryFragment extends Fragment implements CategoryListAdapter.OnItemClickListener, VolleyService.ResponseListener {

    private VolleyService volleyService;
    private AppExecutors mAppExecutors;
    private AppDataBase appDatabase;
    private CategoryViewModel viewModel;
    private static final ArrayList<String> mListSuggestion = new ArrayList<>();
    private CategoryListAdapter categoryListAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.dashboard_activity, container, false);//layout_add_property
        RecyclerView recyclerView = view.findViewById(R.id.userList);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(llm);
        categoryListAdapter = new CategoryListAdapter(CategoryFragment.this);
        recyclerView.setAdapter(categoryListAdapter);
        viewModel = ViewModelProviders.of(this).get(CategoryViewModel.class);
        volleyService = new VolleyService( this);
        mAppExecutors = new AppExecutors();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        appDatabase = AppDataBase.getDatabase(getActivity());
        getCategory();
    }


    private void ShowData(CategoryViewModel viewModel) {
        viewModel.getCategory().observe(this, myProducts -> {
            if (myProducts != null) {
                categoryListAdapter.setProductList(myProducts);
                new asyGetSuggestion(appDatabase).execute();
            }
        });
    }


    //get All category from API
    private void getCategory() {
        volleyService.getObjectResponse();
    }

    private static class asyGetSuggestion extends AsyncTask<Void, Void, Void> {
        private final AppDataBase db;
        asyGetSuggestion(AppDataBase appDatabase) {
            db = appDatabase;
        }
        @Override
        protected Void doInBackground(final Void... params) {
            mListSuggestion.clear();
            List<String> myProducts1 = db.categoryDao().getSearchSuggessionCategory();
            List<String> myProducts2 = db.productDao().getSearchSuggessionProduct();
            mListSuggestion.addAll(myProducts1);
            mListSuggestion.addAll(myProducts2);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            String[] stringArray = mListSuggestion.toArray(new String[0]);
            searchView.setSuggestions(stringArray);
            if (searchView.isSearchOpen()) {
                searchView.closeSearch();
            }
        }
    }

    @Override
    public void onItemClick(Category item) {
        ProductListFragment fragment = new ProductListFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("Id", item.getId());
        bundle.putString("name", item.getCategoryName());
        fragment.setArguments(bundle);
        assert getFragmentManager() != null;
        getFragmentManager()
                .beginTransaction()
                .addToBackStack("category")
                .replace(R.id.fragment_container,
                        fragment, null).commit();
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
        buildDateBase(getActivity(), mAppExecutors, mListCategory, mListProduct, mListVariant);
        ShowData(viewModel);
    }
}
