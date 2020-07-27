package com.minio.ls.pojo;

import io.minio.ObjectWriteResponse;
import okhttp3.Headers;

/**
 * ObjectWriteResponse
 *
 * @author lishen
 * @version 1.0
 * @date 2020/07/24
 */
public class CustomObjectWriteResponse extends ObjectWriteResponse {

    /**
     * 资源路径
     */
    private String url;

    public CustomObjectWriteResponse(Headers headers, String bucket, String region,
                                     String object, String etag, String versionId, String url) {
        super(headers, bucket, region, object, etag, versionId);
        this.url = url;
    }

    public CustomObjectWriteResponse(ObjectWriteResponse response, String url) {
        super(response.headers(), response.bucket(), response.region(),
                response.object(), response.etag(), response.versionId());
        this.url = url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }
}
