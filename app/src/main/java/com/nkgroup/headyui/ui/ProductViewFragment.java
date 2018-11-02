package com.nkgroup.headyui.ui;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.nkgroup.headyui.R;
import com.nkgroup.headyui.Viewmodel.VariantsViewModel;
import com.nkgroup.headyui.model.Variants;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;

public class ProductViewFragment extends Fragment {
    private LinearLayout mLnrColorView;
    private LinearLayout mLnrSizeView;
    private TextView mTvProductName;
    private TextView mTvProductPrice;
    private ImageView mIvProductImage;
    private Button mBtChangeColorSize;
    private BottomSheetBehavior sheetBehavior;
    private ImageView ivSelectedColor;
    private int iColor = 0;
   // private int id;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.product_view_fragment, container, false);//layout_add_property
        mTvProductName = view.findViewById(R.id.tvProductName);
        mTvProductPrice = view.findViewById(R.id.tvProductPrice);
        mIvProductImage = view.findViewById(R.id.ivProductImage);
        mBtChangeColorSize = view.findViewById(R.id.btChangeColorSize);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        mLnrColorView = view.findViewById(R.id.viewColor);
        mLnrSizeView = view.findViewById(R.id.viewSize);
        mLnrColorView.setLayoutParams(layoutParams);
        mLnrSizeView.setLayoutParams(layoutParams);
        configBottomSheet(view);
        mBtChangeColorSize.setOnClickListener(view1 -> sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED));
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        assert getArguments() != null;
        Bundle bundle = getArguments();
        //id = bundle.getInt("id");
        mTvProductName.setText(bundle.getString("name"));
        mTvProductPrice.setText(String.valueOf(bundle.getDouble("price")));
        mIvProductImage.setColorFilter(ContextCompat.getColor(Objects.requireNonNull(getActivity()), R.color.colorPrimary), android.graphics.PorterDuff.Mode.MULTIPLY);
        VariantsViewModel.Factory factory = new VariantsViewModel.Factory(Objects.requireNonNull(getActivity()).getApplication(), getArguments().getInt("Id"));
        final VariantsViewModel model = ViewModelProviders.of(this, factory).get(VariantsViewModel.class);
        getProduct(model);

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

    //observe variants[color,size,price] changes
    private void getProduct(VariantsViewModel model) {
        model.getProduct().observe(this, variants -> {
            assert variants != null;
            getBottomSheet(variants);
        });
    }

    //dynamic create Image view  based on color
    private void getBottomSheet(List<Variants> variants1) {
        HashSet<String> h = new HashSet<>();
        for (int i = 0; i < variants1.size(); i++) {
            h.add(variants1.get(i).getVariantColor());
        }
        List<String> list = new ArrayList<>(h);
        iColor = 0;
        for (int i = 0; i < list.size(); i++) {
            iColor = getColorByName(list.get(i));
            ImageView img1 = new ImageView(getActivity());
            img1.setId(i);
            img1.setBackgroundColor(iColor);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(150, 150);
            lp.setMargins(8, 8, 8, 8);
            img1.setLayoutParams(lp);
            if (i == 0) {
                ivSelectedColor = img1;
                img1.setImageResource(R.drawable.ic_check);
                mIvProductImage.setColorFilter(iColor);
                getSize(variants1, list.get(i));
            }

            img1.setOnClickListener(view -> {
                iColor = getColorByName(list.get(view.getId()));
                ivSelectedColor.setImageResource(0);
                ivSelectedColor = (ImageView) view;
                ivSelectedColor.setImageResource(R.drawable.ic_check);
                mIvProductImage.setColorFilter(iColor);
                getSize(variants1, list.get(view.getId()));
            });
            mLnrColorView.addView(img1);
        }
    }

    //dynamic create radio button based on size
    private void getSize(List<Variants> variants, String selectedColor) {
        if (mLnrSizeView.getChildCount() != 0) {
            mLnrSizeView.removeAllViews();
        }
        boolean first = true;
        RadioGroup radioGroup = new RadioGroup(getActivity());
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, 150);
        lp.setMargins(8, 8, 8, 8);
        radioGroup.setLayoutParams(lp);
        radioGroup.setOrientation(RadioGroup.HORIZONTAL);
        for (int j = 0; j < variants.size(); j++) {
            if (selectedColor.equals(variants.get(j).getVariantColor())) {
                double price = variants.get(j).getVariantPrice();
                String size = variants.get(j).getVariantSize();
                RadioButton radioButton = new RadioButton(getActivity());
                radioButton.setLayoutParams(lp);
                radioButton.setId(j);
                radioButton.setText(size);
                radioGroup.addView(radioButton);
                if (first) {
                    radioButton.setChecked(true);
                    first = false;
                    mTvProductPrice.setText(String.valueOf(price));
                    String str = "COLOR : " + selectedColor + " SIZE : " + variants.get(j).getVariantSize();
                    mBtChangeColorSize.setText(str);
                }

                radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
                    String str = "COLOR : " + variants.get(checkedId).getVariantColor() + " SIZE : " + variants.get(checkedId).getVariantSize();
                    mBtChangeColorSize.setText(str);
                    mTvProductPrice.setText(String.valueOf(variants.get(checkedId).getVariantPrice()));
                });
            }
        }
        mLnrSizeView.addView(radioGroup);
    }

    //get color using name
    private static int getColorByName(String name) {
        int blue = 0;
        try {
            Class color = Class.forName("android.graphics.Color");
            Field field = color.getField(name.toUpperCase());
            blue = field.getInt(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return blue;
    }

}
