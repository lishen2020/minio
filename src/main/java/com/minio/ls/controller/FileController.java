package com.minio.ls.controller;

import com.minio.ls.pojo.CustomObjectWriteResponse;
import com.minio.ls.service.FileService;
import com.minio.ls.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * file controller
 *
 * @author lishen
 * @version 1.0
 * @date 2020/07/23
 */
@RestController
@RequestMapping("/file")
public class FileController {

    @Autowired
    private FileService fileService;

    /**
     * get object from minio
     *
     * @param key object name
     * @return {@link ResponseEntity}
     * @author lishen
     */
    @GetMapping("/get")
    public ResponseEntity get(@RequestParam("key") String key) throws IOException {
        if (StringUtils.trimIsEmpty(key)) {
            return ResponseEntity.badRequest().build();
        }
        String trim = key.trim();
        int lastIndex = trim.lastIndexOf(StringUtils.SLASH);
        String objectName = trim.substring(lastIndex + 1, trim.indexOf(StringUtils.QUESTION_MARK));
        InputStream inputStream = fileService.getObject(key);
        if (inputStream != null) {
            byte[] bytes = this.getBytes(inputStream);
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; fileName="
                            .concat(new String(objectName.getBytes("utf-8"), "ISO-8859-1")))
//                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM.getType())
                    .header("Connection", "close")
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(bytes);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Uploads data to minio
     *
     * @param multipartFile multipartFile
     * @param key           object name
     * @return {@link ResponseEntity}
     * @author lishen
     */
    @PostMapping("/put/url")
    public ResponseEntity putObjectGetUrl(@RequestParam("file") MultipartFile multipartFile,
                                          @RequestParam("key") String key) {
        if (multipartFile == null || multipartFile.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        String url = fileService.putObjectGetUrl(multipartFile, key);
        return ResponseEntity.ok().body(url);
    }

    /**
     * Uploads data to minio
     *
     * @param multipartFile multipartFile
     * @param key           object name
     * @return {@link ResponseEntity}
     * @author lishen
     */
    @PostMapping("/put")
    public ResponseEntity putObject(@RequestParam("file") MultipartFile multipartFile,
                                    @RequestParam("key") String key) {
        if (multipartFile == null || multipartFile.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        CustomObjectWriteResponse objectWriteResponse = fileService.putObject(multipartFile, key);
        return ResponseEntity.ok().eTag(objectWriteResponse.etag()).body(objectWriteResponse.getUrl());
    }

    /**
     * gets bytes from inputStream
     *
     * @param inputStream
     * @return {@link byte[]}
     * @author lishen
     */
    private byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[4096];
        int len;
        while ((len = inputStream.read(buffer)) != -1) {
            byteArrayOutputStream.write(buffer, 0, len);
        }
        inputStream.close();
        return byteArrayOutputStream.toByteArray();
    }

}
