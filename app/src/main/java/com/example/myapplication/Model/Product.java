package com.example.myapplication.Model;

public class Product {
    private int productID;
    private int categoryID;
    private float Price;
    private String productName;
    private long quantity;

    public int getProductID() {
        return productID;
    }

    public void setProductID(int productID) {
        this.productID = productID;
    }

    public int getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(int categoryID) {
        this.categoryID = categoryID;
    }

    public float getPrice() {
        return Price;
    }

    public void setPrice(float price) {
        Price = price;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public long getQuantity() {
        return quantity;
    }

    public void setQuantity(long quantity) {
        this.quantity = quantity;
    }

    public Product(int productID, int categoryID, float price, String productName, long quantity) {
        this.productID = productID;
        this.categoryID = categoryID;
        Price = price;
        this.productName = productName;
        this.quantity = quantity;
    }
}
