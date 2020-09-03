package com.jira.report.model.entity;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Builder
@Getter
@Document(collection = "retrospective")
public class RetrospectiveEntity {
    private String teamName;
    private List<SprintCommitmentEntity> sprints;
}
