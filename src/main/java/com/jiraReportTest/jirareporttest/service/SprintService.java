package com.jiraReportTest.jirareporttest.service;

import com.jiraReportTest.jirareporttest.dao.SprintDao;
import com.jiraReportTest.jirareporttest.model.Sprint;
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

