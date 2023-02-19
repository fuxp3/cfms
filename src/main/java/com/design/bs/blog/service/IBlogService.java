package com.design.bs.blog.service;

import com.design.bs.blog.entity.Blog;
import com.design.bs.core.service.BaseService;

import java.util.List;

public interface IBlogService extends BaseService<Blog> {
    List<Blog> queryList(Blog blog);
}
