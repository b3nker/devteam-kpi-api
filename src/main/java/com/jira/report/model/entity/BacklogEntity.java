package com.jira.report.model.entity;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@Getter
@Document(collection = "backlog")
public class BacklogEntity {
    private String projectName;
    private int nbBugs;
    private int nbBugsLow;
    private int nbBugsMedium;
    private int nbBugsHigh;
    private int nbBugsHighest;
    private int[] nbBugsCreated;
    private int[] nbBugsResolved;
}
