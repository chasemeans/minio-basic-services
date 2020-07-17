package com.github.minio.service;

import com.github.minio.pool.MinioClientPool;
import com.github.minio.vo.MinioItem;
import io.minio.MinioClient;
import io.minio.ObjectStat;
import io.minio.Result;
import io.minio.messages.Bucket;
import io.minio.messages.Item;
import lombok.SneakyThrows;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


public class MinioOptimizTemplate {

    private MinioClientPool pool;

    public MinioOptimizTemplate(MinioClientPool pool) {
        this.pool = pool;
    }

    /**
     * Bucket Operations
     */
    public void createBucket(String bucketName) throws Exception {
        MinioClient client = pool.getMinioClientPool().borrowObject();
        try {
            if (!client.bucketExists(bucketName)) {
                client.makeBucket(bucketName);
            }
        } finally {
            if (null != client) {
                pool.getMinioClientPool().returnObject(client);
            }
        }
    }

    public List<Bucket> getAllBuckets() throws Exception {
        MinioClient client = pool.getMinioClientPool().borrowObject();
        try {
            return client.listBuckets();
        } finally {
            if (null != client) {
                pool.getMinioClientPool().returnObject(client);
            }
        }
    }

    public Optional<Bucket> getBucket(String bucketName) throws Exception {
        MinioClient client = pool.getMinioClientPool().borrowObject();
        try {
            return client.listBuckets().stream().filter(b -> b.name().equals(bucketName)).findFirst();
        } finally {
            if (null != client) {
                pool.getMinioClientPool().returnObject(client);
            }
        }
    }

    public void removeBucket(String bucketName) throws Exception {
        MinioClient client = pool.getMinioClientPool().borrowObject();
        try {
            client.removeBucket(bucketName);
        } finally {
            if (null != client) {
                pool.getMinioClientPool().returnObject(client);
            }
        }
    }

    public List<MinioItem> getAllObjectsByPrefix(String bucketName, String prefix, boolean recursive) throws Exception {
        MinioClient client = pool.getMinioClientPool().borrowObject();
        try {
            List<MinioItem> objectList = new ArrayList<MinioItem>();
            Iterable<Result<Item>> objectsIterator = client.listObjects(bucketName, prefix, recursive);
            objectsIterator.forEach(i -> {
                try {
                    objectList.add(new MinioItem(i.get()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            return objectList;
        } finally {
            if (null != client) {
                pool.getMinioClientPool().returnObject(client);
            }
        }
    }

    /**
     * Object operations
     */

    public String getObjectURL(String bucketName, String objectName, Integer expires) throws Exception {
        MinioClient client = pool.getMinioClientPool().borrowObject();
        try {
            return client.presignedGetObject(bucketName, objectName, expires);
        } finally {
            if (null != client) {
                pool.getMinioClientPool().returnObject(client);
            }
        }
    }

    public String getObjectURL(String bucketName, String objectName) throws Exception {
        MinioClient client = pool.getMinioClientPool().borrowObject();
        try {
            return client.presignedGetObject(bucketName, objectName);
        } finally {
            if (null != client) {
                pool.getMinioClientPool().returnObject(client);
            }
        }
    }

    public void putFile(String bucketName, String fileName, InputStream stream) throws Exception {
        MinioClient client = pool.getMinioClientPool().borrowObject();
        try {
            client.putObject(bucketName, fileName, stream, (long) stream.available(), null, null, "application/octet-stream");
        } finally {
            if (null != client) {
                pool.getMinioClientPool().returnObject(client);
            }
        }
    }

    public void putFile(String bucketName, String fileName, InputStream stream, String contentType) throws Exception {
        MinioClient client = pool.getMinioClientPool().borrowObject();
        try {
            client.putObject(bucketName, fileName, stream, (long) stream.available(), null, null, contentType);
        } finally {
            if (null != client) {
                pool.getMinioClientPool().returnObject(client);
            }
        }
    }

    public void putObject(String bucketName, String objectName, InputStream stream, long size, String contentType) throws Exception {
        MinioClient client = pool.getMinioClientPool().borrowObject();
        try {
            client.putObject(bucketName, objectName, stream, size, null, null, contentType);
        } finally {
            if (null != client) {
                pool.getMinioClientPool().returnObject(client);
            }
        }
    }

    /**
     * 获取文件
     *
     * @param bucketName bucket名称
     * @param objectName 文件名称
     * @return 二进制流
     */
    @SneakyThrows
    public InputStream getObject(String bucketName, String objectName) {
        MinioClient client = pool.getMinioClientPool().borrowObject();
        try {
            return client.getObject(bucketName, objectName);
        } finally {
            if (null != client) {
                pool.getMinioClientPool().returnObject(client);
            }
        }
    }

    public ObjectStat getObjectInfo(String bucketName, String objectName) throws Exception {
        MinioClient client = pool.getMinioClientPool().borrowObject();
        try {
            return client.statObject(bucketName, objectName);
        } finally {
            if (null != client) {
                pool.getMinioClientPool().returnObject(client);
            }
        }
    }

    public void removeObject(String bucketName, String objectName) throws Exception {
        MinioClient client = pool.getMinioClientPool().borrowObject();
        try {
            client.removeObject(bucketName, objectName);
        } finally {
            if (null != client) {
                pool.getMinioClientPool().returnObject(client);
            }
        }
    }

}
