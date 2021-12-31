package com.example.testjava.entity;

import com.example.testjava.annotation.Column;
import com.example.testjava.annotation.Entity;
import com.example.testjava.annotation.Id;
import com.example.testjava.util.SQLDataTypes;
import com.example.testjava.util.ValidationUtil;

import java.util.HashMap;

@Entity(tableName = "phones")
public class Phone {

    @Id(autoIncrement = true)
    @Column(columnName = "id", columnType = SQLDataTypes.INTEGER)
    private int id;
    @Column(columnName = "name", columnType = SQLDataTypes.VARCHAR50)
    private String name;
    @Column(columnName = "brand", columnType = SQLDataTypes.VARCHAR50)
    private String brand;
    @Column(columnName = "price", columnType = SQLDataTypes.DOUBLE)
    private double price;
    @Column(columnName = "description", columnType = SQLDataTypes.VARCHAR255)
    private String description;

    public Phone() {
        this.name = "";
        this.brand = "";
        this.description = "";
        this.price = 0;
    }

    public Phone(int id, String name, String brand, String description, double price) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.brand = brand;
        this.price = price;
    }

    public Phone(String name, String brand, String description, double price) {
        this.name = name;
        this.description = description;
        this.brand = brand;
        this.price = price;
    }

    @Override
    public String toString() {
        return "Phone{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", brand='" + brand + '\'' +
                ", price=" + price +
                ", description='" + description + '\'' +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isValid() {
        return getErrors().size() == 0;
    }

    public HashMap<String, String> getErrors() {
        // Map chứa thông tin lỗi
        HashMap<String, String> errors = new HashMap<>();
        if (name == null || name.length() == 0) {
            errors.put("name", "Enter product's Name.");
        }
        if (description == null || description.length() == 0) {
            errors.put("description", "Enter product's Description.");
        }
        if (brand == null || brand.length() == 0) {
            errors.put("brand", "Enter product's Brand.");
        }
        if (price == 0) {
            errors.put("price", "Enter product's Price.");
        }
        return errors;
    }
}

