package com.example.dqc.engine;

import com.example.dqc.dto.DetectedIssue;
import com.example.dqc.dto.KkdContext;

import java.util.List;

public interface KkdRule {
    String code();
    List<DetectedIssue> evaluate(KkdContext context);
}
