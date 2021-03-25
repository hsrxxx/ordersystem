package com.hsrxxx.mapper;

import com.hsrxxx.entity.SysLogininfor;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Update;

/**
 * <p>
 * 系统访问记录 Mapper 接口
 * </p>
 *
 * @author 松仁
 * @since 2021-03-21
 */
public interface SysLogininforMapper extends BaseMapper<SysLogininfor> {

    /**
     * 清空系统登录日志
     *
     * @return 结果
     */
    @Update("truncate table sys_logininfor")
    int cleanLogininfor();
}
