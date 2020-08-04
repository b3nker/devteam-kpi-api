package com.jira.report.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@ConfigurationProperties("jira.report.query")
@Data
public class JiraReportConfigQuery {
    private int maxResults;
    private String unassignedAccountId;
    private String bug;
    private String incident;
    private String priorityLow;
    private String priorityMedium;
    private String priorityHigh;
    private String priorityHighest;
    private List<String> statuses;
    private List<String> done;
    private List<String> doneBugs;
    private List<String> inProgress;
    private List<String> devDone;
    private List<String> devDoneEnCours;
    private String abandonne;
    private String enCours;
    private String aQualifier;
    private String aFaire;
    private String bacAffinage;
    private String termine;
    private String livre;
    private String valideEnRecette;
    private String valide;
    private String devTermine;
    private String refuseEnRecette;
    private String enAttente;
    private String aLivrer;
    private String aTester;
    private String aValider;
    private String testCroise;
    private String active;

}
