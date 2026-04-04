package marcopagnanini.backend.Infrastructure;

import lombok.RequiredArgsConstructor;
import marcopagnanini.backend.Application.Abstraction.Repository.ProjectRepository;
import marcopagnanini.backend.Model.Project;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ProjectRepositoryImpl implements ProjectRepository {

    private final MongoTemplate mongoTemplate;

    @Override
    public Project save(Project project) {
        return mongoTemplate.save(project);
    }

    @Override
    public List<Project> findAll() {
        return mongoTemplate.findAll(Project.class);
    }

    @Override
    public Project findById(String id) {
        return mongoTemplate.findById(id, Project.class);
    }

    @Override
    public Project delete(String id) {
        Query query = new Query(Criteria.where("_id").is(id));
        return mongoTemplate.findAndRemove(query, Project.class);
    }
}
