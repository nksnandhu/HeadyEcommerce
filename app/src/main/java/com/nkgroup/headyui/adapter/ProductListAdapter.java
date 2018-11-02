package com.nkgroup.headyui.adapter;




import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nkgroup.headyui.R;
import com.nkgroup.headyui.model.Products;

import java.util.List;
import java.util.Objects;

/*Created by Nandhakumar on 25/10/2018.
*/

public class ProductListAdapter extends RecyclerView.Adapter<ProductListAdapter.UserItemViewHolder> {


  private final OnItemClickListener listener;
  private List<? extends Products> mProductList;
    public ProductListAdapter(OnItemClickListener listener) {
      this.listener = listener;
    }


    public void setProductList(final List<? extends Products> productList) {
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
                    Products newProduct = productList.get(newItemPosition);
                    Products oldProduct = mProductList.get(oldItemPosition);
                    return newProduct.getId() == oldProduct.getId()
                            && Objects.equals(newProduct.getProductName(), oldProduct.getProductName());
                }
            });
            mProductList = productList;
            result.dispatchUpdatesTo(this);
        }
    }


    @Override
    public void onBindViewHolder(@NonNull UserItemViewHolder holder, int position) {
        Products user= mProductList.get(position);
        if(user!=null) {
            holder.bindTo(user,listener);
        }
    }


    @NonNull
    @Override
    public UserItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_product, parent, false);
        return new UserItemViewHolder(view);
    }


    @Override
    public int getItemCount() {
        return mProductList == null ? 0 : mProductList.size();
    }
    static class UserItemViewHolder extends RecyclerView.ViewHolder {
        final TextView mTvProductName;
        final TextView mtvProductPrice;

        private UserItemViewHolder(View itemView) {
            super(itemView);
            mTvProductName = itemView.findViewById(R.id.tvName);
            mtvProductPrice = itemView.findViewById(R.id.tvProductPrice);
        }

        private void bindTo(Products user,/*, OnItemClickListener listener*/OnItemClickListener listener) {
            mTvProductName.setText(String.valueOf(user.getProductName()));
            mtvProductPrice.setText(String.valueOf(user.getLowPrice()));
            itemView.setOnClickListener(view -> listener.onItemClick(user));
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Products item);
    }
}
