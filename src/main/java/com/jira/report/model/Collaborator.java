package com.jira.report.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
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
    private int nbTickets;
    private int nbDone;
    private int nbDevDone;
    private int nbInProgress;
    private int nbToDo;
    private int nbEnCoursDevTermine;
    private int nbATester;
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
}

