package com.jira.report.model.entity;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@Getter
@Document(collection = "story_point")
public class StoryPointEntity {
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
