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
@Document(collection = "sprint_commitment")
public class SprintCommitmentEntity {
    @Id
    private Long id;
    private String name;
    private double initialCommitment;
    private double finalCommitment;
    private double addedWork;
    private double completedWork;
    private List<String> addedIssueKeys;

}
