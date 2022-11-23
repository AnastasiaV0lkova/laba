package mongodb.security;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3Client;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
public class S3Configuration {
    @Value("${s3.access.name}")
    String accessKey;
    @Value("${s3.access.secret}")
    String accessSecret;

    @Bean
    public AmazonS3Client generateS3Client() {
        AWSCredentials credentials = new BasicAWSCredentials(accessKey, accessSecret);
        return new AmazonS3Client(credentials);
    }
}
