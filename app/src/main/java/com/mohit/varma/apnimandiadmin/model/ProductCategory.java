package com.mohit.varma.apnimandiadmin.model;

public class ProductCategory {

    private String mProductCategoryName;
    private int mProductCategoryId;
    private String mProductCategoryImage;
    private String mProductCategoryDate;

    public ProductCategory(String mProductCategoryName, int mProductCategoryId, String mProductCategoryImage, String mProductCategoryDate) {
        this.mProductCategoryName = mProductCategoryName;
        this.mProductCategoryId = mProductCategoryId;
        this.mProductCategoryImage = mProductCategoryImage;
        this.mProductCategoryDate = mProductCategoryDate;
    }

    public String getmProductCategoryName() {
        return mProductCategoryName;
    }

    public void setmProductCategoryName(String mProductCategoryName) {
        this.mProductCategoryName = mProductCategoryName;
    }

    public int getmProductCategoryId() {
        return mProductCategoryId;
    }

    public void setmProductCategoryId(int mProductCategoryId) {
        this.mProductCategoryId = mProductCategoryId;
    }

    public String getmProductCategoryImage() {
        return mProductCategoryImage;
    }

    public void setmProductCategoryImage(String mProductCategoryImage) {
        this.mProductCategoryImage = mProductCategoryImage;
    }

    public String getmProductCategoryDate() {
        return mProductCategoryDate;
    }

    public void setmProductCategoryDate(String mProductCategoryDate) {
        this.mProductCategoryDate = mProductCategoryDate;
    }

}
