package com.jiraReportTest.jiraReportTest.Service;

import com.jiraReportTest.jiraReportTest.Dao.SprintDao;
import com.jiraReportTest.jiraReportTest.Model.Sprint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SprintService {
    @Autowired
    private SprintDao sprintDao;

    public Sprint getSprint(){
        return this.sprintDao.getSprint();
    }

}

