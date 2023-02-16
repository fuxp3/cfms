package com.design.bs.dict.controller;

import com.design.bs.core.utils.PageUtils;
import com.design.bs.dict.entity.DictType;
import com.design.bs.core.dto.QueryPageRequest;
import com.design.bs.core.dto.QueryPageResult;
import com.design.bs.dict.service.IDictTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/dictType")
public class DictTypeController {
	@Autowired
    private IDictTypeService dictTypeService;
	
	@PostMapping("/list")
    public QueryPageResult queryList(@RequestBody QueryPageRequest<DictType> queryPageRequest) {
        return PageUtils.queryPage(queryPageRequest, () -> dictTypeService.queryList(queryPageRequest.getData()));
    }
	
	@PostMapping("/")
	public Boolean add(@RequestBody DictType dictType) {
		dictTypeService.save(dictType);
		return true;
	}
	
	@PostMapping("/update")
	public Boolean update(@RequestBody DictType dictType) {
		dictTypeService.update(dictType);
		return true;
	}
	
	@PostMapping("/delete")
	public Boolean delete(@RequestBody List<Long> ids ) {
		dictTypeService.batchDelete(ids, "id", DictType.class);
		return true;
	}
	
	@PostMapping("/checkCode")
    public Boolean checkName(@RequestBody Map<String,String> map) {
		if(null==map.get("id")) {
			return dictTypeService.checkCode(map.get("code"), null);
		}else {
			return dictTypeService.checkCode(map.get("code"), Long.valueOf(map.get("id")));
		}
    }
}
