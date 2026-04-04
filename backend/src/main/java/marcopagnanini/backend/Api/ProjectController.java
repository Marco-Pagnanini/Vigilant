package marcopagnanini.backend.Api;

import lombok.RequiredArgsConstructor;
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
    public ResponseEntity<List<Project>> findAll() {
        return ResponseEntity.ok(projectService.findAll());
    }

    @PostMapping
    public ResponseEntity<Project> save(@RequestBody Project project) {
        return ResponseEntity.ok(projectService.save(project));
    }
}
