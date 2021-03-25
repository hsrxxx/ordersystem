package com.hsrxxx.mapper;

import com.hsrxxx.entity.SysOperLog;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Update;

/**
 * <p>
 * 操作日志记录 Mapper 接口
 * </p>
 *
 * @author 松仁
 * @since 2021-03-21
 */
public interface SysOperLogMapper extends BaseMapper<SysOperLog> {

    /**
     * 清空操作日志
     *
     * @return 结果
     */
    @Update("truncate table sys_oper_log")
    int cleanOperLog();
}
