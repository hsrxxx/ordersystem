package com.hsrxxx.service;

import com.hsrxxx.entity.SysLogininfor;
import com.hsrxxx.entity.SysOperLog;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 操作日志记录 服务类
 * </p>
 *
 * @author 松仁
 * @since 2021-03-21
 */
public interface SysOperLogService extends IService<SysOperLog> {

    /**
     * 根据条件分页查询登录日志
     *
     * @param operLog 登录日志查询信息
     * @return 登录日志集合信息
     */
    List<SysOperLog> selectOperLogList(SysOperLog operLog);

    /**
     * 清空操作日志
     */
    void cleanOperLog();
}
