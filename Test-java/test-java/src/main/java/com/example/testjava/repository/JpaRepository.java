package com.example.testjava.repository;

import com.example.testjava.annotation.Column;
import com.example.testjava.annotation.Entity;
import com.example.testjava.annotation.Id;
import com.example.testjava.entity.Phone;
import com.example.testjava.exception.EntityException;
import com.example.testjava.util.ConnectionHelper;
import com.example.testjava.util.ConvertHelper;
import com.example.testjava.util.SQLConstant;
import com.example.testjava.util.SQLDataTypes;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class JpaRepository<T> {

    // Thông tin getClass của repository.
    private Class<T> clazz;

    // contructor nhận tham số đầu vào là một kiểu class, thông tin này
    // được lưu lại để dùng cho tất cả các function bên dưới.
    public JpaRepository(Class<T> clazz) {
        this.clazz = clazz;
    }

    private boolean isEntity() {
        return clazz.isAnnotationPresent(Entity.class);
    }

    public List<T> findAll() {
        //select * from table name
        //build sql command
        List<T> res = new ArrayList<T>();
        try {
            Connection connection = ConnectionHelper.getConnection();
            if (connection == null) {
                throw new EntityException("Can not connect to datababse!");
            }
            if (!isEntity()) {
                throw new EntityException("Not an entity class");
            }
            String tableName = clazz.getAnnotation(Entity.class).tableName();
            StringBuilder stringCmd = new StringBuilder();
            stringCmd.append(SQLConstant.SELECT_ASTERISK);
            stringCmd.append(SQLConstant.SPACE);
            stringCmd.append(SQLConstant.FROM);
            stringCmd.append(SQLConstant.SPACE);
            stringCmd.append(tableName);
            //execute command
            PreparedStatement preparedStatement = connection.prepareStatement(stringCmd.toString());
            ResultSet resultSet = preparedStatement.executeQuery();
            List<T> fullFiled = fullFillObject(resultSet);
            if (fullFiled.size() == 0) {
                throw new EntityException("Not Found");
            }
            res = fullFiled;
        } catch (EntityException | InstantiationException | IllegalAccessException | SQLException error) {
            System.err.printf("Find all error %s\n", error.getMessage());
            error.printStackTrace();
        }
        return res;
    }

    public T findById(Object id) {
        try {
            Connection connection = ConnectionHelper.getConnection();
            if (!isEntity()) {
                throw new EntityException("Not an entity model check your annotation");
            }
            //select * from teachers where id = {id}
            //build sql cmd
            StringBuilder strCmd = new StringBuilder();
            strCmd.append(SQLConstant.SELECT_ASTERISK);
            strCmd.append(SQLConstant.SPACE);
            strCmd.append(SQLConstant.FROM);
            strCmd.append(SQLConstant.SPACE);
            //get table name
            Entity currentEntity = clazz.getDeclaredAnnotation(Entity.class);
            String tableName = currentEntity.tableName();
            strCmd.append(tableName);
            strCmd.append(SQLConstant.SPACE);
            strCmd.append(SQLConstant.WHERE);
            strCmd.append(SQLConstant.SPACE);
            //get id column name
            for (Field field : clazz.getDeclaredFields()) {
                //skip non column
                if (!field.isAnnotationPresent(Column.class)) {
                    continue;
                }
                if (field.isAnnotationPresent(Id.class)) {
                    Column column = field.getAnnotation(Column.class);
                    strCmd.append(column.columnName());
                    strCmd.append(SQLConstant.SPACE);
                    strCmd.append(SQLConstant.EQUAL);
                    strCmd.append(SQLConstant.SPACE);
                    //append value depends on value type
                    if (!column.columnType().equals(SQLDataTypes.INTEGER)) {
                        strCmd.append(SQLConstant.APOSTROPHE);
                    }
                    strCmd.append(id);
                    if (!column.columnType().equals(SQLDataTypes.INTEGER)) {
                        strCmd.append(SQLConstant.APOSTROPHE);
                    }
                    break;
                }
            }
            PreparedStatement preparedStatement = connection.prepareStatement(strCmd.toString());
            ResultSet resultSet = preparedStatement.executeQuery();

            List<T> fullFiled = fullFillObject(resultSet);
            if (fullFiled.size() == 0) {
                throw new EntityException("Not Found");
            }
            return fullFiled.get(0);
        } catch (EntityException | SQLException | InstantiationException | IllegalAccessException error) {
            System.out.printf("Find by Id model error: %s \n", error.getMessage());
        }
        return null;
    }

    public boolean save(T obj) {
        try {
            if (!isEntity()) {
                // chủ động quăng lỗi cho hàm gọi đến.
                throw new EntityException("Not an entity model check your annotation");
            }
            Connection connection = ConnectionHelper.getConnection();
            if (connection == null) {
                throw new EntityException("Can not connect to database!");
            }
            Entity currentEntity = (Entity) clazz.getAnnotation(Entity.class);
            //build sql cmd
            StringBuilder stringCmd = new StringBuilder();
            stringCmd.append(SQLConstant.INSERT_INTO);
            stringCmd.append(SQLConstant.SPACE);
            stringCmd.append(currentEntity.tableName());
            stringCmd.append(SQLConstant.SPACE);
            stringCmd.append(SQLConstant.OPEN_PARENTHESES);
            Field[] fields = clazz.getDeclaredFields();

            for (int i = 0; i < fields.length; i++) {
                Field field = fields[i];
                String columnName = field.getName().toLowerCase();
                if (field.isAnnotationPresent(Column.class)) {
                    Column currentColumn = field.getAnnotation(Column.class);
                    if (currentColumn.columnName().length() > 0) {
                        columnName = currentColumn.columnName();
                    }
                }
                //id checker
                if (field.isAnnotationPresent(Id.class)) {
                    Id currentId = (Id) field.getAnnotation(Id.class);
                    if (currentId.autoIncrement()) {
                        continue;
                    }
                }
                stringCmd.append(columnName);
                stringCmd.append(SQLConstant.COMMA);
                stringCmd.append(SQLConstant.SPACE);

            }
            stringCmd.setLength(stringCmd.length() - 2);
            stringCmd.append(SQLConstant.CLOSE_PARENTHESES);
            stringCmd.append(SQLConstant.SPACE);
            stringCmd.append(SQLConstant.VALUES);
            stringCmd.append(SQLConstant.SPACE);
            stringCmd.append(SQLConstant.OPEN_PARENTHESES);
            for (int i = 0; i < fields.length; i++) {
                Field field = fields[i];
                if (!field.isAnnotationPresent(Column.class)) {
                    continue;
                }
                Column currentColumn = field.getAnnotation(Column.class);
                String columnType = currentColumn.columnType();
                field.setAccessible(true);
                Object value = field.get(obj);
                if (columnType.equals(SQLDataTypes.DATE)) {
                    Date date = (Date) value;
                    value = ConvertHelper.convertJavaDateToSqlDate(date);
                }
                if (columnType.equals(SQLDataTypes.DATETIME) || columnType.equals(SQLDataTypes.TIME_STAMP)) {
                    Date date = (Date) value;
                    value = ConvertHelper.convertJavaDateToSqlDateTime(date);
                }
                //id checker
                if (field.isAnnotationPresent(Id.class)) {
                    Id currentId = (Id) field.getAnnotation(Id.class);
                    if (currentId.autoIncrement()) {
                        continue;
                    }
                }
                if (value == null) {
                    //append null
                    stringCmd.append(SQLConstant.NULL);
                    stringCmd.append(SQLConstant.COMMA);
                    stringCmd.append(SQLConstant.SPACE);
                    continue;
                }
                if (SQLDataTypes.needApostrophe(columnType)) {
                    stringCmd.append(SQLConstant.APOSTROPHE);
                }
                stringCmd.append(value);
                if (SQLDataTypes.needApostrophe(columnType)) {
                    stringCmd.append(SQLConstant.APOSTROPHE);
                }
                stringCmd.append(SQLConstant.COMMA);
                stringCmd.append(SQLConstant.SPACE);

            }
            stringCmd.setLength(stringCmd.length() - 2);
            stringCmd.append(SQLConstant.CLOSE_PARENTHESES);
            connection.createStatement().execute(stringCmd.toString());
            return true;
        } catch (IllegalAccessException | EntityException | SQLException e) {
            System.err.printf("Save Model Error: %s.\n", e.getMessage());
        }
        return false;
    }

    public boolean update(T obj) {
        //update {table_name} SET column1 = value 1, column2 = value 2 where id = {id}
        //not allow to update id
        try {
            if (!isEntity()) {
                throw new EntityException("Not an entity model check your annotation");
            }
            Connection connection = ConnectionHelper.getConnection();
            if (connection == null) {
                throw new EntityException("Can not connect to db");
            }
            String tableName = clazz.getAnnotation(Entity.class).tableName();
            StringBuilder stringCmd = new StringBuilder();
            stringCmd.append(SQLConstant.UPDATE);
            stringCmd.append(SQLConstant.SPACE);
            stringCmd.append(tableName);
            stringCmd.append(SQLConstant.SPACE);
            stringCmd.append(SQLConstant.SET);
            stringCmd.append(SQLConstant.SPACE);
            Field[] fields = clazz.getDeclaredFields();
            //id information
            String idName = "";
            String idValue = "";
            String idType = "";
            for (Field field : fields) {
                if (!field.isAnnotationPresent(Column.class)) {
                    continue;
                }

                field.setAccessible(true);
                Column columnInformation = field.getDeclaredAnnotation(Column.class);
                String columnName = columnInformation.columnName();
                String columnType = columnInformation.columnType();
                Object value = field.get(obj);
                if (columnType.equals(SQLDataTypes.DATE)) {
                    Date date = (Date) value;
                    value = ConvertHelper.convertJavaDateToSqlDate(date);
                }
                if (columnType.equals(SQLDataTypes.DATETIME) || columnType.equals(SQLDataTypes.TIME_STAMP)) {
                    Date date = (Date) value;
                    value = ConvertHelper.convertJavaDateToSqlDateTime(date);
                }
                if (field.isAnnotationPresent(Id.class)) {
                    //dont update id
                    //but get id information
                    idName = columnName;
                    idValue = value.toString();
                    idType = columnType;
                    continue;
                }
                stringCmd.append(columnName);
                stringCmd.append(SQLConstant.SPACE);
                stringCmd.append(SQLConstant.EQUAL);
                stringCmd.append(SQLConstant.SPACE);
                if (value == null) {
                    stringCmd.append(SQLConstant.NULL);
                    stringCmd.append(SQLConstant.COMMA);
                    stringCmd.append(SQLConstant.SPACE);
                    continue;
                }
                if (!columnType.equals(SQLDataTypes.INTEGER)) {
                    stringCmd.append(SQLConstant.APOSTROPHE);
                }
                stringCmd.append(value);
                if (!columnType.equals(SQLDataTypes.INTEGER)) {
                    stringCmd.append(SQLConstant.APOSTROPHE);
                }
                stringCmd.append(SQLConstant.COMMA);
                stringCmd.append(SQLConstant.SPACE);
            }
            stringCmd.setLength(stringCmd.length() - 2);
            stringCmd.append(SQLConstant.SPACE);
            stringCmd.append(SQLConstant.WHERE);
            stringCmd.append(SQLConstant.SPACE);
            stringCmd.append(idName);
            stringCmd.append(SQLConstant.SPACE);
            stringCmd.append(SQLConstant.EQUAL);
            stringCmd.append(SQLConstant.SPACE);
            if (!idType.equals(SQLDataTypes.INTEGER)) {
                stringCmd.append(SQLConstant.APOSTROPHE);
            }
            stringCmd.append(idValue);
            if (!idType.equals(SQLDataTypes.INTEGER)) {
                stringCmd.append(SQLConstant.APOSTROPHE);
            }
            connection.createStatement().execute(stringCmd.toString());
            return true;
        } catch (EntityException | IllegalAccessException | SQLException error) {
            System.out.printf("Update  failed error: %s \n", error.getMessage());
        }
        return false;
    }

    public boolean delete(Object id) {
        //delete from {tableName} where id = id
        try {
            Connection connection = ConnectionHelper.getConnection();
            if (connection == null) {
                throw new EntityException("Can not connect to db");
            }
            if (!isEntity()) {
                throw new EntityException("Not an entity model check your annotation");
            }
            String tableName = clazz.getAnnotation(Entity.class).tableName();
            StringBuilder stringCmd = new StringBuilder();
            stringCmd.append(SQLConstant.DELETE);
            stringCmd.append(SQLConstant.SPACE);
            stringCmd.append(SQLConstant.FROM);
            stringCmd.append(SQLConstant.SPACE);
            stringCmd.append(tableName);
            stringCmd.append(SQLConstant.SPACE);
            stringCmd.append(SQLConstant.WHERE);
            //id information
            String idName = "";
            String idType = "";
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                if (!field.isAnnotationPresent(Column.class)) {
                    continue;
                }

                field.setAccessible(true);
                Column columnInformation = field.getDeclaredAnnotation(Column.class);
                String columnName = columnInformation.columnName();
                String columnType = columnInformation.columnType();
                if (field.isAnnotationPresent(Id.class)) {
                    //dont update id
                    //but get id information
                    idName = columnName;
                    idType = columnType;
                    break;
                }
            }
            stringCmd.append(SQLConstant.SPACE);
            stringCmd.append(idName);
            stringCmd.append(SQLConstant.SPACE);
            stringCmd.append(SQLConstant.EQUAL);
            stringCmd.append(SQLConstant.SPACE);
            if (!idType.equals(SQLDataTypes.INTEGER)) {
                stringCmd.append(SQLConstant.APOSTROPHE);
            }
            stringCmd.append(id);
            if (!idType.equals(SQLDataTypes.INTEGER)) {
                stringCmd.append(SQLConstant.APOSTROPHE);
            }
            connection.createStatement().execute(stringCmd.toString());
            return true;
        } catch (EntityException | SQLException error) {
            System.out.printf("Delete failed  error: %s \n", error.getMessage());
        }
        return false;
    }

    public List<T> where(Object expr1, String operator, Object expr2) {
        //SELECT * FROM {tableName} WHERE {expr1} {operator} {expr2}
        //build sql command
        List<T> res = new ArrayList<T>();
        try {
            if (!isEntity()) {
                throw new EntityException("Not an entity class");
            }
            Connection connection = ConnectionHelper.getConnection();
            if (connection == null) {
                throw new EntityException("Can not connect to db");
            }
            String tableName = clazz.getAnnotation(Entity.class).tableName();
            StringBuilder stringCmd = new StringBuilder();
            stringCmd.append(SQLConstant.SELECT_ASTERISK);
            stringCmd.append(SQLConstant.SPACE);
            stringCmd.append(SQLConstant.FROM);
            stringCmd.append(SQLConstant.SPACE);
            stringCmd.append(tableName);
            stringCmd.append(SQLConstant.SPACE);
            stringCmd.append(SQLConstant.WHERE);
            stringCmd.append(SQLConstant.SPACE);
            stringCmd.append(expr1);
            stringCmd.append(SQLConstant.SPACE);
            stringCmd.append(operator);
            stringCmd.append(SQLConstant.SPACE);
            stringCmd.append(SQLConstant.APOSTROPHE);
            stringCmd.append(expr2);
            stringCmd.append(SQLConstant.APOSTROPHE);
            //execute command
            PreparedStatement preparedStatement = connection.prepareStatement(stringCmd.toString());
            ResultSet resultSet = preparedStatement.executeQuery();
            List<T> fullFiled = fullFillObject(resultSet);
            if (fullFiled.size() == 0) {
                throw new EntityException("Not Found");
            }
            res = fullFiled;
        } catch (EntityException | InstantiationException | IllegalAccessException | SQLException error) {
            System.err.printf("where clause error %s\n", error.getMessage());
        }
        return res;
    }

    private List<T> fullFillObject(ResultSet resultSet) throws InstantiationException, IllegalAccessException, SQLException {
        ArrayList<T> listObj = new ArrayList<T>();
        while (resultSet.next()) {
            T obj = clazz.newInstance(); // new Product();
            for (Field field : clazz.getDeclaredFields()) {
                String columnName = field.getName().toLowerCase();
                if (field.isAnnotationPresent(Column.class)) {
                    Column columnInfor = field.getAnnotation(Column.class);
                    if (columnInfor.columnName().length() > 0) {
                        columnName = columnInfor.columnName();
                    }
                    field.setAccessible(true);
                    switch (columnInfor.columnType()) {
                        case SQLDataTypes.INTEGER:
                            field.set(obj, resultSet.getInt(columnName));
                            break;
                        case SQLDataTypes.VARCHAR255:
                        case SQLDataTypes.VARCHAR50:
                        case SQLDataTypes.TEXT:
                            field.set(obj, resultSet.getString(columnName));
                            break;
                        case SQLDataTypes.DOUBLE:
                            field.set(obj, resultSet.getDouble(columnName));
                            break;
                        case SQLDataTypes.DATE:
                            field.set(obj, ConvertHelper.convertSqlDateToJavaDate(resultSet.getDate(columnName)));
                            break;
                        case SQLDataTypes.DATETIME:
                        case SQLDataTypes.TIME_STAMP:
                            field.set(obj, ConvertHelper.convertSqlTimeStampToJavaDate(resultSet.getTimestamp(columnName)));
                            break;
                    }
                }
            }
            listObj.add(obj);
        }
        return listObj;
    }

    public int countData(){
        try {
            Connection connection = ConnectionHelper.getConnection();
            if (connection == null) {
                throw new EntityException("Can not connect to datababse!");
            }
            if (!isEntity()) {
                throw new EntityException("Not an entity class");
            }
            String tableName = clazz.getAnnotation(Entity.class).tableName();
            StringBuilder stringCmd = new StringBuilder();
            //tạo câu lệnh select count(*) as total from table
            stringCmd.append(SQLConstant.SELECT);
            stringCmd.append(SQLConstant.SPACE);
            stringCmd.append(SQLConstant.COUNT);
            stringCmd.append(SQLConstant.OPEN_PARENTHESES);
            stringCmd.append(SQLConstant.ASTERISK);
            stringCmd.append(SQLConstant.CLOSE_PARENTHESES);
            stringCmd.append(SQLConstant.SPACE);
            stringCmd.append(SQLConstant.AS);
            stringCmd.append(SQLConstant.SPACE);
            stringCmd.append("total");
            stringCmd.append(SQLConstant.SPACE);
            stringCmd.append(SQLConstant.FROM);
            stringCmd.append(SQLConstant.SPACE);
            stringCmd.append(tableName);
            //execute command
            System.out.println(stringCmd.toString());
            PreparedStatement preparedStatement = connection.prepareStatement(stringCmd.toString());
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            return resultSet.getInt("total");
        } catch (EntityException | SQLException error) {
            System.err.printf("Find all error %s\n", error.getMessage());
            error.printStackTrace();
        }
        return 0;
    }


}
