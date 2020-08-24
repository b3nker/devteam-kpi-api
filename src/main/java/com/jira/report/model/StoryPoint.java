package com.jira.report.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class StoryPoint {
    private double total;
    private double aQualifier;
    private double bacAffinage;
    private double enAttente;
    private double aFaire;
    private double enCours;
    private double abandonne;
    private double devTermine;
    private double aValider;
    private double aLivrer;
    private double aTester;
    private double refuseEnRecette;
    private double valideEnRecette;
    private double livre;
    private double termine;
    private double testCroise;
    private double valide;
    private double mergeRequest;
}
