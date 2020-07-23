package com.jiraReportTest.jiraReportTest.Dao;

import com.jiraReportTest.jiraReportTest.Model.*;
import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import kong.unirest.json.JSONArray;
import kong.unirest.json.JSONObject;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.net.URLEncoder;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static java.lang.Float.parseFloat;
import static java.lang.Integer.parseInt;
import static java.time.temporal.ChronoUnit.DAYS;

@Repository
public class JiraAPI {
    /*
    DEBUT - Déclaration et définition des variables globales
     */
    final static DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yy");
    final static DateTimeFormatter dtfLocalDate = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    final static String USERNAME = "benjamin.kermani@neo9.fr";
    final static String API_TOKEN = "sqjFnTAVspNM4NxLd1QZC5CB";
    final static String PLANNING_PATH = "planning.csv";
    // Project global variables
    final static String BOARD_ID = "391";
    final static String BOARD_ID_ALPHA_SP = "451";
    final static String BOARD_ID_BETA_SP = "443";
    final static String PROJECT_NAME = "BMKP";
    final static String RUN_PROJECT_NAME = "RMKP";
    final static int MAX_RESULTS = 100;
    final static int NB_SPRINTS_RETROSPECTIVE = 4;
    final static int NB_DAYS_BACKLOG = 20;
    final static String TEAM_NAME_ALPHA = "alpha";
    final static String TEAM_NAME_BETA = "beta";
    final static String TEAM_NAME_GAMMA = "gamma";
    final static ArrayList<String> TEAM_NAMES = new ArrayList<>(Arrays.asList(TEAM_NAME_ALPHA, TEAM_NAME_BETA, TEAM_NAME_GAMMA));
    final static String JIRA_API_URL = "https://apriltechnologies.atlassian.net/rest/api/3/";
    final static String JIRA_AGILEAPI_URL = "https://apriltechnologies.atlassian.net/rest/agile/1.0/";
    final static String JIRA_GREENHOPPER_URL = "https://apriltechnologies.atlassian.net/rest/greenhopper/1.0/";
    // JSONObject and JSONArray names in JIRA API's response
    final static String JSON_ISSUES = "issues";
    final static String JSON_FIELDS = "fields";
    final static String JSON_STATUS = "status";
    final static String JSON_PRIORITY = "priority";
    // JSONObject's keys
    final static String JSON_KEY_TIMEESTIMATE = "timeestimate";
    final static String JSON_KEY_TIMEORIGINALESTIMATE = "timeoriginalestimate";
    final static String JSON_KEY_TIMESPENT = "timespent";
    final static String JSON_KEY_STORYPOINTS = "customfield_10005";
    // Sprint variables
    final static Sprint SPRINT_ACTIF = Sprint.builder().build();

    static {
        String startDate = "";
        String endDate = "";
        String sprintName = "";
        int sprintId = 0;
        HttpResponse<JsonNode> response = Unirest.get("https://apriltechnologies.atlassian.net/rest/agile/1.0/board/" + BOARD_ID + "/sprint")
                .basicAuth(USERNAME, API_TOKEN)
                .header("Accept", "application/json")
                .asJson();
        JSONObject myObj = response.getBody().getObject();
        JSONArray values = myObj.getJSONArray("values");
        for (int i = 0; i < values.length(); i++) {
            JSONObject value = values.getJSONObject(i);
            //Condition à modifier quand il n'y aura qu'un seul sprint actif
            if (value.getString("state").equals("active")) {
                sprintName = value.getString("name");
                startDate = value.getString("startDate");
                endDate = value.getString("endDate");
                sprintId = parseInt(value.getString("id"));
            }
        }
        SPRINT_ACTIF.setId(sprintId);
        SPRINT_ACTIF.setName(sprintName);
        SPRINT_ACTIF.setStartDate(Sprint.toLocalDateTime(startDate));
        SPRINT_ACTIF.setEndDate(Sprint.toLocalDateTime(endDate));
    }
    final static String UNASSIGNED_ALPHA = "unassignedAlpha";
    final static String UNASSIGNED_BETA = "unassignedBeta";
    final static String UNASSIGNED_GAMMA = "unassignedGamma";

    final static String SPRINT_NAME = "'" + SPRINT_ACTIF.getName() + "'";
    final static String TODAY = LocalDateTime.now().format(dtf);
    final static String TODAY_LD = LocalDateTime.now().format(dtfLocalDate);
    final static ArrayList<String> TEAM_ALPHA = new ArrayList<>(Arrays.asList(
            "5c17b4599f443a65fecae3ca", // Julien Mosset
            "5a9ebe1c4af2372a88a0656b", // Nicolas Ovejero
            "5bcd8282607ed038040177bb", // Pape Thiam
            "5cf921f6b06c540e82580cbd", // Valentin Pierrel
            "5ed76cdf2fdc580b88f3bbef", // Alex Cheuko
            "5a9ebdf74af2372a88a06565", // Gabriel Roquigny
            UNASSIGNED_ALPHA

    ));
    final static ArrayList<String> TEAM_BETA = new ArrayList<>(Arrays.asList(
            "5cb45bb34064460e407eabe4", // Guillermo Garcès
            "5a2181081594706402dee482", // Etienne Bourgouin
            "5afe92f251d0b7540b43de81", // Malick Diagne
            "5d6e32e06e3e1f0d9623cb5a", // Pierre Tomasina
            "5ed64583620b1d0c168d4e36", // Anthony Hernandez
            "5ef1afd6561e0e0aae904914", // Yong Ma
            "5e98521a3a8b910c085d6a28", // Kévin Youna
            UNASSIGNED_BETA
    ));
    final static ArrayList<String> TEAM_GAMMA = new ArrayList<>(Arrays.asList(
            "5e285008ee264b0e74591993", // Eric Coupal
            "5ed76cc1be03220ab32183be", // Thibault Foucault
            "557058:87b17037-8a69-4b38-8dab-b4cf904e960a", // Pierre Thevenet
            "5d9b0573ea65c10c3fdbaab2", // Maxime Fourt
            "5a8155f0cad06b353733bae8", // Guillaume Coppens
            "5dfd11b39422830cacaa8a79", // Carthy Marie Joseph
            UNASSIGNED_GAMMA
    ));
    // JIRA STATUS
    final static String TERMINE = "Terminé";
    final static String LIVRE = "Livré";
    final static String VALIDE_RECETTE = "Validé en recette";
    final static String VALIDE = "Validé";
    final static String ABANDONNE = "Abandonné";
    final static String EN_COURS = "En cours";
    final static String DEV_TERMINE = "Dév terminé";
    final static String REFUSE_RECETTE = "Refusé en recette";
    final static String EN_ATTENTE = "En attente";
    final static String A_LIVRER = "A Livrer";
    final static String A_TESTER = "A tester";
    final static String A_VALIDER = "A valider";
    /*To define */
    final static ArrayList<String> JIRA_DONE = new ArrayList<>(Arrays.asList(ABANDONNE, LIVRE, TERMINE, VALIDE));
    final static ArrayList<String> JIRA_IN_PROGRESS = new ArrayList<>(Arrays.asList());
    final static ArrayList<String> DONE = new ArrayList<>(Arrays.asList(LIVRE, TERMINE, VALIDE, VALIDE_RECETTE));
    final static ArrayList<String> DONE_BUGS = new ArrayList<>(Arrays.asList(LIVRE, TERMINE, VALIDE_RECETTE, ABANDONNE));
    final static ArrayList<String> IN_PROGRESS = new ArrayList<>(Arrays.asList(EN_COURS, DEV_TERMINE, REFUSE_RECETTE, EN_ATTENTE, A_TESTER, A_LIVRER));
    final static ArrayList<String> DEV_DONE = new ArrayList<>(Arrays.asList(A_TESTER, A_LIVRER));
    final static ArrayList<String> DEV_DONE_EN_COURS = new ArrayList<>(Arrays.asList(DEV_TERMINE, EN_COURS));
    // JIRA ISSUETYPE
    final static String ISSUE_BUG = "Bug";
    final static String ISSUE_INCIDENT = "Incident";
    final static HashMap<String, String> ID_COLLABS = new HashMap<>();

