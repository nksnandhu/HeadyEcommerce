package com.nkgroup.headyui.model;

import java.util.ArrayList;

public class SortName {


  private   String sortName;
    private ArrayList<SortItem> mSortItems;

    public SortName(String sortName, ArrayList<SortItem> mSortItems) {
        this.sortName = sortName;
        this.mSortItems = mSortItems;
    }

    public String getSortName() {
        return sortName;
    }

 /*   public void setSortName(String sortName) {
        this.sortName = sortName;
    }*/

    public ArrayList<SortItem> getmSortItems() {
        return mSortItems;
    }

    /*public void setmSortItems(ArrayList<SortItem> mSortItems) {
        this.mSortItems = mSortItems;
    }*/
}
