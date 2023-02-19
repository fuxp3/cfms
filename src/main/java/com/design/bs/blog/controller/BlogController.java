package com.design.bs.blog.controller;

import com.design.bs.blog.entity.Blog;
import com.design.bs.blog.mapper.BlogMapper;
import com.design.bs.blog.service.IBlogService;
import com.design.bs.core.dto.QueryPageRequest;
import com.design.bs.core.dto.QueryPageResult;
import com.design.bs.core.utils.PageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
public class BlogController {

    @Autowired
    private IBlogService blogService;

    @Autowired
    private BlogMapper blogMapper;

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

    @PostMapping("/blog")
    public Boolean add(@RequestBody Blog blog) {
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
