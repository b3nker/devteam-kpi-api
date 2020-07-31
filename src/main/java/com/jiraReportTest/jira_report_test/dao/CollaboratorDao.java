package com.jiraReportTest.jira_report_test.dao;

import com.jiraReportTest.jira_report_test.dao.api.API;
import com.jiraReportTest.jira_report_test.model.Collaborator;
import org.springframework.stereotype.Repository;
import java.util.*;

@Repository
public class CollaboratorDao {

    private static Map<String,Collaborator> collaboratorsSprint;

    static{
        collaboratorsSprint = API.callJiraCollabSprintAPI();
    }

    public Collection<Collaborator> getAllCollaboratorsPerSprint(){
        return collaboratorsSprint.values();
    }
}
