package com.nkgroup.headyui.ui;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.nkgroup.headyui.R;
import com.nkgroup.headyui.Viewmodel.ProductViewModel;
import com.nkgroup.headyui.adapter.ProductListAdapter;
import com.nkgroup.headyui.model.Products;
import com.nkgroup.headyui.model.SortItem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

import static com.nkgroup.headyui.ui.MainActivity.mListSort;

public class ProductListFragment extends Fragment implements ProductListAdapter.OnItemClickListener {
    private ProductListAdapter mProductListAdapter;
    private RadioGroup radioGroup;
    private BottomSheetBehavior sheetBehavior;
    private List<Products> sortByField;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.product_list_fragment, container, false);//layout_add_property
        RecyclerView recyclerView = view.findViewById(R.id.userList);
        radioGroup = view.findViewById(R.id.rgSort);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(llm);
        mProductListAdapter = new ProductListAdapter(this);
        recyclerView.setAdapter(mProductListAdapter);
        configBottomSheet(view);
        view.findViewById(R.id.tvSort).setOnClickListener(view1 -> sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED));
        return view;
    }


    private void getSort(List<Products> productsList) {
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, 150);
        lp.setMargins(8, 8, 8, 8);
        for (int i = 0; i < mListSort.size(); i++) {
            RadioButton radioButton = new RadioButton(getContext());
            radioButton.setLayoutParams(lp);
            radioButton.setId(i);
            radioButton.setText(mListSort.get(i).getSortName());
            radioGroup.addView(radioButton);
            radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
                sortByField = new ArrayList<>();
                Collections.sort(mListSort.get(checkedId).getmSortItems(), new CustomComparator());
                int z = 0;
                for (int j = z; j < mListSort.get(checkedId).getmSortItems().size(); j++) {
                    SortItem mSortItems = mListSort.get(checkedId).getmSortItems().get(j);
                    int id = mSortItems.getId();
                    for (int k = 0; k < productsList.size(); k++) {
                        if (productsList.get(k).getId() == id) {
                            sortByField.add(new Products(productsList.get(k).getId(), productsList.get(k).getCategoryId(), productsList.get(k).getProductName(),
                                    productsList.get(k).getProductDateAdded(), productsList.get(k).getLowPrice(), productsList.get(k).getDisplayColor()));
                            z=j+1;
                            break;
                        }
                    }
                }
                mProductListAdapter.setProductList(sortByField);
            });
        }


    }

    class CustomComparator implements Comparator<SortItem> {
        @Override
        public int compare(SortItem o1, SortItem o2) {
            return o2.getCount() - o1.getCount();
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        assert getArguments() != null;
        ProductViewModel.Factory factory = new ProductViewModel.Factory(Objects.requireNonNull(getActivity()).getApplication(), getArguments().getInt("Id"));
        final ProductViewModel model = ViewModelProviders.of(this, factory).get(ProductViewModel.class);
        showData(model);
    }

    // Observe product data
    private void showData(final ProductViewModel model) {

      /*  model.loadProduct(getArguments().getInt("Id")).observe(this, new Observer<List<Products>>() {
            @Override
            public void onChanged(@Nullable List<Products> products) {
                if (products != null) {
                    mProductListAdapter.setProductList(products);
                }
            }
        });*/
        model.getProduct().observe(this, productsList -> {
            if (productsList != null) {
                mProductListAdapter.setProductList(productsList);
                if (radioGroup.getChildCount() == 0)
                    getSort(productsList);
            }
        });
    }


    //config bottom sheet
    private void configBottomSheet(View view) {
        LinearLayout layoutBottomSheet = view.findViewById(R.id.bottom_sheet);
        sheetBehavior = BottomSheetBehavior.from(layoutBottomSheet);
        sheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                switch (newState) {
                    case BottomSheetBehavior.STATE_HIDDEN:
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED: {
                        //btnBottomSheet.setText("Close Sheet");
                    }
                    break;
                    case BottomSheetBehavior.STATE_COLLAPSED: {
                        // btnBottomSheet.setText("Expand Sheet");
                    }
                    break;
                    case BottomSheetBehavior.STATE_DRAGGING:
                        break;
                    case BottomSheetBehavior.STATE_SETTLING:
                        break;
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

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
