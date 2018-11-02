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
import com.nkgroup.headyui.model.Category;

import java.util.ArrayList;
import java.util.List;

import static com.nkgroup.headyui.ui.MainActivity.searchView;


public class CategoryFragment extends Fragment implements CategoryListAdapter.OnItemClickListener{

    private AppDataBase appDatabase;
    private static final ArrayList<String> mListSuggestion = new ArrayList<>();
    private CategoryListAdapter categoryListAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.category_list_fragment, container, false);//layout_add_property
        RecyclerView recyclerView = view.findViewById(R.id.userList);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(llm);
        categoryListAdapter = new CategoryListAdapter(CategoryFragment.this);
        recyclerView.setAdapter(categoryListAdapter);
        CategoryViewModel viewModel = ViewModelProviders.of(this).get(CategoryViewModel.class);
        ShowData(viewModel);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        appDatabase = AppDataBase.getDatabase(getActivity());

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

}
