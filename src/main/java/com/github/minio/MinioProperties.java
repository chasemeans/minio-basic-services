package com.github.minio;

import lombok.Data;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ToString
@ConfigurationProperties(prefix = "minio")
public class MinioProperties {

    private String url, accessKey, secretKey;

    private PoolProperties pool;

    private String endpointName, endpointEnabled;


    @Data
    @ToString
    public static class PoolProperties {
        private int maxTotal, maxIdle, minIdle;
        private long maxWaitMillis;
        private boolean blockWhenExhausted;
    }
}
