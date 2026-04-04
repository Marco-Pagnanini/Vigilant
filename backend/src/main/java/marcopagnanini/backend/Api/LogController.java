package marcopagnanini.backend.Api;

import lombok.RequiredArgsConstructor;
import marcopagnanini.backend.Application.Abstraction.Service.LogService;
import marcopagnanini.backend.Model.Log;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/logs")
@RequiredArgsConstructor
public class LogController {

    private final LogService logService;

    @GetMapping("/{projectId}")
    public ResponseEntity<Page<Log>> findAll(
            @PathVariable String projectId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        PageRequest pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "timestamp"));
        return ResponseEntity.ok(logService.findAll(projectId, pageable));
    }

    @PostMapping("/{projectId}")
    public ResponseEntity<Log> save(@PathVariable String projectId, @RequestBody Log log) {
        return ResponseEntity.ok(logService.save(log, projectId));
    }
}
