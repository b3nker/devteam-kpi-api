package com.jiraReportTest.jiraReportTest.Model;

public class Backlog {
    private String projectName;
    private int nbBugs;
    private int nbBugsWVEC;
    private int nbBugsLow;
    private int nbBugsMedium;
    private int nbBugsHigh;
    private int nbBugsHighest;
    private int nbBugsLowWVEC;
    private int nbBugsMediumWVEC;
    private int nbBugsHighWVEC;
    private int nbBugsHighestWVEC;
    private int nbBugsCreated [];
    private int nbBugsResolved [];

    public Backlog() {
    }

    public int getNbBugsWVEC() {
        return nbBugsWVEC;
    }

    public void setNbBugsWVEC(int nbBugsWVEC) {
        this.nbBugsWVEC = nbBugsWVEC;
    }

    public int getNbBugsLowWVEC() {
        return nbBugsLowWVEC;
    }

    public void setNbBugsLowWVEC(int nbBugsLowWVEC) {
        this.nbBugsLowWVEC = nbBugsLowWVEC;
    }

    public int getNbBugsMediumWVEC() {
        return nbBugsMediumWVEC;
    }

    public void setNbBugsMediumWVEC(int nbBugsMediumWVEC) {
        this.nbBugsMediumWVEC = nbBugsMediumWVEC;
    }

    public int getNbBugsHighWVEC() {
        return nbBugsHighWVEC;
    }

    public void setNbBugsHighWVEC(int nbBugsHighWVEC) {
        this.nbBugsHighWVEC = nbBugsHighWVEC;
    }

    public int getNbBugsHighestWVEC() {
        return nbBugsHighestWVEC;
    }

    public void setNbBugsHighestWVEC(int nbBugsHighestWVEC) {
        this.nbBugsHighestWVEC = nbBugsHighestWVEC;
    }

    public int[] getNbBugsCreated() {
        return nbBugsCreated;
    }

    public void setNbBugsCreated(int[] nbBugsCreated) {
        this.nbBugsCreated = nbBugsCreated;
    }

    public int[] getNbBugsResolved() {
        return nbBugsResolved;
    }

    public void setNbBugsResolved(int[] nbBugsResolved) {
        this.nbBugsResolved = nbBugsResolved;
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
