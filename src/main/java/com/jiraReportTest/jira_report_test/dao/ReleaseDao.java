package com.jiraReportTest.jira_report_test.dao;

import com.jiraReportTest.jira_report_test.dao.api.API;
import com.jiraReportTest.jira_report_test.model.Release;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.text.ParseException;
import java.util.Collection;
import java.util.List;

@Repository
public class ReleaseDao {
    private static List<Release> releases;
    static{
        try {
            releases = API.getReleases();
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }

    public Collection<Release> getReleases(){
        return releases;
    }
}
