package com.zuperztar.aws.s3;

import org.springframework.beans.factory.annotation.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.*;

import java.io.*;

@RestController
@RequestMapping("/aws/s3")
public class AwsS3Controller {

    @Autowired
    AwsS3FileUploader awsS3FileUploader;

    @RequestMapping(method = RequestMethod.POST, value = "/upload")
    public String upload2S3(@RequestParam("image") MultipartFile mfile) throws IOException {
        String status = awsS3FileUploader.upload(mfile);
        return status;
    }
}

