package com.jiraReportTest.jiraReportTest.Dao.API;

import com.jiraReportTest.jiraReportTest.Model.*;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;


public class API {

    //Project's variables
    final static String USERNAME = "benjamin.kermani@neo9.fr";
    final static String API_TOKEN = "sqjFnTAVspNM4NxLd1QZC5CB";
    final static String API_TOKEN_TEMPO = "J1eKPcvcMlCvMjNBvXyJmn0vMPvGs0";
    final static String BOARD_ID = "391";
    final static String BOARD_ID_ALPHA_SP = "451";
    final static String BOARD_ID_BETA_SP = "443";
    final static String BOARD_ID_GAMMA_SP = "450";
    final static String PROJECT_NAME = "BMKP";
    final static String RUN_PROJECT_NAME = "RMKP";
    final static int MAX_RESULTS = 100;
    final static int NB_SPRINTS_RETROSPECTIVE = 4;
    final static int NB_DAYS_BACKLOG = 20;
    //Collaborator & team information
    final static String TEAM_NAME_ALPHA = "alpha";
    final static String TEAM_NAME_BETA = "beta";
    final static String TEAM_NAME_GAMMA = "gamma";
    final static String UNASSIGNED = "unassigned";
    final static HashMap<String, String> TEAM_PAIR = new HashMap<>();
    static {
        TEAM_PAIR.put(TEAM_NAME_ALPHA, BOARD_ID_ALPHA_SP);
        TEAM_PAIR.put(TEAM_NAME_BETA, BOARD_ID_BETA_SP);
        TEAM_PAIR.put(TEAM_NAME_GAMMA, BOARD_ID_GAMMA_SP);
    }
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
    final static ArrayList<String> TEAM_ALPHA = new ArrayList<>(Arrays.asList(
            "5c17b4599f443a65fecae3ca", // Julien Mosset
            "5a9ebe1c4af2372a88a0656b", // Nicolas Ovejero
            "5bcd8282607ed038040177bb", // Pape Thiam
            "5cf921f6b06c540e82580cbd", // Valentin Pierrel
            "5ed76cdf2fdc580b88f3bbef", // Alex Cheuko
            "5a9ebdf74af2372a88a06565", // Gabriel Roquigny
            UNASSIGNED
    ));
    final static ArrayList<String> TEAM_BETA = new ArrayList<>(Arrays.asList(
            "5cb45bb34064460e407eabe4", // Guillermo Garcès
            "5a2181081594706402dee482", // Etienne Bourgouin
            "5afe92f251d0b7540b43de81", // Malick Diagne
            "5d6e32e06e3e1f0d9623cb5a", // Pierre Tomasina
            "5ed64583620b1d0c168d4e36", // Anthony Hernandez
            "5ef1afd6561e0e0aae904914", // Yong Ma
            "5e98521a3a8b910c085d6a28", // Kévin Youna
            UNASSIGNED
    ));
    final static ArrayList<String> TEAM_GAMMA = new ArrayList<>(Arrays.asList(
            "5e285008ee264b0e74591993", // Eric Coupal
            "5ed76cc1be03220ab32183be", // Thibault Foucault
            "557058:87b17037-8a69-4b38-8dab-b4cf904e960a", // Pierre Thevenet
            "5d9b0573ea65c10c3fdbaab2", // Maxime Fourt
            "5a8155f0cad06b353733bae8", // Guillaume Coppens
            "5dfd11b39422830cacaa8a79", // Carthy Marie Joseph
            UNASSIGNED
    ));
    final static HashMap<String, ArrayList<String>> TEAMS = new HashMap<>();
    static {
        TEAMS.put(TEAM_NAME_ALPHA, TEAM_ALPHA);
        TEAMS.put(TEAM_NAME_BETA, TEAM_BETA);
        TEAMS.put(TEAM_NAME_GAMMA, TEAM_GAMMA);

    }
    // Sprint Data
    final static Sprint SPRINT_ACTIF = JiraAgileAPI.getLastlyActiveSprint();
    final static Sprint SPRINT_ACTIF_ALPHA = JiraAgileAPI.getLastlyActiveSprint();
    final static Sprint SPRINT_ACTIF_BETA = JiraAgileAPI.getLastlyActiveTeamSprint(TEAM_NAME_BETA);
    final static Sprint SPRINT_ACTIF_GAMMA = JiraAgileAPI.getLastlyActiveSprint();
    final static HashMap<String, Sprint> SPRINTS = new HashMap<>();
    static {
        SPRINTS.put(TEAM_NAME_ALPHA, SPRINT_ACTIF_ALPHA);
        SPRINTS.put(TEAM_NAME_BETA, SPRINT_ACTIF_BETA);
        SPRINTS.put(TEAM_NAME_GAMMA, SPRINT_ACTIF_GAMMA);
    }
    final static String[] REQUESTS_SPRINT_ALPHA = JiraAPI.getSprintRequests(SPRINTS.get(TEAM_NAME_ALPHA).getId(), TEAM_NAME_ALPHA);
    final static String[] REQUESTS_SPRINT_BETA = JiraAPI.getSprintRequests(SPRINTS.get(TEAM_NAME_BETA).getId(), TEAM_NAME_BETA);
    final static String[] REQUESTS_SPRINT_GAMMA = JiraAPI.getSprintRequests(SPRINTS.get(TEAM_NAME_GAMMA).getId(), TEAM_NAME_GAMMA);
    final static HashMap<String, String[]> TEAMS_REQUESTS = new HashMap<>();
    static {
        TEAMS_REQUESTS.put(TEAM_NAME_ALPHA, REQUESTS_SPRINT_ALPHA);
        TEAMS_REQUESTS.put(TEAM_NAME_BETA, REQUESTS_SPRINT_BETA);
        TEAMS_REQUESTS.put(TEAM_NAME_GAMMA, REQUESTS_SPRINT_GAMMA);

    }
    // ExternalData
    final static String PLANNING_PATH = "planning.csv";
    // Queries variables
    final static String ISSUE_BUG = "Bug";
    final static String ISSUE_INCIDENT = "Incident";
    //Settings
    final static DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yy");
    final static DateTimeFormatter dtfLocalDate = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    final static String TODAY = LocalDateTime.now().format(dtf);
    final static String TODAY_LD = LocalDateTime.now().format(dtfLocalDate);


