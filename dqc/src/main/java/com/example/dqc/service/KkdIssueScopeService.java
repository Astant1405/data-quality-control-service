package com.example.dqc.service;

import com.example.dqc.model.KkdIssue;
import com.example.dqc.model.KkdRule;
import com.example.dqc.repository.KkdIssueRepository;
import com.example.dqc.repository.KkdRuleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class KkdIssueScopeService {

    private final KkdIssueRepository issueRepository;
    private final KkdRuleRepository ruleRepository;

    public KkdIssueScopeService(KkdIssueRepository issueRepository, KkdRuleRepository ruleRepository) {
        this.issueRepository = issueRepository;
        this.ruleRepository = ruleRepository;
    }

    @Transactional(readOnly = true)
    public List<KkdIssue> findIssues(Long runId, Set<Long> unitIds) {
        if (unitIds == null || unitIds.isEmpty()) {
            return issueRepository.findAllByRunId(runId);
        }

        return issueRepository.findIssuesByRunIdAndUnitIds(
                runId,
                unitIds.stream().toList()
        );
    }

    @Transactional(readOnly = true)
    public Map<Long, String> getRuleCodes() {
        return ruleRepository.findAll()
                .stream()
                .collect(Collectors.toMap(
                        KkdRule::getId,
                        KkdRule::getCode
                ));
    }
}