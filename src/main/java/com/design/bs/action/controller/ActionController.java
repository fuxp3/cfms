package com.design.bs.action.controller;

import com.design.bs.action.entity.Action;
import com.design.bs.action.mapper.ActionMapper;
import com.design.bs.action.service.IActionService;
import com.design.bs.core.dto.QueryPageRequest;
import com.design.bs.core.dto.QueryPageResult;
import com.design.bs.core.utils.PageUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

@RestController
@Slf4j
public class ActionController {

    @Autowired
    private IActionService actionService;

    @Autowired
    private ActionMapper actionMapper;

    @GetMapping("/action")
    public QueryPageResult list(@RequestParam("pageNum") int pageNum,@RequestParam("pageSize") int pageSize,@RequestParam("keyword") String keyword){
        QueryPageRequest queryPageRequest = new QueryPageRequest();
        queryPageRequest.setPage(pageNum);
        queryPageRequest.setRows(pageSize);
        Action action = new Action();
        action.setType(keyword);
        QueryPageResult queryPageResult = PageUtils.queryPage(queryPageRequest, () -> actionService.queryList(action));
        queryPageResult.setPageNum(pageNum);
        queryPageResult.setPageSize(pageSize);
        return queryPageResult;
    }

    @PostMapping("/action")
    public Boolean add(@RequestBody Action action) {
        actionService.save(action);
        return true;
    }

    @PutMapping("/action")
    public Boolean update(@RequestBody Action action) {
        actionService.update(action);
        return true;
    }

    @DeleteMapping("/action/{id}")
    public Boolean delete(@PathVariable Long id){
        actionService.delete(id);
        return true;
    }

    @GetMapping("/action/{id}")
    public Action find(@PathVariable Long id){
        return actionMapper.selectByPrimaryKey(id);
    }

    @PostMapping("/action/upload")
    public Map<String,String> uplaod(HttpServletRequest req, @RequestParam("file") MultipartFile file) {
        Map map=new HashMap<String,String>();
        map.put("status_code","200");
        String destFileName = "";
        String fileName="";
        try {
            //根据创建时间对文件进行重命名
            fileName = System.currentTimeMillis() + "."+ file.getOriginalFilename().split("\\.")[1];
            //上传文件存储的位置
            destFileName = "C:\\upload\\" + fileName;
            //防止改文件夹不存在，创建一个新文件夹
            File destFile = new File(destFileName);
            destFile.getParentFile().mkdirs();
            //将文件存储到该位置
            IOUtils.copy(file.getInputStream(),new FileOutputStream(destFile));
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
        }
        map.put("name",file.getOriginalFilename());
        map.put("uid",fileName);
        return map;
    }

    @GetMapping("/action/download/{id}")
    public void readImage(@PathVariable("id") Long id, HttpServletResponse response){
        String destFileName = "";
        InputStream fis = null;
        try {
            Action action = actionMapper.selectByPrimaryKey(id);
            //上传文件存储的位置
            destFileName = "C:\\upload\\" + action.getUid();

            File destFile = new File(destFileName);

            fis = new FileInputStream(destFile);

            response.reset();
            response.setContentType("application/octet-stream");
            response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(action.getName()+"", "UTF-8"));
            ServletOutputStream outputStream = response.getOutputStream();

            IOUtils.copy(fis,outputStream);

        }catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
