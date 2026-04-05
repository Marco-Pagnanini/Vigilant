package marcopagnanini.backend.Api;

import lombok.RequiredArgsConstructor;
import marcopagnanini.backend.Api.DTO.ProjectCreatedResponse;
import marcopagnanini.backend.Api.DTO.ProjectResponse;
import marcopagnanini.backend.Application.Abstraction.Service.ProjectService;
import marcopagnanini.backend.Model.Project;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projects")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;

    @GetMapping
    public ResponseEntity<List<ProjectResponse>> findAll() {
        List<ProjectResponse> projects = projectService.findAll().stream()
                .map(p -> ProjectResponse.builder()
                        .id(p.getId())
                        .name(p.getName())
                        .description(p.getDescription())
                        .createdAt(p.getCreatedAt())
                        .build())
                .toList();
        return ResponseEntity.ok(projects);
    }

    @PostMapping
    public ResponseEntity<ProjectCreatedResponse> save(@RequestBody Project project) {
        Project saved = projectService.save(project);
        ProjectCreatedResponse response = ProjectCreatedResponse.builder()
                .id(saved.getId())
                .name(saved.getName())
                .description(saved.getDescription())
                .apiKey(saved.getApiKey())
                .createdAt(saved.getCreatedAt())
                .build();
        return ResponseEntity.ok(response);
    }
}
