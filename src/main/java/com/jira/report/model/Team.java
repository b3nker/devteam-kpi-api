package com.jira.report.model;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class Team {
    private String name;
    private List<Collaborator> collaborators;
}
