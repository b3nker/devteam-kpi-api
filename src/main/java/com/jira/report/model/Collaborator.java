package com.jira.report.model;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class Collaborator {
    private String accountId;
    private String firstName;
    private String name;
    private String emailAddress;
    private double totalWorkingTime;
    private double availableTime;
    private double estimatedTime;
    private double loggedTime;
    private double remainingTime;
    private StoryPoint storyPoints;
    private Ticket tickets;
    private String role;
    private List<String> assignedIssues;


    public void setName(String name) {
        if(name.length() >0){
            name = name.substring(0,1).toUpperCase() + name.substring(1).toLowerCase();
        }
        this.name = name;
    }
}

