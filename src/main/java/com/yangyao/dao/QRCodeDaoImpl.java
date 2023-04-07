package com.yangyao.dao;

import com.yangyao.pojo.QRCode;
import com.yangyao.pojo.QRCodeImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.*;

@Repository
@EnableAutoConfiguration
public class QRCodeDaoImpl implements QRCodeDao {
    //@Autowired
    //DataSource dataSource;
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
    private static String FIND_ALL = "SELECT * FROM " + tableName + " ORDER BY ";
    private static String FIND_BY_ID = "SELECT * FROM " + tableName + " WHERE id=?";
    private static String FIND_BY_NAME = "SELECT * FROM " + tableName + " WHERE name=?";
    private static String INSERT = "INSERT INTO " + tableName + " (id, barcodetext, image, birth) VALUES(?, ?, ?, ?)";
    private static String UPDATE = "UPDATE " + tableName + " SET barcodetext=?, image=?, birth=? WHERE id=?";
    private static String SEARCH = "SELECT * FROM " + tableName + " WHERE barcodetext LIKE ? ORDER BY ";

    public List<QRCode> getAll(String sortField, String sortDirection) {
        connection = null;
        preparedStatement = null;
        try {
            connection = DriverManager.getConnection(url, username, password);
            preparedStatement = connection.prepareStatement(FIND_ALL + sortField + " " + sortDirection);
            //preparedStatement.setString(1, sortField);
            //preparedStatement.setString(2, sortDirection);
            ResultSet resultSet = preparedStatement.executeQuery();
            List<QRCode> codeList = new ArrayList<>();
            while (resultSet.next()) {
                QRCode qrcode = new QRCodeImpl();
                qrcode.setId(resultSet.getInt("id"));
                qrcode.setBarcodeText(resultSet.getString("barcodetext"));
                qrcode.setImage(resultSet.getString("image"));
                qrcode.setBirth(resultSet.getTimestamp("birth"));
                codeList.add(qrcode);
            }
            preparedStatement.close();
            connection.close();
            return codeList;
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

    public void addQRCode(String barcodeText) {
        connection = null;
        preparedStatement = null;
        try {
            connection = DriverManager.getConnection(url, username, password);
            preparedStatement = connection.prepareStatement(INSERT);
            QRCode qrcode = new QRCodeImpl(barcodeText);
            preparedStatement.setInt(1, nextId++);
            preparedStatement.setString(2, barcodeText);
            preparedStatement.setString(3, qrcode.getImage());
            preparedStatement.setTimestamp(4, qrcode.getBirth());
            preparedStatement.executeUpdate();
            preparedStatement.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
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
        if (barcodeText.equals(this.getQRCode(id).getBarcodeText())) {
            return;
        }
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

    public List<QRCode> searchQRCode(String pattern, String sortField, String sortDirection, String fullMatch) {
        connection = null;
        preparedStatement = null;
        try {
            connection = DriverManager.getConnection(url, username, password);
            preparedStatement = connection.prepareStatement(SEARCH + sortField + " " + sortDirection);
            if (fullMatch != null && fullMatch.equals("on")) {
                preparedStatement.setString(1, pattern);
            } else {
                preparedStatement.setString(1, "%" + pattern + "%");
            }
            ResultSet resultSet = preparedStatement.executeQuery();
            List<QRCode> codeList = new ArrayList<>();
            while (resultSet.next()) {
                QRCode qrcode = new QRCodeImpl();
                qrcode.setId(resultSet.getInt("id"));
                qrcode.setBarcodeText(resultSet.getString("barcodetext"));
                qrcode.setImage(resultSet.getString("image"));
                qrcode.setBirth(resultSet.getTimestamp("birth"));
                codeList.add(qrcode);
            }
            preparedStatement.close();
            connection.close();
            return codeList;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
