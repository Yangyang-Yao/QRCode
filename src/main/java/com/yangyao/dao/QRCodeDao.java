package com.yangyao.dao;

import com.yangyao.pojo.QRCode;

import java.util.List;

public interface QRCodeDao {
    List<QRCode> getAll(int userId, String sortField, String sortDirection);
    QRCode getQRCode(Integer id);
    void addQRCode(int userId, String barcodeText);
    void addQRCode(int userId, QRCode qrcode);
    void updateQRCode(Integer id, String barcodeText);
    void deleteQRCode(Integer id);
    List<QRCode> searchQRCode(int userId, String pattern, String sortField, String sortDirection, String fullMatch);
}
