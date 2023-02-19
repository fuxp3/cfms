package com.design.bs.comment.service.impl;

import com.design.bs.comment.entity.Comment;
import com.design.bs.comment.mapper.CommentMapper;
import com.design.bs.comment.service.ICommentService;
import com.design.bs.core.service.impl.BaseServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service
@Transactional
public class CommentServiceImpl extends BaseServiceImpl<Comment> implements ICommentService {

    @Autowired
    private CommentMapper commentMapper;

    @Override
    public List<Comment> queryList(Comment comment) {
        Example example = new Example(Comment.class);
        if (StringUtils.isNotBlank(comment.getComment())){
            example.createCriteria().andLike("comment", "%" + comment.getComment() + "%");
        }
        return commentMapper.selectByExample(example);
    }
}
