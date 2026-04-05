package marcopagnanini.backend.Api.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProjectCreatedResponse {
    private String id;
    private String name;
    private String description;
    private String apiKey;
    private LocalDateTime createdAt;
}
