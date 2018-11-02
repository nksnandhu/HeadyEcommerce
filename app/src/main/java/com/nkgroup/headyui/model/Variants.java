package com.nkgroup.headyui.model;


import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

import static android.arch.persistence.room.ForeignKey.CASCADE;



@Entity(tableName = "variants",foreignKeys = {@ForeignKey(entity = Products.class,
        parentColumns = "id",
        childColumns = "productId",
        onDelete = CASCADE)},
        indices = {@Index(value = "productId") })
public class Variants {
    @PrimaryKey
    private int id;
    private final int productId;
    private final String variantColor;
    private final String  variantSize;
    private final double variantPrice;

    public Variants(int id,int productId, String variantColor, String variantSize, double variantPrice) {
        this.id = id;
        this.productId = productId;
        this.variantColor = variantColor;
        this.variantSize = variantSize;
        this.variantPrice = variantPrice;
    }


    public int getProductId() {
        return productId;
    }



    /*public void setProductId(int productId) {
        this.productId = productId;
    }
*/
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getVariantColor() {
        return variantColor;
    }

  /*  public void setVariantColor(String variantColor) {
        this.variantColor = variantColor;
    }
*/
    public String getVariantSize() {
        return variantSize;
    }

    /*public void setVariantSize(String variantSize) {
        this.variantSize = variantSize;
    }
*/
    public double getVariantPrice() {
        return variantPrice;
    }
/*
    public void setVariantPrice(double variantPrice) {
        this.variantPrice = variantPrice;
    }*/
}
