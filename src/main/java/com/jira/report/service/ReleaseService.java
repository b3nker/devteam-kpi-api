package com.jira.report.service;

import com.jira.report.model.Release;
import com.jira.report.dao.ReleaseDao;
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
