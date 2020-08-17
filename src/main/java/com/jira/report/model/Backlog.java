package com.jira.report.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Backlog {
    private String projectName;
    private int nbBugs;
    private int nbBugsLow;
    private int nbBugsMedium;
    private int nbBugsHigh;
    private int nbBugsHighest;
    private int[] nbBugsCreated;
    private int[] nbBugsResolved;
}