    /* Returns a HashMap <TeamName, Sprint> using JiraAPI and getTeams() method
     * Retrieve all data from the lastly active sprint
     */
    public static HashMap<String,Sprint> callJiraSprintAPI() {
        Collection<Sprint> sprints = new ArrayList<>();
        for (String label : TEAMS_REQUESTS.keySet()) {
            Team t = getTeam(TEAMS_REQUESTS.get(label), label);
            SPRINTS.get(label).setTeam(t);
        }
        return SPRINTS;
    }

    /* Method that calls getCollaborators() and return a HashMap<AccountID, collaborator>
     * Retrieve all data of each ID_COLLABS on the lastly active sprint (if they have at least one assigned ticket)
     */
    public static HashMap<String, Collaborator> callJiraCollabSprintAPI() {
        HashMap<String, Collaborator> collaborators = new HashMap<>();
        for (String label : TEAMS_REQUESTS.keySet()) {
            HashMap<String, Collaborator> c = getCollaboratorsPerTeam(TEAMS_REQUESTS.get(label), label);
            Collaborator unassigned = c.get(UNASSIGNED);
            unassigned.setAccountId(unassigned.getAccountId() + ' ' + label);
            c.remove(UNASSIGNED);
            c.put(unassigned.getAccountId(), unassigned);
            collaborators.putAll(c);
        }
        return collaborators;
    }

