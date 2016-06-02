package com.zuperztar.aws.s3;

import org.springframework.web.multipart.MultipartFile;

public interface AwsS3FileUploader {
    public String upload(MultipartFile file);
}
