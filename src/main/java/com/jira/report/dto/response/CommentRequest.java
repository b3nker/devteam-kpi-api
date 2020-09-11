package com.jira.report.dto.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel("Comment request for creation of a comment")
public class CommentRequest {

    @ApiModelProperty(value = "Id of the sprint link to this comment", example = "12")
    private Long sprintId;

    @ApiModelProperty(value = "The comment", example = "Commentaire de sprint")
    private String comment;

    public CommentRequest() {
        // for deserialization
    }

    public CommentRequest(Long sprintId, String comment) {
        this.sprintId = sprintId;
        this.comment = comment;
    }

    public Long getSprintId() {
        return sprintId;
    }

    public String getComment() {
        return comment;
    }
}
