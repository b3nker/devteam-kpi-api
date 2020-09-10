package com.jira.report.dao.api;

import com.jira.report.config.JiraReportConfigApi;
import com.jira.report.config.JiraReportConfigIndividuals;
import com.jira.report.config.JiraReportConfigQuery;
import com.jira.report.dto.jira.AssigneeDto;
import com.jira.report.dto.jira.FieldsDto;
import com.jira.report.dto.jira.IssueDto;
import com.jira.report.dto.jira.JiraDto;
import com.jira.report.model.entity.StoryPointEntity;
import com.jira.report.model.entity.TicketEntity;
import com.jira.report.model.entity.CollaboratorEntity;
import com.jira.report.model.entity.SprintEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.jira.report.dao.api.constant.APIConstant.TODAY;
import static com.jira.report.dao.api.constant.APIConstant.*;
import static java.time.temporal.ChronoUnit.DAYS;

@Service
@Slf4j
public class JiraAPI {

    private final WebClient jiraWebClient;
    private final JiraTempoAPI jiraTempoAPI;
    private final String baseUrl;
    private final String jiraApiUrl;
    private final Map<String, String> idCollabs;
    private final String unassignedAccountId;

    public JiraAPI(JiraTempoAPI jiraTempoAPI,
                   JiraReportConfigQuery jiraReportConfigQuery,
                   JiraReportConfigIndividuals jiraReportConfigIndividuals,
                   JiraReportConfigApi jiraReportConfigApi,
                   WebClient jiraWebClient) {
        this.jiraTempoAPI = jiraTempoAPI;
        this.jiraWebClient = jiraWebClient;
        this.baseUrl = jiraReportConfigApi.getBaseUrl();
        this.jiraApiUrl = jiraReportConfigApi.getJiraApiUrl();
        this.idCollabs = jiraReportConfigIndividuals.getCollabs();
        this.unassignedAccountId = jiraReportConfigQuery.getUnassignedAccountId();
    }


