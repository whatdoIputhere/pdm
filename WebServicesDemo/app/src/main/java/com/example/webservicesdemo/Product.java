package com.example.webservicesdemo;

public class Product {
    private int Id;
    private String Name;
    private double Price;
    private boolean Success;

    public Product() {
    }

    public Product(String Name, double Price) {
        this.Name = Name;
        this.Price = Price;
    }

    public Product(int Id, String Name, double Price) {
        this.Id = Id;
        this.Name = Name;
        this.Price = Price;
    }

    public int getId() {
        return this.Id;
    }

    public void setId(int Id) {
        this.Id = Id;
    }

    public String getName() {
        return this.Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public double getPrice() {
        return this.Price;
    }

    public void setPrice(double Price) {
        this.Price = Price;
    }

    public boolean getSuccess() {
        return this.Success;
    }

    public void setSuccess(boolean Success) {
        this.Success = Success;
    }

    @Override
    public String toString() {
        return "Product{" +
                "Id=" + Id +
                ", Name='" + Name + '\'' +
                ", Price=" + Price +
                ", Success=" + Success +
                '}';
    }
}
