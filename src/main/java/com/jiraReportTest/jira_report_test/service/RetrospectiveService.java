package com.jiraReportTest.jira_report_test.service;

import com.jiraReportTest.jira_report_test.dao.RetrospectiveDao;
import com.jiraReportTest.jira_report_test.model.Retrospective;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class RetrospectiveService {
    @Autowired
    private RetrospectiveDao retrospectiveDao;

    public Collection<Retrospective> getRetrospectives(){
        return this.retrospectiveDao.getRetrospectives();
    }
}
