package com.design.bs.tip.controller;

import com.design.bs.core.dto.QueryPageRequest;
import com.design.bs.core.dto.QueryPageResult;
import com.design.bs.core.utils.PageUtils;
import com.design.bs.tip.entity.Tip;
import com.design.bs.tip.mapper.TipMapper;
import com.design.bs.tip.service.ITipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class TipController {
    @Autowired
    private ITipService tipService;

    @Autowired
    private TipMapper tipMapper;

    @GetMapping("/tip")
    public QueryPageResult list(@RequestParam("pageNum") int pageNum,@RequestParam("pageSize") int pageSize,@RequestParam("keyword") String keyword){
        QueryPageRequest queryPageRequest = new QueryPageRequest();
        queryPageRequest.setPage(pageNum);
        queryPageRequest.setRows(pageSize);
        Tip tip = new Tip();
        tip.setTip(keyword);
        QueryPageResult queryPageResult = PageUtils.queryPage(queryPageRequest, () -> tipService.queryList(tip));
        queryPageResult.setPageNum(pageNum);
        queryPageResult.setPageSize(pageSize);
        return queryPageResult;
    }

    @PostMapping("/tip")
    public Boolean add(@RequestBody Tip tip) {
        tipService.save(tip);
        return true;
    }

    @PutMapping("/tip")
    public Boolean update(@RequestBody Tip tip) {
        tipService.update(tip);
        return true;
    }

    @DeleteMapping("/tip/{id}")
    public Boolean delete(@PathVariable Long id){
        tipService.delete(id);
        return true;
    }

    @GetMapping("/tip/{id}")
    public Tip find(@PathVariable Long id){
        return tipMapper.selectByPrimaryKey(id);
    }

}