    /* Method that returns a Backlog object
     * Retrieve all information from RUN_PROJECT_NAME (for incident) and PROJECT_NAME (for bugs) since each
     * project's creation
     */
    public static Backlog callJiraBacklogAPI() throws UnsupportedEncodingException {
        int[] incidents = JiraAPI.getProjectIncidentBug(RUN_PROJECT_NAME, ISSUE_INCIDENT);
        int[] bugs = JiraAPI.getProjectIncidentBug(PROJECT_NAME, ISSUE_BUG);
        return Backlog.builder()
                .nbIncidents(incidents[0])
                .nbIncidentsLow(incidents[1])
                .nbIncidentsMedium(incidents[2])
                .nbIncidentsHigh(incidents[3])
                .nbIncidentsHighest(incidents[4])
                .nbIncidentsCreated(JiraAPI.getCreated(NB_DAYS_BACKLOG, RUN_PROJECT_NAME, ISSUE_INCIDENT))
                .nbIncidentsResolved(JiraAPI.getResolved(NB_DAYS_BACKLOG, RUN_PROJECT_NAME, ISSUE_INCIDENT))
                .nbIncidentsInProgress(JiraAPI.getInProgress(NB_DAYS_BACKLOG, RUN_PROJECT_NAME, ISSUE_INCIDENT))
                .nbBugs(bugs[0])
                .nbBugsLow(bugs[1])
                .nbBugsMedium(bugs[2])
                .nbBugsHigh(bugs[3])
                .nbBugsHighest(bugs[4])
                .nbBugsCreated(JiraAPI.getCreated(NB_DAYS_BACKLOG, PROJECT_NAME, ISSUE_BUG))
                .nbBugsResolved(JiraAPI.getResolved(NB_DAYS_BACKLOG, PROJECT_NAME, ISSUE_BUG))
                .nbBugsInProgress(JiraAPI.getInProgress(NB_DAYS_BACKLOG, PROJECT_NAME, ISSUE_BUG))
                .build();
    }

    /* Method that retrieves all data of the last NB_SPRINT_RETROSPECTIVE, including the lastly active sprint
     * for each team (board_id differs)
     */
    public static HashMap<String, Retrospective> callJiraRetrospectiveAPI() {
        HashMap<String, Retrospective> retrospectives = new HashMap<>();
        int i = 0;
        for (String s : TEAM_PAIR.keySet()) {
            SprintCommitment[] sprints = JiraAgileAPI.getLastlyClosedSprints(NB_SPRINTS_RETROSPECTIVE);
            for (SprintCommitment sprint : sprints) {
                double[] commitment = JiraGreenhopperAPI.getCommitment(sprint, TEAM_PAIR.get(s));
                sprint.setInitialCommitment(commitment[0]);
                sprint.setFinalCommitment(commitment[1]);
                sprint.setAddedWork(commitment[2]);
                sprint.setCompletedWork(commitment[3]);
                i++;
            }
            Retrospective r = Retrospective.builder()
                    .teamName(s)
                    .sprints(sprints)
                    .build();
            retrospectives.put(r.getTeamName(), r);
        }
        return retrospectives;
    }

    public static HashMap<String, Collaborator> getCollaboratorsPerTeam(String[] requests, String label) {
        HashMap<String, Collaborator> collaborators = new HashMap<>();
        Collaborator c;
        for (String request : requests) {
            if ((c = JiraAPI.getCollaborator(request)) != null) {
                collaborators.put(c.getAccountId(), c);
            }
        }
        HashMap<String, Float[]> planning = ExternalFiles.getPlanning(PLANNING_PATH);
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

    /* Call above method, getCollaborators() and assign each collaborator to its team
     * returning a HashMap of size nbTeams
     */
    public static Team getTeam(String[] requests, String label) {
        HashMap<String, Collaborator> collaborators = getCollaboratorsPerTeam(requests, label);
        ArrayList<String> team = TEAMS.get(label);
        List<Collaborator> c = new ArrayList<>();
        for (String key : collaborators.keySet()) {
            if (team.contains(key)) {
                c.add(collaborators.get(key));
            }
        }
        return Team.builder()
                .name(label)
                .collaborators(c)
                .build();
    }
}