    /**
     * Creates a Collaborator object. Fetch "timespent" data using method from JiraTempoAPI
     * @param accId Collaborator accountId
     * @param s Sprint, to request data on a period of time and fetch data in JiraTempoAPI
     * @param projectName Project name linked to the specified sprint
     * @param maxResults Number of results returned from GET method.
     * @return A Collaborator object, null if no ticket is assigned to it
     */
    public CollaboratorEntity getCollaborator(String accId, String label, SprintEntity s, String projectName, int maxResults) {
          /*
        Variables
        */
        String request = baseUrl + jiraApiUrl + SEARCH_JQL_PROJECT + projectName + JQL_ASSIGNEE + accId +
                JQL_SPRINT + s.getId() + JQL_MAX_RESULTS + maxResults;
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
        double spMergeRequest = 0;
        int ticketsTotal = 0;
        int ticketsAQualifier = 0;
        int ticketsBacAffinage = 0;
        int ticketsEnAttente = 0;
        int ticketsAFaire = 0;
        int ticketsEnCours = 0;
        int ticketsAbandonne = 0;
        int ticketsDevTermine = 0;
        int ticketsAvalider = 0;
        int ticketsAlivrer = 0;
        int ticketsATester = 0;
        int ticketsRefuseEnRecette = 0;
        int ticketsValideEnRecette = 0;
        int ticketsLivre = 0;
        int ticketsTermine = 0;
        int ticketsTestCroise = 0;
        int ticketsValide = 0;
        int ticketsMergeRequest = 0;
        int ticketsOverEstimated = 0;
        int ticketsUnderEstimated = 0;
        int ticketsBug = 0;
        int ticketsUS = 0;
        int ticketsTask = 0;
        String accountId = "";
        String emailAddress = "";
        String nom = "";
        String prenom = "";
        String role;
        String statut;
        String issueType;
        List<String> assignedIssues = new ArrayList<>();
        JiraDto c = connectToJiraAPI(request);
        // When assignee has no tickets assigned
        if (c.getTotal() == 0) {
            return null;
        }
        for (IssueDto i : c.getIssues()) {
            statut = i.getFields().getStatus().getName();
            //Setting assignee's info
            if (i.getFields().getAssignee() != null) {
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
            // Setting working time
            double ticketEstimatedTime = i.getFields().getTimeoriginalestimate() / (double) 3600;
            remaining += i.getFields().getTimeestimate() / (double) 3600;
            estimated += ticketEstimatedTime;
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
                    ticketsEnCours++;
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
                    break;
                case MERGE_REQUEST:
                    spMergeRequest += curStoryPoints;
                    break;
                default:
                    break;
            }
            //Setting tickets
            issueType = i.getFields().getIssuetype().getName();
            if(!JQL_ISSUE_TYPE_SUBTASK.equals(issueType)){
                assignedIssues.add(i.getKey());
                ticketsTotal++;
                switch (statut) {
                    case A_QUALIFIER:
                        ticketsAQualifier++;
                        break;
                    case BAC_AFFINAGE:
                        ticketsBacAffinage++;
                        break;
                    case EN_ATTENTE:
                        ticketsEnAttente++;
                        break;
                    case A_FAIRE:
                        ticketsAFaire++;
                        break;
                    case EN_COURS:
                        ticketsEnCours++;
                        break;
                    case ABANDONNE:
                        ticketsAbandonne++;
                        break;
                    case DEV_TERMINE:
                        ticketsDevTermine++;
                        break;
                    case A_VALIDER:
                        ticketsAvalider++;
                        break;
                    case A_LIVRER:
                        ticketsAlivrer++;
                        break;
                    case A_TESTER:
                        ticketsATester++;
                        break;
                    case REFUSE_RECETTE:
                        ticketsRefuseEnRecette++;
                        break;
                    case VALIDE_RECETTE:
                        ticketsValideEnRecette++;
                        break;
                    case LIVRE:
                        ticketsLivre++;
                        break;
                    case TERMINE:
                        ticketsTermine++;
                        break;
                    case TEST_CROISE:
                        ticketsTestCroise++;
                        break;
                    case VALIDE:
                        ticketsValide++;
                        break;
                    case MERGE_REQUEST:
                        ticketsMergeRequest++;
                        break;
                    default:
                        break;
                }
                if(JQL_ISSUE_TYPE_BUG.equals(issueType)){
                    ticketsBug++;
                }else if(JQL_ISSUE_TYPE_TASK.equals(issueType)){
                    ticketsTask++;
                }else if(JQL_ISSUE_TYPE_US.equals(issueType)){
                    ticketsUS++;
                }
                if(AT_LEAST_DEV_DONE.contains(statut) && !request.contains("null")){
                    double ticketLoggedTime = jiraTempoAPI.getWorklogByIssue(accountId, i.getKey(),
                            s.getStartDate().format(dtfAmerica),  s.getEndDate().format(dtfAmerica));
                    if(ticketLoggedTime >= ticketEstimatedTime * UPPER_BOUND_MULTIPLIER){
                        ticketsUnderEstimated++;
                    }else if(ticketLoggedTime <= ticketEstimatedTime * LOWER_BOUND_MULTIPLIER){
                        ticketsOverEstimated++;
                    }
                }
            }
        }
        // When assignee is null
        if (request.contains("null")) {
            prenom = UNASSIGNED_FIRST_NAME;
            nom = UNASSIGNED_LAST_NAME;
            accountId = unassignedAccountId;
            role = UNASSIGNED_ROLE;
        } else {
            //Replace ":" with "_"
            role = this.idCollabs.get(accountId);
            //Calling tempo API
            timespent = jiraTempoAPI.getWorklogByAccountID(accId, s.getStartDate().format(dtfAmerica), s.getEndDate().format(dtfAmerica));
        }
        TicketEntity tickets = TicketEntity.builder()
                .total(ticketsTotal)
                .aQualifier(ticketsAQualifier)
                .bacAffinage(ticketsBacAffinage)
                .enAttente(ticketsEnAttente)
                .aFaire(ticketsAFaire)
                .enCours(ticketsEnCours)
                .abandonne(ticketsAbandonne)
                .devTermine(ticketsDevTermine)
                .aValider(ticketsAvalider)
                .aLivrer(ticketsAlivrer)
                .aTester(ticketsATester)
                .refuseEnRecette(ticketsRefuseEnRecette)
                .valideEnRecette(ticketsValideEnRecette)
                .livre(ticketsLivre)
                .termine(ticketsTermine)
                .testCroise(ticketsTestCroise)
                .valide(ticketsValide)
                .mergeRequest(ticketsMergeRequest)
                .overEstimated(ticketsOverEstimated)
                .underEstimated(ticketsUnderEstimated)
                .ticketsBug(ticketsBug)
                .ticketsUS(ticketsUS)
                .ticketsTask(ticketsTask)
                .build();
        StoryPointEntity storyPointsEntity = StoryPointEntity.builder()
                .total(spTotal)
                .aQualifier(spAQualifier)
                .bacAffinage(spBacAffinage)
                .enAttente(spEnAttente)
                .aFaire(spAFaire)
                .enCours(spEnCours)
                .abandonne(spAbandonne)
                .devTermine(spDevTermine)
                .aValider(spAvalider)
                .aLivrer(spAlivrer)
                .aTester(spATester)
                .refuseEnRecette(spRefuseEnRecette)
                .valideEnRecette(spValideEnRecette)
                .livre(spLivre)
                .termine(spTermine)
                .testCroise(spTestCroise)
                .mergeRequest(spMergeRequest)
                .build();
        //Création d'un objet Collaborateur
        return CollaboratorEntity.builder()
                .accountId(accountId)
                .emailAddress(emailAddress)
                .name(nom)
                .firstName(prenom)
                .role(role)
                .loggedTime(timespent)
                .estimatedTime(estimated)
                .remainingTime(remaining)
                .storyPoints(storyPointsEntity)
                .tickets(tickets)
                .assignedIssues(assignedIssues)
                .build();
    }


