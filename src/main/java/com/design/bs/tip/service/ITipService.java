package com.design.bs.tip.service;

import com.design.bs.core.service.BaseService;
import com.design.bs.dict.entity.DictType;
import com.design.bs.tip.entity.Tip;

import java.util.List;

public interface ITipService extends BaseService<Tip> {

    List<Tip> queryList(Tip tip);
}
