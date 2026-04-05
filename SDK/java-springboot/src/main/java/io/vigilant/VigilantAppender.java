package io.vigilant;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;

public class VigilantAppender extends AppenderBase<ILoggingEvent> {

    private final VigilantConfig vigilantConfig;
    private VigilantClient vigilantClient;

    public VigilantAppender(VigilantConfig vigilantConfig) {
        this.vigilantConfig = vigilantConfig;
    }

    @Override
    public void start() {
        if (vigilantConfig.getServerUrl() == null || vigilantConfig.getProjectId() == null || vigilantConfig.getApiKey() == null) {
            addError("[Vigilant] serverUrl, projectId and apiKey are required");
            return;
        }
        vigilantClient = new VigilantClient();
        super.start();
    }

    @Override
    protected void append(ILoggingEvent event) {
        Level minLevel = Level.valueOf(vigilantConfig.getMinLevel());
        if (event.getLevel().isGreaterOrEqual(minLevel)) {
            vigilantClient.sendLog(event, vigilantConfig);
        }
    }
}
