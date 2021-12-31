package com.example.assignmentjava.entity;

import com.example.assignmentjava.annotation.Column;
import com.example.assignmentjava.annotation.Entity;
import com.example.assignmentjava.annotation.ForeignKey;
import com.example.assignmentjava.annotation.Id;
import com.example.assignmentjava.util.SQLDataTypes;
import com.example.assignmentjava.util.ValidationUtil;

import java.util.Date;
import java.util.HashMap;

@Entity(tableName = "products")
public class Product {

    @Id(autoIncrement = true)
    @Column(columnName = "id", columnType = SQLDataTypes.INTEGER)
    private int id;
    @Column(columnName = "name", columnType = SQLDataTypes.VARCHAR50)
    private String name;
    @Column(columnName = "description", columnType = SQLDataTypes.VARCHAR255)
    private String description;
    @Column(columnName = "thumbnail", columnType = SQLDataTypes.TEXT)
    private String thumbnail;
    @Column(columnName = "price", columnType = SQLDataTypes.DOUBLE)
    private double price;
    @Column(columnName = "status", columnType = SQLDataTypes.INTEGER)
    private int status;
    @Column(columnName = "started_date", columnType = SQLDataTypes.DATE)
    private Date startedDate;
    @Column(columnName = "edit_date", columnType = SQLDataTypes.DATE)
    private Date editDate;
    @Column(columnName = "is_deleted", columnType = SQLDataTypes.INTEGER)
    private int isDeleted;
    @Column(columnName = "categoryId", columnType = SQLDataTypes.INTEGER)
    @ForeignKey(referenceColumn = "id", referenceTable = "categories")
    private int categoryId;

    public Product() {
        this.name = "";
        this.description = "";
        this.thumbnail = "";
        this.status = 1;
        this.price = 0;
        this.startedDate = new Date();
        this.editDate = new Date();
        this.isDeleted = 0;
    }

    public Product(int id, String name, String description, String thumbnail, double price, int status, Date startedDate, Date editDate) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.thumbnail = thumbnail;
        this.price = price;
        this.status = status;
        this.startedDate = startedDate;
        this.editDate = editDate;
        this.categoryId = categoryId;
        this.isDeleted = 0;
    }

    public Product(String name, String description, String thumbnail, double price, Date startedDate, int status) {
        this.name = name;
        this.description = description;
        this.thumbnail = thumbnail;
        this.price = price;
        this.status = status;
        this.startedDate = startedDate;
        this.categoryId = categoryId;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", thumbnail='" + thumbnail + '\'' +
                ", price=" + price +
                ", status=" + status +
                ", startedDate=" + startedDate +
                ", editDate=" + editDate +
                ", isDeleted=" + isDeleted +
                ", categoryId=" + categoryId +
                '}';
    }

    public int getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(int isDeleted) {
        this.isDeleted = isDeleted;
    }

    public int getCategoryId() { return categoryId; }

    public void setCategoryId(int categoryId) { this.categoryId = categoryId; }

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setStartedDate(Date startedDate) {
        this.startedDate = startedDate;
    }

    public Date getStartedDate() {
        return startedDate;
    }

    public void setEditDate(Date editDate) {
        this.editDate = editDate;
    }

    public Date getEditDate() {
        return editDate;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public boolean isValid() {
        return getErrors().size() == 0;
    }

    public HashMap<String, String> getErrors() {
        // Map chứa thông tin lỗi
        HashMap<String, String> errors = new HashMap<>();
        if (name == null || name.length() == 0) {
            errors.put("name", "Enter product's Name.");
        } else if (name.length() <= 7) {
            errors.put("name", "Name must have more than 7 characters.");
        }
        if (description == null || description.length() == 0) {
            errors.put("description", "Enter product's Description.");
        }
        if (thumbnail == null || thumbnail.length() == 0) {
            errors.put("thumbnail", "Enter product's Thumbnail.");
        } else if (!ValidationUtil.checkUrl(thumbnail)) {
            errors.put("thumbnail", "URL is not right, please input again.");
        }
        if (price == 0) {
            errors.put("price", "Enter product's Price.");
        }
        return errors;
    }

}