    static {
        ID_COLLABS.put("5c17b4599f443a65fecae3ca", "middle lead dev"); // Julien Mosset
        ID_COLLABS.put("5a9ebe1c4af2372a88a0656b", "front lead dev"); // Nicolas Ovejero
        ID_COLLABS.put("5bcd8282607ed038040177bb", "middle"); // Pape Thiam
        ID_COLLABS.put("5cf921f6b06c540e82580cbd", "front"); // Valentin Pierrel
        ID_COLLABS.put("5ed76cdf2fdc580b88f3bbef", "middle"); // Alex Cheuko
        ID_COLLABS.put("5ed64583620b1d0c168d4e36", "middle"); // Anthony Hernandez
        ID_COLLABS.put("5cb45bb34064460e407eabe4", "middle lead dev"); // Guillermo Garcès
        ID_COLLABS.put("5a9ebdf74af2372a88a06565", "middle lead dev"); // Gabriel Roquigny
        ID_COLLABS.put("5a2181081594706402dee482", "front lead dev"); // Etienne Bourgouin
        ID_COLLABS.put("5afe92f251d0b7540b43de81", "middle"); // Malick Diagne
        ID_COLLABS.put("5e98521a3a8b910c085d6a28", "middle"); // Kévin Youna
        ID_COLLABS.put("5d6e32e06e3e1f0d9623cb5a", "middle"); // Pierre Tomasina
        ID_COLLABS.put("5e285008ee264b0e74591993", "middle lead dev"); // Eric Coupal
        ID_COLLABS.put("5ed76cc1be03220ab32183be", "front lead dev"); // Thibault Foucault
        ID_COLLABS.put("557058:87b17037-8a69-4b38-8dab-b4cf904e960a", "middle"); // Pierre Thevenet
        ID_COLLABS.put("5d9b0573ea65c10c3fdbaab2", "middle"); // Maxime Fourt
        ID_COLLABS.put("5a8155f0cad06b353733bae8", "middle"); // Guillaume Coppens
        ID_COLLABS.put("5dfd11b39422830cacaa8a79", "front"); // Carthy Marie Joseph
        ID_COLLABS.put("5ef1afd6561e0e0aae904914", "middle"); // Yong Ma
        ID_COLLABS.put("5aafb6012235812a6233652d", "scrum"); //Lionel Sjarbi
        ID_COLLABS.put("5ed754b0f93b230ba59a3d38", "scrum"); // Nicolas Beucler
        ID_COLLABS.put("557058:1f318bba-6336-4f60-a3b1-67e03a32a3dc", "transverse"); // Kévin Labesse
        ID_COLLABS.put("5ef5ec9a7e95e80a8126e509", "scrum"); // Pierre-Yves Garic
        ID_COLLABS.put("5b97bc461b4803467d26fd6e", "transverse"); // Xavier Michel
        ID_COLLABS.put("5e787dfb2466490c495f2a85", "transverse"); // Maxime Ancellin
        ID_COLLABS.put("5a96abe5e9dc0033a7af8cfb", "transverse"); // Joël Royer
        ID_COLLABS.put("5db2f070af604e0db364eb12", "transverse"); // David Boucard Planel
        ID_COLLABS.put("5a27b9ed466b4a37eec61268", "transverse"); // Pierre Bertin
        ID_COLLABS.put("557058:d8f506a1-fa47-4681-9ab1-7214a062c264", "transverse"); // Vincent Martin
        ID_COLLABS.put("557058:834cbf83-d227-4823-bfdf-db62c8672ad1", "transverse"); // Victor Dumesny
        ID_COLLABS.put("557058:df17bf30-7843-415e-985d-151faba64429", "transverse"); // Philippe Fleur
        ID_COLLABS.put(null, ""); // unassigned
    }

    final static String[] REQUESTS_SPRINT = new String[ID_COLLABS.size()];

    static {
        int i = 0;
        for (String s : ID_COLLABS.keySet()) {
            REQUESTS_SPRINT[i] = JIRA_API_URL + "search?jql=project=" + PROJECT_NAME + "+AND+assignee=" + s +
                    "+AND+sprint=" + SPRINT_NAME + "&maxResults=" + MAX_RESULTS;
            i++;
        }
    }

    /*
    FIN - Déclaration et définition des variables globales
     */


