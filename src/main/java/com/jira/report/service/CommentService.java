package com.jira.report.service;

import com.jira.report.dao.CommentDao;
import com.jira.report.model.entity.CommentEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;


@Service
public class CommentService {
    private static Logger LOGGER = LoggerFactory.getLogger(CommentService.class);
    private final CommentDao commentDao;

    public CommentService(CommentDao commentDao) {
        this.commentDao = commentDao;
    }

    @Transactional
    public void createComment(Long sprintId,
                              String comment) {
        CommentEntity commentEntity = new CommentEntity(sprintId,comment);
        commentDao.save(commentEntity);
    }

    @Transactional(readOnly = true)
    public CommentEntity getComment(Long sprintId) throws ResponseStatusException{
        return commentDao.findById(sprintId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Comment " + sprintId + " not found"));
    }
}
