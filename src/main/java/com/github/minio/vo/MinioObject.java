package com.github.minio.vo;

import io.minio.ObjectStat;
import lombok.Data;

import java.util.Date;

@Data
public class MinioObject {

    private String bucketName;
    private String name;
    private Date createdTime;
    private long length;
    private String etag;
    private String contentType;

    public MinioObject(String bucketName, String name, Date createdTime, long length, String etag, String contentType) {
        this.bucketName = bucketName;
        this.name = name;
        this.createdTime = createdTime;
        this.length = length;
        this.etag = etag;
        this.contentType = contentType;
    }

    public MinioObject(ObjectStat os) {
        this.bucketName = os.bucketName();
        this.name = os.name();
        this.createdTime = os.createdTime();
        this.length = os.length();
        this.etag = os.etag();
        this.contentType = os.contentType();
    }
}