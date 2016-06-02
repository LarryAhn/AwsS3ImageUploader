package com.zuperztar.aws.s3;

import com.amazonaws.AmazonClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.Upload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

@Service
public class AwsS3FileUploaderImpl implements AwsS3FileUploader {

    @Autowired
    private AmazonS3 amazonS3;

    @Value("${bucket}")
    private String bucketName;

    @Override
    public String upload(MultipartFile file) {
        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String prefix = sdf.format(d);
        String uuid = UUID.randomUUID().toString();
        TransferManager transferManager = new TransferManager(this.amazonS3);
        Upload upload = null;

        try {

            BufferedImage image = ImageIO.read(file.getInputStream());
            int width = image.getWidth();
            int height = image.getHeight();
            //System.out.println(width);
            //System.out.println(height);
            //System.out.println(file.getOriginalFilename());

            upload = transferManager.upload(bucketName,
                    prefix + "/" + uuid, file.getInputStream(), generateObjectMetaData(file));
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            try {
                upload.waitForCompletion();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //return "Success-FileUpload:" + upload.getState();
            return prefix + "/" + uuid;
        } catch (AmazonClientException amazonClientException) {
            amazonClientException.printStackTrace();
            return "Unable to upload file, upload was aborted.";
        }
    }

    private ObjectMetadata generateObjectMetaData(MultipartFile file) {
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.addUserMetadata("UploadedBy", "AwsS3ImageUploader");
        objectMetadata.setContentType(file.getContentType());

        return objectMetadata;
    }
}
