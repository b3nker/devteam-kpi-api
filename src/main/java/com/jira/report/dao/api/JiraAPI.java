package com.jira.report.dao.api;

import com.jira.report.config.JiraReportConfigApi;
import com.jira.report.config.JiraReportConfigIndividuals;
import com.jira.report.config.JiraReportConfigQuery;
import com.jira.report.dto.jiraApi.AssigneeDto;
import com.jira.report.dto.jiraApi.FieldsDto;
import com.jira.report.dto.jiraApi.IssueDto;
import com.jira.report.dto.jiraApi.JiraDto;
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
    private final WebClient jiraWebClient;
    private final JiraTempoAPI jiraTempoAPI;
    private final String baseUrl;
    private final String jiraApiUrl;
    private final Map<String, String> idCollabs;
    private final String unassignedAccountId;
    // JIRA STATUS & CUSTOM STATUS
    private static final String aQualifier = "A qualifier";
    private static final String aFaire = "A Faire";
    private static final String bacAffinage = "Bac d'affinage";
    private static final String termine = "Terminé";
    private static final String livre = "Livré";
    private static final String valideRecette = "Validé en recette";
    private static final String valide = "Validé";
    private static final String abandonne = "Abandonné";
    private static final String enCours = "En cours";
    private static final String devTermine = "Dév terminé";
    private static final String refuseRecette = "Refusé en recette";
    private static final String enAttente = "En attente";
    private static final String aLivrer = "A Livrer";
    private static final String aTester = "A tester";
    private static final String aValider = "A valider";
    private static final String testCroise = "Test croisé";
    //Unassigned infos
    private static final String UNASSIGNED_ROLE = "none";
    private static final String UNASSIGNED_FIRST_NAME = "Non";
    private static final String UNASSIGNED_LAST_NAME = "Assigné";
    //Priority
    private static final String priorityLow = "Low";
    private static final String priorityMedium = "Medium";
    private static final String priorityHigh = "High";
    private static final String priorityHighest = "Highest";
    private static final List<String> done = new ArrayList<>(Arrays.asList(livre,termine,valide,valideRecette));
    private static final List<String> doneBugs = new ArrayList<>(Arrays.asList(livre,termine,valideRecette,abandonne));
    private static final List<String> inProgress = new ArrayList<>(Arrays.asList(enCours,devTermine,refuseRecette,enAttente,aTester,aLivrer));
    private static final List<String> devDone = new ArrayList<>(Arrays.asList(aTester, aLivrer));
    private static final List<String> devDoneEnCours = new ArrayList<>(Arrays.asList(devTermine, enCours));

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
        String request = baseUrl + jiraApiUrl + "search?jql=project=" + projectName + "+AND+assignee=" + accId +
                "+AND+sprint=" + s.getId() + "+AND+labels=" + label + "&maxResults=" + maxResults;
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
            if (done.contains(statut)) {
                ticketsDone++;
            } else if (inProgress.contains(statut)) {
                if (devDone.contains(statut)) {
                    ticketsDevDone++;
                } else {
                    ticketsInProgress++;
                }
            } else {
                ticketsToDo++;
            }
            if (devDoneEnCours.contains(statut)) {
                ticketsEnCoursDevTermine++;
            }
            if (aTester.contains(statut)) {
                ticketsAtester++;
            }
            // Setting working time
            remaining += i.getFields().getTimeestimate() / (double) 3600;
            estimated += i.getFields().getTimeoriginalestimate() / (double) 3600;
            timespent += i.getFields().getTimespent() / (double) 3600;
            //Setting story points
            double curStoryPoints = i.getFields().getStoryPoints();
            spTotal += curStoryPoints;
            switch (statut) {
                case aQualifier:
                    spAQualifier += curStoryPoints;
                    break;
                case bacAffinage:
                    spBacAffinage += curStoryPoints;
                    break;
                case enAttente:
                    spEnAttente += curStoryPoints;
                    break;
                case aFaire:
                    spAFaire += curStoryPoints;
                    break;
                case enCours:
                    spEnCours += curStoryPoints;
                    break;
                case abandonne:
                    spAbandonne += curStoryPoints;
                    break;
                case devTermine:
                    spDevTermine += curStoryPoints;
                    break;
                case aValider:
                    spAvalider += curStoryPoints;
                    break;
                case aLivrer:
                    spAlivrer += curStoryPoints;
                    break;
                case aTester:
                    spATester += curStoryPoints;
                    break;
                case refuseRecette:
                    spRefuseEnRecette += curStoryPoints;
                    break;
                case valideRecette:
                    spValideEnRecette += curStoryPoints;
                    break;
                case livre:
                    spLivre += curStoryPoints;
                    break;
                case termine:
                    spTermine += curStoryPoints;
                    break;
                case testCroise:
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
        String request = baseUrl + jiraApiUrl + "search?jql=project=" + projectName +
                "+AND+issuetype='" + issueType + "'&maxResults=" + maxResults + "&startAt=" + startAt;
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
                if (!doneBugs.contains(statut)) {
                    incidentsBugs[0]++;
                    switch (priority) {
                        case priorityLow:
                            incidentsBugs[1]++;
                            break;
                        case priorityMedium:
                            incidentsBugs[2]++;
                            break;
                        case priorityHigh:
                            incidentsBugs[3]++;
                            break;
                        case priorityHighest:
                            incidentsBugs[4]++;
                            break;
                        default:
                            break;
                    }
                }
            }
            startAt += maxResults;
            request = baseUrl + jiraApiUrl + "search?jql=project=" + projectName +
                    "+AND+issuetype='" + issueType + "'&maxResults=" + maxResults + "&startAt=" + startAt;
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
        String request = baseUrl + jiraApiUrl + "search?jql=project=" + projectName + "+AND+issuetype='" + issueType +
                "'+AND+created>=-" + nbDays + "d&maxResults=" + maxResults + "&startAt=" + startAt;
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
            request = baseUrl + jiraApiUrl + "search?jql=project=" + projectName + "+AND+issuetype='" + issueType +
                    "'+AND+created>=-" + nbDays + "d&maxResults=" + maxResults + "&startAt=" + startAt;
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
        String request = baseUrl + jiraApiUrl + "search?jql=project=" + projectName + "+AND+issuetype='" + issueType +
                "'+AND+updated>=-" + nbDays + "d&maxResults=" + maxResults + "&startAt=" + startAt;
        /*
        Logic
         */
        JiraDto jDto = connectToJiraAPI(request);
        int total = jDto.getTotal();
        do {
            for (IssueDto i : jDto.getIssues()) {
                statut = i.getFields().getStatus().getName();
                if (statut.contains(termine) || statut.contains(livre)) {
                    updateDate = i.getFields().getUpdated().substring(0, 10);
                    ldtBug = LocalDate.parse(updateDate);
                    days = (int) DAYS.between(ldtBug, LocalDate.parse(today));
                    bugsResolved[nbDays - days] += 1;
                }
            }
            startAt += maxResults;
            request = baseUrl + jiraApiUrl + "search?jql=project=" + projectName + "+AND+issuetype='" + issueType +
                    "'+AND+updated>=-" + nbDays + "d&maxResults=" + maxResults + "&startAt=" + startAt;
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
        String request = baseUrl + jiraApiUrl + "search?jql=project=" + projectName + "+AND+issuetype='" + issueType +
                "'+AND+updated>=-" + nbDays + "d&maxResults=" + maxResults + "&startAt=" + startAt;
        /*
        Logic
         */
        JiraDto jDto = connectToJiraAPI(request);
        int total = jDto.getTotal();
        do {
            for (IssueDto i : jDto.getIssues()) {
                statut = i.getFields().getStatus().getName();
                if (inProgress.contains(statut)) {
                    updateDate = i.getFields().getUpdated().substring(0, 10);
                    ldtBug = LocalDate.parse(updateDate);
                    days = (int) DAYS.between(ldtBug, LocalDate.parse(today));
                    bugsInProgressPerDay[nbDays - days] += 1;
                }
            }
            startAt += maxResults;
            request = baseUrl + jiraApiUrl + "search?jql=project=" + projectName + "+AND+issuetype='" + issueType +
                    "'+AND+updated>=-" + nbDays + "d&maxResults=" + maxResults + "&startAt=" + startAt;
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
        String request = baseUrl + jiraApiUrl + "search?jql=project=" + projectName + "+AND+issue=" + issueID;
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

