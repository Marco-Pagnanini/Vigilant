package marcopagnanini.backend.Application.Service;

import lombok.RequiredArgsConstructor;
import marcopagnanini.backend.Application.Abstraction.Repository.ProjectRepository;
import marcopagnanini.backend.Application.Abstraction.Service.ProjectService;
import marcopagnanini.backend.Model.Project;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;

    @Override
    public Project save(Project project) {
        project.setApiKey(UUID.randomUUID().toString());
        project.setCreatedAt(LocalDateTime.now());
        return projectRepository.save(project);
    }

    @Override
    public List<Project> findAll() {
        return projectRepository.findAll();
    }

    @Override
    public Project findById(String id) {
        return projectRepository.findById(id);
    }

    @Override
    public Project delete(String id) {
        return projectRepository.delete(id);
    }
}
