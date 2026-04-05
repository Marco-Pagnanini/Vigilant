package marcopagnanini.backend.Infrastructure.Security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import marcopagnanini.backend.Application.Abstraction.Repository.ProjectRepository;
import marcopagnanini.backend.Model.Project;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@RequiredArgsConstructor
public class ApiKeyInterceptor implements HandlerInterceptor {

    private final ProjectRepository projectRepository;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String apiKey = request.getHeader("X-API-Key");
        String projectId = extractProjectId(request.getRequestURI());

        if (apiKey == null || projectId == null) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Missing API Key");
            return false;
        }

        Project project = projectRepository.findById(projectId);

        if (project == null || !project.getApiKey().equals(apiKey)) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid API Key");
            return false;
        }

        return true;
    }

    private String extractProjectId(String uri) {
        // URI format: /api/logs/{projectId}
        String[] parts = uri.split("/");
        if (parts.length >= 4) {
            return parts[3];
        }
        return null;
    }
}
