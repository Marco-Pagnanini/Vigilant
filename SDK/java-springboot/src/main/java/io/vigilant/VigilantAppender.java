package io.vigilant;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;
import lombok.Setter;

@Setter
public class VigilantAppender extends AppenderBase<ILoggingEvent> {

    private String serverUrl;
    private String projectId;
    private String apiKey;

    private VigilantClient vigilantClient;
    private VigilantConfig vigilantConfig;

    @Override
    public void start() {
        if (serverUrl == null || projectId == null || apiKey == null) {
            addError("[Vigilant] serverUrl, projectId and apiKey are required");
            return;
        }
        vigilantConfig = new VigilantConfig(serverUrl, projectId, apiKey);
        vigilantClient = new VigilantClient();
        super.start();
    }

    @Override
    protected void append(ILoggingEvent event) {
        vigilantClient.sendLog(event, vigilantConfig);
    }
}
