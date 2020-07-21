package com.jiraReportTest.jiraReportTest.Service;

import com.jiraReportTest.jiraReportTest.Dao.RetrospectiveDao;
import com.jiraReportTest.jiraReportTest.Model.Retrospective;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
public class RetrospectiveService {
    @Autowired
    private RetrospectiveDao retrospectiveDao;

    public Collection<Retrospective> getRetrospectives(){
        return this.retrospectiveDao.getRetrospectives();
    }
}
