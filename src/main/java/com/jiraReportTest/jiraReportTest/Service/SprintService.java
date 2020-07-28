package com.jiraReportTest.jiraReportTest.Service;

import com.jiraReportTest.jiraReportTest.Dao.SprintDao;
import com.jiraReportTest.jiraReportTest.Model.Sprint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class SprintService {
    @Autowired
    private SprintDao sprintDao;

    public Collection<Sprint> getSprint(){
        return this.sprintDao.getSprints();
    }

    public Sprint getSprintTeam(String teamName) { return this.sprintDao.getSprintTeam(teamName); }
}

