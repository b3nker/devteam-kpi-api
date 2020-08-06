package com.jira.report.dao.api;

import com.jira.report.config.*;
import com.jira.report.model.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Slf4j
@Service
public class API {
    private final JiraAPI jiraAPI;
    private final String projectBoardId;
    private final int maxResults;
    private final JiraGreenhopperAPI jiraGreenhopperAPI;
    private final JiraAgileAPI jiraAgileAPI;
    private final ExternalFiles externalFiles;
    private final String projectName;
    private final String runProjectName;
    private final int nbSprintsRetrospective;
    private final int nbDaysBacklog;
    private final String unassignedAccountId;
    private final HashMap<String, String> teamPair = new HashMap<>();
    private final HashMap<String, List<String>> teams = new HashMap<>();
    private final HashMap<String, Sprint> activeSprints = new HashMap<>();
    private final String planningPath;
    private final String releasePath;
    private final String bug;
    private final String incident;

    //Settings
    static final DateTimeFormatter dtfEurope = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    static final DateTimeFormatter dtfAmerica = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    static final LocalDateTime TODAY = LocalDateTime.now();

    public API(JiraAPI jiraAPI, JiraGreenhopperAPI jiraGreenhopperAPI,
               JiraAgileAPI jiraAgileAPI, ExternalFiles externalFiles,
               JiraReportConfigIndividuals jiraReportConfigIndividuals,
               JiraReportConfigQuery jiraReportConfigQuery,
               JiraReportConfigExternal jiraReportConfigExternal,
               JiraReportConfigGlobal jiraReportConfigGlobal){
        this.jiraAPI = jiraAPI;
        this.jiraGreenhopperAPI = jiraGreenhopperAPI;
        this.jiraAgileAPI = jiraAgileAPI;
        this.externalFiles = externalFiles;
        this.maxResults = jiraReportConfigQuery.getMaxResults();
        this.projectBoardId = jiraReportConfigGlobal.getBoardIdProject();
        String teamNameAlpha = jiraReportConfigIndividuals.getTeamNameOne();
        String teamNameBeta = jiraReportConfigIndividuals.getTeamNameTwo();
        String teamNameGamma = jiraReportConfigIndividuals.getTeamNameThree();
        List<String> teamAlpha = jiraReportConfigIndividuals.getTeamOne();
        List<String> teamBeta = jiraReportConfigIndividuals.getTeamTwo();
        List<String> teamGamma = jiraReportConfigIndividuals.getTeamThree();
        Sprint sprintActifAlpha = jiraAgileAPI.getLastlyActiveTeamSprint(teamNameAlpha, projectBoardId);
        Sprint sprintActifBeta = jiraAgileAPI.getLastlyActiveTeamSprint(teamNameBeta, projectBoardId);
        Sprint sprintActifGamma = jiraAgileAPI.getLastlyActiveTeamSprint(teamNameGamma, projectBoardId);
        String boardIdAlpha = jiraReportConfigGlobal.getBoardIdOne();
        String boardIdBeta = jiraReportConfigGlobal.getBoardIdTwo();
        String boardIdGamma = jiraReportConfigGlobal.getBoardIdThree();
        this.runProjectName = jiraReportConfigGlobal.getRunProjectName();
        this.projectName = jiraReportConfigGlobal.getProjectName();
        this.activeSprints.put(teamNameAlpha, sprintActifAlpha);
        this.activeSprints.put(teamNameBeta, sprintActifBeta);
        this.activeSprints.put(teamNameGamma, sprintActifGamma);
        this.teamPair.put(teamNameAlpha, boardIdAlpha);
        this.teamPair.put(teamNameBeta, boardIdBeta);
        this.teamPair.put(teamNameGamma, boardIdGamma);
        this.teams.put(teamNameAlpha, teamAlpha);
        this.teams.put(teamNameBeta, teamBeta);
        this.teams.put(teamNameGamma, teamGamma);
        this.bug = jiraReportConfigQuery.getBug();
        this.incident = jiraReportConfigQuery.getIncident();
        this.planningPath = jiraReportConfigExternal.getPlanning();
        this.releasePath = jiraReportConfigExternal.getRelease();
        this.unassignedAccountId = jiraReportConfigQuery.getUnassignedAccountId();
        this.nbDaysBacklog = jiraReportConfigGlobal.getNbDaysBacklog();
        this.nbSprintsRetrospective = jiraReportConfigGlobal.getNbSprintsRetrospective();

    }

    /* Returns a HashMap <TeamName, Sprint> using JiraAPI and getTeams() method
     * Retrieve all data from the lastly active sprint
     */
    public Map<String,Sprint> callJiraSprintAPI() {
        for (Map.Entry<String, List<String>> entry: teams.entrySet()) {
            String label = entry.getKey();
            Team t = getTeam(teams.get(label), label);
            activeSprints.get(label).setTeam(t);
        }
        return activeSprints;
    }

