package com.jiraReportTest.jiraReportTest.Model;

public class Collaborator {
    private String accountId;
    private String firstName;
    private String name;
    private String emailAddress;
    private double velocity;
    private double totalWorkingTime;
    private double availableTime;
    private double estimatedTime;
    private double loggedTime;
    private double remainingTime;
    private int nbTickets, nbDone, nbDevDone, nbInProgress, nbToDo, nbEnCoursDevTermine, nbATester;
    private double spTotal;
    private double spAqualifier;
    private double spBacAffinage;
    private double spEnAttente;
    private double spAfaire;
    private double spEncours;
    private double spAbandonne;
    private double spDevTermine;
    private double spAvalider;
    private double spAlivrer;
    private double spATester;
    private double spRefuseEnRecette;
    private double spValideEnRecette;
    private double spLivre;
    private double spTermine;
    private String role;


    public Collaborator() {
    }

    public int getNbEnCoursDevTermine() {
        return nbEnCoursDevTermine;
    }

    public void setNbEnCoursDevTermine(int nbEnCoursDevTermine) {
        this.nbEnCoursDevTermine = nbEnCoursDevTermine;
    }

    public int getNbATester() {
        return nbATester;
    }

    public void setNbATester(int nbATester) {
        this.nbATester = nbATester;
    }

    public double getSpTermine() {
        return spTermine;
    }

    public void setSpTermine(double spTermine) {
        this.spTermine = spTermine;
    }

    public void setSpAqualifier(double spAqualifier) {
        this.spAqualifier = spAqualifier;
    }

    public double getSpAqualifier() {
        return spAqualifier;
    }

    public double getSpBacAffinage() {
        return spBacAffinage;
    }

    public double getSpEnAttente() {
        return spEnAttente;
    }

    public double getSpAfaire() {
        return spAfaire;
    }

    public double getSpEncours() {
        return spEncours;
    }

    public double getSpAbandonne() {
        return spAbandonne;
    }

    public double getSpDevTermine() {
        return spDevTermine;
    }

    public double getSpAvalider() {
        return spAvalider;
    }

    public double getSpAlivrer() {
        return spAlivrer;
    }

    public double getSpATester() {
        return spATester;
    }

    public double getSpRefuseEnRecette() {
        return spRefuseEnRecette;
    }

    public double getSpValideEnRecette() {
        return spValideEnRecette;
    }

    public double getSpLivre() {
        return spLivre;
    }

    public void setSpBacAffinage(double spBacAffinage) {
        this.spBacAffinage = spBacAffinage;
    }

    public void setSpEnAttente(double spEnAttente) {
        this.spEnAttente = spEnAttente;
    }

    public void setSpAfaire(double spAfaire) {
        this.spAfaire = spAfaire;
    }

    public void setSpEncours(double spEncours) {
        this.spEncours = spEncours;
    }

    public void setSpAbandonne(double spAbandonne) {
        this.spAbandonne = spAbandonne;
    }

    public void setSpDevTermine(double spDevTermine) {
        this.spDevTermine = spDevTermine;
    }

    public void setSpAvalider(double spAvalider) {
        this.spAvalider = spAvalider;
    }

    public void setSpAlivrer(double spAlivrer) {
        this.spAlivrer = spAlivrer;
    }

    public void setSpATester(double spATester) {
        this.spATester = spATester;
    }

    public void setSpRefuseEnRecette(double spRefuseEnRecette) {
        this.spRefuseEnRecette = spRefuseEnRecette;
    }

    public void setSpValideEnRecette(double spValideEnRecette) {
        this.spValideEnRecette = spValideEnRecette;
    }

    public void setSpLivre(double spLivre) {
        this.spLivre = spLivre;
    }

    public int getNbDevDone() {
        return nbDevDone;
    }

    public void setNbDevDone(int nbDevDone) {
        this.nbDevDone = nbDevDone;
    }


    public double getAvailableTime() {
        return availableTime;
    }

    public void setAvailableTime(double availableTime) {
        this.availableTime = availableTime;
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

    public double getTotalWorkingTime() {
        return totalWorkingTime;
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
        firstName = firstName.substring(0,1).toUpperCase() + firstName.substring(1).toLowerCase();
        this.firstName = firstName;
    }

    public void setName(String name) {
        if(name.length() >0){
            name = name.substring(0,1).toUpperCase() + name.substring(1).toLowerCase();
        }
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

    public void setTotalWorkingTime(double totalWorkingTime) {
        this.totalWorkingTime = totalWorkingTime;
    }

    public void setRemainingTime(double remainingTime) {
        this.remainingTime = remainingTime;
    }

    public void setSpTotal(double spTotal) {
        this.spTotal = spTotal;
    }

    public double getSpTotal() {
        return spTotal;
    }



    public String toString() {
        return ("accountId: " + this.accountId + ", prénom: " + this.firstName + ", nom: " + this.name + ", adresseMail: "
                + this.emailAddress + ", nbTickets: " + this.nbTickets + ", estimatedTime: " + this.estimatedTime + ", loggedTime: "
                + this.loggedTime + ", tickets Done: " + this.nbDone + ", tickets InProgress: " + nbInProgress + ", tickets ToDo: "
                + this.nbToDo+ ", role:" + this.role + "spALivrer: " + this.spAlivrer);
    }
}

