package marcopagnanini.backend.Model;

import com.mongodb.lang.Nullable;
import lombok.*;
import marcopagnanini.backend.Utils.Enum.LogType;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document
public class Log {
    @Id
    private String id;
    private String projectId;
    private LogType level;
    private String message;
    private LocalDateTime timestamp;
    private String stackTrace;
}
