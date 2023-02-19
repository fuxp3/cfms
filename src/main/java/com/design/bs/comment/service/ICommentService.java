package com.design.bs.comment.service;

import com.design.bs.comment.entity.Comment;
import com.design.bs.core.service.BaseService;

import java.util.List;

public interface ICommentService extends BaseService<Comment> {
    List<Comment> queryList(Comment comment);
}
