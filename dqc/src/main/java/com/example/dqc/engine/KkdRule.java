package com.example.dqc.engine;

import com.example.dqc.domain.DetectedIssue;
import com.example.dqc.domain.KkdContext;

import java.util.List;

public interface KkdRule {
    String code();
    List<DetectedIssue> evaluate(KkdContext context);
}
