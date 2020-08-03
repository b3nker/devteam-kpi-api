package com.jira.report.dao;

import com.jira.report.model.Release;
import com.jira.report.dao.api.API;
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
