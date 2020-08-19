# KPI API
KPI API is a reporting API that fetch data in JIRA APIs and serialize it into intelligible objects.
This app is destinated to April's projects (BMKP,RMKP) for management purposes.
KPI API is a Spring (Maven) application.

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
        5c17b4599f443a65fzedae3ca: middle lead dev
        5a9ebe1c4af2372a8drd0656b: front lead dev 
        5bcd8282607ed0380dc0177bb: middle #Pape Thiam
      teamNameOne: alpha
      teamNameTwo: beta
      teamNameThree: gamma
      teamOne:
        - 5c17b4599f443a652343ae3ca
        - 5a9ebe1c23d2dzdzd88a0656b 
        - 5bcd828260iop0dzdz40177bb 
        - null #Unassigned
      teamTwo:
        - 5cb45bb34877d60e407eabe4
        - 5a2181081Opdz06402dee482
        - null #Unassigned
      teamThree:
        - 5e285008ee264b0e74591993
        - 5ed76cc1be03220ab32183be
        - 557058:87b17037-8a69-4b3232Dd4cf904e960a
        - null #Unassigned
```

## Installation
First you have to clone the project to your local repository : 

```shell
$ git clone URL
```

Then you have to build and run the project with the following commands :

```shell
$ mvn clean install
$ mvn spring-boot:run
```

This project uses Project Lombok, make sure that the corresponding plugin is installed on your IDE.

## Libraries used
This project uses different librairies to work : 

 1. SpringBoot Test
 2. Springboot Web
 3. SpringBoot Webflux
 4. SpringBoot Actuator
 5. Reactor Spring
 6. Lombok
 7. Opencsv

## Features / Services
KPI API offers following services : 

* Collaborator **Read** requests
* Sprint **Read** requests
* Release **Read** requests
* Backlog **Read** requests
* Retrospective **Read** requests


## Authors
* **Benjamin Kermani** - *Initial work* [Intern at @Neo9](https://github.com/b3nker)

## License/Copyright
[@Neo9](https://neo9.fr/)