package mongodb.services;


import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.util.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
public class S3Factory {

    private final AmazonS3Client amazonS3Client;

    @Value("${s3.buckek.name}")
    private String defaultBucketName;
    @Value("${s3.default.folder}")
    private String defaultBaseFolder;

    private String getRandomUuidName() {
        UUID uuid = java.util.UUID.randomUUID();
        return uuid.toString();
    }

    public void deleteFileFromS3Bucket(String fileName) {
        try {
            amazonS3Client.deleteObject(new DeleteObjectRequest(defaultBucketName, defaultBaseFolder + "/" + fileName));
        } catch (AmazonServiceException ex) {
            throw new RuntimeException("Error: can't delete file");
        }
    }

    public String copyObject(String fileName) {
        try {
            String newName = UUID.randomUUID().toString();
            this.amazonS3Client.copyObject(defaultBucketName, defaultBaseFolder + "/" + fileName + ".png", defaultBucketName, defaultBaseFolder + "/" + newName + ".png");
            return newName;
        } catch (AmazonServiceException e){
            throw new RuntimeException("Error: can't copy picture");
        }
    }

    @Autowired
    public S3Factory(AmazonS3Client amazonS3Client) {
        this.amazonS3Client = amazonS3Client;
    }

    public List<Bucket> getAllBuckets() {
        return amazonS3Client.listBuckets();
    }

    public String uploadFileToS3Bucket(MultipartFile multipartFile) {
        String fileName = multipartFile.getOriginalFilename();
        try {
            //creating the file in the server (temporarily)
            File file = new File(fileName);
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(multipartFile.getBytes());
            fos.close();
            String randomName = getRandomUuidName();
            this.amazonS3Client.putObject(defaultBucketName, defaultBaseFolder + "/" + randomName + ".png", file);
            //removing the file created in the server
            file.delete();
            return "https://dq2g5czw4138n.cloudfront.net/questions/" + randomName + ".png";
        } catch (IOException | AmazonServiceException ex) {
            throw new RuntimeException("Error: can't upload file");
        }
    }

    public byte[] getFile(String key) {
        S3Object obj = amazonS3Client.getObject(defaultBucketName, defaultBaseFolder + "/" + key);
        S3ObjectInputStream stream = obj.getObjectContent();
        try {
            byte[] content = IOUtils.toByteArray(stream);
            obj.close();
            return content;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}