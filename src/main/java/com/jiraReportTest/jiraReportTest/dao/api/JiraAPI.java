package com.jiraReportTest.jiraReportTest.dao.api;

import com.jiraReportTest.jiraReportTest.dto.jiraApi.*;
import com.jiraReportTest.jiraReportTest.model.*;
import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import kong.unirest.json.JSONArray;
import kong.unirest.json.JSONObject;
import org.springframework.stereotype.Repository;
import org.springframework.web.reactive.function.client.ExchangeFilterFunctions;
import org.springframework.web.reactive.function.client.WebClient;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import static com.jiraReportTest.jiraReportTest.dao.api.API.*;

import static java.time.temporal.ChronoUnit.DAYS;

@Repository
public class JiraAPI {
    final static String JIRA_API_URL = "https://apriltechnologies.atlassian.net/rest/api/3/";
    // JSONObject and JSONArray names in JIRA API's response
    final static String JSON_ISSUES = "issues";
    final static String JSON_FIELDS = "fields";
    final static String JSON_STATUS = "status";
    final static String JSON_PRIORITY = "priority";
    // JSONObject's keys
    final static String JSON_KEY_STORYPOINTS = "customfield_10005";
    // JIRA STATUS & CUSTOM STATUS
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
    final static ArrayList<String> DONE = new ArrayList<>(Arrays.asList(LIVRE, TERMINE, VALIDE, VALIDE_RECETTE));
    final static ArrayList<String> DONE_BUGS = new ArrayList<>(Arrays.asList(LIVRE, TERMINE, VALIDE_RECETTE, ABANDONNE));
    final static ArrayList<String> IN_PROGRESS = new ArrayList<>(Arrays.asList(EN_COURS, DEV_TERMINE, REFUSE_RECETTE, EN_ATTENTE, A_TESTER, A_LIVRER));
    final static ArrayList<String> DEV_DONE = new ArrayList<>(Arrays.asList(A_TESTER, A_LIVRER));
    final static ArrayList<String> DEV_DONE_EN_COURS = new ArrayList<>(Arrays.asList(DEV_TERMINE, EN_COURS));

