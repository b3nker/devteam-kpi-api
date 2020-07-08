package com.jiraReportTest.jiraReportTest.Model;

import java.util.List;

public class Team {
    private String name;
    private List<Collaborator> collaborators;


    public Team(String name, List<Collaborator> collaborators) {
        this.name = name;
        this.collaborators = collaborators;
    }

    public Team(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public List<Collaborator> getCollaborators() {
        return collaborators;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCollaborators(List<Collaborator> collaborators) {
        this.collaborators = collaborators;
    }
}
