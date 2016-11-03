package com.greg.dagger;

/**
 * Created by Greg on 31-10-2016.
 */
public class dummyClass implements IdummyClass {
    private String productName;
    private String description;
    private double salePrice;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public double getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(double salePrice) {
        this.salePrice = salePrice;
    }

    @Override
    public void doWork() {

    }
}
