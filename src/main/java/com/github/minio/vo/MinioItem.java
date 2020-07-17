package com.github.minio.vo;

import io.minio.messages.Item;
import io.minio.messages.Owner;
import lombok.Data;

import java.util.Date;

@Data
public class MinioItem {

    private String objectName;
    private Date lastModified;
    private String etag;
    private long size;
    private String storageClass;
    private Owner owner;
    private String type;

    public MinioItem(String objectName, Date lastModified, String etag, long size, String storageClass, Owner owner, String type) {
        this.objectName = objectName;
        this.lastModified = lastModified;
        this.etag = etag;
        this.size = size;
        this.storageClass = storageClass;
        this.owner = owner;
        this.type = type;
    }


    public MinioItem(Item item) {
        this.objectName = item.objectName();
        this.lastModified = item.lastModified();
        this.etag = item.etag();
        this.size = item.size();
        this.storageClass = item.storageClass();
        this.owner = item.owner();
        this.type = item.isDir() ? "directory" : "file";
    }
}