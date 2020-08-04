package com.jira.report.dao.api;

import com.jira.report.config.JiraReportConfigIndividuals;
import com.jira.report.model.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Slf4j
@Service
public class API {
    //Project's variables
    private final JiraReportConfigIndividuals jiraReportConfig;
    private final JiraAPI jiraAPI;
    private final JiraGreenhopperAPI jiraGreenhopperAPI;
    private final JiraAgileAPI jiraAgileAPI;
    private final ExternalFiles externalFiles;
    public static final String USERNAME = "benjamin.kermani@neo9.fr";
    public static final String API_TOKEN = "sqjFnTAVspNM4NxLd1QZC5CB";
    public static final String API_TOKEN_TEMPO = "J1eKPcvcMlCvMjNBvXyJmn0vMPvGs0";
    static final String PROJECT_BOARD_ID = "391";
    static final String BOARD_ID_ALPHA_SP = "451";
    static final String BOARD_ID_BETA_SP = "443";
    static final String BOARD_ID_GAMMA_SP = "450";
    static final String PROJECT_NAME = "BMKP";
    static final String RUN_PROJECT_NAME = "RMKP";
    static final int MAX_RESULTS = 100;
    static final int NB_SPRINTS_RETROSPECTIVE = 1;
    static final int NB_DAYS_BACKLOG = 20;
    //Collaborator & team information
    static final String TEAM_NAME_ALPHA = "alpha";
    static final String TEAM_NAME_BETA = "beta";
    static final String TEAM_NAME_GAMMA = "gamma";
    static final String UNASSIGNED = "unassigned";
    static final HashMap<String, String> TEAM_PAIR = new HashMap<>();
    static {
        TEAM_PAIR.put(TEAM_NAME_ALPHA, BOARD_ID_ALPHA_SP);
        TEAM_PAIR.put(TEAM_NAME_BETA, BOARD_ID_BETA_SP);
        TEAM_PAIR.put(TEAM_NAME_GAMMA, BOARD_ID_GAMMA_SP);
    }
    static final ArrayList<String> TEAM_ALPHA = new ArrayList<>(Arrays.asList(
            "5c17b4599f443a65fecae3ca", // Julien Mosset
            "5a9ebe1c4af2372a88a0656b", // Nicolas Ovejero
            "5bcd8282607ed038040177bb", // Pape Thiam
            "5cf921f6b06c540e82580cbd", // Valentin Pierrel
            "5ed76cdf2fdc580b88f3bbef", // Alex Cheuko
            "5a9ebdf74af2372a88a06565", // Gabriel Roquigny
            "null"
    ));
    static final ArrayList<String> TEAM_BETA = new ArrayList<>(Arrays.asList(
            "5cb45bb34064460e407eabe4", // Guillermo Garcès
            "5a2181081594706402dee482", // Etienne Bourgouin
            "5afe92f251d0b7540b43de81", // Malick Diagne
            "5d6e32e06e3e1f0d9623cb5a", // Pierre Tomasina
            "5ed64583620b1d0c168d4e36", // Anthony Hernandez
            "5e98521a3a8b910c085d6a28", // Kévin Youna
            "null"
    ));
    static final ArrayList<String> TEAM_GAMMA = new ArrayList<>(Arrays.asList(
            "5e285008ee264b0e74591993", // Eric Coupal
            "5ed76cc1be03220ab32183be", // Thibault Foucault
            "557058:87b17037-8a69-4b38-8dab-b4cf904e960a", // Pierre Thevenet
            "5d9b0573ea65c10c3fdbaab2", // Maxime Fourt
            "5a8155f0cad06b353733bae8", // Guillaume Coppens
            "5dfd11b39422830cacaa8a79", // Carthy Marie Joseph
            "null"
    ));
    static final HashMap<String, ArrayList<String>> TEAMS = new HashMap<>();
    static {
        TEAMS.put(TEAM_NAME_ALPHA, TEAM_ALPHA);
        TEAMS.put(TEAM_NAME_BETA, TEAM_BETA);
        TEAMS.put(TEAM_NAME_GAMMA, TEAM_GAMMA);

    }
    // Sprint Data
    private Sprint SPRINT_ACTIF;
    private Sprint SPRINT_ACTIF_ALPHA;
    private Sprint SPRINT_ACTIF_BETA;
    private Sprint SPRINT_ACTIF_GAMMA;
    final HashMap<String, Sprint> SPRINTS = new HashMap<>();
    // ExternalData
    static final String PLANNING_PATH = "planning.csv";
    static final String RELEASE_PATH = "release.csv";
    // Queries variables
    static final String ISSUE_BUG = "Bug";
    static final String ISSUE_INCIDENT = "Incident";
    //Settings
    static final DateTimeFormatter dtfEurope = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    static final DateTimeFormatter dtfAmerica = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    static final LocalDateTime TODAY = LocalDateTime.now();

