package com.jiraReportTest.jirareporttest.service;

import com.jiraReportTest.jirareporttest.dao.BacklogDao;
import com.jiraReportTest.jirareporttest.model.Backlog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class BacklogService {
    @Autowired
    private BacklogDao backlogDao;

    public Backlog getBacklog(){
        return this.backlogDao.getBacklog();
    }
}
