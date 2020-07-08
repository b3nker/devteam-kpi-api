package com.jiraReportTest.jiraReportTest.Model;

public class Collaborator {
    private String accountId;
    private String firstName;
    private String name;
    private String emailAddress;
    private double velocity;
    private double workedTime;
    private double estimatedTime;
    private double loggedTime;
    private double remainingTime;
    private int nbTickets, nbDone, nbInProgress, nbToDo;
    private double spTotal, spDone, spInProgress, spToDo;
    private String role;

    public Collaborator() {
    }

    public Collaborator(String accountId) {
        this.accountId = accountId;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public double getWorkedTime() {
        return workedTime;
    }

    public double getRemainingTime() {
        return remainingTime;
    }

    public String getAccountId() {
        return accountId;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getName() {
        return name;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public double getVelocity() {
        return velocity;
    }

    public double getEstimatedTime() {
        return estimatedTime;
    }

    public double getLoggedTime() {
        return loggedTime;
    }

    public int getNbTickets() {
        return nbTickets;
    }

    public int getNbDone() {
        return nbDone;
    }

    public int getNbInProgress() {
        return nbInProgress;
    }

    public int getNbToDo() {
        return nbToDo;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public void setVelocity(double velocity) {
        this.velocity = velocity;
    }

    public void setEstimatedTime(double estimatedTime) {
        this.estimatedTime = estimatedTime;
    }

    public void setLoggedTime(double loggedTime) {
        this.loggedTime = loggedTime;
    }

    public void setNbTickets(int nbTickets) {
        this.nbTickets = nbTickets;
    }

    public void setNbDone(int nbDone) {
        this.nbDone = nbDone;
    }

    public void setNbInProgress(int nbInProgress) {
        this.nbInProgress = nbInProgress;
    }

    public void setNbToDo(int nbToDo) {
        this.nbToDo = nbToDo;
    }

    public void setWorkedTime(double workedTime) {
        this.workedTime = workedTime;
    }

    public void setRemainingTime(double remainingTime) {
        this.remainingTime = remainingTime;
    }

    public void setSpTotal(double spTotal) {
        this.spTotal = spTotal;
    }

    public void setSpDone(double spDone) {
        this.spDone = spDone;
    }

    public void setSpInProgress(double spInProgress) {
        this.spInProgress = spInProgress;
    }

    public void setSpToDo(double spToDo) {
        this.spToDo = spToDo;
    }

    public double getSpTotal() {
        return spTotal;
    }

    public double getSpDone() {
        return spDone;
    }

    public double getSpInProgress() {
        return spInProgress;
    }

    public double getSpToDo() {
        return spToDo;
    }

    public String toString() {
        return ("accountId: " + this.accountId + ", prénom: " + this.firstName + ", nom: " + this.name + ", adresseMail: "
                + this.emailAddress + ", nbTickets: " + this.nbTickets + ", estimatedTime: " + this.estimatedTime + ", loggedTime: "
                + this.loggedTime + ", tickets Done: " + this.nbDone + ", tickets InProgress: " + nbInProgress + ", tickets ToDo: "
                + this.nbToDo+ ", role:" + this.role);
    }
}

