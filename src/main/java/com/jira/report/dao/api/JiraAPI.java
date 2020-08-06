package com.jira.report.dao.api;

import com.jira.report.config.JiraReportConfigApi;
import com.jira.report.config.JiraReportConfigIndividuals;
import com.jira.report.config.JiraReportConfigQuery;
import com.jira.report.dto.jira.AssigneeDto;
import com.jira.report.dto.jira.FieldsDto;
import com.jira.report.dto.jira.IssueDto;
import com.jira.report.dto.jira.JiraDto;
import com.jira.report.model.Collaborator;
import com.jira.report.model.Sprint;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import static com.jira.report.dao.api.API.*;
import static java.time.temporal.ChronoUnit.DAYS;

@Service
@Slf4j
public class JiraAPI {

    private static final String JQL_SPRINT = "+AND+sprint=";
    private static final String JQL_LABELS = "+AND+labels=";
    private final WebClient jiraWebClient;
    private final JiraTempoAPI jiraTempoAPI;
    private final String baseUrl;
    private final String jiraApiUrl;
    private final Map<String, String> idCollabs;
    private final String unassignedAccountId;
    // JIRA STATUS & CUSTOM STATUS
    private static final String A_QUALIFIER = "A qualifier";
    private static final String A_FAIRE = "A Faire";
    private static final String BAC_AFFINAGE = "Bac d'affinage";
    private static final String TERMINE = "Terminé";
    private static final String LIVRE = "Livré";
    private static final String VALIDE_RECETTE = "Validé en recette";
    private static final String VALIDE = "Validé";
    private static final String ABANDONNE = "Abandonné";
    private static final String EN_COURS = "En cours";
    private static final String DEV_TERMINE = "Dév terminé";
    private static final String REFUSE_RECETTE = "Refusé en recette";
    private static final String EN_ATTENTE = "En attente";
    private static final String A_LIVRER = "A Livrer";
    private static final String A_TESTER = "A tester";
    private static final String A_VALIDER = "A valider";
    private static final String TEST_CROISE = "Test croisé";
    //Unassigned infos
    private static final String UNASSIGNED_ROLE = "none";
    private static final String UNASSIGNED_FIRST_NAME = "Non";
    private static final String UNASSIGNED_LAST_NAME = "Assigné";
    //Priority
    private static final String PRIORITY_LOW = "Low";
    private static final String PRIORITY_MEDIUM = "Medium";
    private static final String PRIORITY_HIGH = "High";
    private static final String PRIORITY_HIGHEST = "Highest";
    private static final List<String> DONE = new ArrayList<>(Arrays.asList(LIVRE, TERMINE, VALIDE, VALIDE_RECETTE));
    private static final List<String> DONE_BUGS = new ArrayList<>(Arrays.asList(LIVRE, TERMINE, VALIDE_RECETTE, ABANDONNE));
    private static final List<String> IN_PROGRESS = new ArrayList<>(Arrays.asList(EN_COURS, DEV_TERMINE, REFUSE_RECETTE, EN_ATTENTE, A_TESTER, A_LIVRER));
    private static final List<String> DEV_DONE = new ArrayList<>(Arrays.asList(A_TESTER, A_LIVRER));
    private static final List<String> DEV_DONE_EN_COURS = new ArrayList<>(Arrays.asList(DEV_TERMINE, EN_COURS));

    //Queries URI
    private static final String SEARCH_JQL_PROJECT = "search?jql=project=";
    private static final String JQL_ASSIGNEE = "+AND+assignee=";
    private static final String JQL_ISSUE_TYPE = "+AND+issuetype=";
    private static final String JQL_MAX_RESULTS = "&maxResults=";
    private static final String JQL_START_AT = "&startAt=";

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


    /* Main method : Returns a Collaborator object if it has at least one ticket
     * else return null
     */
    public Collaborator getCollaborator(String accId, String label, Sprint s, String projectName, int maxResults) {
          /*
        Variables
        */
        String request = baseUrl + jiraApiUrl + SEARCH_JQL_PROJECT + projectName + JQL_ASSIGNEE + accId +
                JQL_SPRINT + s.getId() + JQL_LABELS + label + JQL_MAX_RESULTS + maxResults;
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
            estimated += i.getFields().getTimeoriginalestimate() / (double) 3600;
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
                    break;
                default:
                    break;
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
                if (!DONE_BUGS.contains(statut)) {
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

    /* Returns an array of integer of length 'nbDays'. Each element corresponds to the number of bug/incident created
     *  Index i represent the number of bugs created (nbDays-i) ago
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

    /* Returns an array of integer of length 'nbDays'. Each element corresponds to the number of bug/incident resolved (Jira status "terminé/livré")
     *  Index i represent the number of bugs resolved (nbDays-i) ago
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

    /* Returns an array of integer of length 'nbDays'. Each element corresponds to the number of bug/incident in progress (Jira status "terminé/livré")
     *  Index i represent the number of bugs resolved (nbDays-i) ago
     */
    public int[] getInProgress(int nbDays, String projectName, String issueType, int maxResults) {
        /*
        Variables
         */
        int[] bugsInProgressPerDay = new int[nbDays + 1];
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
                if (IN_PROGRESS.contains(statut)) {
                    updateDate = i.getFields().getUpdated().substring(0, 10);
                    ldtBug = LocalDate.parse(updateDate);
                    days = (int) DAYS.between(ldtBug, LocalDate.parse(today));
                    bugsInProgressPerDay[nbDays - days] += 1;
                }
            }
            startAt += maxResults;
            request = baseUrl + jiraApiUrl + SEARCH_JQL_PROJECT + projectName + JQL_ISSUE_TYPE + "'" +  issueType +
                    "'+AND+updated>=-" + nbDays + "d" + JQL_MAX_RESULTS + maxResults + JQL_START_AT + startAt;
            jDto = connectToJiraAPI(request);
        } while (startAt < total);
        return bugsInProgressPerDay;
    }

    /* Method that returns the number of story points assigned to an issue.
     * Used to retrieve story points linked to added tickets in getCommitment()
     */
    public double getStoryPoint(String issueID, String projectName) {
        /*
        Variables
         */
        String request = baseUrl + jiraApiUrl + SEARCH_JQL_PROJECT + projectName + "+AND+issue=" + issueID;
        /*
        Logic
         */
        JiraDto jDto = connectToJiraAPI(request);
        IssueDto i = jDto.getIssues()[0];
        return i.getFields().getStoryPoints();
    }

    public JiraDto connectToJiraAPI(String request) {
        return jiraWebClient
                .get()
                .uri(request)
                .retrieve()
                .bodyToMono(JiraDto.class)
                .block();
    }
}

