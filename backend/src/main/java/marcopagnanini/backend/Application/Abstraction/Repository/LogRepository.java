package marcopagnanini.backend.Application.Abstraction.Repository;

import marcopagnanini.backend.Model.Log;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface LogRepository {
    Log save(Log log, String projectId);
    Page<Log> findByProjectId(String projectId, Pageable pageable);
    Log findById(String id, String projectId);
    Log delete(String id, String projectId);
    Log update(Log log, String projectId);
}
