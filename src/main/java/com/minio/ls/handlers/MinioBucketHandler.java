package com.minio.ls.handlers;

import com.minio.ls.config.MinioConfig;
import com.minio.ls.util.StringUtils;
import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.RemoveBucketArgs;
import io.minio.errors.*;
import io.minio.messages.Bucket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

/**
 * minio bucket handler class
 *
 * @author lishen
 * @version 1.0
 * @date 2020/07/23
 */
public class MinioBucketHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(MinioBucketHandler.class);

    /**
     * Remove an empty bucket using arguments
     *
     * @param bucketName bucket name
     * @return {@link boolean} true:removed, false:remove failure or bucket not exist or not empty
     * @author lishen
     */
    public static boolean removeBucket(@Nonnull String bucketName) throws IOException, InvalidKeyException,
            InvalidResponseException, InsufficientDataException, NoSuchAlgorithmException, ServerException,
            InternalException, XmlParserException, InvalidBucketNameException, ErrorResponseException {
        StringUtils.requireNonNullOrEmpty(bucketName, "bucketName must not be null or empty");
        MinioClient client = MinioConfig.getClientInstance();
        BucketExistsArgs bucketExistsArgs = BucketExistsArgs.builder().bucket(bucketName).build();
        if (!client.bucketExists(bucketExistsArgs)) {
            RemoveBucketArgs removeBucketArgs = RemoveBucketArgs.builder().bucket(bucketName).build();
            client.removeBucket(removeBucketArgs);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Create a bucket.
     *
     * @param bucketName bucket name
     * @return {@link boolean} true:create success, false:create failure or bucket exists
     * @author lishen
     */
    public static boolean makeBucket(@Nonnull String bucketName) throws IOException, InvalidKeyException,
            InvalidResponseException, InsufficientDataException, NoSuchAlgorithmException, ServerException,
            InternalException, XmlParserException, InvalidBucketNameException, ErrorResponseException,
            RegionConflictException {
        StringUtils.requireNonNullOrEmpty(bucketName, "bucketName must not be null or empty");
        MinioClient client = MinioConfig.getClientInstance();
        BucketExistsArgs bucketExistsArgs = BucketExistsArgs.builder().bucket(bucketName).build();
        if (!client.bucketExists(bucketExistsArgs)) {
            MakeBucketArgs makeBucketArgs = MakeBucketArgs.builder().bucket(bucketName).build();
            client.makeBucket(makeBucketArgs);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Lists bucket information of all buckets
     *
     * @return {@link List<Bucket>}
     * @author lishen
     */
    public static List<Bucket> listBuckets() throws IOException, InvalidKeyException, InvalidResponseException,
            InsufficientDataException, NoSuchAlgorithmException, ServerException, InternalException,
            XmlParserException, InvalidBucketNameException, ErrorResponseException {
        MinioClient client = MinioConfig.getClientInstance();
        List<Bucket> listBuckets = client.listBuckets();
        return listBuckets;
    }

    /**
     * Checks if a bucket exists
     *
     * @param bucketName bucket name
     * @return {@link boolean}  true:exists, false:not exist
     * @author lishen
     */
    public static boolean bucketExists(@Nonnull String bucketName) throws IOException, InvalidKeyException,
            InvalidResponseException, InsufficientDataException, NoSuchAlgorithmException, ServerException,
            InternalException, XmlParserException, InvalidBucketNameException, ErrorResponseException {
        StringUtils.requireNonNullOrEmpty(bucketName, "bucketName must not be null or empty");
        MinioClient client = MinioConfig.getClientInstance();
        BucketExistsArgs bucketExistsArgs = BucketExistsArgs.builder().bucket(bucketName).build();
        boolean exists = client.bucketExists(bucketExistsArgs);
        if (exists) {
            return true;
        } else {
            return false;
        }
    }

}