    public API(JiraAPI jiraAPI, JiraGreenhopperAPI jiraGreenhopperAPI, JiraAgileAPI jiraAgileAPI, JiraReportConfigIndividuals jiraReportConfig){
        this.jiraAPI = jiraAPI;
        this.jiraReportConfig = jiraReportConfig;
        this.jiraGreenhopperAPI = jiraGreenhopperAPI;
        this.jiraAgileAPI = jiraAgileAPI;
        this.externalFiles = new ExternalFiles();
        this.SPRINT_ACTIF = jiraAgileAPI.getLastlyActiveSprint();
        this.SPRINT_ACTIF_ALPHA = jiraAgileAPI.getLastlyActiveSprint();
        this.SPRINT_ACTIF_BETA = jiraAgileAPI.getLastlyActiveTeamSprint(TEAM_NAME_BETA);
        this.SPRINT_ACTIF_GAMMA = jiraAgileAPI.getLastlyActiveSprint();
        this.SPRINTS.put(TEAM_NAME_ALPHA, SPRINT_ACTIF_ALPHA);
        this.SPRINTS.put(TEAM_NAME_BETA, SPRINT_ACTIF_BETA);
        this.SPRINTS.put(TEAM_NAME_GAMMA, SPRINT_ACTIF_GAMMA);
    }

    /* Returns a HashMap <TeamName, Sprint> using JiraAPI and getTeams() method
     * Retrieve all data from the lastly active sprint
     */
    public Map<String,Sprint> callJiraSprintAPI() {
        Map<String, Float[]> planning = externalFiles.getPlanning(PLANNING_PATH, SPRINT_ACTIF);
        for (Map.Entry<String, ArrayList<String>> entry: TEAMS.entrySet()) {
            String label = entry.getKey();
            Team t = getTeam(TEAMS.get(label), label);
            for(Collaborator c: t.getCollaborators()){
                if(planning.containsKey(c.getAccountId())){
                    Float[] timeData = planning.get(c.getAccountId());
                    c.setTotalWorkingTime(timeData[0]);
                    c.setAvailableTime(timeData[1]);
                }
            }
            SPRINTS.get(label).setTeam(t);

        }
        return SPRINTS;
    }

    /* Method that calls getCollaborators() and return a HashMap<AccountID, collaborator>
     * Retrieve all data of each ID_COLLABS on the lastly active sprint (if they have at least one assigned ticket)
     */
    public Map<String, Collaborator> callJiraCollabSprintAPI() {
        Map<String, Float[]> planning = externalFiles.getPlanning(PLANNING_PATH, SPRINT_ACTIF);
        HashMap<String, Collaborator> collaborators = new HashMap<>();
        for (Map.Entry<String, ArrayList<String>> entry : TEAMS.entrySet()) {
            String label = entry.getKey();
            Map<String, Collaborator> c = getCollaboratorsPerTeam(TEAMS.get(label), label);
            for(Collaborator collab: c.values()){
                if(planning.containsKey(collab.getAccountId())){
                    Float[] timeData = planning.get(collab.getAccountId());
                    collab.setTotalWorkingTime(timeData[0]);
                    collab.setAvailableTime(timeData[1]);
                }
            }
            Collaborator unassigned = c.get(UNASSIGNED);
            if(unassigned != null){
                unassigned.setAccountId(UNASSIGNED + ' ' + label);
                c.remove(UNASSIGNED);
            }else{
                unassigned = Collaborator.builder()
                        .accountId(UNASSIGNED)
                        .build();
            }
            c.put(unassigned.getAccountId(), unassigned);
            collaborators.putAll(c);
        }
        return collaborators;
    }