    /*
    DEBUT - Méthodes utilisés pour obtenir les informations sur la couche données (DAO)
     */
    //Retourne les informations sur le sprint actif
    public static Sprint callJiraSprintAPI() {
        HashMap<String, Team> hmTeams = getTeams(REQUESTS_SPRINT);
        Team[] teams = new Team[hmTeams.size()];
        int i = 0;
        for (String s : hmTeams.keySet()) {
            teams[i] = hmTeams.get(s);
            i++;
        }
        SPRINT_ACTIF.setTeams(teams);
        SPRINT_ACTIF.setTotalTime(Sprint.durationOfSprint(SPRINT_ACTIF.getStartDate(), SPRINT_ACTIF.getEndDate()));
        SPRINT_ACTIF.setTimeLeft(Sprint.timeLeftOnSprint(SPRINT_ACTIF.getEndDate()));
        return SPRINT_ACTIF;
    }

    //Retourne la liste des collaborateurs en prenant en compte les tickets sur le sprint actif
    public static HashMap<String, Collaborator> callJiraCollabSprintAPI() {
        return getCollaborators(REQUESTS_SPRINT);
    }

    public static Backlog callJiraBacklogAPI() throws UnsupportedEncodingException {
        int[] incidents = getProjectIncidentBug(RUN_PROJECT_NAME, ISSUE_INCIDENT);
        int[] bugs = getProjectIncidentBug(PROJECT_NAME, ISSUE_BUG);
        return Backlog.builder()
                .nbIncidents(incidents[0])
                .nbIncidentsLow(incidents[1])
                .nbIncidentsMedium(incidents[2])
                .nbIncidentsHigh(incidents[3])
                .nbIncidentsHighest(incidents[4])
                .nbIncidentsCreated(getCreated(NB_DAYS_BACKLOG, RUN_PROJECT_NAME, ISSUE_INCIDENT))
                .nbIncidentsResolved(getResolved(NB_DAYS_BACKLOG, RUN_PROJECT_NAME, ISSUE_INCIDENT))
                .nbIncidentsInProgress(getInProgress(NB_DAYS_BACKLOG, RUN_PROJECT_NAME, ISSUE_INCIDENT))
                .nbBugs(bugs[0])
                .nbBugsLow(bugs[1])
                .nbBugsMedium(bugs[2])
                .nbBugsHigh(bugs[3])
                .nbBugsHighest(bugs[4])
                .nbBugsCreated(getCreated(NB_DAYS_BACKLOG, PROJECT_NAME, ISSUE_BUG))
                .nbBugsResolved(getResolved(NB_DAYS_BACKLOG, PROJECT_NAME, ISSUE_BUG))
                .nbBugsInProgress(getInProgress(NB_DAYS_BACKLOG, PROJECT_NAME, ISSUE_BUG))
                .build();
    }

    public static HashMap<String, Retrospective> callJiraRetrospectiveAPI() {
        HashMap<String, Retrospective> retrospectives = new HashMap<>();
        ArrayList<SprintCommitment> sprints = getLastlyClosedSprints(NB_SPRINTS_RETROSPECTIVE);
        SprintCommitment[] s = new SprintCommitment[sprints.size()];
        int i = 0;
        for (SprintCommitment sprint : sprints) {
            double[] commitment = getCommitment(sprint, BOARD_ID_BETA_SP);
            sprint.setInitialCommitment(commitment[0]);
            sprint.setFinalCommitment(commitment[1]);
            sprint.setAddedWork(commitment[2]);
            sprint.setCompletedWork(commitment[3]);
            s[i] = sprint;
            i++;
        }
        Retrospective r = Retrospective.builder()
                .teamName("alpha")
                .sprints(s)
                .build();
        retrospectives.put(r.getTeamName(), r);
        return retrospectives;
    }
    /*
     FIN - Méthodes utilisés pour obtenir les informations sur la couche données (DAO)
     */

    /*
    DEBUT - Méthodes pour appeler l'API, les services externes et stocker ces données
     */

    public static HashMap<String, Collaborator> getCollaborators(String[] requests) {
        HashMap<String, Collaborator> collaborators = new HashMap<>();
        Collaborator c;
        for (String request : requests) {
            if ((c = getCollaborator(request)) != null){
                collaborators.put(c.getAccountId(), c);
            }
        }
        HashMap<String, Float[]> planning = getPlanning(PLANNING_PATH);
        for (String s : planning.keySet()) {
            if (collaborators.containsKey(s)) {
                c = collaborators.get(s);
                c.setTotalWorkingTime(planning.get(s)[0]);
                c.setAvailableTime(planning.get(s)[1]);
                collaborators.put(s, c);
            }
        }
        return collaborators;
    }

