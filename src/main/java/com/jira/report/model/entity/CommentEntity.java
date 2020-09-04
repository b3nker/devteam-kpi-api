package com.jira.report.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@Getter
@AllArgsConstructor
@Document(collection = "comment")
public class CommentEntity {
    //TODO: add comment as a Sprint fields instead of using this entity
    @Id
    private Long sprintId;
    private String comment;
}
