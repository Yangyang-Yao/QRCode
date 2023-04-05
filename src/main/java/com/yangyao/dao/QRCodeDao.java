package com.yangyao.dao;

import com.yangyao.pojo.QRCode;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface QRCodeDao {
    List<QRCode> getAll(String sortField, String sortDirection);
    QRCode getQRCode(Integer id);
    void addQRCode(String barcodeText);
    void addQRCode(QRCode qrcode);
    void updateQRCode(Integer id, String barcodeText);
    void deleteQRCode(Integer id);
    List<QRCode> searchQRCode(String pattern, String sortField, String sortDirection);
}
