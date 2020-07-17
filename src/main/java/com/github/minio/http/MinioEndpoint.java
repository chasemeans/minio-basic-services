package com.github.minio.http;

import com.github.minio.service.MinioTemplate;
import com.github.minio.vo.MinioItem;
import com.github.minio.vo.MinioObject;
import io.minio.errors.*;
import io.minio.messages.Bucket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ConditionalOnProperty(name = "minio.endpointEnabled", havingValue = "true")
@RestController
@RequestMapping("${minio.endpointName}")
public class MinioEndpoint {

    private static final Logger LOGGER = LoggerFactory.getLogger(MinioEndpoint.class);

    public MinioEndpoint(MinioTemplate minioTemplate) {
        LOGGER.info("Hello, MinioEndpoint init");
        this.minioTemplate = minioTemplate;
    }

    private final MinioTemplate minioTemplate;

    private static final Integer ONE_DAY = 60 * 60 * 24;

    /**
     * 创建 bucket
     * Bucket Endpoints
     */
    @PostMapping("/bucket/{bucketName}")
    public Bucket createBucker(@PathVariable String bucketName) throws Exception {
        minioTemplate.createBucket(bucketName);
        return minioTemplate.getBucket(bucketName).get();
    }

    /**
     * 获取所有bucket
     *
     * @return
     * @throws IOException
     * @throws XmlPullParserException
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     * @throws InvalidPortException
     * @throws ErrorResponseException
     * @throws NoResponseException
     * @throws InvalidBucketNameException
     * @throws InsufficientDataException
     * @throws InvalidEndpointException
     * @throws InternalException
     * @throws InvalidResponseException
     */
    @GetMapping("/bucket")
    public List<Bucket> getBuckets() throws Exception {
        return minioTemplate.getAllBuckets();
    }

    /**
     * 根据名称获取bucket
     *
     * @param bucketName
     * @return
     * @throws IOException
     * @throws XmlPullParserException
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     * @throws InvalidPortException
     * @throws ErrorResponseException
     * @throws NoResponseException
     * @throws InvalidBucketNameException
     * @throws InsufficientDataException
     * @throws InvalidEndpointException
     * @throws InternalException
     * @throws InvalidResponseException
     */
    @GetMapping("/bucket/{bucketName}")
    public Bucket getBucket(@PathVariable String bucketName) throws Exception {
        return minioTemplate.getBucket(bucketName).orElseThrow(() -> new IllegalArgumentException("Bucket Name not found!"));
    }

    /**
     * 删除bucket
     *
     * @param bucketName
     * @throws IOException
     * @throws XmlPullParserException
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     * @throws InvalidPortException
     * @throws ErrorResponseException
     * @throws NoResponseException
     * @throws InvalidBucketNameException
     * @throws InsufficientDataException
     * @throws InvalidEndpointException
     * @throws InternalException
     * @throws InvalidResponseException
     */
    @DeleteMapping("/bucket/{bucketName}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void deleteBucket(@PathVariable String bucketName) throws Exception {
        minioTemplate.removeBucket(bucketName);
    }

    /**
     * 存入对象到bucket
     * Object Endpoints
     */
    @PostMapping("/object/{bucketName}")
    public MinioObject putObject(@RequestBody MultipartFile object, @PathVariable String bucketName) throws Exception {
        String name = object.getOriginalFilename();
        minioTemplate.putObject(bucketName, name, object.getInputStream(), object.getSize(), object.getContentType());
        return new MinioObject(minioTemplate.getObjectInfo(bucketName, name));

    }

    /**
     * 存入对象到bucket并设置对象名称
     *
     * @param object
     * @param bucketName
     * @param objectName
     * @return
     * @throws IOException
     * @throws InvalidKeyException
     * @throws NoSuchAlgorithmException
     * @throws XmlPullParserException
     * @throws InvalidPortException
     * @throws ErrorResponseException
     * @throws InternalException
     * @throws NoResponseException
     * @throws InvalidBucketNameException
     * @throws InsufficientDataException
     * @throws InvalidEndpointException
     * @throws RegionConflictException
     * @throws InvalidArgumentException
     * @throws InvalidResponseException
     */
    @PostMapping("/object/{bucketName}/{objectName}")
    public MinioObject putObject(@RequestBody MultipartFile object, @PathVariable String bucketName, @PathVariable String objectName) throws Exception {
        minioTemplate.putObject(bucketName, objectName, object.getInputStream(), object.getSize(), object.getContentType());
        return new MinioObject(minioTemplate.getObjectInfo(bucketName, objectName));
    }


    /**
     * 根据bucket名称和对象名称过滤所有对象
     *
     * @param bucketName
     * @param objectName
     * @return
     * @throws InvalidPortException
     * @throws InvalidEndpointException
     */
    @GetMapping("/object/{bucketName}/{objectName}")
    public List<MinioItem> filterObject(@PathVariable String bucketName, @PathVariable String objectName) throws Exception {
        return minioTemplate.getAllObjectsByPrefix(bucketName, objectName, true);
    }

    /**
     * 根据名称获取bucket下的对象并设置外链的过期时间
     *
     * @param bucketName
     * @param objectName
     * @param expires
     * @return
     * @throws IOException
     * @throws XmlPullParserException
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     * @throws InvalidPortException
     * @throws ErrorResponseException
     * @throws NoResponseException
     * @throws InvalidBucketNameException
     * @throws InsufficientDataException
     * @throws InvalidEndpointException
     * @throws InternalException
     * @throws InvalidExpiresRangeException
     * @throws InvalidResponseException
     */
    @GetMapping("/object/{bucketName}/{objectName}/{expires}")
    public Map<String, Object> getObject(@PathVariable String bucketName, @PathVariable String objectName, @PathVariable Integer expires) throws Exception {
        Map<String, Object> responseBody = new HashMap<>();
        // Put Object info
        responseBody.put("bucket", bucketName);
        responseBody.put("object", objectName);
        responseBody.put("url", minioTemplate.getObjectURL(bucketName, objectName, expires));
        responseBody.put("expires", expires);
        return responseBody;
    }

    /**
     * 根据名称对指定bucket下的对象进行删除
     *
     * @param bucketName
     * @param objectName
     * @throws IOException
     * @throws XmlPullParserException
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     * @throws InvalidPortException
     * @throws ErrorResponseException
     * @throws NoResponseException
     * @throws InvalidBucketNameException
     * @throws InsufficientDataException
     * @throws InvalidEndpointException
     * @throws InternalException
     * @throws InvalidArgumentException
     * @throws InvalidResponseException
     */
    @DeleteMapping("/object/{bucketName}/{objectName}/")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void deleteObject(@PathVariable String bucketName, @PathVariable String objectName) throws Exception {
        minioTemplate.removeObject(bucketName, objectName);
    }
}