    /* Main method : Returns a collaborator if it has at least one ticket
     * else return null
     */
    public static Collaborator getCollaborator(String request) {
            /*
            Variables
             */
        int timespent = 0, estimated = 0, remaining = 0;
        double spTotal = 0;
        double spAQualifier = 0;
        double spBacAffinage = 0;
        double spEnAttente = 0;
        double spAFaire = 0;
        double spEnCours = 0;
        double spAbandonne = 0;
        double spDevTermine = 0;
        double spAvalider = 0;
        double spAlivrer = 0;
        double spATester = 0;
        double spRefuseEnRecette = 0;
        double spValideEnRecette = 0;
        double spLivre = 0;
        double spTermine = 0;
        int ticketsDone = 0;
        int ticketsInProgress = 0;
        int ticketsToDo = 0;
        int ticketsDevDone = 0;
        int ticketsEnCoursDevTermine = 0;
        int ticketsAtester = 0;
        String accountId = "";
        String emailAddress = "";
        String nom = "";
        String prenom = "";
        JSONObject issue;
        JSONObject fields;
        JSONObject status;
        JSONArray issues;
        int total;
        String statut;
        HttpResponse<JsonNode> response = Unirest.get(request)
                .basicAuth(USERNAME, API_TOKEN)
                .header("Accept", "application/json")
                .asJson();
        JSONObject myObj = response.getBody().getObject();
        total = myObj.getInt("total");
        issues = myObj.getJSONArray(JSON_ISSUES);
        if (total == 0) {
            return null;
        }
        for (int j = 0; j < issues.length(); j++) {
            issue = issues.getJSONObject(j);
            fields = issue.getJSONObject(JSON_FIELDS);
            status = fields.getJSONObject(JSON_STATUS);
            // Collaborator information
            if (request.contains("null")) {
                prenom = "Non";
                nom = "Assigné";
            } else {
                JSONObject assignee = fields.getJSONObject("assignee");
                if (accountId.isEmpty()) {
                    accountId = assignee.getString("accountId");
                }
                if (emailAddress.isEmpty() && assignee.has("emailAddress")) {
                    emailAddress = assignee.getString("emailAddress");
                    int indexDot = emailAddress.indexOf(".");
                    int indexAt = emailAddress.indexOf("@");
                    prenom = emailAddress.substring(0, indexDot);
                    nom = emailAddress.substring(indexDot + 1, indexAt);
                } else if (nom.isEmpty() && prenom.isEmpty()) {
                    String fullName = assignee.getString("displayName");
                    int fullNameLength = fullName.length();
                    int indexSpace = fullName.indexOf(" ");
                    if (indexSpace < 0) {
                        prenom = fullName;
                    } else {
                        prenom = fullName.substring(0, indexSpace);
                        nom = fullName.substring(indexSpace + 1, fullNameLength);
                    }
                }
            }
            statut = status.getString("name");
            // Setting Tickets
            if (DONE.contains(statut)) {
                ticketsDone++;
            } else if (IN_PROGRESS.contains(statut)) {
                if (DEV_DONE.contains(statut)) {
                    ticketsDevDone++;
                } else {
                    ticketsInProgress++;
                }
            } else {
                ticketsToDo++;
            }
            if (DEV_DONE_EN_COURS.contains(statut)) {
                ticketsEnCoursDevTermine++;
            }
            if (A_TESTER.contains(statut)) {
                ticketsAtester++;
            }
            // Setting working time
            if (!fields.isNull(JSON_KEY_TIMEESTIMATE)) {
                remaining += (fields.getInt(JSON_KEY_TIMEESTIMATE) / 3600);
            }
            if (!fields.isNull(JSON_KEY_TIMEORIGINALESTIMATE)) {
                estimated += (fields.getInt(JSON_KEY_TIMEORIGINALESTIMATE) / 3600);
            }
            if (!fields.isNull(JSON_KEY_TIMESPENT)) {
                timespent += (fields.getInt(JSON_KEY_TIMESPENT) / 3600);
            }

            //Attribution des story points
            if (!fields.isNull(JSON_KEY_STORYPOINTS)) {
                double curStoryPoints = fields.getDouble(JSON_KEY_STORYPOINTS);
                spTotal += curStoryPoints;
                switch (statut) {
                    case "A qualifier":
                        spAQualifier += curStoryPoints;
                        break;
                    case "Bac d'affinage":
                        spBacAffinage += curStoryPoints;
                        break;
                    case EN_ATTENTE:
                        spEnAttente += curStoryPoints;
                        break;
                    case "A Faire":
                        spAFaire += curStoryPoints;
                        break;
                    case EN_COURS:
                        spEnCours += curStoryPoints;
                        break;
                    case ABANDONNE:
                        spAbandonne += curStoryPoints;
                        break;
                    case DEV_TERMINE:
                        spDevTermine += curStoryPoints;
                        break;
                    case A_VALIDER:
                        spAvalider += curStoryPoints;
                        break;
                    case A_LIVRER:
                        spAlivrer += curStoryPoints;
                        break;
                    case A_TESTER:
                        spATester += curStoryPoints;
                        break;
                    case REFUSE_RECETTE:
                        spRefuseEnRecette += curStoryPoints;
                        break;
                    case VALIDE_RECETTE:
                        spValideEnRecette += curStoryPoints;
                        break;
                    case LIVRE:
                        spLivre += curStoryPoints;
                        break;
                    case TERMINE:
                        spTermine += curStoryPoints;
                        break;
                    default:
                        break;
                }
            }
        }
        //Attribution du role
        String role = ID_COLLABS.get(accountId);

        //Création d'un objet Collaborateur
        return Collaborator.builder()
                .accountId(accountId)
                .emailAddress(emailAddress)
                .name(nom)
                .firstName(prenom)
                .role(role)
                .loggedTime(timespent)
                .estimatedTime(estimated)
                .remainingTime(remaining)
                .nbTickets(total)
                .nbDone(ticketsDone)
                .nbDevDone(ticketsDevDone)
                .nbInProgress(ticketsInProgress)
                .nbToDo(ticketsToDo)
                .nbATester(ticketsAtester)
                .nbEnCoursDevTermine(ticketsEnCoursDevTermine)
                .spTotal(spTotal)
                .spAqualifier(spAQualifier)
                .spBacAffinage(spBacAffinage)
                .spEnAttente(spEnAttente)
                .spAfaire(spAFaire)
                .spEncours(spEnCours)
                .spAbandonne(spAbandonne)
                .spDevTermine(spDevTermine)
                .spAvalider(spAvalider)
                .spAlivrer(spAlivrer)
                .spATester(spATester)
                .spRefuseEnRecette(spRefuseEnRecette)
                .spValideEnRecette(spValideEnRecette)
                .spLivre(spLivre)
                .spTermine(spTermine)
                .build();
    }

