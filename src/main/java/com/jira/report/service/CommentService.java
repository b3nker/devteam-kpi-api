package com.jira.report.service;

import com.jira.report.dao.CommentDao;
import com.jira.report.model.entity.CommentEntity;
import com.jira.report.model.entity.SprintEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


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

    @Transactional
    public void loadComment(){
        LOGGER.info("Removing data from the comment collection");
        commentDao.deleteAll();
        LOGGER.info("Starting to construct Comments objects");
        List<CommentEntity> c = new ArrayList<>();
        c.add(new CommentEntity((long) 1108, "RAS"));
        Collection<CommentEntity> comments;
        comments = c;
        commentDao.saveAll(comments);
        LOGGER.info("Finished constructing Comments objects");
    }
}
