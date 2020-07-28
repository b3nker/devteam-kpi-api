package com.jiraReportTest.jiraReportTest.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Retrospective{
    private String teamName;
    private SprintCommitment[] sprints;
}
