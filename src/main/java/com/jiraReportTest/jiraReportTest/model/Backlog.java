package com.jiraReportTest.jiraReportTest.model;

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
    private int nbIncidents;
    private int nbIncidentsLow;
    private int nbIncidentsMedium;
    private int nbIncidentsHigh;
    private int nbIncidentsHighest;
    private int[] nbIncidentsCreated;
    private int[] nbIncidentsResolved;
    private int[] nbIncidentsInProgress;
    private int[] nbBugsCreated;
    private int[] nbBugsResolved;
    private int[] nbBugsInProgress;

}