    /* Creates Collaborator objects for unassigned assignee (assignee=null)
     * for each team (A MAJ, PAS OPTI)
     */
    public static Collaborator getUnassignedPerTeam(String unassignedAccountId, String label) {
        String request = JIRA_API_URL + "search?jql=project=" + PROJECT_NAME + "+AND+assignee=null+AND+sprint=" +
                SPRINT_NAME + "&maxResults=" + MAX_RESULTS;
        int timespent = 0, estimated = 0, remaining = 0;
        double spTotal = 0;
        double spAQualifier = 0;
        double spBacAffinage = 0;
        double spEnAttente = 0;
        double spAFaire = 0;
        double spEnCours = 0;
        double spAbandonne = 0;
        double spDevTermine = 0;
        double spAvalider = 0;
        double spAlivrer = 0;
        double spATester = 0;
        double spRefuseEnRecette = 0;
        double spValideEnRecette = 0;
        double spLivre = 0;
        double spTermine = 0;
        int ticketsDone = 0;
        int ticketsInProgress = 0;
        int ticketsToDo = 0;
        int ticketsDevDone = 0;
        int ticketsEnCoursDevTermine = 0;
        int ticketsAtester = 0;
        String accountId = "";
        String emailAddress = "";
        String nom = "";
        String prenom = "";
        JSONObject issue;
        JSONObject fields;
        JSONObject status;
        int total;
        String statut;
        HttpResponse<JsonNode> response = Unirest.get(request)
                .basicAuth(USERNAME, API_TOKEN)
                .header("Accept", "application/json")
                .asJson();
        JSONObject myObj = response.getBody().getObject();
        JSONArray issues = myObj.getJSONArray("issues");
        total = myObj.getInt("total");
        for (int i = 0; i < issues.length(); i++) {
            issue = issues.getJSONObject(i);
            fields = issue.getJSONObject("fields");
            status = fields.getJSONObject("status");
            JSONArray labels = fields.getJSONArray("labels");
            for (int j = 0; j < labels.length(); j++) {
                if (labels.getString(j).toLowerCase().contains(label.toLowerCase())) {
                    accountId = unassignedAccountId;
                    prenom = "Non";
                    nom = "Assigné (" + label + ")";
                    statut = status.getString("name");
                    // Setting Tickets
                    if (DONE.contains(statut)) {
                        ticketsDone++;
                    } else if (IN_PROGRESS.contains(statut)) {
                        if (DEV_DONE.contains(statut)) {
                            ticketsDevDone++;
                        } else {
                            ticketsInProgress++;
                        }
                    } else {
                        ticketsToDo++;
                    }
                    if (DEV_DONE_EN_COURS.contains(statut)) {
                        ticketsEnCoursDevTermine++;
                    }
                    if (A_TESTER.contains(statut)) {
                        ticketsAtester++;
                    }
                    // Setting working time
                    if (!fields.isNull(JSON_KEY_TIMEESTIMATE)) {
                        remaining += (fields.getInt(JSON_KEY_TIMEESTIMATE) / 3600);
                    }
                    if (!fields.isNull(JSON_KEY_TIMEORIGINALESTIMATE)) {
                        estimated += (fields.getInt(JSON_KEY_TIMEORIGINALESTIMATE) / 3600);
                    }
                    if (!fields.isNull(JSON_KEY_TIMESPENT)) {
                        timespent += (fields.getInt(JSON_KEY_TIMESPENT) / 3600);
                    }

                    //Attribution des story points
                    if (!fields.isNull(JSON_KEY_STORYPOINTS)) {
                        double curStoryPoints = fields.getDouble(JSON_KEY_STORYPOINTS);
                        spTotal += curStoryPoints;
                        switch (statut) {
                            case "A qualifier":
                                spAQualifier += curStoryPoints;
                                break;
                            case "Bac d'affinage":
                                spBacAffinage += curStoryPoints;
                                break;
                            case EN_ATTENTE:
                                spEnAttente += curStoryPoints;
                                break;
                            case "A Faire":
                                spAFaire += curStoryPoints;
                                break;
                            case EN_COURS:
                                spEnCours += curStoryPoints;
                                break;
                            case ABANDONNE:
                                spAbandonne += curStoryPoints;
                                break;
                            case DEV_TERMINE:
                                spDevTermine += curStoryPoints;
                                break;
                            case A_VALIDER:
                                spAvalider += curStoryPoints;
                                break;
                            case A_LIVRER:
                                spAlivrer += curStoryPoints;
                                break;
                            case A_TESTER:
                                spATester += curStoryPoints;
                                break;
                            case REFUSE_RECETTE:
                                spRefuseEnRecette += curStoryPoints;
                                break;
                            case VALIDE_RECETTE:
                                spValideEnRecette += curStoryPoints;
                                break;
                            case LIVRE:
                                spLivre += curStoryPoints;
                                break;
                            case TERMINE:
                                spTermine += curStoryPoints;
                                break;
                            default:
                                break;
                        }
                    }
                    break;
                }
            }
        }
        //Création d'un objet Collaborateur
        return Collaborator.builder()
                .accountId(accountId)
                .emailAddress(emailAddress)
                .name(nom)
                .firstName(prenom)
                .loggedTime(timespent)
                .estimatedTime(estimated)
                .remainingTime(remaining)
                .nbTickets(total)
                .nbDone(ticketsDone)
                .nbDevDone(ticketsDevDone)
                .nbInProgress(ticketsInProgress)
                .nbToDo(ticketsToDo)
                .nbATester(ticketsAtester)
                .nbEnCoursDevTermine(ticketsEnCoursDevTermine)
                .spTotal(spTotal)
                .role("none")
                .spAqualifier(spAQualifier)
                .spBacAffinage(spBacAffinage)
                .spEnAttente(spEnAttente)
                .spAfaire(spAFaire)
                .spEncours(spEnCours)
                .spAbandonne(spAbandonne)
                .spDevTermine(spDevTermine)
                .spAvalider(spAvalider)
                .spAlivrer(spAlivrer)
                .spATester(spATester)
                .spRefuseEnRecette(spRefuseEnRecette)
                .spValideEnRecette(spValideEnRecette)
                .spLivre(spLivre)
                .spTermine(spTermine)
                .build();
    }

    /* Call getCollaborator() and assign each collaborator to its team
     * returning a HashMap of size nbTeams
     */
    public static HashMap<String, Team> getTeams(String[] requests) {
        HashMap<String, Collaborator> collaborators = getCollaborators(requests);
        Collaborator uAlpha = getUnassignedPerTeam(UNASSIGNED_ALPHA, TEAM_NAME_ALPHA);
        Collaborator uBeta = getUnassignedPerTeam(UNASSIGNED_BETA, TEAM_NAME_BETA);
        Collaborator uGamma = getUnassignedPerTeam(UNASSIGNED_GAMMA, TEAM_NAME_GAMMA);
        collaborators.put(uAlpha.getAccountId(), uAlpha);
        collaborators.put(uBeta.getAccountId(), uBeta);
        collaborators.put(uGamma.getAccountId(), uGamma);

        HashMap<String, Team> teams = new HashMap<>();
        List<Collaborator> collaboratorsAlpha = new ArrayList<>();
        List<Collaborator> collaboratorsBeta = new ArrayList<>();
        List<Collaborator> collaboratorsGamma = new ArrayList<>();
        for (Collaborator c : collaborators.values()) {
            if (TEAM_ALPHA.contains(c.getAccountId())) {
                collaboratorsAlpha.add(c);
            } else if (TEAM_BETA.contains(c.getAccountId())) {
                collaboratorsBeta.add(c);
            } else if (TEAM_GAMMA.contains(c.getAccountId())) {
                collaboratorsGamma.add(c);
            }
        }
        Team alpha = Team.builder()
                .name("alpha")
                .collaborators(collaboratorsAlpha)
                .build();
        Team beta = Team.builder()
                .name("beta")
                .collaborators(collaboratorsBeta)
                .build();
        Team gamma = Team.builder()
                .name("gamma")
                .collaborators(collaboratorsGamma)
                .build();
        teams.put(alpha.getName(), alpha);
        teams.put(beta.getName(), beta);
        teams.put(gamma.getName(), gamma);
        return teams;
    }

