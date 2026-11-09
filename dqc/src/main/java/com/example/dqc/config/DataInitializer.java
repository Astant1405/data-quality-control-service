package com.example.dqc.config;

import com.example.dqc.domain.KkdRunResult;
import com.example.dqc.domain.StartKkdRunRequest;
import com.example.dqc.repository.KkdRunRepository;
import com.example.dqc.service.KkdRunService;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;

import java.time.LocalDate;

@Component
public class DataInitializer implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);

    private final KkdRunService runService;
    private final KkdRunRepository runRepository;

    public DataInitializer(KkdRunService runService, KkdRunRepository runRepository) {
        this.runService = runService;
        this.runRepository = runRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        try{
            if (runRepository.count() == 0) {

                KkdRunResult run1 = runService.startRun(new StartKkdRunRequest(
                        "RUN-001",
                        LocalDate.of(2026, 11, 6),
                        null
                ));

                log.info("=== Создан прогон RUN-001 с ID: {} ===", run1.runId());

                KkdRunResult run2 = runService.startRun(new StartKkdRunRequest(
                        "RUN-002",
                        LocalDate.of(2026, 11, 7),
                        "RUN-001"
                ));

                log.info("=== Создан прогон RUN-002 с ID: {} ===", run2.runId());
            }
        }
        catch (Exception ignored){

        }
    }
}
