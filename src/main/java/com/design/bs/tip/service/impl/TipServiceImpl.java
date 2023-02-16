package com.design.bs.tip.service.impl;

import com.design.bs.core.service.impl.BaseServiceImpl;
import com.design.bs.tip.entity.Tip;
import com.design.bs.tip.mapper.TipMapper;
import com.design.bs.tip.service.ITipService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service
@Transactional
public class TipServiceImpl extends BaseServiceImpl<Tip> implements ITipService {
    @Autowired
    private TipMapper tipMapper;

    @Override
    public List<Tip> queryList(Tip tip) {
        Example example = new Example(Tip.class);
        if (StringUtils.isNotBlank(tip.getTip())){
            example.createCriteria().andLike("tip", "%" + tip.getTip() + "%");
        }
        return tipMapper.selectByExample(example);
    }
}
