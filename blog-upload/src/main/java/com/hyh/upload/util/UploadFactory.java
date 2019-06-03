package com.hyh.upload.util;

import com.qiniu.util.Auth;

public class UploadFactory {

    public static QiniuUtil createUpload(String accessKey, String secretKeySpec, String bucketName) {
        Auth auth = Auth.create(accessKey, secretKeySpec);
        return new QiniuUtil(null, bucketName, auth);
    }
}
