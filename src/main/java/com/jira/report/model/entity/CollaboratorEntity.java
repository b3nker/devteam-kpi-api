package com.jira.report.model.entity;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;


@Data
@Builder
@Getter
@Document(collection = "collaborator")
public class CollaboratorEntity {

    @Id
    private String accountId;
    private String firstName;
    private String name;
    private String emailAddress;
    private double totalWorkingTime;
    private double availableTime;
    private double estimatedTime;
    private double loggedTime;
    private double remainingTime;
    private StoryPointEntity storyPoints;
    private TicketEntity tickets;
    private String role;
    private List<String> assignedIssues;
}
