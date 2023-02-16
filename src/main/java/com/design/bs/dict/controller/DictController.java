package com.design.bs.dict.controller;

import com.design.bs.dict.entity.Dict;
import com.design.bs.dict.service.IDictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/dict")
public class DictController {
	@Autowired
    private IDictService dictService;
	
	/**
	 * 根据字典编码查询字典
	 * @param code
	 * @return
	 */
	@GetMapping("/list/{code}")
	public List<Dict> getDictListByCode(@PathVariable String code, @RequestParam(value = "select",required = false)Integer select){
		List<Dict> list = dictService.getDictListByCode(code);
		if (null!=select && select>0){
			Dict dict = new Dict();
			dict.setValue("");
			dict.setName("全部");
			list.add(0,dict);
		}
		return list;
	}
	
	@PostMapping("/add")
	public Boolean add(@RequestBody Dict dict) {
		dictService.save(dict);
		return true;
	}
	
	@PostMapping("/update")
	public Boolean update(@RequestBody Dict dict) {
		dictService.update(dict);
		return true;
	}
	
	@PostMapping("/delete")
	public Boolean delete(@RequestBody List<Long> ids ) {
		dictService.batchDelete(ids, "id", Dict.class);
		return true;
	}
}