    /* Returns an array of integer containing information on bugs/incidents since project's creation (number and priority)
     * A bug is active when NOT in following jira states : Terminé, Livré
     */
    public static int[] getProjectIncidentBug(String projectName, String issueType) {
        /*
        Variables
         */
        // 0 : total, 1: low, 2: medium, 3: high, 4: highest
        int[] incidentsBugs = new int[5];
        int startAt = 0;
        JSONObject myObj;
        JSONArray issues;
        JSONObject issue;
        JSONObject fields;
        JSONObject priority;
        JSONObject status;
        String statut;
        String request = JIRA_API_URL + "search?jql=project=" + projectName +
                "+AND+issuetype='" + issueType + "'&maxResults=" + MAX_RESULTS + "&startAt=" + startAt;
        /*
        Logic
         */
        HttpResponse<JsonNode> response = Unirest.get(request)
                .basicAuth(USERNAME, API_TOKEN)
                .header("Accept", "application/json")
                .asJson();
        myObj = response.getBody().getObject();
        issues = myObj.getJSONArray(JSON_ISSUES);
        int total = myObj.getInt("total");
        while (startAt < total) {
            for (int j = 0; j < issues.length(); j++) {
                //Ensemble des objets JSON utiles
                issue = issues.getJSONObject(j);
                fields = issue.getJSONObject(JSON_FIELDS);
                priority = fields.getJSONObject(JSON_PRIORITY);
                status = fields.getJSONObject(JSON_STATUS);
                statut = status.getString("name");
                if (!DONE_BUGS.contains(statut)) {
                    incidentsBugs[0]++;
                    switch (priority.getString("name")) {
                        case "Low":
                            incidentsBugs[1]++;
                            break;
                        case "Medium":
                            incidentsBugs[2]++;
                            break;
                        case "High":
                            incidentsBugs[3]++;
                            break;
                        case "Highest":
                            incidentsBugs[4]++;
                            break;
                        default:
                            break;
                    }
                }

            }
            startAt += MAX_RESULTS;
            request = JIRA_API_URL + "search?jql=project=" + projectName +
                    "+AND+issuetype='" + issueType + "'&maxResults=" + MAX_RESULTS + "&startAt=" + startAt;
            response = Unirest.get(request)
                    .basicAuth(USERNAME, API_TOKEN)
                    .header("Accept", "application/json")
                    .asJson();
            myObj = response.getBody().getObject();
            issues = myObj.getJSONArray("issues");
        }
        return incidentsBugs;
    }

    /* Returns an array of integer of length 'nbDays'. Each element corresponds to the number of bug/incident created
     *  Index i represent the number of bugs created (nbDays-i) ago
     */
    public static int[] getCreated(int nbDays, String projectName, String issueType) throws UnsupportedEncodingException {
        /*
        Variables
         */
        int[] bugsCreated = new int[nbDays + 1];
        int startAt = 0;
        int days;
        String dateCreation;
        JSONObject myObj;
        JSONArray issues;
        JSONObject issue;
        JSONObject fields;
        LocalDate ldtBug;
        String request = JIRA_API_URL + "search?jql=project=" + projectName + "+AND+issuetype='" + issueType + "'+AND+created" +
                URLEncoder.encode(">=", "utf-8") + "-" + nbDays + "d&maxResults=" + MAX_RESULTS + "&startAt=" + startAt;
        /*
        Logic
         */
        HttpResponse<JsonNode> response = Unirest.get(request)
                .basicAuth(USERNAME, API_TOKEN)
                .header("Accept", "application/json")
                .asJson();
        myObj = response.getBody().getObject();
        issues = myObj.getJSONArray(JSON_ISSUES);
        int total = myObj.getInt("total");
        while (startAt < total) {
            for (int j = 0; j < issues.length(); j++) {
                //Ensemble des objets JSON utiles
                issue = issues.getJSONObject(j);
                fields = issue.getJSONObject(JSON_FIELDS);
                dateCreation = fields.getString("created").substring(0, 10);
                ldtBug = LocalDate.parse(dateCreation);
                days = (int) DAYS.between(ldtBug, LocalDate.parse(TODAY_LD));
                bugsCreated[nbDays - days] += 1;
            }
            //on incrémente du nombre de résultats dans la requête
            startAt += MAX_RESULTS;
            request = JIRA_API_URL + "search?jql=project=" + projectName + "+AND+issuetype='" + issueType + "'+AND+created" +
                    URLEncoder.encode(">=", "utf-8") + "-" + nbDays + "d&maxResults=" + MAX_RESULTS + "&startAt=" + startAt;
            response = Unirest.get(request)
                    .basicAuth(USERNAME, API_TOKEN)
                    .header("Accept", "application/json")
                    .asJson();
            myObj = response.getBody().getObject();
            issues = myObj.getJSONArray("issues");
        }
        return bugsCreated;
    }

