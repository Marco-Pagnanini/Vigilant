package marcopagnanini.backend.Application.Abstraction.Repository;

import marcopagnanini.backend.Model.Project;

import java.util.List;

public interface ProjectRepository {
    Project save(Project project);
    List<Project> findAll();
    Project findById(String id);
    Project delete(String id);
}
