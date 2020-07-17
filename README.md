# minio-basic-services

####  封装了minio的文件操作，使用方式如下：
1. 将本项目clone到本地
2. 执行 mvn clean install, 打成 jar 包上传到个人私服, 在需要的项目内引用该依赖
    >
          <dependency>
              <groupId>com.github</groupId>
              <artifactId>minio-starter</artifactId>
              <version>0.0.1-SNAPSHOT</version>
          </dependency>
3. 项目配置文件中定义属性
    >
          minio:
             url: http://127.0.0.1:9000
             access-key: minio
             secret-key: minio123
             bucket-name: feedback
             endpointEnabled: true
             endpointName: minio
             pool:
               max-total: 30
               max-idle: 10
               min-idle: 3
               max-wait-millis: 3000
               block-when-exhausted: true
          
          参数说明：
              * url: minio地址
              * access-key: minio账号
              * bucket-name: minio密码
              * bucket-name: bucket名称
              * endpointEnabled: 是否激活封装API
              * endpointName: 自定义API前缀 (/minio/***)
              * pool: 池配置
              *      max-total: 资源池中的最大连接数
              *      max-idle: 资源池允许的最大空闲连接数
              *      min-idle: 资源池确保的最少空闲连接数
              *      block-when-exhausted: 当资源池用尽后，调用者是否要等待。只有当值为true时，下面的max-wait-millis才会生效
              *      max-wait-millis: 当资源池连接用尽后，调用者的最大等待时间（单位为毫秒）

4. API

|地址|请求方式|作用
| :-------| :-----  | :----- 
| /minio/bucket/{bucketName}     | POST |创建 bucket   
| /minio/bucket     | GET |获取所有bucket   
| /minio/bucket/{bucketName} | GET   | 根据名称获取bucket  
| /minio/bucket/{bucketName} |  DELETE    | 删除bucket  
| /minio/object/{bucketName} |  POST   | 存入对象到bucket
| /minio/object/{bucketName} |  POST   | 存入对象到bucket
| /minio/object/{bucketName}/{objectName} |  POST   | 存入对象到bucket并设置对象名称
| /minio/object/{bucketName}/{objectName} |  DELETE   | 根据名称对指定bucket下的对象进行删除
| /minio/object/{bucketName}/{objectName} |  GET   | 根据bucket名称和对象名称过滤所有对象
| /minio/object/{bucketName}/{objectName}/{expires} |  GET   | 根据名称获取bucket下的对象并设置外链的过期时间
