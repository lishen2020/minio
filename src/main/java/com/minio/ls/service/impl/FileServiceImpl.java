package com.minio.ls.service.impl;

import com.minio.ls.handlers.MinioBucketHandler;
import com.minio.ls.handlers.MinioObjectHandler;
import com.minio.ls.pojo.CustomObjectWriteResponse;
import com.minio.ls.service.FileService;
import com.minio.ls.util.CodeGeneratorUtils;
import com.minio.ls.util.StringUtils;
import io.minio.ObjectWriteResponse;
import io.minio.errors.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

/**
 * file service implement class
 *
 * @author lishen
 * @version 1.0
 * @date 2020/07/23
 */
@Service
public class FileServiceImpl implements FileService {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileServiceImpl.class);

    /**
     * Uploads data to minio
     *
     * @param multipartFile multipartFile
     * @param key           object name
     * @return {@link CustomObjectWriteResponse} return object info
     * @author lishen
     */
    @Override
    public CustomObjectWriteResponse putObject(MultipartFile multipartFile, String key) {
        if (multipartFile == null || multipartFile.isEmpty()) {
            throw new NullPointerException("#### multipartFile is null or content is empty ####");
        }
        // bucket name
        LocalDate localDate = LocalDate.now();
        String bucketName = String.valueOf(localDate.getYear());
        // folder
        String month = String.valueOf(localDate.getMonthValue());
        String day = String.valueOf(localDate.getDayOfMonth());
        String folder = String.join(StringUtils.SLASH, month, day, StringUtils.EMPTY);
        try {
            // make bucket
            MinioBucketHandler.makeBucket(bucketName);
            // object name
            String objectName;
            if (StringUtils.trimIsEmpty(key)) {
                String code = CodeGeneratorUtils.code();
                String originalFilename = multipartFile.getOriginalFilename();
                String expandedName = originalFilename.substring(originalFilename.lastIndexOf(StringUtils.DOT));
                objectName = code.concat(expandedName);
            } else {
                objectName = key;
            }
            // put object
            ObjectWriteResponse objectWriteResponse = MinioObjectHandler
                    .putObject(bucketName, folder, objectName, multipartFile, null);
            List<String> strings = new LinkedList<>();
            strings.add(objectWriteResponse.bucket());
            strings.add(StringUtils.SLASH);
            strings.add(objectWriteResponse.object());
            strings.add(StringUtils.QUESTION_MARK);
            strings.add("etag=");
            strings.add(objectWriteResponse.etag());
            strings.add(StringUtils.AND);
            strings.add("versionId=");
            strings.add(objectWriteResponse.versionId());
            String url = String.join(StringUtils.EMPTY, strings);
            CustomObjectWriteResponse customObjectWriteResponse = new CustomObjectWriteResponse(objectWriteResponse, url);
            return customObjectWriteResponse;
        } catch (MinioException e) {
            e.printStackTrace();
            LOGGER.error(e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            LOGGER.error(e.getMessage());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            LOGGER.error(e.getMessage());
        } catch (InvalidKeyException e) {
            e.printStackTrace();
            LOGGER.error(e.getMessage());
        }
        return null;
    }

    /**
     * get object from minio
     *
     * @param key object name
     * @return {@link InputStream}
     * @author lishen
     */
    @Override
    public InputStream getObject(String key) {
        StringUtils.requireNonNullOrEmpty(key, "key must noy be null or empty.");
        String trim = key.trim();
        int firstIndex = trim.indexOf(StringUtils.SLASH);
        int lastIndex = trim.lastIndexOf(StringUtils.SLASH);
        String bucketName = trim.substring(0, firstIndex);
        String folder = trim.substring(firstIndex + 1, lastIndex + 1);
        String objectName = trim.substring(lastIndex + 1, trim.indexOf(StringUtils.QUESTION_MARK));
        String matchETag = null;
        String notMatchETag = null;
        String versionId = null;
        try {
            InputStream inputStream =
                    MinioObjectHandler.getObject(bucketName, folder, objectName, matchETag, notMatchETag, versionId);
            return inputStream;
        } catch (MinioException e) {
            e.printStackTrace();
            LOGGER.error(e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            LOGGER.error(e.getMessage());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            LOGGER.error(e.getMessage());
        } catch (InvalidKeyException e) {
            e.printStackTrace();
            LOGGER.error(e.getMessage());
        }
        return null;
    }

}