    /**
     * Creates an array of Integer where each index represents number of issuetype with specified priority
     * @param projectName Project name from which we desire to collect data
     * @param issueType Type of issue we want to collect (bug, incident,...)
     * @param maxResults Number of results returned from GET method.
     * @return An array of integer
     */
    public int[] getProjectIncidentBug(String projectName, String issueType, int maxResults) {
        /*
        Variables
         */
        // 0 : total, 1: low, 2: medium, 3: high, 4: highest
        int[] incidentsBugs = new int[5];
        int startAt = 0;
        String request = baseUrl + jiraApiUrl + SEARCH_JQL_PROJECT + projectName + JQL_ISSUE_TYPE + "'" + issueType +
                "'" + JQL_MAX_RESULTS + maxResults + JQL_START_AT + startAt;
        JiraDto c = connectToJiraAPI(request);
        int total = c.getTotal();
        /*
        Logic
         */
        do {
            for (IssueDto i : c.getIssues()) {
                FieldsDto fDto = i.getFields();
                String statut = fDto.getStatus().getName();
                String priority = fDto.getPriority().getName();
                if (!BUGS_DONE.contains(statut)) {
                    incidentsBugs[0]++;
                    switch (priority) {
                        case PRIORITY_LOW:
                            incidentsBugs[1]++;
                            break;
                        case PRIORITY_MEDIUM:
                            incidentsBugs[2]++;
                            break;
                        case PRIORITY_HIGH:
                            incidentsBugs[3]++;
                            break;
                        case PRIORITY_HIGHEST:
                            incidentsBugs[4]++;
                            break;
                        default:
                            break;
                    }
                }
            }
            startAt += maxResults;
            request = baseUrl + jiraApiUrl + SEARCH_JQL_PROJECT + projectName + JQL_ISSUE_TYPE + "'" + issueType +
                    "'" + JQL_MAX_RESULTS + maxResults + JQL_START_AT + startAt;
            c = connectToJiraAPI(request);
        } while (startAt < total);
        return incidentsBugs;
    }

    /**
     * Creates an array of Integer where each index corresponds to the number of issuetype created per day
     * Index (nbDays) being today. Thus, index i represent the number of bugs created (nbDays-i) ago
     * @param nbDays Length of the array
     * @param projectName Project name from which we desire to collect data
     * @param issueType Type of issue we want to collect (bug, incident,...)
     * @param maxResults Number of results returned from GET method.
     * @return An array of Integer
     */

    public int[] getCreated(int nbDays, String projectName, String issueType, int maxResults) {
        /*
        Variables
         */
        int[] bugsCreated = new int[nbDays + 1];
        int startAt = 0;
        int days;
        String dateCreation;
        LocalDate ldtBug;
        String request = baseUrl + jiraApiUrl + SEARCH_JQL_PROJECT + projectName + JQL_ISSUE_TYPE + "'" +  issueType +
                "'+AND+created>=-" + nbDays + "d" + JQL_MAX_RESULTS + maxResults + JQL_START_AT + startAt;
        JiraDto c = connectToJiraAPI(request);
        int total = c.getTotal();
        /*
        Logic
         */
        do {
            for (IssueDto i : c.getIssues()) {
                dateCreation = i.getFields().getCreated().substring(0, 10);
                ldtBug = LocalDate.parse(dateCreation);
                days = (int) DAYS.between(ldtBug, LocalDate.parse(TODAY.format(dtfAmerica)));
                bugsCreated[nbDays - days] += 1;
            }
            startAt += maxResults;
            request = baseUrl + jiraApiUrl + SEARCH_JQL_PROJECT + projectName + JQL_ISSUE_TYPE + "'" +  issueType +
                    "'+AND+created>=-" + nbDays + "d" + JQL_MAX_RESULTS + maxResults + JQL_START_AT + startAt;
            c = connectToJiraAPI(request);
        } while (startAt < total);
        return bugsCreated;
    }

