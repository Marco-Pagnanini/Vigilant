package marcopagnanini.backend.Infrastructure.Security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import marcopagnanini.backend.Application.Abstraction.Repository.ProjectRepository;
import marcopagnanini.backend.Model.Project;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@RequiredArgsConstructor
public class ApiKeyInterceptor implements HandlerInterceptor {

    private static final Logger log = LoggerFactory.getLogger(ApiKeyInterceptor.class);
    private final ProjectRepository projectRepository;
    private final JwtUtil jwtUtil;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // GET requests require a valid JWT (dashboard only)
        if ("GET".equalsIgnoreCase(request.getMethod())) {
            String authHeader = request.getHeader("Authorization");
            if (authHeader == null || !authHeader.startsWith("Bearer ") || !jwtUtil.isTokenValid(authHeader.substring(7))) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType("application/json");
                response.getWriter().write("{\"error\":\"JWT token required\"}");
                return false;
            }
            return true;
        }

        String apiKey = request.getHeader("X-API-Key");
        String projectId = extractProjectId(request.getRequestURI());

        if (apiKey == null || projectId == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write("{\"error\":\"Missing API Key\"}");
            return false;
        }

        Project project = projectRepository.findById(projectId);

        if (project == null) {
            log.warn("Project not found for id: '{}'", projectId);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write("{\"error\":\"Invalid API Key\"}");
            return false;
        }

        if (!project.getApiKey().equals(apiKey)) {
            log.warn("API key mismatch for project '{}'. Expected: '{}', Got: '{}'",
                    projectId, project.getApiKey(), apiKey);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write("{\"error\":\"Invalid API Key\"}");
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
