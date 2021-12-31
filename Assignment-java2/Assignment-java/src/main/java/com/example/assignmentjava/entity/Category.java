package com.example.assignmentjava.entity;

import com.example.assignmentjava.annotation.Column;
import com.example.assignmentjava.annotation.Entity;
import com.example.assignmentjava.annotation.Id;
import com.example.assignmentjava.util.SQLDataTypes;

import java.util.Date;

@Entity(tableName = "categories")
public class Category {
    @Id(autoIncrement = true)
    @Column(columnName = "id", columnType = SQLDataTypes.INTEGER)
    private int id;
    @Column(columnName = "name", columnType = SQLDataTypes.VARCHAR50)
    private String name;
    @Column(columnName = "is_deleted", columnType = SQLDataTypes.INTEGER)
    private int isDeleted;
    @Column(columnName = "edit_date", columnType = SQLDataTypes.DATETIME)
    private Date editDate;

    public Category() {
        this.name = "";
        this.isDeleted = 0;
        this.editDate = new Date();
    }

    public Category(int id, String name) {
        this.id = id;
        this.name = name;
        this.isDeleted = 0;
        this.editDate = new Date();
    }

    @Override
    public String toString() {
        return "Category{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", isDeleted=" + isDeleted +
                ", editDate=" + editDate +
                '}';
    }

    public int getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(int isDeleted) {
        this.isDeleted = isDeleted;
    }

    public Category(String name) {
        this.name = name;
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
}