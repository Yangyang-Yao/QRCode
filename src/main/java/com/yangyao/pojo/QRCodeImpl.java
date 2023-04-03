package com.yangyao.pojo;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import javax.imageio.ImageIO;
import java.io.ByteArrayOutputStream;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;

public class QRCodeImpl implements QRCode {
    private int id;
    private String barcodeText;
    private String image;
    private Timestamp birth;

    public QRCodeImpl() {

    }

    public QRCodeImpl(String barcodeText) {
        this.setBarcodeText(barcodeText);
        this.setImage(this.generateImage());
        this.setBirth(new Timestamp(System.currentTimeMillis()));
    }

    public static String generateQRCodeImage(String barcodeText) {
        try {
            QRCodeWriter barcodeWriter = new QRCodeWriter();
            BitMatrix bitMatrix = barcodeWriter.encode(barcodeText, BarcodeFormat.QR_CODE, 200, 200);
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            ImageIO.write(MatrixToImageWriter.toBufferedImage(bitMatrix), "png", output);
            String imageAsBase64 = Base64.getEncoder().encodeToString(output.toByteArray());
            return imageAsBase64;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String getBarcodeText() {
        return barcodeText;
    }

    @Override
    public void setBarcodeText(String barcodeText) {
        this.barcodeText = barcodeText;
        this.birth = new Timestamp(System.currentTimeMillis());
        this.setImage(this.generateImage());
    }

    @Override
    public String getImage() {
        return image;
    }

    @Override
    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public String generateImage() {
        return generateQRCodeImage(this.getBarcodeText());
    }

    @Override
    public Timestamp getBirth() {
        return birth;
    }

    @Override
    public void setBirth(Timestamp birth) {
        this.birth = birth;
    }

    @Override
    public String getBirthAsString() {
        return birth.toLocalDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }
}