    /**
     * Creates an array of Integer where each index corresponds to the number of issuetype resolved per day
     * Index (nbDays) being today. Thus, index i represent the number of bugs resolved (nbDays-i) ago
     * @param nbDays Length of the array
     * @param projectName Project name from which we desire to collect data
     * @param issueType Type of issue we want to collect (bug, incident,...)
     * @param maxResults Number of results returned from GET method.
     * @return An array of Integer
     */
    public int[] getResolved(int nbDays, String projectName, String issueType, int maxResults) {
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
        String request = baseUrl + jiraApiUrl + SEARCH_JQL_PROJECT + projectName + JQL_ISSUE_TYPE + "'" +  issueType +
                "'+AND+updated>=-" + nbDays + "d" + JQL_MAX_RESULTS + maxResults + JQL_START_AT + startAt;
        /*
        Logic
         */
        JiraDto jDto = connectToJiraAPI(request);
        int total = jDto.getTotal();
        do {
            for (IssueDto i : jDto.getIssues()) {
                statut = i.getFields().getStatus().getName();
                if (statut.contains(TERMINE) || statut.contains(LIVRE)) {
                    updateDate = i.getFields().getUpdated().substring(0, 10);
                    ldtBug = LocalDate.parse(updateDate);
                    days = (int) DAYS.between(ldtBug, LocalDate.parse(today));
                    bugsResolved[nbDays - days] += 1;
                }
            }
            startAt += maxResults;
            request = baseUrl + jiraApiUrl + SEARCH_JQL_PROJECT + projectName + JQL_ISSUE_TYPE + "'" +  issueType +
                    "'+AND+updated>=-" + nbDays + "d" + JQL_MAX_RESULTS + maxResults + JQL_START_AT + startAt;
            jDto = connectToJiraAPI(request);
        } while (startAt < total);
        return bugsResolved;
    }

    /**
     * Creates a Double. Retrieve the number of storypoints linked to issueId
     * @param issueID Ticket Id
     * @return Number of story points assigned to an issue
     */
    public double getStoryPoint(String issueID) {
        String request = baseUrl + jiraApiUrl + "search?jql=issue=" + issueID;
        JiraDto jDto = connectToJiraAPI(request);
        IssueDto i = jDto.getIssues()[0];
        return i.getFields().getStoryPoints();
    }

    /**
     * Creates a Ticket. Retrieve the number of tickets (and their issuetype) in issuesID list and merge information
     * in a unique object
     * @param issuesID List of issues
     * @return A Ticket object
     */
    public TicketEntity getTicketsInfos(List<String> issuesID){
        int totalTickets = issuesID.size();
        int ticketsBug = 0;
        int ticketsUS = 0;
        int ticketsTask = 0;
        String issueType;
        for(String issue: issuesID){
            String request = baseUrl + jiraApiUrl + "search?jql=issue=" + issue;
            JiraDto jDto = connectToJiraAPI(request);
            IssueDto i = jDto.getIssues()[0];
            issueType = i.getFields().getIssuetype().getName();
            if(JQL_ISSUE_TYPE_BUG.equals(issueType)){
                ticketsBug++;
            }else if(JQL_ISSUE_TYPE_TASK.equals(issueType)){
                ticketsTask++;
            }else if(JQL_ISSUE_TYPE_US.equals(issueType)){
                ticketsUS++;
            }
        }
        return TicketEntity.builder()
                .total(totalTickets)
                .ticketsTask(ticketsTask)
                .ticketsBug(ticketsBug)
                .ticketsUS(ticketsUS)
                .build();
    }

    /**
     * Creates a Double. Retrieve the number of estimated hours in issuesID list.
     * @param issuesID List of issues
     * @return A Double representing estimated time on issuesID tickets (in hours)
     */
    public double getEstimatedTime(List<String> issuesID){
        double addedWork = 0;
        for(String issue: issuesID){
            String request = baseUrl + jiraApiUrl + "search?jql=issue=" + issue;
            JiraDto jDto = connectToJiraAPI(request);
            IssueDto i = jDto.getIssues()[0];
            addedWork += i.getFields().getTimeoriginalestimate() / (double) 3600;
        }
        return addedWork;
    }

    /**
     * Creates a JiraDto object
     * @param request The request we want to GET data from
     * @return A JiraDto object containing parsed data from the GET request to the API
     */
    public JiraDto connectToJiraAPI(String request) {
        return jiraWebClient
                .get()
                .uri(request)
                .retrieve()
                .bodyToMono(JiraDto.class)
                .block();
    }
}

