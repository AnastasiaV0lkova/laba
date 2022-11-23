package mongodb.controllers;

import mongodb.payload.response.MessageResponse;
import mongodb.services.S3Factory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/file")
public class PhotoController {

    private final S3Factory s3Factory;

    @Autowired
    public PhotoController(S3Factory s3Factory) {
        this.s3Factory = s3Factory;
    }

    @PostMapping(path = "/upload", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<?> uploadFile(@RequestPart(value = "file") MultipartFile file) {
        String url = this.s3Factory.uploadFileToS3Bucket(file);
        return ResponseEntity.ok(new MessageResponse(url));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteFile(@RequestParam(value = "file") String file) {
        s3Factory.deleteFileFromS3Bucket(file);
        return ResponseEntity.ok(new MessageResponse(file));
    }

    @PostMapping("/copy")
    public ResponseEntity<?> copyObject(@RequestParam(value = "file") String file){
        String newFile = s3Factory.copyObject(file);
        return ResponseEntity.ok(newFile);
    }


}