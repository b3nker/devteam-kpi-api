package com.jiraReportTest.jiraReportTest.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SprintCommitment {
    private int id;
    private String name;
    private double initialCommitment;
    private double finalCommitment;
    private double addedWork;
    private double completedWork;

}
