package com.hyh.upload.service;

import com.hyh.upload.util.QiniuUtil;
import com.hyh.upload.util.UploadFactory;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.List;

@Service
public class UploadService {


/*    // 支持的文件类型
    private static final List<String> suffixes = Arrays.asList("image/png", "image/jpeg","image/jpg");
    @Autowired
    FastFileStorageClient storageClient;
    public String upload(MultipartFile file) {
        try {
            // 1、图片信息校验
            // 1)校验文件类型
            String type = file.getContentType();
            if (!suffixes.contains(type)) {
                return null;
            }
            // 2)校验图片内容
            BufferedImage image = ImageIO.read(file.getInputStream());
            if (image == null) {
                return null;
            }
            // 2、将图片上传到FastDFS
            // 2.1、获取文件后缀名
            String extension = StringUtils.substringAfterLast(file.getOriginalFilename(), ".");
            // 2.2、上传
            StorePath storePath = this.storageClient.uploadFile(
                    file.getInputStream(), file.getSize(), extension, null);
            // 2.3、返回完整路径
            return "http://192.168.25.133/" + storePath.getFullPath();
        } catch (Exception e) {
            return null;
        }
    }*/

    private String accesskey = "cta95C_UJY09OB3JbMG6Ct5UzMJazN97kv3H1ll4";

    private String secretKey = "waK0r9zkw4ltCFgqyrt9Q6QGNxM7cYEX4ftciide";

    private String bucketName = "blog";


    public String upload(MultipartFile image) throws Exception {
        QiniuUtil uploadUtil = UploadFactory.createUpload(this.accesskey, this.secretKey,
                 this.bucketName);
        return uploadUtil.uploadFile("/img/", image);
    }
}

