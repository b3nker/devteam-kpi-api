package com.jiraReportTest.jiraReportTest.service;

import com.jiraReportTest.jiraReportTest.dao.ReleaseDao;
import com.jiraReportTest.jiraReportTest.model.Release;
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
