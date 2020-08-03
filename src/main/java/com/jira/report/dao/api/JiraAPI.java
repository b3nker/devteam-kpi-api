package com.jira.report.dao.api;

import com.jira.report.dto.jiraApi.AssigneeDto;
import com.jira.report.dto.jiraApi.FieldsDto;
import com.jira.report.dto.jiraApi.IssueDto;
import com.jira.report.dto.jiraApi.JiraDto;
import com.jira.report.model.Collaborator;
import com.jira.report.model.Sprint;
import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import kong.unirest.json.JSONArray;
import kong.unirest.json.JSONObject;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import static com.jira.report.dao.api.API.*;

import static java.time.temporal.ChronoUnit.DAYS;

@Service
@RequiredArgsConstructor
public class JiraAPI {
    static final String JIRA_API_URL = "rest/api/3/";
    // JSONObject and JSONArray names in JIRA API's response
    static final String JSON_ISSUES = "issues";
    static final String JSON_FIELDS = "fields";
    static final String JSON_STATUS = "status";
    static final String JSON_PRIORITY = "priority";
    // JSONObject's keys
    static final String JSON_KEY_STORYPOINTS = "customfield_10005";
    // JIRA STATUS & CUSTOM STATUS
    static final String A_QUALIFIER = "A qualifier";
    static final String A_FAIRE = "A Faire";
    static final String BAC_AFFINAGE = "Bac d'affinage";

    static final String TERMINE = "Terminé";
    static final String LIVRE = "Livré";
    static final String VALIDE_RECETTE = "Validé en recette";
    static final String VALIDE = "Validé";
    static final String ABANDONNE = "Abandonné";
    static final String EN_COURS = "En cours";
    static final String DEV_TERMINE = "Dév terminé";
    static final String REFUSE_RECETTE = "Refusé en recette";
    static final String EN_ATTENTE = "En attente";
    static final String A_LIVRER = "A Livrer";
    static final String A_TESTER = "A tester";
    static final String A_VALIDER = "A valider";
    static final String TEST_CROISE = "Test croisé";

    static final ArrayList<String> DONE = new ArrayList<>(Arrays.asList(LIVRE, TERMINE, VALIDE, VALIDE_RECETTE));
    static final ArrayList<String> DONE_BUGS = new ArrayList<>(Arrays.asList(LIVRE, TERMINE, VALIDE_RECETTE, ABANDONNE));
    static final ArrayList<String> IN_PROGRESS = new ArrayList<>(Arrays.asList(EN_COURS, DEV_TERMINE, REFUSE_RECETTE, EN_ATTENTE, A_TESTER, A_LIVRER));
    static final ArrayList<String> DEV_DONE = new ArrayList<>(Arrays.asList(A_TESTER, A_LIVRER));
    static final ArrayList<String> DEV_DONE_EN_COURS = new ArrayList<>(Arrays.asList(DEV_TERMINE, EN_COURS));

    private final WebClient jiraWebClient;


    /* Main method : Returns a Collaborator object if it has at least one ticket
     * else return null
     */
    public Collaborator getCollaborator(String accId, String label, Sprint s) {
        /*
        Variables
        */
        String request = BASE_URL + JIRA_API_URL + "search?jql=project=" + PROJECT_NAME + "+AND+assignee=" + accId +
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
        double spTestCroise = 0;
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
        JiraDto c = connectToJiraAPI(request);
        total = c.getTotal();
        // When assignee has no tickets assigned
        if (total == 0) {
            return null;
        }
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
                case A_QUALIFIER:
                    spAQualifier += curStoryPoints;
                    break;
                case BAC_AFFINAGE:
                    spBacAffinage += curStoryPoints;
                    break;
                case EN_ATTENTE:
                    spEnAttente += curStoryPoints;
                    break;
                case A_FAIRE:
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
                case TEST_CROISE:
                    spTestCroise += curStoryPoints;
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
                .spTestCroise(spTestCroise)
                .build();
    }

    /* Returns an array of integer containing information on bugs/incidents since project's creation (number and priority)
     * A bug is active when NOT in following jira states :  LIVRE, TERMINE, VALIDE_RECETTE, ABANDONNE
     */
    public int[] getProjectIncidentBug(String projectName, String issueType) {
        /*
        Variables
         */
        // 0 : total, 1: low, 2: medium, 3: high, 4: highest
        int[] incidentsBugs = new int[5];
        int startAt = 0;
        String request = BASE_URL + JIRA_API_URL + "search?jql=project=" + projectName +
                "+AND+issuetype='" + issueType + "'&maxResults=" + MAX_RESULTS + "&startAt=" + startAt;
        JiraDto c = connectToJiraAPI(request);
        int total = c.getTotal();
        /*
        Logic
         */
        do{
            for (IssueDto i : c.getIssues()) {
                FieldsDto fDto = i.getFields();
                String statut = fDto.getStatus().getName();
                String priority = fDto.getPriority().getName();
                if(!DONE_BUGS.contains(statut)){
                    incidentsBugs[0]++;
                    switch (priority) {
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
            request = BASE_URL + JIRA_API_URL + "search?jql=project=" + projectName +
                    "+AND+issuetype='" + issueType + "'&maxResults=" + MAX_RESULTS + "&startAt=" + startAt;
            c = connectToJiraAPI(request);
        }while(startAt< total);
        return incidentsBugs;
    }

    /* Returns an array of integer of length 'nbDays'. Each element corresponds to the number of bug/incident created
     *  Index i represent the number of bugs created (nbDays-i) ago
     */
    public int[] getCreated(int nbDays, String projectName, String issueType) {
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
        String request = BASE_URL + JIRA_API_URL + "search?jql=project=" + projectName + "+AND+issuetype='" + issueType + "'+AND+created" +
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
            request = BASE_URL + JIRA_API_URL + "search?jql=project=" + projectName + "+AND+issuetype='" + issueType + "'+AND+created" +
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
    public int[] getResolved(int nbDays, String projectName, String issueType) {
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
        String request = BASE_URL + JIRA_API_URL + "search?jql=project=" + projectName + "+AND+issuetype='" + issueType + "'+AND+updated" +
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
            request = BASE_URL + JIRA_API_URL + "search?jql=project=" + projectName + "+AND+issuetype='" + issueType + "'+AND+updated" +
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
    public int[] getInProgress(int nbDays, String projectName, String issueType) {
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
        String request = BASE_URL + JIRA_API_URL + "search?jql=project=" + projectName + "+AND+issuetype='" + issueType + "'+AND+updated" +
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
            request = BASE_URL + JIRA_API_URL + "search?jql=project=" + projectName + "+AND+issuetype='" + issueType + "'+AND+updated" +
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
    public double getStoryPoint(String issueID) {
        /*
        Variables
         */
        double spIssue = 0;
        String request = BASE_URL + JIRA_API_URL + "search?jql=project=" + PROJECT_NAME + "+AND+issue=" + issueID;
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

    public JiraDto connectToJiraAPI(String request){
        return jiraWebClient
                .get()
                .uri(request)
                .retrieve()
                .bodyToMono(JiraDto.class)
                .block();
    }
}