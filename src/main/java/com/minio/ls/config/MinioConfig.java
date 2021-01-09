package com.minio.ls.config;

import io.minio.MinioClient;
import io.minio.SetBucketPolicyArgs;
import io.minio.errors.*;
import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.TimeUnit;

/**
 * minio configuration class
 *
 * @author lishen
 * @version 1.0
 * @date 2020/07/22
 */
@Configuration
@PropertySource(value = "classpath:application.properties", encoding = "UTF-8")
@ConfigurationProperties(prefix = "minio.server")
public class MinioConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(MinioConfig.class);

    /**
     * endPoint
     */
    private static String endPoint;

    /**
     * accessKey
     */
    private static String accessKey;

    /**
     * secretKey
     */
    private static String secretKey;

    /**
     * MinioClient
     */
    private static MinioClient client;

    /**
     * get minio client instance
     *
     * @return {@link MinioClient}
     * @author lishen
     */
    public static MinioClient getClientInstance() {
        if (client == null) {
            synchronized (MinioConfig.class) {
                if (client == null) {
                    client = buildClient();
                }
            }
        }
        LOGGER.error("#### get minio client instance ####");
        return client;
    }

    /**
     * build a new minio client
     *
     * @return {@link MinioClient}
     * @author lishen
     */
    private static MinioClient buildClient() {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .writeTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.MINUTES)
                .connectTimeout(10, TimeUnit.MINUTES)
                .connectionPool(new ConnectionPool(5, 5L, TimeUnit.MINUTES))
                .retryOnConnectionFailure(true)
                .build();
        MinioClient client = MinioClient.builder()
                .endpoint(endPoint)
                .credentials(accessKey, secretKey)
                .httpClient(okHttpClient)
                .build();
        LOGGER.error("#### build a new minio client ####");
        return client;
    }

    public void setEndPoint(String endPoint) {
        MinioConfig.endPoint = endPoint;
    }

    public void setAccessKey(String accessKey) {
        MinioConfig.accessKey = accessKey;
    }

    public void setSecretKey(String secretKey) {
        MinioConfig.secretKey = secretKey;
    }
}
