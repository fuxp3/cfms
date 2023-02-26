package com.design.bs.blog.controller;

import com.design.bs.blog.entity.Blog;
import com.design.bs.blog.mapper.BlogMapper;
import com.design.bs.blog.service.IBlogService;
import com.design.bs.comment.entity.Comment;
import com.design.bs.comment.mapper.CommentMapper;
import com.design.bs.core.dto.QueryPageRequest;
import com.design.bs.core.dto.QueryPageResult;
import com.design.bs.core.utils.PageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.List;

@RestController
public class BlogController {

    @Autowired
    private IBlogService blogService;

    @Autowired
    private BlogMapper blogMapper;

    @Autowired
    private CommentMapper commentMapper;

    @GetMapping("/blog")
    public QueryPageResult list(@RequestParam("pageNum") int pageNum, @RequestParam("pageSize") int pageSize, @RequestParam("keyword") String keyword){
        QueryPageRequest queryPageRequest = new QueryPageRequest();
        queryPageRequest.setPage(pageNum);
        queryPageRequest.setRows(pageSize);
        Blog blog = new Blog();
        blog.setContent(keyword);
        QueryPageResult queryPageResult = PageUtils.queryPage(queryPageRequest, () -> blogService.queryList(blog));
        queryPageResult.setPageNum(pageNum);
        queryPageResult.setPageSize(pageSize);
        return queryPageResult;
    }

    @GetMapping("/frontend/blog")
    public List<Blog> frontendList(){
        Example example = new Example(Blog.class);
        example.createCriteria().andEqualTo("state","已通过");
        return blogMapper.selectByExample(example);
    }

    @GetMapping("/frontend/blog/comment")
    public List<Comment> frontendList(@RequestParam("blogId") String blogId){
        Example example = new Example(Comment.class);
        example.createCriteria().andEqualTo("blogId",blogId);
        return commentMapper.selectByExample(example);
    }

    @PostMapping("/blog")
    public Boolean add(@RequestBody Blog blog) {
        blog.setState("未审核");
        blog.setCreateTime(new Date());
        blogService.save(blog);
        return true;
    }

    @PostMapping("/frontend/blog")
    public Boolean frontendAdd(@RequestBody Blog blog) {
        blog.setState("未审核");
        blog.setCreateTime(new Date());
        blogService.save(blog);
        return true;
    }

    @PutMapping("/blog")
    public Boolean update(@RequestBody Blog blog) {
        blogMapper.updateByPrimaryKeySelective(blog);
        return true;
    }

    @DeleteMapping("/blog/{id}")
    public Boolean delete(@PathVariable Long id){
        blogService.delete(id);
        return true;
    }

    @GetMapping("/blog/{id}")
    public Blog find(@PathVariable Long id){
        return blogMapper.selectByPrimaryKey(id);
    }
}
