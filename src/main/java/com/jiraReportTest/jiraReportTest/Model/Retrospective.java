package com.jiraReportTest.jiraReportTest.Model;

public class Retrospective{
    private String teamName;
    private Sprint[] sprints;

    public Retrospective() {}

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public Sprint[] getSprints() {
        return sprints;
    }

    public void setSprints(Sprint[] sprints) {
        this.sprints = sprints;
    }
}