    /* Returns an array of integer of length 'nbDays'. Each element corresponds to the number of bug/incident resolved (Jira status "terminé/livré")
     *  Index i represent the number of bugs resolved (nbDays-i) ago
     */
    public static int[] getResolved(int nbDays, String projectName, String issueType) throws UnsupportedEncodingException {
        /*
        Variables
         */
        int[] bugsResolved = new int[nbDays + 1];
        int startAt = 0;
        String statut;
        String updateDate;
        LocalDate ldtBug;
        int days;
        String today = LocalDateTime.now().format(dtfLocalDate);
        String request = JIRA_API_URL + "search?jql=project=" + projectName + "+AND+issuetype='" + issueType + "'+AND+updated" +
                URLEncoder.encode(">=", "utf-8") + "-" + nbDays + "d&maxResults=" + MAX_RESULTS + "&startAt=" + startAt;
        JSONObject myObj;
        JSONArray issues;
        JSONObject issue;
        JSONObject fields;
        JSONObject status;

        /*
        Logic
         */
        HttpResponse<JsonNode> response = Unirest.get(request)
                .basicAuth(USERNAME, API_TOKEN)
                .header("Accept", "application/json")
                .asJson();
        myObj = response.getBody().getObject();
        issues = myObj.getJSONArray(JSON_ISSUES);
        int total = myObj.getInt("total");
        while (startAt < total) {
            for (int j = 0; j < issues.length(); j++) {
                //Ensemble des objets JSON utiles
                issue = issues.getJSONObject(j);
                fields = issue.getJSONObject(JSON_FIELDS);
                status = fields.getJSONObject(JSON_STATUS);
                statut = status.getString("name");
                if (statut.contains(TERMINE) || statut.contains(LIVRE)) {
                    updateDate = fields.getString("updated").substring(0, 10);
                    ldtBug = LocalDate.parse(updateDate);
                    days = (int) DAYS.between(ldtBug, LocalDate.parse(today));
                    bugsResolved[nbDays - days] += 1;
                }
            }
            startAt += MAX_RESULTS;
            request = JIRA_API_URL + "search?jql=project=" + projectName + "+AND+issuetype='Bug'+AND+updated" +
                    URLEncoder.encode(">=", "utf-8") + "-" + nbDays + "d&maxResults=" + MAX_RESULTS + "&startAt=" + startAt;
            response = Unirest.get(request)
                    .basicAuth(USERNAME, API_TOKEN)
                    .header("Accept", "application/json")
                    .asJson();
            myObj = response.getBody().getObject();
            issues = myObj.getJSONArray("issues");
        }
        return bugsResolved;
    }

    /* Returns an array of integer of length 'nbDays'. Each element corresponds to the number of bug/incident in progress (Jira status "terminé/livré")
     *  Index i represent the number of bugs resolved (nbDays-i) ago
     */
    public static int[] getInProgress(int nbDays, String projectName, String issueType) throws UnsupportedEncodingException {
        /*
        Variables
         */
        int[] inProgress = new int[nbDays + 1];
        int startAt = 0;
        String statut;
        String updateDate;
        LocalDate ldtBug;
        int days;
        String today = LocalDateTime.now().format(dtfLocalDate);
        String request = JIRA_API_URL + "search?jql=project=" + projectName + "+AND+issuetype='" + issueType + "'+AND+updated" +
                URLEncoder.encode(">=", "utf-8") + "-" + nbDays + "d&maxResults=" + MAX_RESULTS + "&startAt=" + startAt;
        JSONObject myObj;
        JSONArray issues;
        JSONObject issue;
        JSONObject fields;
        JSONObject status;

        /*
        Logic
         */
        HttpResponse<JsonNode> response = Unirest.get(request)
                .basicAuth(USERNAME, API_TOKEN)
                .header("Accept", "application/json")
                .asJson();
        myObj = response.getBody().getObject();
        issues = myObj.getJSONArray(JSON_ISSUES);
        int total = myObj.getInt("total");
        while (startAt < total) {
            for (int j = 0; j < issues.length(); j++) {
                //Ensemble des objets JSON utiles
                issue = issues.getJSONObject(j);
                fields = issue.getJSONObject(JSON_FIELDS);
                status = fields.getJSONObject(JSON_STATUS);
                statut = status.getString("name");
                if (IN_PROGRESS.contains(statut)) {
                    updateDate = fields.getString("updated").substring(0, 10);
                    ldtBug = LocalDate.parse(updateDate);
                    days = (int) DAYS.between(ldtBug, LocalDate.parse(today));
                    inProgress[nbDays - days] += 1;
                }
            }
            startAt += MAX_RESULTS;
            request = JIRA_API_URL + "search?jql=project=" + projectName + "+AND+issuetype='Bug'+AND+updated" +
                    URLEncoder.encode(">=", "utf-8") + "-" + nbDays + "d&maxResults=" + MAX_RESULTS + "&startAt=" + startAt;
            response = Unirest.get(request)
                    .basicAuth(USERNAME, API_TOKEN)
                    .header("Accept", "application/json")
                    .asJson();
            myObj = response.getBody().getObject();
            issues = myObj.getJSONArray("issues");
        }
        return inProgress;
    }

    /* Returns an array of size (nbSprints) of the lastly closed sprints including the lastly active sprint
     */
    public static ArrayList<SprintCommitment> getLastlyClosedSprints(int nbSprints) {
        /*
        Variables
         */
        ArrayList<SprintCommitment> sprints = new ArrayList<>(nbSprints);
        String sprintName;
        int sprintId;
        JSONObject myObj;
        JSONArray values;
        JSONObject value;
        String request = JIRA_AGILEAPI_URL + "/board/" + BOARD_ID + "/sprint";
        int lastlyActiveSprintIndex = -1;

        /*
        Logic
         */
        HttpResponse<JsonNode> response = Unirest.get(request)
                .basicAuth(USERNAME, API_TOKEN)
                .header("Accept", "application/json")
                .asJson();
        myObj = response.getBody().getObject();
        values = myObj.getJSONArray("values");
        for (int i = 0; i < values.length(); i++) {
            value = values.getJSONObject(i);
            if (value.getString("state").equals("active")) {
                lastlyActiveSprintIndex = i;
            }
        }
        int i = 0;
        while (i < nbSprints) {
            value = values.getJSONObject(lastlyActiveSprintIndex - i);
            sprintName = value.getString("name");
            sprintId = parseInt(value.getString("id"));
            SprintCommitment s = SprintCommitment.builder()
                    .name(sprintName)
                    .id(sprintId)
                    .build();
            sprints.add(s);
            i++;
        }
        return sprints;
    }

