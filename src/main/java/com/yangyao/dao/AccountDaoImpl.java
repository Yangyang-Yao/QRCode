package com.yangyao.dao;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Repository
@EnableAutoConfiguration
public class AccountDaoImpl implements AccountDao {
    private static int nextId = 0;
    private final static String dbUrl = "jdbc:mysql://localhost:3306";
    private final static String username = "root";
    private final static String password = "yao123456";
    private final static String dbName = "qrcodegenerator";
    private final static String tableName = "account";
    private final static String url = "jdbc:mysql://localhost:3306/" + dbName + "?useUnicode=true&characterEncoding=utf8";
    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection(dbUrl, username, password);
            Statement statement = connection.createStatement();
            String query = "CREATE DATABASE IF NOT EXISTS " + dbName + " CHARACTER SET utf8 COLLATE utf8_general_ci";
            statement.executeUpdate(query);
            connection = DriverManager.getConnection(url, username, password);
            statement = connection.createStatement();
            query = "CREATE TABLE IF NOT EXISTS " +
                    tableName +
                    "(id INT PRIMARY KEY, " +
                    "username VARCHAR(100), " +
                    "password VARCHAR(100))";
            statement.executeUpdate(query);
            query = "SELECT MAX(id) AS nextid FROM " + tableName;
            ResultSet resultSet = statement.executeQuery(query);
            if (resultSet.next()) {
                nextId = resultSet.getInt("nextid") + 1;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private Connection connection;

    private PreparedStatement preparedStatement;

    private final static String INSERT = "INSERT INTO " + tableName + " (id, username, password) VALUES(?, ?, ?)";

    private final static String DUPLICATE = "SELECT * FROM " + tableName + " WHERE username=?";

    private final static String LOGIN = "SELECT * FROM " + tableName + " WHERE username=? AND password=?";

    private final static String VALID = "^[A-Za-z0-9][A-Za-z0-9.@_-]{1,60}$";

    private final static Pattern pattern = Pattern.compile(VALID);

    public void addAccount(String username, String password) {
        connection = null;
        preparedStatement = null;
        try {
            connection = DriverManager.getConnection(url, AccountDaoImpl.username, AccountDaoImpl.password);
            preparedStatement = connection.prepareStatement(INSERT);
            preparedStatement.setInt(1, nextId++);
            preparedStatement.setString(2, username);
            preparedStatement.setString(3, password);
            preparedStatement.executeUpdate();
            preparedStatement.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean checkDuplicate(String username) {
        connection = null;
        preparedStatement = null;
        try {
            connection = DriverManager.getConnection(url, AccountDaoImpl.username, AccountDaoImpl.password);
            preparedStatement = connection.prepareStatement(DUPLICATE);
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                preparedStatement.close();
                connection.close();
                return true;
            } else {
                preparedStatement.close();
                connection.close();
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return true;
        }
    }

    public int checkLogin(String username, String password) {
        connection = null;
        preparedStatement = null;
        try {
            connection = DriverManager.getConnection(url, AccountDaoImpl.username, AccountDaoImpl.password);
            preparedStatement = connection.prepareStatement(LOGIN);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                int id = resultSet.getInt("id");
                preparedStatement.close();
                connection.close();
                return id;
            } else {
                preparedStatement.close();
                connection.close();
                return -1;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public boolean validUsername(String username) {
        Matcher matcher = pattern.matcher(username);
        return matcher.matches();
    }
}
