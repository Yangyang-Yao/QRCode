package com.yangyao.dao;

import com.yangyao.pojo.QRCode;

import java.util.Collection;
import java.util.Map;

public interface QRCodeDao {
    Collection<QRCode> getAll();
    QRCode getQRCode(Integer id);
    void addQRCode(QRCode qrcode);
    void updateQRCode(Integer id, String barcodeText);
    void deleteQRCode(Integer id);
    public Collection<QRCode> searchQRCode(String pattern);
}
