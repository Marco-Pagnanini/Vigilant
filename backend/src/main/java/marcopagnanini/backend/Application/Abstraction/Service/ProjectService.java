package marcopagnanini.backend.Application.Abstraction.Service;

import marcopagnanini.backend.Model.Project;

import java.util.List;

public interface ProjectService {
    Project save(Project project);
    List<Project> findAll();
    Project findById(String id);
    Project delete(String id);
}
