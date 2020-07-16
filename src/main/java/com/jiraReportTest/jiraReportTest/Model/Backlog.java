package com.jiraReportTest.jiraReportTest.Model;

public class Backlog {
    private String projectName;
    private int nbBugs;
    private int nbBugsLow;
    private int nbBugsMedium;
    private int nbBugsHigh;
    private int nbBugsHighest;

    public Backlog() {
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public int getNbBugs() {
        return nbBugs;
    }

    public void setNbBugs(int nbBugs) {
        this.nbBugs = nbBugs;
    }

    public int getNbBugsLow() {
        return nbBugsLow;
    }

    public void setNbBugsLow(int nbBugsLow) {
        this.nbBugsLow = nbBugsLow;
    }

    public int getNbBugsMedium() {
        return nbBugsMedium;
    }

    public void setNbBugsMedium(int nbBugsMedium) {
        this.nbBugsMedium = nbBugsMedium;
    }

    public int getNbBugsHigh() {
        return nbBugsHigh;
    }

    public void setNbBugsHigh(int nbBugsHigh) {
        this.nbBugsHigh = nbBugsHigh;
    }

    public int getNbBugsHighest() {
        return nbBugsHighest;
    }

    public void setNbBugsHighest(int nbBugsHighest) {
        this.nbBugsHighest = nbBugsHighest;
    }
}
