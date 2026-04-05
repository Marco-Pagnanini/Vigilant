package io.vigilant;

import ch.qos.logback.classic.spi.ILoggingEvent;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

public class VigilantClient {

    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    public VigilantClient() {
        this.httpClient = HttpClient.newHttpClient();
        this.objectMapper = new ObjectMapper();
    }

    public void sendLog(ILoggingEvent event, VigilantConfig config) {
        try {
            Map<String, String> body = new HashMap<>();
            body.put("level", event.getLevel().toString());
            body.put("message", event.getFormattedMessage());
            body.put("timestamp", Instant.ofEpochMilli(event.getTimeStamp()).toString());
            body.put("stackTrace", VigilantParser.parseStackTrace(event.getThrowableProxy()));

            String json = objectMapper.writeValueAsString(body);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(config.getServerUrl() + "/api/logs/" + config.getProjectId()))
                    .header("Content-Type", "application/json")
                    .header("X-API-Key", config.getApiKey())
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();

            httpClient.send(request, HttpResponse.BodyHandlers.discarding());

        } catch (Exception e) {
            System.err.println("[Vigilant] Failed to send log: " + e.getMessage());
        }
    }
}
