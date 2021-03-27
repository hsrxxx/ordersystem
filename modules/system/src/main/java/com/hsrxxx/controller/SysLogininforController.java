package com.hsrxxx.controller;


import com.hsrxxx.common.core.constant.Constants;
import com.hsrxxx.common.core.utils.SecurityUtils;
import com.hsrxxx.common.core.utils.ServletUtils;
import com.hsrxxx.common.core.utils.ip.IpUtils;
import com.hsrxxx.common.core.utils.poi.ExcelUtil;
import com.hsrxxx.common.core.web.controller.BaseController;
import com.hsrxxx.common.core.web.domain.AjaxResult;
import com.hsrxxx.common.core.web.page.TableDataInfo;
import com.hsrxxx.common.log.annotation.Log;
import com.hsrxxx.common.log.enums.BusinessType;
import com.hsrxxx.common.security.annotation.PreAuthorize;
import com.hsrxxx.entity.SysLogininfor;
import com.hsrxxx.entity.SysUser;
import com.hsrxxx.service.SysLogininforService;
import com.hsrxxx.service.SysUserService;
import eu.bitwalker.useragentutils.Browser;
import eu.bitwalker.useragentutils.OperatingSystem;
import eu.bitwalker.useragentutils.UserAgent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 系统访问记录 前端控制器
 * </p>
 *
 * @author 松仁
 * @since 2021-03-21
 */
@RestController
@RequestMapping("/logininfor")
public class SysLogininforController extends BaseController {

    @Autowired
    private SysLogininforService logininforService;

    @Autowired
    private SysUserService userService;

    @PreAuthorize(hasPermi = "system:logininfor:list")
    @GetMapping("/list")
    public TableDataInfo list(SysLogininfor logininfor)
    {
        startPage();
        List<SysLogininfor> list = logininforService.selectLogininforList(logininfor);
        return getDataTable(list);
    }

    /**
     * 导出登陆日志数据表格
     * @param response
     * @param logininfor
     * @throws IOException
     */
    @Log(title = "登录日志", businessType = BusinessType.EXPORT)
    @PreAuthorize(hasPermi = "system:logininfor:export")
    @PostMapping("/export")
    public void export(HttpServletResponse response, SysLogininfor logininfor) throws IOException
    {
        List<SysLogininfor> list = logininforService.selectLogininforList(logininfor);
        ExcelUtil<SysLogininfor> util = new ExcelUtil<SysLogininfor>(SysLogininfor.class);
        util.exportExcel(response, list, "登录日志");
    }

    @PreAuthorize(hasPermi = "system:logininfor:remove")
    @Log(title = "登录日志", businessType = BusinessType.DELETE)
    @DeleteMapping
    public AjaxResult remove(@RequestParam List<Long> infoIds)
    {
        return toAjax(logininforService.removeByIds(infoIds));
    }

    @PreAuthorize(hasPermi = "system:logininfor:remove")
    @Log(title = "登录日志", businessType = BusinessType.DELETE)
    @DeleteMapping("/clean")
    public AjaxResult clean()
    {
        logininforService.cleanLogininfor();
        return AjaxResult.success();
    }

    @PostMapping
    public AjaxResult add(@RequestParam("username") String username, @RequestParam("status") String status,
                          @RequestParam("message") String message)
    {
        String ip = IpUtils.getIpAddr(ServletUtils.getRequest());

        // 封装对象
        SysLogininfor logininfor = new SysLogininfor();
        logininfor.setUserName(username);
        logininfor.setIpaddr(ip);
        logininfor.setMsg(message);
        logininfor.setLoginTime(LocalDateTime.now());

        // 获取请求头 User-Agent 信息
        String agent = ServletUtils.getRequest().getHeader("User-Agent");
        // 解析浏览器，系统详情信息
        UserAgent userAgent = UserAgent.parseUserAgentString(agent);
        //获取浏览器对象
        Browser browser = userAgent.getBrowser();
        //获取操作系统对象
        OperatingSystem operatingSystem = userAgent.getOperatingSystem();

        logininfor.setBrowser(browser.getName());
        logininfor.setOs(operatingSystem.getName());

        // 日志状态
        if (Constants.LOGIN_SUCCESS.equals(status) || Constants.LOGOUT.equals(status))
        {
            logininfor.setStatus("0");
        }
        else if (Constants.LOGIN_FAIL.equals(status))
        {
            logininfor.setStatus("1");
        }
        return toAjax(logininforService.save(logininfor));
    }
}

