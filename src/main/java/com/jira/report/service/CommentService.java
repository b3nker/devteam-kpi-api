package com.jira.report.service;

import com.jira.report.dao.CommentDao;
import com.jira.report.model.entity.CommentEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class CommentService {

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

    @Transactional
    public void updateComment(Long sprintId,
                              String comment) {

        CommentEntity commentEntity = commentDao.findById(sprintId).get();
        commentEntity.setComment(comment);
        commentDao.save(commentEntity);
    }

    @Transactional(readOnly = true)
    public CommentEntity getComment(Long sprintId) {
        return commentDao.findById(sprintId).get();
    }
}
