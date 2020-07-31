package com.jiraReportTest.jirareporttest.dao;

import com.jiraReportTest.jirareporttest.dao.api.API;
import com.jiraReportTest.jirareporttest.model.Release;
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