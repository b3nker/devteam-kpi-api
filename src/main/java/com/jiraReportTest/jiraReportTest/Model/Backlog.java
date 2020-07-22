package com.jiraReportTest.jiraReportTest.Model;


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
    private int nbIncidentsCreated [];
    private int nbIncidentsResolved [];
    private int nbIncidentsInProgress[];
    private int nbBugsCreated [];
    private int nbBugsResolved [];
    private int nbBugsInProgress[];

    public Backlog() { }

    public int[] getNbIncidentsInProgress() {
        return nbIncidentsInProgress;
    }

    public void setNbIncidentsInProgress(int[] nbIncidentsInProgress) {
        this.nbIncidentsInProgress = nbIncidentsInProgress;
    }

    public int[] getNbBugsInProgress() {
        return nbBugsInProgress;
    }

    public void setNbBugsInProgress(int[] nbBugsInProgress) {
        this.nbBugsInProgress = nbBugsInProgress;
    }

    public int[] getNbIncidentsCreated() {
        return nbIncidentsCreated;
    }

    public void setNbIncidentsCreated(int[] nbIncidentsCreated) {
        this.nbIncidentsCreated = nbIncidentsCreated;
    }

    public int[] getNbIncidentsResolved() {
        return nbIncidentsResolved;
    }

    public void setNbIncidentsResolved(int[] nbIncidentsResolved) {
        this.nbIncidentsResolved = nbIncidentsResolved;
    }

    public int getNbIncidents() {
        return nbIncidents;
    }

    public void setNbIncidents(int nbIncidents) {
        this.nbIncidents = nbIncidents;
    }

    public int getNbIncidentsLow() {
        return nbIncidentsLow;
    }

    public void setNbIncidentsLow(int nbIncidentsLow) {
        this.nbIncidentsLow = nbIncidentsLow;
    }

    public int getNbIncidentsMedium() {
        return nbIncidentsMedium;
    }

    public void setNbIncidentsMedium(int nbIncidentsMedium) {
        this.nbIncidentsMedium = nbIncidentsMedium;
    }

    public int getNbIncidentsHigh() {
        return nbIncidentsHigh;
    }

    public void setNbIncidentsHigh(int nbIncidentsHigh) {
        this.nbIncidentsHigh = nbIncidentsHigh;
    }

    public int getNbIncidentsHighest() {
        return nbIncidentsHighest;
    }

    public void setNbIncidentsHighest(int nbIncidentsHighest) {
        this.nbIncidentsHighest = nbIncidentsHighest;
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
