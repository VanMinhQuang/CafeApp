package com.example.myapplication.Model;

import java.util.HashMap;
import java.util.Map;

public class Product {
    private int productID;
    private String categoryProduct;
    private float price;
    private String productName;
    private long quantity;
    private String productURI;

    public int getProductID() {
        return productID;
    }

    public void setProductID(int productID) {
        this.productID = productID;
    }

    public String getCategoryProduct() {
        return categoryProduct;
    }

    public void setCategoryProduct(String categoryProduct) {
        this.categoryProduct = categoryProduct;
    }

    public String getProductURI() {
        return productURI;
    }

    public void setProductURI(String productURI) {
        this.productURI = productURI;
    }


    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
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


    public Product() {
    }

    public Product(int productID, String category, float price, String productName, long quantity, String productURI) {
        this.productID = productID;
        this.categoryProduct = category;
        this.price = price;
        this.productName = productName;
        this.quantity = quantity;
        this.productURI = productURI;
    }
    public Map<String,Object> toMap(){
        HashMap<String, Object> result = new HashMap<>();
        result.put("productID", productID);
        result.put("productName", productName);
        result.put("categoryProduct", categoryProduct);
        result.put("price",price);
        result.put("quantity",quantity);
        return result;
    }
}
