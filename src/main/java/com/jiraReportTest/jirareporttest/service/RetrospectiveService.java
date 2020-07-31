package com.jiraReportTest.jirareporttest.service;

import com.jiraReportTest.jirareporttest.dao.RetrospectiveDao;
import com.jiraReportTest.jirareporttest.model.Retrospective;
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