    /* Returns 4 information on a sprint
     * 0: initialCommitment
     * 1: finalCommitment
     * 2: addedWork
     * 3: completedWork
     */
    public static double[] getCommitment(SprintCommitment s, String boardId) {
        /*
        Variables
         */
        double[] commitment = new double[4];
        double initialCommitment = 0;
        double finalCommitment = 0;
        double addedWork = 0;
        double completedWork = 0;
        String request = JIRA_GREENHOPPER_URL + "rapid/charts/sprintreport" + "?rapidViewId=" + boardId + "&sprintId=" + s.getId();
        /*
        Logic
         */
        HttpResponse<JsonNode> response = Unirest.get(request)
                .basicAuth(USERNAME, API_TOKEN)
                .header("Accept", "application/json")
                .asJson();
        JSONObject myObj = response.getBody().getObject();
        JSONObject contents = myObj.getJSONObject("contents");
        JSONObject addedIssues = contents.getJSONObject("issueKeysAddedDuringSprint");
        // Completed Issues
        JSONObject completedIssuesEstimateSum = contents.getJSONObject("completedIssuesEstimateSum");
        JSONObject completedIssuesInitialEstimateSum = contents.getJSONObject("completedIssuesInitialEstimateSum");
        // Not Completed Issues
        JSONObject issuesNotCompletedEstimateSum = contents.getJSONObject("issuesNotCompletedEstimateSum");
        JSONObject issuesNotCompletedInitialEstimateSum = contents.getJSONObject("issuesNotCompletedInitialEstimateSum");
        //All issues
        JSONObject allIssuesEstimateSum = contents.getJSONObject("allIssuesEstimateSum");

        if (completedIssuesEstimateSum.has("value")) {
            completedWork += completedIssuesEstimateSum.getInt("value");
            initialCommitment += completedIssuesEstimateSum.getInt("value");
        }
        if (completedIssuesInitialEstimateSum.has("value")) {
            initialCommitment += completedIssuesInitialEstimateSum.getInt("value");
        }
        if (issuesNotCompletedEstimateSum.has("value")) {
            initialCommitment += issuesNotCompletedEstimateSum.getInt("value");
        }
        if (issuesNotCompletedInitialEstimateSum.has("value")) {
            initialCommitment += issuesNotCompletedInitialEstimateSum.getInt("value");
        }
        if (allIssuesEstimateSum.has("value")) {
            finalCommitment += allIssuesEstimateSum.getInt("value");
        }
        //Added issues
        Iterator<String> keys = addedIssues.keys();
        while (keys.hasNext()) {
            String issueID = keys.next();
            addedWork += getStoryPoint(issueID);
        }
        commitment[0] = initialCommitment;
        commitment[1] = finalCommitment;
        commitment[2] = addedWork;
        commitment[3] = completedWork;
        return commitment;
    }

    /* Method that returns the number of story points assigned to an issue.
     * Used to retrieve story points linked to added tickets in getCommitment()
     */
    public static double getStoryPoint(String issueID) {
        /*
        Variables
         */
        double spIssue = 0;
        String request = JIRA_API_URL + "search?jql=project=" + PROJECT_NAME + "+AND+issue=" + issueID;
        JSONObject myObj;
        JSONArray issues;
        JSONObject issue;
        JSONObject fields;

        /*
        Logic
         */
        HttpResponse<JsonNode> response = Unirest.get(request)
                .basicAuth(USERNAME, API_TOKEN)
                .header("Accept", "application/json")
                .asJson();
        myObj = response.getBody().getObject();
        issues = myObj.getJSONArray(JSON_ISSUES);
        for (int j = 0; j < issues.length(); j++) {
            //Ensemble des objets JSON utiles
            issue = issues.getJSONObject(j);
            fields = issue.getJSONObject(JSON_FIELDS);
            if (!fields.isNull(JSON_KEY_STORYPOINTS)) {
                spIssue = fields.getDouble(JSON_KEY_STORYPOINTS);
            }
        }
        return spIssue;
    }

    /* Method that reads a CSV (planning/absences) which is located at PLANNING_PATH
     * Returns in a HashMap two floats (values), the totalWorkingTime and availableTime
     * The key being accountId
     */
    public static HashMap<String, Float[]> getPlanning(String PLANNING_PATH) {
        /*
         Variables
         */
        HashMap<String, Float[]> planning = new HashMap<>();
        float totalWorkingTime;
        float availableTime;
        String accountId;
        int startIndex = -1;
        int endIndex = -1;
        int todayIndex = -1;
        //Two constants giving the column containing information about accountId and the 1st date
        final int INDEX_ACC_ID = 2;
        int FIRST_ROW = 4;
        Float[] workTime = new Float[2];
        String[] dates;
        /*
        Logic
         */
        try {
            FileReader filereader = new FileReader(PLANNING_PATH);
            CSVParser parser = new CSVParserBuilder().withSeparator(',').build();
            CSVReader csvReader = new CSVReaderBuilder(filereader)
                    .withCSVParser(parser)
                    .build();
            for (int i = 0; i < 4; i++) {
                csvReader.readNext();
            }
            dates = csvReader.readNext();
            for (int i = 0; i < dates.length; i++) {
                if (SPRINT_ACTIF.getStartDate().format(dtf).equals(dates[i])) {
                    startIndex = i;
                }
                if (SPRINT_ACTIF.getEndDate().format(dtf).equals(dates[i])) {
                    endIndex = i;
                }
                if (TODAY.equals(dates[i])) {
                    todayIndex = i;
                }
            }
            // When the initial date is not contained in the CSV
            if (startIndex < 0) {
                startIndex = FIRST_ROW;
            }
            //On saute une ligne
            csvReader.readNext();
            String[] infos;
            while ((infos = csvReader.readNext()) != null) {
                if (!infos[INDEX_ACC_ID].isEmpty()) {
                    accountId = infos[INDEX_ACC_ID];
                    totalWorkingTime = 8 * (endIndex - startIndex + 1);
                    availableTime = 8 * (endIndex - todayIndex + 1);
                    for (int i = startIndex; i <= endIndex; i++) {
                        if (!infos[i].isEmpty()) {
                            totalWorkingTime -= parseFloat(infos[i]) * 8;
                        }
                    }
                    for (int i = todayIndex; i <= endIndex; i++) {
                        if (!infos[i].isEmpty()) {
                            availableTime -= parseFloat(infos[i]) * 8;
                        }
                    }
                    workTime[0] = totalWorkingTime;
                    workTime[1] = availableTime;
                    planning.put(accountId, workTime);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return planning;
    }
    /*
    FIN - Méthodes pour appeler l'API, les services externes et stocker ces données
     */
}




