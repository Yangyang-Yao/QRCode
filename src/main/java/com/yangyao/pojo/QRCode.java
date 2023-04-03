package com.yangyao.pojo;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import javax.imageio.ImageIO;
import java.io.ByteArrayOutputStream;
import java.sql.Timestamp;
import java.util.Base64;

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