    /* Method that returns a Backlog object
     * Retrieve all information from RUN_PROJECT_NAME (for incident) and PROJECT_NAME (for bugs) since each
     * project's creation
     */
    public Backlog callJiraBacklogAPI() {
        int[] incidents = jiraAPI.getProjectIncidentBug(RUN_PROJECT_NAME, ISSUE_INCIDENT);
        int[] bugs = jiraAPI.getProjectIncidentBug(PROJECT_NAME, ISSUE_BUG);
        return Backlog.builder()
                .nbIncidents(incidents[0])
                .nbIncidentsLow(incidents[1])
                .nbIncidentsMedium(incidents[2])
                .nbIncidentsHigh(incidents[3])
                .nbIncidentsHighest(incidents[4])
                .nbIncidentsCreated(jiraAPI.getCreated(NB_DAYS_BACKLOG, RUN_PROJECT_NAME, ISSUE_INCIDENT))
                .nbIncidentsResolved(jiraAPI.getResolved(NB_DAYS_BACKLOG, RUN_PROJECT_NAME, ISSUE_INCIDENT))
                .nbIncidentsInProgress(jiraAPI.getInProgress(NB_DAYS_BACKLOG, RUN_PROJECT_NAME, ISSUE_INCIDENT))
                .nbBugs(bugs[0])
                .nbBugsLow(bugs[1])
                .nbBugsMedium(bugs[2])
                .nbBugsHigh(bugs[3])
                .nbBugsHighest(bugs[4])
                .nbBugsCreated(jiraAPI.getCreated(NB_DAYS_BACKLOG, PROJECT_NAME, ISSUE_BUG))
                .nbBugsResolved(jiraAPI.getResolved(NB_DAYS_BACKLOG, PROJECT_NAME, ISSUE_BUG))
                .nbBugsInProgress(jiraAPI.getInProgress(NB_DAYS_BACKLOG, PROJECT_NAME, ISSUE_BUG))
                .build();
    }

    /* Method that retrieves all data of the last NB_SPRINT_RETROSPECTIVE, including the lastly active sprint
     * for each team (board_id differs)
     */
    public Map<String, Retrospective> callJiraRetrospectiveAPI() {
        HashMap<String, Retrospective> retrospectives = new HashMap<>();
        for (Map.Entry<String,String> entry: TEAM_PAIR.entrySet()) {
            String teamName = entry.getKey();
            List<SprintCommitment> sprints = jiraAgileAPI.getLastlyClosedSprints(NB_SPRINTS_RETROSPECTIVE, teamName);
            for (SprintCommitment sprint : sprints) {
                if(sprint.getId() != 0){
                    double[] commitment = jiraGreenhopperAPI.getCommitment(sprint, TEAM_PAIR.get(teamName));
                    List<String> issueKeys = jiraGreenhopperAPI.getIssueKeys(sprint, TEAM_PAIR.get(teamName));
                    sprint.setAddedIssueKeys(issueKeys);
                    sprint.setInitialCommitment(commitment[0]);
                    sprint.setFinalCommitment(commitment[1]);
                    sprint.setAddedWork(commitment[2]);
                    sprint.setCompletedWork(commitment[3]);
                }
            }
            Retrospective r = Retrospective.builder()
                    .teamName(teamName)
                    .sprints(sprints)
                    .build();
            retrospectives.put(r.getTeamName(), r);
        }
        return retrospectives;
    }

    /* Method that calls getReleases() from ExternalFiles class
     * Returns a list of Release object
     */
    public List<Release> getReleases() throws IOException, ParseException {
        return externalFiles.getReleases(RELEASE_PATH);
    }

    public Map<String, Collaborator> getCollaboratorsPerTeam(List<String> teamAccId, String teamName) {
        HashMap<String, Collaborator> collaborators = new HashMap<>();
        Collaborator c;
        for (String accId : teamAccId) {
            if ((c = jiraAPI.getCollaborator(accId, teamName, SPRINTS.get(teamName))) != null) {
                collaborators.put(c.getAccountId(), c);
            }
        }
        return collaborators;
    }

    /* Call above method, getCollaborators() and assign each collaborator to its team
     * returning a HashMap of size nbTeams
     */
    public Team getTeam(ArrayList<String> teamAccId, String label) {
        Map<String, Collaborator> collaborators = getCollaboratorsPerTeam(teamAccId, label);
        List<Collaborator> c = new ArrayList<>(collaborators.values());
        return Team.builder()
                .name(label)
                .collaborators(c)
                .build();
    }
}
