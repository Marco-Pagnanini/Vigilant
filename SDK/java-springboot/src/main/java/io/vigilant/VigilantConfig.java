package io.vigilant;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;


@ConfigurationProperties(prefix = "vigilant")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class VigilantConfig {
    private String serverUrl;
    private String projectId;
    private String apiKey;
}
