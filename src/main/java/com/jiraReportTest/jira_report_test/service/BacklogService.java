package com.jiraReportTest.jira_report_test.service;

import com.jiraReportTest.jira_report_test.dao.BacklogDao;
import com.jiraReportTest.jira_report_test.model.Backlog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class BacklogService {
    @Autowired
    private BacklogDao backlogDao;

    public Backlog getBacklog(){
        return this.backlogDao.getBacklog();
    }
}
