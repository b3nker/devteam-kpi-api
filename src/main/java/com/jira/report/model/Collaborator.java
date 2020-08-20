package com.jira.report.model;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class Collaborator {
    private String accountId;
    private String firstName;
    private String name;
    private String emailAddress;
    private double totalWorkingTime;
    private double availableTime;
    private double estimatedTime;
    private double loggedTime;
    private double remainingTime;
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
    private double spTestCroise;
    private double spMergeRequest;
    private int ticketsTotal;
    private int ticketsAqualifier;
    private int ticketsBacAffinage;
    private int ticketsEnAttente;
    private int ticketsAfaire;
    private int ticketsEncours;
    private int ticketsAbandonne;
    private int ticketsDevTermine;
    private int ticketsAvalider;
    private int ticketsAlivrer;
    private int ticketsATester;
    private int ticketsRefuseEnRecette;
    private int ticketsValideEnRecette;
    private int ticketsLivre;
    private int ticketsTermine;
    private int ticketsTestCroise;
    private int ticketsValide;
    private int ticketsMergeRequest;
    private String role;
    private List<String> assignedIssues;


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

