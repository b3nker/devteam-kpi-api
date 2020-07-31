package com.jiraReportTest.jirareporttest.service;

import com.jiraReportTest.jirareporttest.dao.ReleaseDao;
import com.jiraReportTest.jirareporttest.model.Release;
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
