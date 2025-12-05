package com.sky.utils;

import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayInputStream;

@Data
@AllArgsConstructor
@Slf4j
public class AliOssUtil {

    private String endpoint;           // http://127.0.0.1:9000
    private String accessKeyId;        // minioadmin
    private String accessKeySecret;    // minioadmin
    private String bucketName;         // sky-itcast

    /**
     * 文件上传
     *
     * @param bytes      文件字节
     * @param objectName 对象名（文件名/路径）
     * @return 文件访问 URL
     */
    public String upload(byte[] bytes, String objectName) {

        try {
            // 1. 创建 MinIO 客户端
            MinioClient minioClient = MinioClient.builder()
                    .endpoint(endpoint)                      // http://127.0.0.1:9000
                    .credentials(accessKeyId, accessKeySecret)
                    .build();

            // 2. 上传文件
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)              // sky-itcast
                            .object(objectName)              // xxx.png
                            .stream(new ByteArrayInputStream(bytes), bytes.length, -1)
                            .contentType("image/png")
                            .build()
            );

        } catch (Exception e) {
            log.error("上传文件到 MinIO 失败", e);
            throw new RuntimeException("上传文件失败", e);
        }

        // 3. 生成访问 URL： http://127.0.0.1:9000/sky-itcast/xxx.png
        String url = endpoint;
        if (!url.startsWith("http://") && !url.startsWith("https://")) {
            url = "http://" + url;
        }
        if (!url.endsWith("/")) {
            url = url + "/";
        }
        url = url + bucketName + "/" + objectName;

        log.info("文件上传到:{}", url);
        return url;
    }
}
