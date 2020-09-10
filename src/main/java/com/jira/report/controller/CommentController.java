package com.jira.report.controller;

import com.jira.report.model.entity.CommentEntity;
import com.jira.report.service.CommentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiResponse;
import org.springframework.web.bind.annotation.*;


@RestController
@Api(value = "Sprint endpoints")
public class CommentController {
    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @ApiResponse(code = 200, message = "Comment retrieved", response = CommentEntity.class)
    @GetMapping(value = "/comment/{sprintId}")
    public CommentEntity getSprintTeam(@PathVariable("sprintId") Long sprintId) {
        return commentService.getComment(sprintId);
    }

    @ApiResponse(code = 200, message = "Comment updated")
    @PutMapping(value = "/comment/{sprintId}")
    public void updateSprintTeam(@PathVariable("sprintId") Long sprintId,
                                 @RequestBody String comment) {
        commentService.updateComment(sprintId, comment);
    }

    @ApiResponse(code = 200, message = "Comment created")
    @PostMapping(value = "/comment")
    public void createSprintTeam(@RequestBody CommentEntity commentEntity) {
        commentService.createComment(commentEntity.getSprintId(), commentEntity.getComment());
    }
}
