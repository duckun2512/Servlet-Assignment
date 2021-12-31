package com.example.testjava;

import com.example.testjava.annotation.Column;
import com.example.testjava.annotation.Entity;
import com.example.testjava.annotation.Id;
import com.example.testjava.util.ConnectionHelper;
import com.example.testjava.util.SQLConstant;
import org.reflections.Reflections;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Set;

public class MainApplication {
    public static void main(String[] args) {
        // Quét toàn bộ project xem những class nào được đánh dấu là @Table,
        Reflections reflections = new Reflections("com.example.testjava");
        // trả về một set tập hợp các class được đánh dấu.
        Set<Class<?>> annotated = reflections.getTypesAnnotatedWith(Entity.class);
        for (Class<?> clazz :
                annotated) {
            // thực hiện migrate cho class đó.
            doMigrate(clazz);
        }
    }

    static void doMigrate(Class clazz) {
        StringBuilder stringBuilder = new StringBuilder();
        // lấy tên class
        System.out.println("Migrating class " + clazz.getName());
        if (!clazz.isAnnotationPresent(Entity.class)) {
            System.err.println("Class không được đánh dấu là table trong database. Bỏ qua.");
            return;
        }
        // chắc chắc class đã được đánh dấu annotation là @Table
        // @Table
        // lấy thông tin annotation ra.
        String tableName = clazz.getSimpleName().toLowerCase() + "s";
        Entity annotationTable = (Entity) clazz.getAnnotation(Entity.class);
        String annotationTableName = annotationTable.tableName();
        if (annotationTableName != null && annotationTableName.length() > 0) {
            tableName = annotationTableName;
        }
        stringBuilder.append(SQLConstant.CREATE_TABLE);
        stringBuilder.append(SQLConstant.SPACE);
        stringBuilder.append(tableName);
        stringBuilder.append(SQLConstant.SPACE);
        stringBuilder.append(SQLConstant.OPEN_PARENTHESES);

        // trả về danh sách các thuộc tính.
        Field[] fields = clazz.getDeclaredFields();

        for (int i = 0; i < fields.length; i++) {
            String fieldName = fields[i].getName(); // tên trường
            String fieldType = fields[i].getType().getName(); // kiểu giá trị của trường.
            if (fields[i].isAnnotationPresent(Column.class)) {
                Column annotationColumn = fields[i].getAnnotation(Column.class);
                if (annotationColumn.columnName().length() > 0) {
                    fieldName = annotationColumn.columnName();
                }
                if (annotationColumn.columnType().length() > 0) {
                    fieldType = annotationColumn.columnType();
                }
            }
            stringBuilder.append(fieldName);
            stringBuilder.append(SQLConstant.SPACE);
            stringBuilder.append(fieldType);
            // Check xem trường có phải là khoá chính hay không?
            if (fields[i].isAnnotationPresent(Id.class)) {
                stringBuilder.append(SQLConstant.SPACE);
                stringBuilder.append(SQLConstant.PRIMARY_KEY);
                Id annotationId = fields[i].getAnnotation(Id.class); // lấy ra thông tin annotation
                // để check thuộc tính auto_increment.
                if (annotationId.autoIncrement()) {
                    stringBuilder.append(SQLConstant.SPACE);
                    stringBuilder.append(SQLConstant.AUTO_INCREMENT);
                }
            }
            stringBuilder.append(SQLConstant.COMMA);
        }
        stringBuilder.setLength(stringBuilder.length() - 1);
        stringBuilder.append(SQLConstant.CLOSE_PARENTHESES);

        Connection cnn = null;
        try {
            cnn = ConnectionHelper.getConnection();
            Statement stt = cnn.createStatement();
            try {
                System.out.println("Try to drop table '" + tableName + "' before recreate.");
                stt.execute(SQLConstant.DROP_TABLE + SQLConstant.SPACE + tableName);
                System.out.println("Drop table '" + tableName + "' success!");
            } catch (Exception ex) {
                System.err.println("Drop table fails, error: " + ex.getMessage());
            }
            System.out.println("Try to execute statement: '" + stringBuilder.toString() + "'");
            stt.execute(stringBuilder.toString());
            System.out.println("Create table success!");
        } catch (SQLException e) {
            System.err.println("Create table fails, error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}