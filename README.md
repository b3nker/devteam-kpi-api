# KPI API
KPI API is a reporting API that fetch data in JIRA APIs and serialize it into intelligible objects.
This app is destinated to April's projects (BMKP,RMKP) for management purposes.

## Targeted versions
* Java: 11
* Maven: 3.6.3+ 

## Requirements
Environment variables : 

- Template (.yaml)
```
jira:
  external:
    planning: filepath to "planning.csv"
    release: filepath to "release.csv"
  report:
    global:
      projectName: Jira project name
      runProjectName: Jira run project name
      boardIdProject: Jira project boardId
      boardId#: Jira team #number boardId 
      nbSprintsRetrospective: Number of former sprints in Retrospective object
      nbDaysBacklog: Number of days in Backlog object
    api:
      baseUrl: baseUrl contained in Jira company's url
      jiraApiUrl: Jira API url
      jiraGreenhopperApiUrl: Greenhopper API url
      tempoApiUrl: Tempo API url
      jiraAgileApiUrl: Jira Agile API url
      username: username to authenticate using basicAuth
      jiraToken: Jira token to authenticate using basicAuth
      tempoToken: Jira Tempo token to authenticate using BearerToken
    query:
      active: Jira sprint status when a sprint is active
      maxResults: Number of results retrieve when doing an HTTP GET
      unassignedAccountId: Selected unassigned accountId by default
      # anomalies names in Jira
      bug: Jira issuetype name for bugs
      incident: Jira issuetype name for incidents
    individuals:
      idCollabs:
        - Jira_ACCOUNT_ID: role [must contain at least one of the following middle/front/lead dev/scrum/transverse]
        - ...
      teamName#: team name assigned to team #
      team#:
        - Jira_ACCOUNT_ID
        - ...
        - null #Unassigned
```

- Example

```
jira:
  external:
    planning: planning.csv
    release: release.csv
  report:
    global:
      projectName: BMKP
      runProjectName: RMKP
      boardIdProject: 391
      boardIdOne: 451
      boardIdTwo: 443
      boardIdThree: 450
      nbSprintsRetrospective: 2
      nbDaysBacklog: 20
    api:
      baseUrl: https://apriltechnologies.atlassian.net/
      jiraApiUrl: rest/api/3/
      jiraGreenhopperApiUrl: rest/greenhopper/1.0/
      tempoApiUrl: https://api.tempo.io/core/3
      jiraAgileApiUrl: rest/agile/1.0/
      username: benjamin.kermani@neo9.fr
      jiraToken: sqjFnTAVspNM4NxLd1QZC5CB
      tempoToken: J1eKPcvcMlCvMjNBvXyJmn0vMPvGs0
    query:
      active: active
      maxResults: 100
      unassignedAccountId: unassigned
      # anomalies names in Jira
      bug: Bug
      incident: Incident
    individuals:
      idCollabs:
        5c17b4599f443a65fecae3ca: middle lead dev # Julien Mosset
        5a9ebe1c4af2372a88a0656b: front lead dev # Nicolas Ovejero
        5bcd8282607ed038040177bb: middle #Pape Thiam
        5cf921f6b06c540e82580cbd: front #Valentin Pierrel
        5ed76cdf2fdc580b88f3bbef: middle #Alex Cheuko
        5ed64583620b1d0c168d4e36: middle #Anthony Hernandez
        5cb45bb34064460e407eabe4: middle lead dev #Guillermo Garcès
        5a9ebdf74af2372a88a06565: middle #Gabriel Roquigny
        5a2181081594706402dee482: front lead dev #Etienne Bourgouin
        5afe92f251d0b7540b43de81: middle #Malick Diagne
        5e98521a3a8b910c085d6a28: front #Kévin Youna
        5d6e32e06e3e1f0d9623cb5a: middle #Pierre Tomasina
        5e285008ee264b0e74591993: middle lead dev #Eric Coupal
        5ed76cc1be03220ab32183be: front lead dev #Thibault Foucault
        5d9b0573ea65c10c3fdbaab2: middle #Maxime Fourt
        5a8155f0cad06b353733bae8: middle #Guillaume Coppens
        5dfd11b39422830cacaa8a79: front #Carthy Marie Joseph
        5aafb6012235812a6233652d: scrum #Lionel Sjarbi
        5ed754b0f93b230ba59a3d38: scrum #Nicolas Beucler
        557058_1f318bba-6336-4f60-a3b1-67e03a32a3dc: transverse #Kévin Labesse
        5ef5ec9a7e95e80a8126e509: scrum #Pierre-Yves Garic
        5b97bc461b4803467d26fd6e: transverse #Xavier Michel
        5e787dfb2466490c495f2a85: transverse #Maxime Ancellin
        5a96abe5e9dc0033a7af8cfb: transverse #Joël Royer
        5db2f070af604e0db364eb12: transverse #David Boucard Planel
        5a27b9ed466b4a37eec61268: transverse #Pierre Bertin
        557058_d8f506a1-fa47-4681-9ab1-7214a062c264: transverse #Vincent Martin
        557058_834cbf83-d227-4823-bfdf-db62c8672ad1: transverse #Victor Dumesny
        557058_df17bf30-7843-415e-985d-151faba64429: transverse #Philippe Fleur
        557058_87b17037-8a69-4b38-8dab-b4cf904e960a: middle #Pierre Thevenet
      teamNameOne: alpha
      teamNameTwo: beta
      teamNameThree: gamma
      teamOne:
        - 5c17b4599f443a65fecae3ca #Julien Mosset
        - 5a9ebe1c4af2372a88a0656b #Nicolas Ovejero
        - 5bcd8282607ed038040177bb #Pape Thiam
        - 5cf921f6b06c540e82580cbd #Valentin Pierrel
        - 5ed76cdf2fdc580b88f3bbef #Alex Cheuko
        - 5a9ebdf74af2372a88a06565 #Gabriel Roquigny
        - null #Unassigned
      teamTwo:
        - 5cb45bb34064460e407eabe4 #Guillermo Garcès
        - 5a2181081594706402dee482 #Etienne Bourgouin
        - 5afe92f251d0b7540b43de81 #Malick Diagne
        - 5d6e32e06e3e1f0d9623cb5a #Pierre Tomasina
        - 5ed64583620b1d0c168d4e36 #Anthony Hernandez
        - 5e98521a3a8b910c085d6a28 #Kévin Youna
        - null #Unassigned
      teamThree:
        - 5e285008ee264b0e74591993 #Eric Coupal
        - 5ed76cc1be03220ab32183be #Thibault Foucault
        - 557058:87b17037-8a69-4b38-8dab-b4cf904e960a #Pierre Thevenet
        - 5d9b0573ea65c10c3fdbaab2 #Maxime Fourt
        - 5a8155f0cad06b353733bae8 #Guillaume Coppens
        - 5dfd11b39422830cacaa8a79 #Carthy Marie Joseph
        - null #Unassigned
```

## Installation
First you have to clone the project to a local repository : 

```shell
$ git clone URL
```

Then you can build and run the project with the following commands :

```shell
$ mvn clean install
$ mvn spring-boot:run
```
## Authors
* **Benjamin Kermani** - *Initial work* [Intern at @Neo9](https://github.com/b3nker)

## License/Copyright
This project is licensed under @Neo9 license