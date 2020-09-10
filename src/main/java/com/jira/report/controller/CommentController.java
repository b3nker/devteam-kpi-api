package com.jira.report.controller;

import com.jira.report.model.entity.CommentEntity;
import com.jira.report.service.CommentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;


@RestController
@Api(value = "Sprint endpoints")
public class CommentController {
    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Comment Retrieved", response = CommentEntity.class),
            @ApiResponse(code = 404, message = "Comment not found")})
    @GetMapping(value = "/comment/{sprintId}")
    public CommentEntity getComment(@PathVariable("sprintId") Long sprintId) throws ResponseStatusException {
        return commentService.getComment(sprintId);
    }

    @ApiResponse(code = 200, message = "Comment created")
    @PostMapping(value = "/comment")
    public void createComment(@RequestBody CommentEntity commentEntity) {
        commentService.createComment(commentEntity.getSprintId(), commentEntity.getComment());
    }
}
