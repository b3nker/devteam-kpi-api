package com.jiraReportTest.jira_report_test.service;

import com.jiraReportTest.jira_report_test.dao.ReleaseDao;
import com.jiraReportTest.jira_report_test.model.Release;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class ReleaseService {
    @Autowired
    private ReleaseDao releaseDao;

    public Collection<Release> getRelease(){
        return this.releaseDao.getReleases();
    }
}
