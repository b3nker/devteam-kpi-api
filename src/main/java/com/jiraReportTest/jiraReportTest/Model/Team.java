package com.jiraReportTest.jiraReportTest.Model;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class Team {
    private String name;
    private List<Collaborator> collaborators;
}
