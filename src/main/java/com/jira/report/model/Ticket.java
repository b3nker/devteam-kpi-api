package com.jira.report.model;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class Ticket {
    private int total;
    private int aQualifier;
    private int bacAffinage;
    private int enAttente;
    private int aFaire;
    private int enCours;
    private int abandonne;
    private int devTermine;
    private int aValider;
    private int aLivrer;
    private int aTester;
    private int refuseEnRecette;
    private int valideEnRecette;
    private int livre;
    private int termine;
    private int testCroise;
    private int valide;
    private int mergeRequest;
    private int overEstimated;
    private int underEstimated;
}
