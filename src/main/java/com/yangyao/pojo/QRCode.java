package com.yangyao.pojo;

import java.sql.Timestamp;

public interface QRCode {
    int getId();

    void setId(int id);

    String getBarcodeText();

    void setBarcodeText(String barcodeText);

    String getImage();

    void setImage(String image);

    String generateImage();

    Timestamp getBirth();

    void setBirth(Timestamp birth);

    String getBirthAsString();
}
