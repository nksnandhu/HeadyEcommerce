package com.nkgroup.headyui.ui;

import android.arch.lifecycle.ViewModelProviders;
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
import com.nkgroup.headyui.Viewmodel.ProductViewModel;
import com.nkgroup.headyui.adapter.ProductListAdapter;
import com.nkgroup.headyui.model.Products;

import java.util.Objects;

public class ProductListFragment extends Fragment implements ProductListAdapter.OnItemClickListener{
    private ProductListAdapter mCommentAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dashboard_activity, container, false);//layout_add_property
        RecyclerView recyclerView = view.findViewById(R.id.userList);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(llm);
        mCommentAdapter= new ProductListAdapter(this);
        recyclerView.setAdapter(mCommentAdapter);
        return view;
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

       /* CategoryViewModel viewModel=ViewModelProviders.of(this).get(CategoryViewModel.class);
        viewModel.getSelectedCategory().observe(this, new Observer<Category>() {
            @Override
            public void onChanged(@Nullable Category categories) {
                ProductViewModel.Factory factory = new ProductViewModel.Factory(Objects.requireNonNull(getActivity()).getApplication(), categories.getId());
                final ProductViewModel model = ViewModelProviders.of(getActivity(),factory).get(ProductViewModel.class);
                subscribeToModel(model);
            }
        });*/

        assert getArguments() != null;
        ProductViewModel.Factory factory = new ProductViewModel.Factory(Objects.requireNonNull(getActivity()).getApplication(), getArguments().getInt("Id"));
        final ProductViewModel model = ViewModelProviders.of(this,factory).get(ProductViewModel.class);
        subscribeToModel(model);
    }

    private void subscribeToModel(final ProductViewModel model) {
        // Observe product data
      /*  model.loadProduct(getArguments().getInt("Id")).observe(this, new Observer<List<Products>>() {
            @Override
            public void onChanged(@Nullable List<Products> products) {
                if (products != null) {
                    mCommentAdapter.setProductList(products);
                }
            }
        });*/
        model.getProduct().observe(this, commentEntities -> {
            if (commentEntities != null) {
                mCommentAdapter.setProductList(commentEntities);
            }
        });
    }
    @Override
    public void onItemClick(Products item) {
        ProductViewFragment fragment = new ProductViewFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("Id", item.getId());
        bundle.putString("name", item.getProductName());
        bundle.putString("color", item.getDisplayColor());
        bundle.putDouble("price", item.getLowPrice());
        fragment.setArguments(bundle);
        assert getFragmentManager() != null;
        getFragmentManager()
                .beginTransaction()
                .addToBackStack("product")
                .replace(R.id.fragment_container,
                        fragment, null).commit();
    }
}
