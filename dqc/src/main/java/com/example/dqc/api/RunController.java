package com.example.dqc.api;

import com.example.dqc.api.dto.RunResponse;
import com.example.dqc.domain.KkdRunResult;
import com.example.dqc.domain.StartKkdRunRequest;
import com.example.dqc.service.KkdRunService;
import com.example.dqc.service.RunQueryService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/runs")
public class RunController {

    private final RunQueryService runQueryService;
    private final KkdRunService runService;

    public RunController(RunQueryService runQueryService, KkdRunService runService) {
        this.runQueryService = runQueryService;
        this.runService = runService;
    }

    @GetMapping
    public List<RunResponse> getRuns() {
        return runQueryService.getRuns();
    }

    @PostMapping
    public KkdRunResult startRun(@RequestBody StartKkdRunRequest request) {
        return runService.startRun(request);
    }
}
