package com.design.bs.action.service.impl;

import com.design.bs.action.entity.Action;
import com.design.bs.action.mapper.ActionMapper;
import com.design.bs.action.service.IActionService;
import com.design.bs.core.service.impl.BaseServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service
@Transactional
public class ActionServiceImpl extends BaseServiceImpl<Action> implements IActionService {
    @Autowired
    private ActionMapper actionMapper;

    @Override
    public List<Action> queryList(Action action) {
        Example example = new Example(Action.class);
        if (StringUtils.isNotBlank(action.getType())){
            example.createCriteria().andLike("type", action.getType());
        }
        return actionMapper.selectByExample(example);
    }
}
