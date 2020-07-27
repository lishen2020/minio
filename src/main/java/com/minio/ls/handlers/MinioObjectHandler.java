package com.minio.ls.handlers;

import com.minio.ls.config.MinioConfig;
import com.minio.ls.util.CodeGeneratorUtils;
import com.minio.ls.util.StringUtils;
import io.minio.*;
import io.minio.errors.*;
import io.minio.messages.DeleteError;
import io.minio.messages.DeleteObject;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * minio object handler class
 *
 * @author lishen
 * @version 1.0
 * @date 2020/07/23
 */
public class MinioObjectHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(MinioObjectHandler.class);

    /**
     * Gets information of an object
     *
     * @param bucketName   bucket name
     * @param folder       object path, example: path/to/
     * @param objectName   object name
     * @param matchETag    object match eTag
     * @param notMatchETag object not match eTag
     * @param versionId    object versionId
     * @return {@link ObjectStat}
     * @author lishen
     */
    public static ObjectStat statObject(@Nonnull String bucketName, @Nullable String folder,
                                        @Nonnull String objectName, @Nullable String matchETag,
                                        @Nullable String notMatchETag, @Nullable String versionId)
            throws IOException, InvalidKeyException, InvalidResponseException, InsufficientDataException,
            NoSuchAlgorithmException, ServerException, InternalException, XmlParserException,
            InvalidBucketNameException, ErrorResponseException, NullPointerException {
        /*
        validate params
         */
        StringUtils.requireNonNullOrEmpty(bucketName, "bucketName must not be null or empty");
        StringUtils.requireNonNullOrEmpty(objectName, "objectName must not be null or empty");

        MinioClient client = MinioConfig.getClientInstance();
        BucketExistsArgs bucketExistsArgs = BucketExistsArgs.builder().bucket(bucketName).build();
        boolean exists = client.bucketExists(bucketExistsArgs);
        if (exists) {
            if (StringUtils.isEmpty(folder)) {
                folder = StringUtils.EMPTY;
            }
            String pathToObject = folder.concat(objectName);
            StatObjectArgs statObjectArgs = StatObjectArgs.builder()
                    .bucket(bucketName)
                    .object(pathToObject)
                    .matchETag(matchETag)
                    .notMatchETag(notMatchETag)
                    .versionId(versionId)
                    .build();
            ObjectStat objectStat = client.statObject(statObjectArgs);
            return objectStat;
        } else {
            throw new InvalidBucketNameException(bucketName, "#### bucket does not exists ####");
        }
    }

    /**
     * Removes multiple objects lazily. Its required to iterate the returned Iterable to perform removal
     *
     * @param bucketName  bucket name
     * @param objectNames object names list, each name must contains object path, examples: path/to/objectName
     * @return {@link Iterable<Result<DeleteError>>} each item contains object info and error message
     * @author lishen
     */
    public static Iterable<Result<DeleteError>> removeObjects(@Nonnull String bucketName,
                                                              @Nonnull List<String> objectNames)
            throws IOException, InvalidKeyException, InvalidResponseException, InsufficientDataException,
            NoSuchAlgorithmException, ServerException, InternalException, XmlParserException,
            InvalidBucketNameException, ErrorResponseException, NullPointerException {
        // validate bucket name
        StringUtils.requireNonNullOrEmpty(bucketName, "bucketName must not be null or empty");
        // validate object names
        if (CollectionUtils.isEmpty(objectNames)) {
            throw new NullPointerException("#### object names must not be null or empty ####");
        }
        MinioClient client = MinioConfig.getClientInstance();
        BucketExistsArgs bucketExistsArgs = BucketExistsArgs.builder().bucket(bucketName).build();
        boolean exists = client.bucketExists(bucketExistsArgs);
        if (exists) {
            List<DeleteObject> deleteObjects = new LinkedList<>();
            objectNames.forEach(objectName -> deleteObjects.add(new DeleteObject(objectName)));
            RemoveObjectsArgs removeObjectsArgs = RemoveObjectsArgs.builder()
                    .bucket(bucketName)
                    .objects(deleteObjects)
                    .build();
            Iterable<Result<DeleteError>> results = client.removeObjects(removeObjectsArgs);
            for (Result<DeleteError> result : results) {
                DeleteError error = result.get();
                LOGGER.error("Error in deleting object {};\nError message: {}", error.objectName(), error.message());
            }
            return results;
        } else {
            throw new InvalidBucketNameException(bucketName, "#### bucket does not exists ####");
        }
    }

    /**
     * Removes an object
     *
     * @param bucketName bucket name
     * @param folder     object path, example: path/to/
     * @param objectName object name
     * @param versionId  object versionId
     * @author lishen
     */
    public static void removeObject(@Nonnull String bucketName, @Nullable String folder,
                                    @Nonnull String objectName, @Nullable String versionId)
            throws IOException, InvalidKeyException, InvalidResponseException, InsufficientDataException,
            NoSuchAlgorithmException, ServerException, InternalException, XmlParserException,
            InvalidBucketNameException, ErrorResponseException, NullPointerException {
        /*
        validate params
         */
        StringUtils.requireNonNullOrEmpty(bucketName, "bucketName must not be null or empty");
        StringUtils.requireNonNullOrEmpty(objectName, "objectName must not be null or empty");

        MinioClient client = MinioConfig.getClientInstance();
        BucketExistsArgs bucketExistsArgs = BucketExistsArgs.builder().bucket(bucketName).build();
        boolean exists = client.bucketExists(bucketExistsArgs);
        if (exists) {
            if (StringUtils.isEmpty(folder)) {
                folder = StringUtils.EMPTY;
            }
            String pathToObject = folder.concat(objectName);
            RemoveObjectArgs removeObjectArgs = RemoveObjectArgs.builder()
                    .bucket(bucketName)
                    .object(pathToObject)
                    .versionId(versionId)
                    .build();
            client.removeObject(removeObjectArgs);
        } else {
            throw new InvalidBucketNameException(bucketName, "#### bucket does not exists ####");
        }
    }

    /**
     * Gets data stream
     *
     * @param bucketName   bucket name
     * @param folder       object path, example: path/to/
     * @param objectName   object name
     * @param matchETag    object match eTag
     * @param notMatchETag object not match eTag
     * @param versionId    object versionId
     * @return {@link InputStream} object inputStream
     * @author lishen
     */
    public static InputStream getObject(@Nonnull String bucketName, @Nullable String folder, @Nonnull String objectName,
                                        @Nullable String matchETag, @Nullable String notMatchETag,
                                        @Nullable String versionId)
            throws IOException, InvalidKeyException, InvalidResponseException, InsufficientDataException,
            NoSuchAlgorithmException, ServerException, InternalException, XmlParserException,
            InvalidBucketNameException, ErrorResponseException, NullPointerException {
        /*
        validate params
         */
        StringUtils.requireNonNullOrEmpty(bucketName, "bucketName must not be null or empty");
        StringUtils.requireNonNullOrEmpty(objectName, "objectName must not be null or empty");

        MinioClient client = MinioConfig.getClientInstance();
        BucketExistsArgs bucketExistsArgs = BucketExistsArgs.builder().bucket(bucketName).build();
        boolean exists = client.bucketExists(bucketExistsArgs);
        if (exists) {
            if (StringUtils.isEmpty(folder)) {
                folder = StringUtils.EMPTY;
            }
            String pathToObject = folder.concat(objectName);
            GetObjectArgs getObjectArgs = GetObjectArgs.builder()
                    .bucket(bucketName)
                    .object(pathToObject)
                    .matchETag(matchETag)
                    .notMatchETag(notMatchETag)
                    .versionId(versionId)
                    .build();
            InputStream inputStream = client.getObject(getObjectArgs);
            return inputStream;
        } else {
            throw new InvalidBucketNameException(bucketName, "#### bucket does not exists ####");
        }
    }

    /**
     * Uploads data from a stream to an object.
     *
     * @param bucketName  bucket name
     * @param folder      object path, example: path/to/
     * @param objectName  object name
     * @param inputStream inputStream
     * @param tags        tags
     * @return {@link ObjectWriteResponse} contains etag and versionId
     * @author lishen
     */
    public static ObjectWriteResponse putObject(@Nonnull String bucketName, @Nullable String folder,
                                                @Nullable String objectName, @Nonnull InputStream inputStream,
                                                @Nonnull String contentType, @Nullable Map<String, String> tags)
            throws IOException, InvalidKeyException, InvalidResponseException, InsufficientDataException,
            NoSuchAlgorithmException, ServerException, InternalException, XmlParserException,
            InvalidBucketNameException, ErrorResponseException, NullPointerException {
        /*
        validate params
         */
        StringUtils.requireNonNullOrEmpty(bucketName, "bucketName must not be null or empty");
        StringUtils.requireNonNullOrEmpty(contentType, "contentType must not be null or empty");
        if (inputStream == null || inputStream.available() <= 0) {
            throw new NullPointerException("#### inputStream must not be null or empty ####");
        }
        MinioClient client = MinioConfig.getClientInstance();
        BucketExistsArgs bucketExistsArgs = BucketExistsArgs.builder().bucket(bucketName).build();
        boolean exists = client.bucketExists(bucketExistsArgs);
        if (exists) {
            if (StringUtils.isEmpty(objectName)) {
                objectName = CodeGeneratorUtils.code();
            }
            if (StringUtils.isEmpty(folder)) {
                folder = StringUtils.EMPTY;
            }
            String pathToObject = folder.concat(objectName);
            PutObjectArgs putObjectArgs = PutObjectArgs.builder()
                    .bucket(bucketName)
                    .object(pathToObject)
                    .contentType(contentType)
                    .tags(tags)
                    .stream(inputStream, inputStream.available(), -1)
                    .build();
            ObjectWriteResponse response = client.putObject(putObjectArgs);
            return response;
        } else {
            throw new InvalidBucketNameException(bucketName, "#### bucket does not exists ####");
        }
    }

    /**
     * Uploads data from a stream to an object
     *
     * @param bucketName    bucket name
     * @param folder        object path, example: path/to/
     * @param objectName    object name
     * @param multipartFile multipartFile
     * @param tags          tags
     * @return {@link ObjectWriteResponse} contains etag and versionId
     * @author lishen
     */
    public static ObjectWriteResponse putObject(@Nonnull String bucketName, @Nullable String folder,
                                                @Nullable String objectName, @Nonnull MultipartFile multipartFile,
                                                @Nullable Map<String, String> tags)
            throws IOException, InvalidKeyException, InvalidResponseException, InsufficientDataException,
            NoSuchAlgorithmException, ServerException, InternalException, XmlParserException,
            InvalidBucketNameException, ErrorResponseException, NullPointerException {
        /*
        validate params
         */
        StringUtils.requireNonNullOrEmpty(bucketName, "bucketName must not be null or empty");
        if (multipartFile == null || multipartFile.isEmpty()) {
            throw new NullPointerException("#### multipartFile is null or content is empty ####");
        }
        MinioClient client = MinioConfig.getClientInstance();
        BucketExistsArgs bucketExistsArgs = BucketExistsArgs.builder().bucket(bucketName).build();
        boolean exists = client.bucketExists(bucketExistsArgs);
        if (exists) {
            if (StringUtils.isEmpty(objectName)) {
                objectName = CodeGeneratorUtils.code();
            }
            if (StringUtils.isEmpty(folder)) {
                folder = StringUtils.EMPTY;
            }
            String pathToObject = folder.concat(objectName);
            PutObjectArgs putObjectArgs = PutObjectArgs.builder()
                    .bucket(bucketName)
                    .object(pathToObject)
                    .contentType(multipartFile.getContentType())
                    .tags(tags)
                    .stream(multipartFile.getInputStream(), multipartFile.getSize(), -1)
                    .build();
            ObjectWriteResponse response = client.putObject(putObjectArgs);
            return response;
        } else {
            throw new InvalidBucketNameException(bucketName, "#### bucket does not exists ####");
        }
    }

}