    /* Method that calls getCollaborators() and return a HashMap<AccountID, collaborator>
     * Retrieve all data of each ID_COLLABS on the lastly active sprint (if they have at least one assigned ticket)
     */
    public Map<String, Collaborator> callJiraCollabSprintAPI() {
        HashMap<String, Collaborator> collaborators = new HashMap<>();
        for (Map.Entry<String, List<String>> entry : teams.entrySet()) {
            String label = entry.getKey();
            Map<String, Collaborator> c = getCollaboratorsPerTeam(teams.get(label), label);
            Collaborator unassigned = c.get(this.unassignedAccountId);
            if(unassigned != null){
                unassigned.setAccountId(this.unassignedAccountId + ' ' + label);
                c.remove(this.unassignedAccountId);
            }else{
                unassigned = Collaborator.builder()
                        .accountId(this.unassignedAccountId)
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
        int[] incidents = jiraAPI.getProjectIncidentBug(runProjectName, incident,maxResults);
        int[] bugs = jiraAPI.getProjectIncidentBug(projectName, bug,maxResults);
        return Backlog.builder()
                .nbIncidents(incidents[0])
                .nbIncidentsLow(incidents[1])
                .nbIncidentsMedium(incidents[2])
                .nbIncidentsHigh(incidents[3])
                .nbIncidentsHighest(incidents[4])
                .nbIncidentsCreated(jiraAPI.getCreated(nbDaysBacklog, runProjectName, incident,maxResults))
                .nbIncidentsResolved(jiraAPI.getResolved(nbDaysBacklog, runProjectName, incident,maxResults))
                .nbIncidentsInProgress(jiraAPI.getInProgress(nbDaysBacklog, runProjectName, incident,maxResults))
                .nbBugs(bugs[0])
                .nbBugsLow(bugs[1])
                .nbBugsMedium(bugs[2])
                .nbBugsHigh(bugs[3])
                .nbBugsHighest(bugs[4])
                .nbBugsCreated(jiraAPI.getCreated(nbDaysBacklog, projectName, bug,maxResults))
                .nbBugsResolved(jiraAPI.getResolved(nbDaysBacklog, projectName, bug,maxResults))
                .nbBugsInProgress(jiraAPI.getInProgress(nbDaysBacklog, projectName, bug,maxResults))
                .build();
    }

    /* Method that retrieves all data of the last NB_SPRINT_RETROSPECTIVE, including the lastly active sprint
     * for each team (board_id differs)
     */
    public Map<String, Retrospective> callJiraRetrospectiveAPI() {
        HashMap<String, Retrospective> retrospectives = new HashMap<>();
        for (Map.Entry<String,String> entry: teamPair.entrySet()) {
            String teamName = entry.getKey();
            List<SprintCommitment> sprints = jiraAgileAPI.getLastlyClosedSprints(nbSprintsRetrospective, teamName,projectBoardId);
            for (SprintCommitment sprint : sprints) {
                if(sprint.getId() != 0){
                    double[] commitment = jiraGreenhopperAPI.getCommitment(sprint, teamPair.get(teamName));
                    List<String> issueKeys = jiraGreenhopperAPI.getIssueKeys(sprint, teamPair.get(teamName));
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
        return externalFiles.getReleases(releasePath);
    }

    public Map<String, Collaborator> getCollaboratorsPerTeam(List<String> teamAccId, String teamName) {
        Map<String, Float[]> planning = externalFiles.getPlanning(planningPath, activeSprints.get(teamName));
        Map<String, Collaborator> collaborators = new HashMap<>();
        Collaborator c;
        for (String accId : teamAccId) {
            // In application.yml, unassigned who is defined with a nil value is returned as an empty char
            if(accId.isEmpty()){
                accId = null;
            }
            if ((c = jiraAPI.getCollaborator(accId, teamName, activeSprints.get(teamName),this.projectName,maxResults)) != null) {
                if(planning.containsKey(accId)){
                    Float[] timeValues = planning.get(accId);
                    c.setTotalWorkingTime(timeValues[0]);
                    c.setAvailableTime(timeValues[1]);
                }
                collaborators.put(c.getAccountId(), c);
            }
        }
        return collaborators;
    }

    /* Call above method, getCollaborators() and assign each collaborator to its team
     * returning a HashMap of size nbTeams
     */
    public Team getTeam(List<String> teamAccId, String teamName) {
        Map<String, Collaborator> collaborators = getCollaboratorsPerTeam(teamAccId, teamName);
        List<Collaborator> c = new ArrayList<>(collaborators.values());
        return Team.builder()
                .name(teamName)
                .collaborators(c)
                .build();
    }
}
