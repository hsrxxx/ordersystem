package com.hsrxxx.service;

import com.hsrxxx.entity.SysDictType;
import com.hsrxxx.entity.SysLogininfor;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 系统访问记录 服务类
 * </p>
 *
 * @author 松仁
 * @since 2021-03-21
 */
public interface SysLogininforService extends IService<SysLogininfor> {

    /**
     * 根据条件分页查询登录日志
     *
     * @param logininfor 登录日志查询信息
     * @return 登录日志集合信息
     */
    List<SysLogininfor> selectLogininforList(SysLogininfor logininfor);

    /**
     * 清空系统登录日志
     */
    void cleanLogininfor();
}
