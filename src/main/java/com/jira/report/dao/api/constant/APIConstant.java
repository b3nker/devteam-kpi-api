package com.jira.report.dao.api.constant;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class APIConstant {

    private APIConstant() {
    }

    public static final String JQL_SPRINT = "+AND+sprint=";
    public static final String JQL_LABELS = "+AND+labels=";

    // JIRA STATUS & CUSTOM STATUS
    public static final String A_QUALIFIER = "A qualifier";
    public static final String A_FAIRE = "A Faire";
    public static final String BAC_AFFINAGE = "Bac d'affinage";
    public static final String TERMINE = "Terminé";
    public static final String LIVRE = "Livré";
    public static final String VALIDE_RECETTE = "Validé en recette";
    public static final String VALIDE = "Validé";
    public static final String ABANDONNE = "Abandonné";
    public static final String EN_COURS = "En cours";
    public static final String DEV_TERMINE = "Dév terminé";
    public static final String REFUSE_RECETTE = "Refusé en recette";
    public static final String EN_ATTENTE = "En attente";
    public static final String A_LIVRER = "A Livrer";
    public static final String A_TESTER = "A tester";
    public static final String A_VALIDER = "A valider";
    public static final String TEST_CROISE = "Test croisé";
    public static final String MERGE_REQUEST = "Merge Request";
    //Unassigned infos
    public static final String UNASSIGNED_ROLE = "none";
    public static final String UNASSIGNED_FIRST_NAME = "Non";
    public static final String UNASSIGNED_LAST_NAME = "Assigné";
    public static final String UNASSIGNED_NAME = "Assigné";
    //Priority
    public static final String PRIORITY_LOW = "Low";
    public static final String PRIORITY_MEDIUM = "Medium";
    public static final String PRIORITY_HIGH = "High";
    public static final String PRIORITY_HIGHEST = "Highest";
    public static final List<String> BUGS_DONE = new ArrayList<>(Arrays.asList(LIVRE, TERMINE, VALIDE_RECETTE, ABANDONNE));
    public static final List<String> AT_LEAST_DEV_DONE = new ArrayList<>(Arrays.asList(DEV_TERMINE, TEST_CROISE,
            MERGE_REQUEST, A_LIVRER, A_TESTER, A_VALIDER, VALIDE_RECETTE, VALIDE, LIVRE, TERMINE, ABANDONNE));
    //Queries URI
    public static final String SEARCH_JQL_PROJECT = "search?jql=project=";
    public static final String JQL_ASSIGNEE = "+AND+assignee=";
    public static final String JQL_ISSUE_TYPE = "+AND+issuetype=";
    public static final String JQL_MAX_RESULTS = "&maxResults=";
    public static final String JQL_START_AT = "&startAt=";
    //Settings
    public static final double LOWER_BOUND_MULTIPLIER = 0.8;
    public static final double UPPER_BOUND_MULTIPLIER = 1.2;
    public static final String JQL_ISSUE_TYPE_TASK = "Task";
    public static final String JQL_ISSUE_TYPE_US = "User Story";
    public static final String JQL_ISSUE_TYPE_BUG = "Bug";
    public static final String JQL_ISSUE_TYPE_SUBTASK = "Sous-tâche";
    public static final DateTimeFormatter dtfSmallEurope = DateTimeFormatter.ofPattern("dd/MM/yy");
    public static final DateTimeFormatter dtfAmerica = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    public static final LocalDateTime TODAY = LocalDateTime.now();
}
