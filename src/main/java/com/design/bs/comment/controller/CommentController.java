package com.design.bs.comment.controller;

import com.design.bs.comment.entity.Comment;
import com.design.bs.comment.mapper.CommentMapper;
import com.design.bs.comment.service.ICommentService;
import com.design.bs.core.dto.QueryPageRequest;
import com.design.bs.core.dto.QueryPageResult;
import com.design.bs.core.utils.PageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
public class CommentController {

    @Autowired
    private ICommentService commentService;

    @Autowired
    private CommentMapper commentMapper;

    @GetMapping("/comment")
    public QueryPageResult list(@RequestParam("pageNum") int pageNum, @RequestParam("pageSize") int pageSize, @RequestParam("keyword") String keyword){
        QueryPageRequest queryPageRequest = new QueryPageRequest();
        queryPageRequest.setPage(pageNum);
        queryPageRequest.setRows(pageSize);
        Comment comment = new Comment();
        comment.setComment(keyword);
        QueryPageResult queryPageResult = PageUtils.queryPage(queryPageRequest, () -> commentService.queryList(comment));
        queryPageResult.setPageNum(pageNum);
        queryPageResult.setPageSize(pageSize);
        return queryPageResult;
    }

    @PostMapping("/comment")
    public Boolean add(@RequestBody Comment comment) {
        comment.setState("未审核");
        comment.setCreateTime(new Date());
        commentService.save(comment);
        return true;
    }

    @PutMapping("/comment")
    public Boolean update(@RequestBody Comment comment) {
        commentMapper.updateByPrimaryKeySelective(comment);
        return true;
    }

    @DeleteMapping("/comment/{id}")
    public Boolean delete(@PathVariable Long id){
        commentService.delete(id);
        return true;
    }

    @GetMapping("/comment/{id}")
    public Comment find(@PathVariable Long id){
        return commentMapper.selectByPrimaryKey(id);
    }
}
