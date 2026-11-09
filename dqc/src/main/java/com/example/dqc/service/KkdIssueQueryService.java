package com.example.dqc.service;

import com.example.dqc.api.dto.IssueResponse;
import com.example.dqc.api.dto.IssuesPageResponse;
import com.example.dqc.model.KkdIssue;
import com.example.dqc.repository.DepartmentRepository;
import com.example.dqc.repository.KkdIssueRepository;
import com.example.dqc.repository.KkdRunRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
public class KkdIssueQueryService {

    private final KkdRunRepository runRepository;
    private final KkdIssueRepository issueRepository;
    private final KkdIssueScopeService issueScopeService;
    private final DepartmentRepository departmentRepository;

    public KkdIssueQueryService(KkdRunRepository runRepository, KkdIssueRepository issueRepository, KkdIssueScopeService issueScopeService, DepartmentRepository departmentRepository) {
        this.runRepository = runRepository;
        this.issueRepository = issueRepository;
        this.issueScopeService = issueScopeService;
        this.departmentRepository = departmentRepository;
    }

    @Transactional(readOnly = true)
    public IssuesPageResponse getIssues(
            Long runId,
            Long unitId,
            String ruleCode,
            String severity,
            int limit,
            int offset
    ) {
        validatePagination(limit, offset);
        validateSeverity(severity);

        if (!runRepository.existsById(runId)) {
            throw new EntityNotFoundException("Прогон не найден");
        }

        if (unitId != null && !departmentRepository.existsById(unitId)) {
            throw new EntityNotFoundException("Подразделение не найдено");
        }

        Pageable pageable = PageRequest.of(offset / limit, limit);
        Page<KkdIssue> page = issueRepository.findIssuesPaginated(
                runId,
                unitId,
                ruleCode,
                severity == null ? null : severity.toUpperCase(),
                pageable
        );

        Map<Long, String> ruleCodes = issueScopeService.getRuleCodes();

        List<IssueResponse> responses = page.getContent().stream()
                .map(issue -> toResponse(issue, ruleCodes))
                .toList();

        return new IssuesPageResponse(
                responses,
                limit,
                offset,
                page.getTotalElements()
        );
    }

    private IssueResponse toResponse(KkdIssue issue, Map<Long, String> ruleCodes) {
        boolean goalIssue = issue.getGoalId() != null;

        return new IssueResponse(
                issue.getId(),
                ruleCodes.get(issue.getRuleId()),
                issue.getSeverity(),
                goalIssue ? "GOAL" : "KEY_RESULT",
                goalIssue ? issue.getGoalId() : issue.getKeyResultId(),
                issue.getDescription(),
                issue.getCreatedAt()
        );
    }

    private void validatePagination(int limit, int offset) {
        if (limit < 1 || limit > 100) {
            throw new IllegalArgumentException("limit должен быть от 1 до 100");
        }
        if (offset < 0) {
            throw new IllegalArgumentException("offset не может быть отрицательным");
        }
    }

    private void validateSeverity(String severity) {
        if (severity == null) return;

        boolean valid = List.of("CRITICAL", "MAJOR", "MINOR").contains(severity.toUpperCase());
        if (!valid) {
            throw new IllegalArgumentException("Недопустимое значение severity");
        }
    }
}