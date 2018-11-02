package com.nkgroup.headyui.model;


import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(tableName = "sub_category",foreignKeys = {@ForeignKey(entity = Category.class,
        parentColumns = "id",
        childColumns = "categoryId",
        onDelete = CASCADE)},
        indices = {@Index(value = "categoryId") })
public class Products {

    @PrimaryKey
    private int id;
    private int categoryId;
    private final String productName;
    private final String productDateAdded;
    private final String displayColor;
    private final double lowPrice;

    public Products(int id, int categoryId,String productName, String productDateAdded,double lowPrice,String displayColor) {
        this.id = id;
        this.categoryId = categoryId;
        this.productName = productName;
        this.productDateAdded = productDateAdded;
        this.lowPrice = lowPrice;
        this.displayColor = displayColor;

    }

    public String getDisplayColor() {
        return displayColor;
    }

   /* public void setDisplayColor(String displayColor) {
        this.displayColor = displayColor;
    }
*/
    public double getLowPrice() {
        return lowPrice;
    }

 /*   public void setLowPrice(double lowPrice) {
        this.lowPrice = lowPrice;
    }*/

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProductName() {
        return productName;
    }

   /* public void setProductName(String productName) {
        this.productName = productName;
    }
*/
    public String getProductDateAdded() {
        return productDateAdded;
    }

   /* public void setProductDateAdded(String productDateAdded) {
        this.productDateAdded = productDateAdded;
    }*/
}