    private JiraAPI(){}
    /* Main method : Returns a Collaborator object if it has at least one ticket
     * else return null
     */
    public static Collaborator getCollaborator(String accId, String label, Sprint s) {
        /*
        Variables
        */
        String request = JIRA_API_URL + "search?jql=project=" + PROJECT_NAME + "+AND+assignee=" + accId +
                "+AND+sprint=" + s.getId() + "+AND+labels=" + label + "&maxResults=" + MAX_RESULTS;
        double timespent = 0;
        double estimated = 0;
        double remaining = 0;
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
        String role;
        int total;
        String statut;
        JiraDto c = JiraAPI.connectToJiraAPI(request);
        /* New logic with web client
         *
         */
        total = c.getTotal();
        // When assignee has no tickets assigned
        if (total == 0) {
            return null;
        }
        //Setting assignee's personal information
        for (IssueDto i : c.getIssues()) {
            statut = i.getFields().getStatus().getName();
            if(i.getFields().getAssignee() != null){
                AssigneeDto assignee = i.getFields().getAssignee();
                accountId = assignee.getAccountId();
                if (emailAddress.isEmpty() && assignee.getEmailAddress() != null) {
                    emailAddress = assignee.getEmailAddress();
                    int indexDot = emailAddress.indexOf('.');
                    int indexAt = emailAddress.indexOf('@');
                    prenom = emailAddress.substring(0, indexDot);
                    nom = emailAddress.substring(indexDot + 1, indexAt);
                } else if (nom.isEmpty() && prenom.isEmpty()) {
                    String fullName = assignee.getDisplayName();
                    int fullNameLength = fullName.length();
                    int indexSpace = fullName.indexOf(' ');
                    if (indexSpace < 0) {
                        prenom = fullName;
                    } else {
                        prenom = fullName.substring(0, indexSpace);
                        nom = fullName.substring(indexSpace + 1, fullNameLength);
                    }
                }
            }
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
            remaining += i.getFields().getTimeestimate() / (double) 3600;
            estimated += i.getFields().getTimeoriginalestimate() / (double)3600;
            timespent += i.getFields().getTimespent() / (double)3600;
            //Setting story points
            double curStoryPoints = i.getFields().getStoryPoints();
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
        // When assignee is null
        if (request.contains("null")) {
            prenom = "Non";
            nom = "Assigné";
            accountId = UNASSIGNED;
            role = "none";
        }else{
            role = ID_COLLABS.get(accountId);
            //Calling tempo API
            timespent = JiraTempoAPI.getWorklogByAccountID(accId, s.getStartDate().format(dtfAmerica), s.getEndDate().format(dtfAmerica));
        }
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
    public static int[] getCreated(int nbDays, String projectName, String issueType) {
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
                URLEncoder.encode(">=", StandardCharsets.UTF_8) + "-" + nbDays + "d&maxResults=" + MAX_RESULTS + "&startAt=" + startAt;
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
                days = (int) DAYS.between(ldtBug, LocalDate.parse(TODAY.format(dtfAmerica)));
                bugsCreated[nbDays - days] += 1;
            }
            //on incrémente du nombre de résultats dans la requête
            startAt += MAX_RESULTS;
            request = JIRA_API_URL + "search?jql=project=" + projectName + "+AND+issuetype='" + issueType + "'+AND+created" +
                    URLEncoder.encode(">=", StandardCharsets.UTF_8) + "-" + nbDays + "d&maxResults=" + MAX_RESULTS + "&startAt=" + startAt;
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
    public static int[] getResolved(int nbDays, String projectName, String issueType) {
        /*
        Variables
         */
        int[] bugsResolved = new int[nbDays + 1];
        int startAt = 0;
        String statut;
        String updateDate;
        LocalDate ldtBug;
        int days;
        String today = LocalDateTime.now().format(dtfAmerica);
        String request = JIRA_API_URL + "search?jql=project=" + projectName + "+AND+issuetype='" + issueType + "'+AND+updated" +
                URLEncoder.encode(">=", StandardCharsets.UTF_8) + "-" + nbDays + "d&maxResults=" + MAX_RESULTS + "&startAt=" + startAt;
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
            request = JIRA_API_URL + "search?jql=project=" + projectName + "+AND+issuetype='" + issueType + "'+AND+updated" +
                    URLEncoder.encode(">=", StandardCharsets.UTF_8) + "-" + nbDays + "d&maxResults=" + MAX_RESULTS + "&startAt=" + startAt;
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
    public static int[] getInProgress(int nbDays, String projectName, String issueType) {
        /*
        Variables
         */
        int[] inProgress = new int[nbDays + 1];
        int startAt = 0;
        String statut;
        String updateDate;
        LocalDate ldtBug;
        int days;
        String today = LocalDateTime.now().format(dtfAmerica);
        String request = JIRA_API_URL + "search?jql=project=" + projectName + "+AND+issuetype='" + issueType + "'+AND+updated" +
                URLEncoder.encode(">=", StandardCharsets.UTF_8) + "-" + nbDays + "d&maxResults=" + MAX_RESULTS + "&startAt=" + startAt;
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
            request = JIRA_API_URL + "search?jql=project=" + projectName + "+AND+issuetype='" + issueType + "'+AND+updated" +
                    URLEncoder.encode(">=", StandardCharsets.UTF_8) + "-" + nbDays + "d&maxResults=" + MAX_RESULTS + "&startAt=" + startAt;
            response = Unirest.get(request)
                    .basicAuth(USERNAME, API_TOKEN)
                    .header("Accept", "application/json")
                    .asJson();
            myObj = response.getBody().getObject();
            issues = myObj.getJSONArray("issues");
        }
        return inProgress;
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

    public static JiraDto connectToJiraAPI(String request){
        WebClient webClient = WebClient.builder()
                .filter(ExchangeFilterFunctions.basicAuthentication(USERNAME, API_TOKEN))
                .defaultHeader("Accept", "application/json")
                .build();
        return webClient
                .get()
                .uri(request)
                .retrieve()
                .bodyToMono(JiraDto.class)
                .block();
    }
}
