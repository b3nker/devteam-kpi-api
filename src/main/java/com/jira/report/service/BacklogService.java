package com.jira.report.service;

import com.jira.report.dao.BacklogDao;
import com.jira.report.model.Backlog;
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
