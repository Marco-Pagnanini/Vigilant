package marcopagnanini.backend.Application.Abstraction.Service;

import marcopagnanini.backend.Model.Log;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface LogService {
    Log save(Log log, String projectId);
    Log findById(String id, String projectId);
    Page<Log> findAll(String projectId, Pageable pageable);
    Log delete(String id, String projectId);
    Log update(Log log, String projectId);
}
