package com.hsrxxx.controller;


import com.hsrxxx.common.core.web.controller.BaseController;
import com.hsrxxx.common.core.web.domain.AjaxResult;
import com.hsrxxx.common.core.web.page.TableDataInfo;
import com.hsrxxx.entity.SysType;
import com.hsrxxx.mapper.SysTypeMapper;
import com.hsrxxx.service.SysTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 松仁
 * @since 2021-01-19
 */
@RestController
@RequestMapping("/type")
public class SysTypeController extends BaseController {
    @Autowired
    private SysTypeService service;

    @GetMapping("/list")
    public TableDataInfo list(){
        startPage();
        List<SysType> list = service.list(null);
        return getDataTable(list);
    }
}

