package com.example.dqc.api;

import com.example.dqc.api.dto.IssuesPageResponse;
import com.example.dqc.api.dto.KkdSummaryResponse;
import com.example.dqc.service.KkdIssueQueryService;
import com.example.dqc.service.KkdSummaryService;
import jakarta.validation.constraints.Min;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/api/kkd")
@Validated
public class KkdController {

    private final KkdSummaryService summaryService;
    private final KkdIssueQueryService issueQueryService;

    public KkdController(
            KkdSummaryService summaryService,
            KkdIssueQueryService issueQueryService
    ) {
        this.summaryService = summaryService;
        this.issueQueryService = issueQueryService;
    }

    @GetMapping("/summary")
    public KkdSummaryResponse getSummary(
            @RequestParam(name = "run_id") Long runId,
            @RequestParam(name = "unit_id") Long unitId
    ) {
        return summaryService.getSummary(runId, unitId);
    }

    @GetMapping("/issues")
    public IssuesPageResponse getIssues(
            @RequestParam(name = "run_id") Long runId,
            @RequestParam(name = "unit_id", required = false) Long unitId,
            @RequestParam(name = "rule_code", required = false) String ruleCode,
            @RequestParam(required = false) String severity,
            @RequestParam(defaultValue = "20") @Min(1) int limit,
            @RequestParam(defaultValue = "0") @Min(0) int offset
    ) {
        return issueQueryService.getIssues(
                runId,
                unitId,
                ruleCode,
                severity,
                limit,
                offset
        );
    }
}
