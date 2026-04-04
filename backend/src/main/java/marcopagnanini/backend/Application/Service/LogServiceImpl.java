package marcopagnanini.backend.Application.Service;

import lombok.RequiredArgsConstructor;
import marcopagnanini.backend.Application.Abstraction.Repository.LogRepository;
import marcopagnanini.backend.Application.Abstraction.Service.LogService;
import marcopagnanini.backend.Model.Log;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LogServiceImpl implements LogService {

    private final LogRepository logRepository;

    @Override
    public Log save(Log log, String projectId) {
        log.setProjectId(projectId);
        return logRepository.save(log, projectId);
    }

    @Override
    public Log findById(String id, String projectId) {
        return logRepository.findById(id, projectId);
    }

    @Override
    public Page<Log> findAll(String projectId, Pageable pageable) {
        return logRepository.findByProjectId(projectId, pageable);
    }

    @Override
    public Log delete(String id, String projectId) {
        return logRepository.delete(id, projectId);
    }

    @Override
    public Log update(Log log, String projectId) {
        return logRepository.update(log, projectId);
    }
}
