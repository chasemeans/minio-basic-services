package com.github.minio;

import com.github.minio.pool.MinioClientPool;
import com.github.minio.service.MinioOptimizTemplate;
import com.github.minio.service.MinioTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

@Configuration
@EnableConfigurationProperties({MinioProperties.class})
@ComponentScan(basePackages = "com.github.minio.http")
public class MinioAutoConfiguration {

    @Autowired
    private MinioProperties properties;

    @Bean
    @ConditionalOnMissingBean(MinioTemplate.class)
    @ConditionalOnProperty(name = "minio.url")
    MinioTemplate template() {
        return new MinioTemplate(
                properties.getUrl(),
                properties.getAccessKey(),
                properties.getSecretKey()
        );
    }

    @Bean
    MinioClientPool minioClientPool() {
        return new MinioClientPool(properties);
    }

    @Bean
    @DependsOn("minioClientPool")
    MinioOptimizTemplate minioOptimizTemplate(@Qualifier("minioClientPool") MinioClientPool minioClientPool) {
        return new MinioOptimizTemplate(minioClientPool);
    }
}