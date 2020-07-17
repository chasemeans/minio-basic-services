package com.github.minio.pool;

import io.minio.MinioClient;
import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;

public class MinioClientPoolFactory extends BasePooledObjectFactory<MinioClient> {

    private String endpoint, accessKey, secretKey;

    public MinioClientPoolFactory(String endpoint, String accessKey, String secretKey) {
        this.endpoint = endpoint;
        this.accessKey = accessKey;
        this.secretKey = secretKey;
    }

    @Override
    public MinioClient create() throws Exception {
        return new MinioClient(this.endpoint, this.accessKey, this.secretKey);
    }

    @Override
    public PooledObject<MinioClient> wrap(MinioClient minioClient) {
        return new DefaultPooledObject<>(minioClient);
    }
}
