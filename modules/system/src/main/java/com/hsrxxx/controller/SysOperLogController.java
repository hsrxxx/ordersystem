package com.hsrxxx.controller;


import com.hsrxxx.common.core.utils.poi.ExcelUtil;
import com.hsrxxx.common.core.web.controller.BaseController;
import com.hsrxxx.common.core.web.domain.AjaxResult;
import com.hsrxxx.common.core.web.page.TableDataInfo;
import com.hsrxxx.common.log.annotation.Log;
import com.hsrxxx.common.log.enums.BusinessType;
import com.hsrxxx.common.security.annotation.PreAuthorize;
import com.hsrxxx.entity.SysOperLog;
import com.hsrxxx.service.SysOperLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 操作日志记录 前端控制器
 * </p>
 *
 * @author 松仁
 * @since 2021-03-21
 */
@RestController
@RequestMapping("/operlog")
public class SysOperLogController extends BaseController {

    @Autowired
    private SysOperLogService operLogService;

    @PreAuthorize(hasPermi = "system:operlog:list")
    @GetMapping("/list")
    public TableDataInfo list(SysOperLog operLog)
    {
        startPage();
        List<SysOperLog> list = operLogService.selectOperLogList(operLog);
        return getDataTable(list);
    }

    /**
     * 导出操作日志数据表格
     * @param response
     * @param operLog
     * @throws IOException
     */
    @Log(title = "操作日志", businessType = BusinessType.EXPORT)
    @PreAuthorize(hasPermi = "system:operlog:export")
    @PostMapping("/export")
    public void export(HttpServletResponse response, SysOperLog operLog) throws IOException
    {
        List<SysOperLog> list = operLogService.selectOperLogList(operLog);
        ExcelUtil<SysOperLog> util = new ExcelUtil<SysOperLog>(SysOperLog.class);
        util.exportExcel(response, list, "操作日志");
    }


    @PreAuthorize(hasPermi = "system:operlog:remove")
    @DeleteMapping
    public AjaxResult remove(@RequestParam List<Long> operIds)
    {
        return toAjax(operLogService.removeByIds(operIds));
    }

    @PreAuthorize(hasPermi = "system:operlog:remove")
    @Log(title = "操作日志", businessType = BusinessType.CLEAN)
    @DeleteMapping("/clean")
    public AjaxResult clean()
    {
        operLogService.cleanOperLog();
        return AjaxResult.success();
    }

    @PostMapping
    public AjaxResult add(@RequestBody SysOperLog operLog)
    {
        operLog.setOperTime(LocalDateTime.now());
        return toAjax(operLogService.save(operLog));
    }
}

