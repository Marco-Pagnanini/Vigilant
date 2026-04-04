package marcopagnanini.backend.Infrastructure;

import lombok.RequiredArgsConstructor;
import marcopagnanini.backend.Application.Repository.LogRepository;
import marcopagnanini.backend.Model.Log;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class LogRepositoryImpl implements LogRepository {

    private final MongoTemplate mongoTemplate;

    private String getCollectionName(String projectId) {
        return "logs_" + projectId;
    }

    @Override
    public Log save(Log log, String projectId) {
        return mongoTemplate.save(log, getCollectionName(projectId));
    }

    @Override
    public List<Log> findAll(String projectId) {
        return mongoTemplate.findAll(Log.class, getCollectionName(projectId));
    }

    @Override
    public Log findById(String id) {
        return mongoTemplate.findById(id, Log.class);
    }

    @Override
    public Log delete(String id) {
        Log log = mongoTemplate.findById(id, Log.class);
        assert log != null;
        mongoTemplate.remove(log);
        return log;
    }

    @Override
    public Log update(Log log, String projectId) {
        return null;
    }
}
