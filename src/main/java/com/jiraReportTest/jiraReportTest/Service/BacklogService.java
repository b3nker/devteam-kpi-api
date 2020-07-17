package com.jiraReportTest.jiraReportTest.Service;

import com.jiraReportTest.jiraReportTest.Dao.BacklogDao;
import com.jiraReportTest.jiraReportTest.Model.Backlog;
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
