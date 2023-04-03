package com.yangyao.dao;

import com.yangyao.pojo.QRCode;
import com.yangyao.pojo.QRCodeImpl;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
@Repository
public class QRCodeDaoImpl implements QRCodeDao {
    private static int nextId = 0;
    private static String dbUrl = "jdbc:mysql://localhost:3306";
    private static String username = "root";
    private static String password = "yao123456";
    private static String dbName = "qrcodegenerator";
    private static String tableName = "qrcode";
    private static String url = "jdbc:mysql://localhost:3306/" + dbName + "?useUnicode=true&characterEncoding=utf8";
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
                    "barcodetext VARCHAR(100), " +
                    "image VARCHAR(1000), " +
                    "birth TIMESTAMP)";
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

    private static String DELETE = "DELETE FROM " + tableName + " WHERE id=?";
    private static String FIND_ALL = "SELECT * FROM " + tableName + " ORDER BY id DESC";
    private static String FIND_BY_ID = "SELECT * FROM " + tableName + " WHERE id=?";
    private static String FIND_BY_NAME = "SELECT * FROM " + tableName + " WHERE name=?";
    private static String INSERT = "INSERT INTO " + tableName + " (id, barcodetext, image, birth) VALUES(?, ?, ?, ?)";
    private static String UPDATE = "UPDATE " + tableName + " SET barcodetext=?, image=?, birth=? WHERE id=?";
    private static String SEARCH = "SELECT * FROM " + tableName + " WHERE barcodetext LIKE ?";

    public Collection<QRCode> getAll() {
        connection = null;
        preparedStatement = null;
        try {
            connection = DriverManager.getConnection(url, username, password);
            preparedStatement = connection.prepareStatement(FIND_ALL);
            ResultSet resultSet = preparedStatement.executeQuery();
            Collection<QRCode> set = new HashSet();
            while (resultSet.next()) {
                QRCode qrcode = new QRCodeImpl();
                qrcode.setId(resultSet.getInt("id"));
                qrcode.setBarcodeText(resultSet.getString("barcodetext"));
                qrcode.setImage(resultSet.getString("image"));
                qrcode.setBirth(resultSet.getTimestamp("birth"));
                set.add(qrcode);
            }
            preparedStatement.close();
            connection.close();
            return set;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public QRCode getQRCode(Integer id) {
        connection = null;
        preparedStatement = null;
        try {
            connection = DriverManager.getConnection(url, username, password);
            preparedStatement = connection.prepareStatement(FIND_BY_ID);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            QRCode qrcode = new QRCodeImpl();
            if (resultSet.next()) {
                qrcode.setId(resultSet.getInt("id"));
                qrcode.setBarcodeText(resultSet.getString("barcodetext"));
                qrcode.setImage(resultSet.getString("image"));
                qrcode.setBirth(resultSet.getTimestamp("birth"));
            }
            preparedStatement.close();
            connection.close();
            return qrcode;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void addQRCode(QRCode qrcode) {
        connection = null;
        preparedStatement = null;
        try {
            connection = DriverManager.getConnection(url, username, password);
            preparedStatement = connection.prepareStatement(INSERT);
            preparedStatement.setInt(1, nextId++);
            preparedStatement.setString(2, qrcode.getBarcodeText());
            preparedStatement.setString(3, qrcode.getImage());
            preparedStatement.setTimestamp(4, qrcode.getBirth());
            preparedStatement.executeUpdate();
            preparedStatement.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateQRCode(Integer id, String barcodeText) {
        connection = null;
        preparedStatement = null;
        try {
            connection = DriverManager.getConnection(url, username, password);
            preparedStatement = connection.prepareStatement(UPDATE);
            preparedStatement.setString(1, barcodeText);
            preparedStatement.setString(2, QRCodeImpl.generateQRCodeImage(barcodeText));
            preparedStatement.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
            preparedStatement.setInt(4, id);
            preparedStatement.executeUpdate();
            preparedStatement.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteQRCode(Integer id) {
        connection = null;
        preparedStatement = null;
        try {
            connection = DriverManager.getConnection(url, username, password);
            preparedStatement = connection.prepareStatement(DELETE);
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
            preparedStatement.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Collection<QRCode> searchQRCode(String pattern) {
        connection = null;
        preparedStatement = null;
        try {
            connection = DriverManager.getConnection(url, username, password);
            preparedStatement = connection.prepareStatement(SEARCH);
            preparedStatement.setString(1, "%" + pattern + "%");
            ResultSet resultSet = preparedStatement.executeQuery();
            Collection<QRCode> set = new HashSet();
            while (resultSet.next()) {
                QRCode qrcode = new QRCodeImpl();
                qrcode.setId(resultSet.getInt("id"));
                qrcode.setBarcodeText(resultSet.getString("barcodetext"));
                qrcode.setImage(resultSet.getString("image"));
                qrcode.setBirth(resultSet.getTimestamp("birth"));
                set.add(qrcode);
            }
            preparedStatement.close();
            connection.close();
            return set;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
