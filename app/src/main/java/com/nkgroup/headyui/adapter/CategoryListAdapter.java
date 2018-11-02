package com.nkgroup.headyui.adapter;


import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nkgroup.headyui.R;
import com.nkgroup.headyui.model.Category;

import java.util.List;
import java.util.Objects;

/*Created by Nandhakumar on 25/10/2018.
 */

public class CategoryListAdapter extends RecyclerView.Adapter<CategoryListAdapter.UserItemViewHolder> {

    private final OnItemClickListener listener;
    private List<? extends Category> mProductList;

    public CategoryListAdapter(OnItemClickListener listener) {
        this.listener = listener;
    }
    public void setProductList(final List<? extends Category> productList) {
        if (mProductList == null) {
            mProductList = productList;
            notifyItemRangeInserted(0, productList.size());
        } else {
            DiffUtil.DiffResult result = DiffUtil.calculateDiff(new DiffUtil.Callback() {
                @Override
                public int getOldListSize() {
                    return mProductList.size();
                }

                @Override
                public int getNewListSize() {
                    return productList.size();
                }

                @Override
                public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                    return mProductList.get(oldItemPosition).getId() ==
                            productList.get(newItemPosition).getId();
                }

                @Override
                public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                    Category newProduct = productList.get(newItemPosition);
                    Category oldProduct = mProductList.get(oldItemPosition);
                    return newProduct.getId() == oldProduct.getId()
                            && Objects.equals(newProduct.getCategoryName(), oldProduct.getCategoryName());
                }
            });
            mProductList = productList;
            result.dispatchUpdatesTo(this);
        }
    }


    @Override
    public void onBindViewHolder(@NonNull UserItemViewHolder holder, int position) {
        Category user = mProductList.get(position);
        if (user != null) {
            holder.bindTo(user, listener);
        }
    }


    @NonNull
    @Override
    public UserItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_user_list, parent, false);
        return new UserItemViewHolder(view);
    }


    @Override
    public int getItemCount() {
        return mProductList == null ? 0 : mProductList.size();
    }

    static class UserItemViewHolder extends RecyclerView.ViewHolder {
        final TextView mTvCategoryName;// mTvCategoryId;

        UserItemViewHolder(View itemView) {
            super(itemView);
            mTvCategoryName = itemView.findViewById(R.id.tvName);
          //  mTvCategoryId = itemView.findViewById(R.id.tvId);
        }

        void bindTo(Category user,/*, OnItemClickListener listener*/OnItemClickListener listener) {
            mTvCategoryName.setText(String.valueOf(user.getCategoryName()));
            //mTvCategoryId.setText(String.valueOf(user.getCategoryName()));
            itemView.setOnClickListener(view -> listener.onItemClick(user));
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Category item);
    }
}
