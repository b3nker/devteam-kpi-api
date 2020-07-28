package com.jiraReportTest.jiraReportTest.service;

import com.jiraReportTest.jiraReportTest.dao.BacklogDao;
import com.jiraReportTest.jiraReportTest.model.Backlog;
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
