package com.minio.ls.service;

import com.minio.ls.pojo.CustomObjectWriteResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

/**
 * file service interface
 *
 * @author lishen
 * @version 1.0
 * @date 2020/07/23
 */
public interface FileService {

    /**
     * Uploads data to minio
     *
     * @param multipartFile multipartFile
     * @param key           object name
     * @return {@link CustomObjectWriteResponse} return object info
     * @author lishen
     */
    CustomObjectWriteResponse putObject(MultipartFile multipartFile, String key);

    /**
     * get object from minio
     *
     * @param key object name
     * @return {@link InputStream}
     * @author lishen
     */
    InputStream getObject(String key);
}
