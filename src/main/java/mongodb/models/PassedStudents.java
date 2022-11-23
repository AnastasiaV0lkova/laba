package mongodb.models;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PassedStudents {
    private String id;
    private String status;
    private LocalDateTime time;
}
