package marcopagnanini.backend.Infrastructure;

import lombok.RequiredArgsConstructor;
import marcopagnanini.backend.Application.Abstraction.Repository.LogRepository;
import marcopagnanini.backend.Model.Log;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

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
    public Page<Log> findByProjectId(String projectId, Pageable pageable) {
        Query query = new Query().with(pageable);
        String collection = getCollectionName(projectId);
        return PageableExecutionUtils.getPage(
                mongoTemplate.find(query, Log.class, collection),
                pageable,
                () -> mongoTemplate.count(new Query(), Log.class, collection)
        );
    }

    @Override
    public Log findById(String id, String projectId) {
        return mongoTemplate.findById(id, Log.class, getCollectionName(projectId));
    }

    @Override
    public Log delete(String id, String projectId) {
        Query query = new Query(Criteria.where("_id").is(id));
        return mongoTemplate.findAndRemove(query, Log.class, getCollectionName(projectId));
    }

    @Override
    public Log update(Log log, String projectId) {
        return mongoTemplate.save(log, getCollectionName(projectId));
    }
}
