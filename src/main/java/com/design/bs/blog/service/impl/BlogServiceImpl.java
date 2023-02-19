package com.design.bs.blog.service.impl;

import com.design.bs.blog.entity.Blog;
import com.design.bs.blog.mapper.BlogMapper;
import com.design.bs.blog.service.IBlogService;
import com.design.bs.core.service.impl.BaseServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.List;
@Service
@Transactional
public class BlogServiceImpl extends BaseServiceImpl<Blog> implements IBlogService {
    @Autowired
    private BlogMapper blogMapper;

    @Override
    public List<Blog> queryList(Blog blog) {
        Example example = new Example(Blog.class);
        if (StringUtils.isNotBlank(blog.getContent())){
            example.createCriteria().andLike("content", "%" + blog.getContent() + "%");
        }
        return blogMapper.selectByExample(example);
    }
}
