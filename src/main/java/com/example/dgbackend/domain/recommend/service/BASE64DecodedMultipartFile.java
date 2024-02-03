package com.example.dgbackend.domain.recommend.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

/*
 * BASE64DecodedMultipartFile.java
 * BASE64로 인코딩된 MultipartFile을 디코딩하여 사용하기 위한 클래스
 * S3에 이미지를 업로드하기 위해 사용
 */
public class BASE64DecodedMultipartFile implements MultipartFile {
    private final byte[] imgContent;            // 이미지를 담을 바이트 배열(Based64로 인코딩된 이미지)
    private final String contentType;           // 이미지 타입 (예: "image/jpeg")
    private final String fileName;              // 파일명


    public BASE64DecodedMultipartFile(byte[] imgContent, String contentType, String fileName) {
        this.imgContent = imgContent;
        this.contentType = contentType;
        this.fileName = fileName;
    }

    @Override
    public String getName() {
        // 반환할 파일명
        return this.fileName;
    }

    @Override
    public String getOriginalFilename() {
        // 원본 파일명
        return this.fileName;
    }

    @Override
    public String getContentType() {
        // 파일 타입, 예: "image/jpeg"
        return this.contentType;
    }

    @Override
    public boolean isEmpty() {
        return imgContent == null || imgContent.length == 0;
    }

    @Override
    public long getSize() {
        return imgContent.length;
    }

    @Override
    public byte[] getBytes() throws IOException {
        return imgContent;
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return new ByteArrayInputStream(imgContent);
    }

    @Override
    public void transferTo(java.io.File dest) throws IOException, IllegalStateException {
        throw new UnsupportedOperationException("Transfer to file is not supported");
    }
}
