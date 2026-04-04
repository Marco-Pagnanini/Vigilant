package marcopagnanini.backend.Application.Repository;

import marcopagnanini.backend.Model.Log;

import java.util.List;

public interface LogRepository {
    Log save(Log log, String projectId);
    List<Log> findAll(String projectId);
    Log findById(String id);
    Log delete(String id);
    Log update(Log log, String projectId);


}
