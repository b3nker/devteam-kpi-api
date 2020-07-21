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
import org.springframework.stereotype.Repository;

import java.io.*;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static java.lang.Float.parseFloat;
import static java.lang.Integer.parseInt;
import static java.time.temporal.ChronoUnit.DAYS;

@Repository
public class JiraAPI {

    /*
    DEBUT - Déclaration et définition des variables
     */
    final static String USERNAME = "benjamin.kermani@neo9.fr";
    final static String API_TOKEN = "sqjFnTAVspNM4NxLd1QZC5CB";
    final static String PLANNING_PATH = "planning.csv";
    final static String BOARD_ID = "391";
    final static String BOARD_ID_ALPHA_SP = "451";

    final static Sprint sprint = new Sprint();

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
            //Condition à modifier quant il n'y aura qu'un seul sprint actif
            if (value.getString("state").equals("active") && value.getString("name").equals("Sprint 30")) {
                sprintName = value.getString("name");
                startDate = value.getString("startDate");
                endDate = value.getString("endDate");
                sprintId = parseInt(value.getString("id"));
            }
        }
        sprint.setId(sprintId);
        sprint.setName(sprintName);
        sprint.setStartDate(Sprint.toLocalDateTime(startDate));
        sprint.setEndDate(Sprint.toLocalDateTime(endDate));
    }

    final static String SPRINT_NAME = "'" + sprint.getName() + "'";
    final static DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/YY");
    final static String START_DAY_SPRINT = sprint.getStartDate().format(dtf);
    final static String END_DAY_SPRINT = sprint.getEndDate().format(dtf);
    final static String TODAY = LocalDateTime.now().format(dtf);

    final static ArrayList<String> TEAM_ALPHA = new ArrayList<>(Arrays.asList(
            "5c17b4599f443a65fecae3ca", // Julien Mosset
            "5a9ebe1c4af2372a88a0656b", // Nicolas Ovejero
            "5bcd8282607ed038040177bb", // Pape Thiam
            "5cf921f6b06c540e82580cbd", // Valentin Pierrel
            "5ed76cdf2fdc580b88f3bbef", // Alex Cheuko
            "5e98521a3a8b910c085d6a28", // Kévin Youna
            "unassignedAlpha"

    ));
    final static ArrayList<String> TEAM_BETA = new ArrayList<>(Arrays.asList(
            "5cb45bb34064460e407eabe4", // Guillermo Garcès
            "5a9ebdf74af2372a88a06565", // Gabriel Roquigny
            "5a2181081594706402dee482", // Etienne Bourgouin
            "5afe92f251d0b7540b43de81", // Malick Diagne
            "5d6e32e06e3e1f0d9623cb5a", // Pierre Tomasina
            "5ed64583620b1d0c168d4e36", // Anthony Hernandez
            "5ef1afd6561e0e0aae904914", // Yong Ma
            "unassignedBeta"
    ));
    final static ArrayList<String> TEAM_GAMMA = new ArrayList<>(Arrays.asList(
            "5e285008ee264b0e74591993", // Eric Coupal
            "5ed76cc1be03220ab32183be", // Thibault Foucault
            "557058:87b17037-8a69-4b38-8dab-b4cf904e960a", // Pierre Thevenet
            "5d9b0573ea65c10c3fdbaab2", // Maxime Fourt
            "5a8155f0cad06b353733bae8", // Guillaume Coppens
            "5dfd11b39422830cacaa8a79", // Carthy Marie Joseph
            "unassignedGamma"
    ));
    final static ArrayList<String> DONE = new ArrayList<>(Arrays.asList("Livré", "Terminé",
            "Validé en recette")); // Ne contient pas les statuts jira suivants : Abandonné, a valider
    final static ArrayList<String> IN_PROGRESS = new ArrayList<>(Arrays.asList("En cours", "Dév terminé",
            "Refusé en recette", "En attente", "A tester", "A Livrer"));
    final static ArrayList<String> DEV_DONE = new ArrayList<>(Arrays.asList("A tester", "A Livrer"));
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
            REQUESTS_SPRINT[i] = "search?jql=project=BMKP+AND+assignee=" + s +
                    "+AND+sprint=" + SPRINT_NAME + "&maxResults=100";
            i++;
        }
    }

    final static String PROJECT_NAME = "BMKP";

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
        sprint.setTeams(teams);
        sprint.setTotalTime(Sprint.durationOfSprint(sprint.getStartDate(), sprint.getEndDate()));
        sprint.setTimeLeft(Sprint.timeLeftOnSprint(sprint.getEndDate()));
        return sprint;
    }

    //Retourne la liste des collaborateurs en prenant en compte les tickets sur le sprint actif
    public static HashMap<String, Collaborator> callJiraCollabSprintAPI() {
        return getCollaborators(REQUESTS_SPRINT);
    }

    public static Backlog callJiraBacklogAPI() throws UnsupportedEncodingException {
        int nbDays = 20;
        Backlog backlog = getProjectBugs(PROJECT_NAME);
        backlog.setNbBugsCreated(getBugsCreated(nbDays, PROJECT_NAME));
        backlog.setNbBugsResolved(getBugsResolved(nbDays, PROJECT_NAME));

        return backlog;
    }

    public static HashMap<String,Retrospective> callJiraRestrospectiveAPI() throws UnsupportedEncodingException {
        int nbSprints = 4;
        HashMap<String,Retrospective> retrospectives = new HashMap<>();
        ArrayList<Sprint> sprints = getLastlyClosedSprints(nbSprints);
        HashMap<String, Team> teams = getTeams(REQUESTS_SPRINT);
        Sprint[] s = new Sprint[sprints.size()];
        for(Sprint sprint: sprints){
            double[] commitment = getCommitment(sprint,BOARD_ID_ALPHA_SP);
            sprint.setInitialCommitment(commitment[0]);
            sprint.setFinalCommitment(commitment[1]);
            sprint.setAddedWork(commitment[2]);
            sprint.setCompletedWork(commitment[3]);
        }
        int i = 0;
        for(Sprint sprint: sprints){
            s[i] = sprint;
            i++;
        }
        Retrospective r = new Retrospective();
        r.setTeamName(teams.get("alpha").getName());
        r.setSprints(s);
        retrospectives.put(r.getTeamName(),r);
        return retrospectives;
    }
    /*
     FIN - Méthodes utilisés pour obtenir les informations sur la couche données (DAO)
     */

    /*
    DEBUT - Méthodes pour appeler l'API, les services externes et stocker ces données
     */
    //Méthode principale : Appel à l'API JIRA et stockage des informations dans une HashMap
    public static HashMap<String, Collaborator> getCollaborators(String[] requests) {
        HashMap<String, Collaborator> collaborators = new HashMap<>();
        for (String request : requests) {
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
            HttpResponse<JsonNode> response = Unirest.get("https://apriltechnologies.atlassian.net/rest/api/3/" +
                    request)
                    .basicAuth(USERNAME, API_TOKEN)
                    .header("Accept", "application/json")
                    .asJson();
            JSONObject myObj = response.getBody().getObject();
            int total = myObj.getInt("total");
            JSONArray issues = myObj.getJSONArray("issues");
            if (request.contains("null")) {
                List<Collaborator> collabs = new ArrayList<>();
                collabs = getUnassignedPerTeam();
                for (Collaborator c : collabs) {
                    collaborators.put(c.getAccountId(), c);
                }
            }
            if (total == 0) {
                continue;
            }
            for (int j = 0; j < issues.length(); j++) {
                //Ensemble des objets JSON utiles
                JSONObject issue = issues.getJSONObject(j);
                JSONObject fields = issue.getJSONObject("fields");
                JSONObject status = fields.getJSONObject("status");
                //Renseignements sur le collaborateur
                /*
                Cas spécial pour les tâches non-assignées
                 */
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
                        //Si plusieurs noms/prenoms le découpage peut-être incorrecte
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
                //Renseignements sur le statut de la demande
                String statut = status.getString("name");
                //Répartition des tickets
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
                if (statut.contains("En cours") || statut.contains("Dév terminé")) {
                    ticketsEnCoursDevTermine++;
                }
                if (statut.contains("A tester")) {
                    ticketsAtester++;
                }
                //Attribution du temps de travail

                if (!fields.isNull("timeestimate")) {
                    remaining += (fields.getInt("timeestimate") / 3600);
                }
                if (!fields.isNull("timeoriginalestimate")) {
                    estimated += (fields.getInt("timeoriginalestimate") / 3600);
                }
                if (!fields.isNull("timespent")) {
                    timespent += (fields.getInt("timespent") / 3600);
                }


                //Attribution des story points
                if (!fields.isNull("customfield_10005")) {
                    double curStoryPoints = fields.getDouble("customfield_10005");
                    switch (statut) {
                        case "A qualifier":
                            spTotal += curStoryPoints;
                            spAQualifier += curStoryPoints;
                            break;
                        case "Bac d'affinage":
                            spTotal += curStoryPoints;
                            spBacAffinage += curStoryPoints;
                            break;
                        case "En attente":
                            spTotal += curStoryPoints;
                            spEnAttente += curStoryPoints;
                            break;
                        case "A Faire":
                            spTotal += curStoryPoints;
                            spAFaire += curStoryPoints;
                            break;
                        case "En cours":
                            spTotal += curStoryPoints;
                            spEnCours += curStoryPoints;
                            break;
                        case "Abandonné":
                            spTotal += curStoryPoints;
                            spAbandonne += curStoryPoints;
                            break;
                        case "Dév terminé":
                            spTotal += curStoryPoints;
                            spDevTermine += curStoryPoints;
                            break;
                        case "A valider":
                            spTotal += curStoryPoints;
                            spAvalider += curStoryPoints;
                            break;
                        case "A Livrer":
                            spTotal += curStoryPoints;
                            spAlivrer += curStoryPoints;
                            break;
                        case "A tester":
                            spTotal += curStoryPoints;
                            spATester += curStoryPoints;
                            break;
                        case "Refusé en recette":
                            spTotal += curStoryPoints;
                            spRefuseEnRecette += curStoryPoints;
                            break;
                        case "Validé en recette":
                            spTotal += curStoryPoints;
                            spValideEnRecette += curStoryPoints;
                            break;
                        case "Livré":
                            spTotal += curStoryPoints;
                            spLivre += curStoryPoints;
                            break;
                        case "Terminé":
                            spTotal += curStoryPoints;
                            spTermine += curStoryPoints;
                            break;
                        default:
                            spTotal += curStoryPoints;
                    }
                }

            }
            //Attribution du role
            String role = ID_COLLABS.get(accountId);

            //Création d'un objet Collaborateur
            Collaborator c = new Collaborator();
            c.setAccountId(accountId);
            c.setEmailAddress(emailAddress);
            c.setName(nom);
            c.setFirstName(prenom);
            c.setRole(role);
            c.setLoggedTime(timespent);
            c.setEstimatedTime(estimated);
            c.setRemainingTime(remaining);
            c.setNbTickets(total);
            c.setNbDone(ticketsDone);
            c.setNbDevDone(ticketsDevDone);
            c.setNbInProgress(ticketsInProgress);
            c.setNbToDo(ticketsToDo);
            c.setNbATester(ticketsAtester);
            c.setNbEnCoursDevTermine(ticketsEnCoursDevTermine);
            c.setSpTotal(spTotal);
            c.setSpAqualifier(spAQualifier);
            c.setSpBacAffinage(spBacAffinage);
            c.setSpEnAttente(spEnAttente);
            c.setSpAfaire(spAFaire);
            c.setSpEncours(spEnCours);
            c.setSpAbandonne(spAbandonne);
            c.setSpDevTermine(spDevTermine);
            c.setSpAvalider(spAvalider);
            c.setSpAlivrer(spAlivrer);
            c.setSpATester(spATester);
            c.setSpRefuseEnRecette(spRefuseEnRecette);
            c.setSpValideEnRecette(spValideEnRecette);
            c.setSpLivre(spLivre);
            c.setSpTermine(spTermine);
            //Ajout de l'objet dans une Hash Map
            collaborators.put(c.getAccountId(), c);
        }
        // On assigne le temps de travail sur le sprint
        HashMap<String, Float[]> planning = getPlanning(PLANNING_PATH);
        for (String s : planning.keySet()) {
            if (collaborators.containsKey(s)) {
                Collaborator c = collaborators.get(s);
                c.setTotalWorkingTime(planning.get(s)[0]);
                c.setAvailableTime(planning.get(s)[1]);
                collaborators.put(s, c);
            }
        }
        return collaborators;
    }

    //Retourne dans un objet Collaborator les informations par équipe, où "assignee=null" (MAUVAISE INTEGRATION)
    public static List<Collaborator> getUnassignedPerTeam() {
        List<Collaborator> collaborators = new ArrayList<>();
        String request = "search?jql=project=BMKP+AND+assignee=null+AND+sprint=" + SPRINT_NAME + "&maxResults=100";
        //0 : alpha , 1: beta, 2: gamma
        int[] timespent = new int[3];
        int[] estimated = new int[3];
        int[] remaining = new int[3];
        double[] spTotal = new double[3];
        int[] ticketsDone = new int[3];
        int[] ticketsDevDone = new int[3];
        int[] ticketsInProgress = new int[3];
        int[] ticketsToDo = new int[3];
        String[] accountId = new String[3];
        String[] prenom = new String[3];
        String[] nom = new String[3];
        HttpResponse<JsonNode> response = Unirest.get("https://apriltechnologies.atlassian.net/rest/api/3/" +
                request)
                .basicAuth(USERNAME, API_TOKEN)
                .header("Accept", "application/json")
                .asJson();
        JSONObject myObj = response.getBody().getObject();
        JSONArray issues = myObj.getJSONArray("issues");
        for (int i = 0; i < issues.length(); i++) {
            JSONObject issue = issues.getJSONObject(i);
            JSONObject fields = issue.getJSONObject("fields");
            JSONObject status = fields.getJSONObject("status");
            JSONArray labels = fields.getJSONArray("labels");
            for (int j = 0; j < labels.length(); j++) {
                if (labels.getString(j).contains("ALPHA")) {
                    accountId[0] = "unassignedAlpha";
                    prenom[0] = "Non";
                    nom[0] = "Assigné (Alpha)";
                    //Renseignements sur le statut de la demande
                    String statut = status.getString("name");
                    //Répartition des tickets
                    if (DONE.contains(statut)) {
                        ticketsDone[0]++;
                    } else if (IN_PROGRESS.contains(statut)) {
                        if (DEV_DONE.contains(statut)) {
                            ticketsDevDone[0]++;
                        } else {
                            ticketsInProgress[0]++;
                        }
                    } else {
                        ticketsToDo[0]++;
                    }
                    //Attribution du temps de travail

                    if (!fields.isNull("timeestimate")) {
                        remaining[0] += (fields.getInt("timeestimate") / 3600);
                    }
                    if (!fields.isNull("timeoriginalestimate")) {
                        estimated[0] += (fields.getInt("timeoriginalestimate") / 3600);
                    }
                    if (!fields.isNull("aggregatetimespent")) {
                        timespent[0] += (fields.getInt("aggregatetimespent") / 3600);
                    }
                    //Attribution des story points
                    if (!fields.isNull("customfield_10005")) {
                        double curStoryPoints = fields.getDouble("customfield_10005");
                        spTotal[0] += curStoryPoints;
                    }

                }
                if (labels.getString(j).contains("BETA")) {
                    accountId[1] = "unassignedBeta";
                    prenom[1] = "Non";
                    nom[1] = "Assigné (Beta)";
                    //Renseignements sur le statut de la demande
                    String statut = status.getString("name");
                    //Répartition des tickets
                    if (DONE.contains(statut)) {
                        ticketsDone[1]++;
                    } else if (IN_PROGRESS.contains(statut)) {
                        if (DEV_DONE.contains(statut)) {
                            ticketsDevDone[1]++;
                        } else {
                            ticketsInProgress[1]++;
                        }
                    } else {
                        ticketsToDo[1]++;
                    }
                    //Attribution du temps de travail

                    if (!fields.isNull("timeestimate")) {
                        remaining[1] += (fields.getInt("timeestimate") / 3600);
                    }
                    if (!fields.isNull("timeoriginalestimate")) {
                        estimated[1] += (fields.getInt("timeoriginalestimate") / 3600);
                    }
                    if (!fields.isNull("aggregatetimespent")) {
                        timespent[1] += (fields.getInt("aggregatetimespent") / 3600);
                    }
                    //Attribution des story points
                    if (!fields.isNull("customfield_10005")) {
                        double curStoryPoints = fields.getDouble("customfield_10005");
                        spTotal[1] += curStoryPoints;
                    }
                }
                if (labels.getString(j).contains("GAMMA") || labels.getString(j).contains("GAMA")) {
                    accountId[2] = "unassignedGamma";
                    prenom[2] = "Non";
                    nom[2] = "Assigné (Gamma)";
                    //Renseignements sur le statut de la demande
                    String statut = status.getString("name");
                    //Répartition des tickets
                    if (DONE.contains(statut)) {
                        ticketsDone[2]++;
                    } else if (IN_PROGRESS.contains(statut)) {
                        if (DEV_DONE.contains(statut)) {
                            ticketsDevDone[2]++;
                        } else {
                            ticketsInProgress[2]++;
                        }
                    } else {
                        ticketsToDo[2]++;
                    }
                    //Attribution du temps de travail

                    if (!fields.isNull("timeestimate")) {
                        remaining[2] += (fields.getInt("timeestimate") / 3600);
                    }
                    if (!fields.isNull("timeoriginalestimate")) {
                        estimated[2] += (fields.getInt("timeoriginalestimate") / 3600);
                    }
                    if (!fields.isNull("aggregatetimespent")) {
                        timespent[2] += (fields.getInt("aggregatetimespent") / 3600);
                    }
                    //Attribution des story points
                    if (!fields.isNull("customfield_10005")) {
                        double curStoryPoints = fields.getDouble("customfield_10005");
                        spTotal[2] += curStoryPoints;
                    }
                }
            }
        }
        for (int i = 0; i < 3; i++) {
            int totalTickets = ticketsDone[i] + ticketsInProgress[i] + ticketsToDo[i];
            Collaborator c = new Collaborator();
            c.setAccountId(accountId[i]);
            c.setName(nom[i]);
            c.setFirstName(prenom[i]);
            c.setLoggedTime(timespent[i]);
            c.setEstimatedTime(estimated[i]);
            c.setNbTickets(totalTickets);
            c.setNbDone(ticketsDone[i]);
            c.setNbDevDone(ticketsDevDone[i]);
            c.setNbInProgress(ticketsInProgress[i]);
            c.setNbToDo(ticketsToDo[i]);
            c.setRemainingTime(remaining[i]);
            c.setSpTotal(spTotal[i]);
            collaborators.add(c);
        }
        return collaborators;
    }


    //Fait un appel à getCollaborators() et aggrège les données par équipe
    public static HashMap<String, Team> getTeams(String[] requests) {
        HashMap<String, Collaborator> collaborators = getCollaborators(requests);
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
        Team alpha = new Team("alpha", collaboratorsAlpha);
        Team beta = new Team("beta", collaboratorsBeta);
        Team gamma = new Team("gamma", collaboratorsGamma);
        teams.put(alpha.getName(), alpha);
        teams.put(beta.getName(), beta);
        teams.put(gamma.getName(), gamma);
        return teams;
    }

    /*Retourne un objet Backlog pour un projet (depuis sa création) qui contient des informations concernant les bugs  en cours(nombre et priorité)
     * Un bug est en cours quand il n'est pas dans un des états JIRA suivants: Terminé, livré
     * Lors de l'appel à cette API, il y a plus de 100 résultats, il faut boucler jusqu'à obtenir la totalité des résultats
     * */
    public static Backlog getProjectBugs(String projectName) {
        Backlog backlog = new Backlog();
        int maxResults = 100;
        int nbBugs = 0;
        int nbBugsWVEC = 0;
        int nbBugsLow = 0;
        int nbBugsMedium = 0;
        int nbBugsHigh = 0;
        int nbBugsHighest = 0;
        int nbBugsLowWVEC = 0;
        int nbBugsMediumWVEC = 0;
        int nbBugsHighWVEC = 0;
        int nbBugsHighestWVEC = 0;
        int startAt = 0;
        String request = "search?jql=project=" + projectName + "+AND+issuetype='Bug'" + "&maxResults=" + maxResults + "&startAt=" + Integer.toString(startAt);
        HttpResponse<JsonNode> response = Unirest.get("https://apriltechnologies.atlassian.net/rest/api/3/" +
                request)
                .basicAuth(USERNAME, API_TOKEN)
                .header("Accept", "application/json")
                .asJson();
        JSONObject myObj = response.getBody().getObject();
        JSONArray issues = myObj.getJSONArray("issues");
        int total = myObj.getInt("total");
        while (startAt < total) {
            for (int j = 0; j < issues.length(); j++) {
                //Ensemble des objets JSON utiles
                JSONObject issue = issues.getJSONObject(j);
                JSONObject fields = issue.getJSONObject("fields");
                JSONObject priority = fields.getJSONObject("priority");
                JSONObject status = fields.getJSONObject("status");
                String statut = status.getString("name");
                if (statut.contains("Terminé") || statut.contains("Livré") || statut.contains("Abandonné")) {
                    continue;
                }
                //Attribution des bugs
                nbBugs++;
                if (!statut.contains("Validé en recette")) {
                    nbBugsWVEC++;
                }
                if (priority.getString("name").equals("Low")) {
                    nbBugsLow++;
                    if (!statut.contains("Validé en recette")) {
                        nbBugsLowWVEC++;
                    }
                } else if (priority.getString("name").equals("Medium")) {
                    nbBugsMedium++;
                    if (!statut.contains("Validé en recette")) {
                        nbBugsMediumWVEC++;
                    }
                } else if (priority.getString("name").equals("High")) {
                    nbBugsHigh++;
                    if (!statut.contains("Validé en recette")) {
                        nbBugsHighWVEC++;
                    }
                } else if (priority.getString("name").equals("Highest")) {
                    nbBugsHighest++;
                    if (!statut.contains("Validé en recette")) {
                        nbBugsHighestWVEC++;
                    }
                }
            }
            //on incrémente du nombre de résultats dans la requête
            startAt += maxResults;
            request = "search?jql=project=" + projectName + "+AND+issuetype='Bug'" + "&maxResults=100&startAt=" + startAt;
            response = Unirest.get("https://apriltechnologies.atlassian.net/rest/api/3/" +
                    request)
                    .basicAuth(USERNAME, API_TOKEN)
                    .header("Accept", "application/json")
                    .asJson();
            myObj = response.getBody().getObject();
            issues = myObj.getJSONArray("issues");
        }
        backlog.setProjectName(projectName);
        backlog.setNbBugs(nbBugs);
        backlog.setNbBugsWVEC(nbBugsWVEC);
        backlog.setNbBugsLow(nbBugsLow);
        backlog.setNbBugsMedium(nbBugsMedium);
        backlog.setNbBugsHigh(nbBugsHigh);
        backlog.setNbBugsHighest(nbBugsHighest);
        backlog.setNbBugsLowWVEC(nbBugsLowWVEC);
        backlog.setNbBugsMediumWVEC(nbBugsMediumWVEC);
        backlog.setNbBugsHighWVEC(nbBugsHighWVEC);
        backlog.setNbBugsHighestWVEC(nbBugsHighestWVEC);
        return backlog;

    }

    /* Returns an array of integer of length 'nbDays'. Each element corresponds to the number of bug created
     *  Index i represent the number of bugs created (nbDays-i) ago
     */
    public static int[] getBugsCreated(int nbDays, String projectName) throws UnsupportedEncodingException {
        int[] bugsCreated = new int[nbDays];
        int maxResults = 100;
        String request = "search?jql=project=" + projectName + "+AND+issuetype='Bug'+AND+created" +
                URLEncoder.encode(">=", "utf-8") + "-" + nbDays + "d&maxResults=" + maxResults;
        HttpResponse<JsonNode> response = Unirest.get("https://apriltechnologies.atlassian.net/rest/api/3/" +
                request)
                .basicAuth(USERNAME, API_TOKEN)
                .header("Accept", "application/json")
                .asJson();
        JSONObject myObj = response.getBody().getObject();
        JSONArray issues = myObj.getJSONArray("issues");
        for (int j = 0; j < issues.length(); j++) {
            //Ensemble des objets JSON utiles
            JSONObject issue = issues.getJSONObject(j);
            JSONObject fields = issue.getJSONObject("fields");
            String dateCreation = fields.getString("created");
            LocalDateTime ldtBug = Sprint.toLocalDateTime(dateCreation);
            int days = (int) DAYS.between(ldtBug, LocalDateTime.now());
            bugsCreated[nbDays - 1 - days] += 1;
        }
        return bugsCreated;
    }

    /* Returns an array of integer of length 'nbDays'. Each element corresponds to the number of bug resolved (Jira status "terminé/livré")
     *  Index i represent the number of bugs resolved (nbDays-i) ago
     */
    public static int[] getBugsResolved(int nbDays, String projectName) throws UnsupportedEncodingException {
        int[] bugsResolved = new int[nbDays];
        int maxResults = 100;
        int startAt = 0;
        String request = "search?jql=project=" + projectName + "+AND+issuetype='Bug'+AND+updated" +
                URLEncoder.encode(">=", "utf-8") + "-" + nbDays + "d&maxResults=" + maxResults + "&startAt=" + startAt;
        HttpResponse<JsonNode> response = Unirest.get("https://apriltechnologies.atlassian.net/rest/api/3/" +
                request)
                .basicAuth(USERNAME, API_TOKEN)
                .header("Accept", "application/json")
                .asJson();
        JSONObject myObj = response.getBody().getObject();
        JSONArray issues = myObj.getJSONArray("issues");
        int total = myObj.getInt("total");
        while (startAt < total) {
            for (int j = 0; j < issues.length(); j++) {
                //Ensemble des objets JSON utiles
                JSONObject issue = issues.getJSONObject(j);
                JSONObject fields = issue.getJSONObject("fields");
                JSONObject status = fields.getJSONObject("status");
                String statut = status.getString("name");
                if (!statut.contains("Terminé") || !statut.contains("Livré")) {
                    String updateDate = fields.getString("updated");
                    LocalDateTime ldtBug = Sprint.toLocalDateTime(updateDate);
                    int days = (int) DAYS.between(ldtBug, LocalDateTime.now());
                    bugsResolved[nbDays - 1 - days] += 1;
                }

            }
            startAt += maxResults;
            request = "search?jql=project=" + projectName + "+AND+issuetype='Bug'+AND+updated" +
                    URLEncoder.encode(">=", "utf-8") + "-" + nbDays + "d&maxResults=" + maxResults + "&startAt=" + startAt;
            response = Unirest.get("https://apriltechnologies.atlassian.net/rest/api/3/" +
                    request)
                    .basicAuth(USERNAME, API_TOKEN)
                    .header("Accept", "application/json")
                    .asJson();
            myObj = response.getBody().getObject();
            issues = myObj.getJSONArray("issues");
        }
        return bugsResolved;
    }

    /* Returns an array of size (nbSprints) of the lastly closed sprints including the lastly active sprint
     */
    public static ArrayList<Sprint> getLastlyClosedSprints(int nbSprints) {
        ArrayList<Sprint> sprints = new ArrayList<>(nbSprints);
        String startDate = "";
        String endDate = "";
        String sprintName = "";
        int sprintId = 0;
        int lastlyActiveSprintIndex = -1;
        HttpResponse<JsonNode> response = Unirest.get("https://apriltechnologies.atlassian.net/rest/agile/1.0/board/" + BOARD_ID + "/sprint")
                .basicAuth(USERNAME, API_TOKEN)
                .header("Accept", "application/json")
                .asJson();
        JSONObject myObj = response.getBody().getObject();
        JSONArray values = myObj.getJSONArray("values");
        for (int i = 0; i < values.length(); i++) {
            JSONObject value = values.getJSONObject(i);
            if (value.getString("state").equals("active") && value.getString("name").equals("Sprint 30")) {
                lastlyActiveSprintIndex = i;
            }
        }
        int i = 0;
        while (i < nbSprints) {
            JSONObject value = values.getJSONObject(lastlyActiveSprintIndex - i);
            sprintName = value.getString("name");
            startDate = value.getString("startDate");
            endDate = value.getString("endDate");
            sprintId = parseInt(value.getString("id"));
            Sprint s = new Sprint();
            s.setName(sprintName);
            s.setId(sprintId);
            s.setStartDate(Sprint.toLocalDateTime(startDate));
            s.setEndDate(Sprint.toLocalDateTime(endDate));
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
    public static double[] getCommitment(Sprint s, String boardId) throws UnsupportedEncodingException {
        double[] commitment = new double[4];
        double initialCommitment = 0;
        double finalCommitment = 0;
        double addedWork = 0;
        double completedWork = 0;
        String request = "https://apriltechnologies.atlassian.net/rest/greenhopper/1.0/rapid/charts/sprintreport" +
                "?rapidViewId=" + boardId +
                "&sprintId=" + s.getId();
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

        if(completedIssuesEstimateSum.has("value")){
            completedWork += completedIssuesEstimateSum.getInt("value");
            initialCommitment += completedIssuesEstimateSum.getInt("value");
        }
        if(completedIssuesInitialEstimateSum.has("value")){
            initialCommitment += completedIssuesInitialEstimateSum.getInt("value");
        }
        if(issuesNotCompletedEstimateSum.has("value")){
            initialCommitment += issuesNotCompletedEstimateSum.getInt("value");
        }
        if(issuesNotCompletedInitialEstimateSum.has("value")){
            initialCommitment += issuesNotCompletedInitialEstimateSum.getInt("value");
        }
        if(allIssuesEstimateSum.has("value")){
            finalCommitment += allIssuesEstimateSum.getInt("value");
        }
        //Added issues
        Iterator<String> keys = addedIssues.keys();
        while(keys.hasNext()){
            String issueID = keys.next();
            addedWork += getStoryPoint(issueID);
        }
        commitment[0] = initialCommitment;
        commitment[1] = finalCommitment;
        commitment[2] = addedWork;
        commitment[3] = completedWork;
        return commitment;
    }

    public static double getStoryPoint(String issueID){
        double spIssue = 0;
        String request = "https://apriltechnologies.atlassian.net/rest/api/3/search?jql=project=BMKP+AND+issue=" + issueID;
        HttpResponse<JsonNode> response = Unirest.get(request)
                .basicAuth(USERNAME, API_TOKEN)
                .header("Accept", "application/json")
                .asJson();
        JSONObject myObj = response.getBody().getObject();
        JSONArray issues = myObj.getJSONArray("issues");
        for (int j = 0; j < issues.length(); j++) {
            //Ensemble des objets JSON utiles
            JSONObject issue = issues.getJSONObject(j);
            JSONObject fields = issue.getJSONObject("fields");
            if (!fields.isNull("customfield_10005")) {
                spIssue = fields.getDouble("customfield_10005");
            }
        }
        return spIssue;
    }
    //Lit le planning (CSV) et retourne les informations dans une table de hachage <accountId, [totalWorkingTime, availableTime]>
    public static HashMap<String, Float[]> getPlanning(String PLANNING_PATH) {
        HashMap<String, Float[]> planning = new HashMap<>();
        float totalWorkingTime = 0;
        float availableTime = 0;
        String accountId;
        final int INDEX_ACC_ID = 2; //3ème colonne
        int FIRST_ROW = 4; //5ème colonne
        int startIndex = -1;
        int endIndex = -1;
        int todayIndex = -1;
        try {
            FileReader filereader = new FileReader(PLANNING_PATH);
            CSVParser parser = new CSVParserBuilder().withSeparator(',').build();
            CSVReader csvReader = new CSVReaderBuilder(filereader)
                    .withCSVParser(parser)
                    .build();
            //On saute 4 lignes du CSV
            for (int i = 0; i < 4; i++) {
                csvReader.readNext();
            }
            String[] dates = csvReader.readNext();
            for (int i = 0; i < dates.length; i++) {
                if (dates[i].equals(START_DAY_SPRINT)) {
                    startIndex = i;
                }
                if (dates[i].equals(END_DAY_SPRINT)) {
                    endIndex = i;
                }
                if (dates[i].equals(TODAY)) {
                    todayIndex = i;
                }
            }
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
                    Float[] workTime = new Float[2];
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




