package com.jira.report.service;

import com.jira.report.dao.RetrospectiveDao;
import com.jira.report.model.Retrospective;
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
