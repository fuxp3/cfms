package com.design.bs.action.service;

import com.design.bs.action.entity.Action;
import com.design.bs.core.service.BaseService;

import java.util.List;

public interface IActionService extends BaseService<Action> {

    List<Action> queryList(Action action);

}
