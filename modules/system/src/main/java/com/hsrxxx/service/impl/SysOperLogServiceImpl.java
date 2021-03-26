package com.hsrxxx.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hsrxxx.common.core.utils.StringUtils;
import com.hsrxxx.entity.SysLogininfor;
import com.hsrxxx.entity.SysOperLog;
import com.hsrxxx.mapper.SysOperLogMapper;
import com.hsrxxx.service.SysOperLogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 操作日志记录 服务实现类
 * </p>
 *
 * @author 松仁
 * @since 2021-03-21
 */
@Service
public class SysOperLogServiceImpl extends ServiceImpl<SysOperLogMapper, SysOperLog> implements SysOperLogService {

    @Autowired
    private SysOperLogMapper operLogMapper;

    /**
     * 根据条件分页查询登录日志
     *
     * @param operLog 登录日志查询信息
     * @return 登录日志集合信息
     */
    @Override
    public List<SysOperLog> selectOperLogList(SysOperLog operLog) {
        QueryWrapper<SysOperLog> query = new QueryWrapper<>();
        query.like(StringUtils.isNotNull(operLog.getTitle()), "title", operLog.getTitle());
        query.like(StringUtils.isNotNull(operLog.getOperName()), "oper_name", operLog.getOperName());
        query.like(StringUtils.isNotNull(operLog.getBusinessType()), "business_type", operLog.getBusinessType());
        query.like(StringUtils.isNotNull(operLog.getStatus()), "status", operLog.getStatus());
        if (!operLog.getParams().isEmpty()){
            query.between("oper_time", operLog.getParams().get("beginTime"), operLog.getParams().get("endTime"));
        }
        return operLogMapper.selectList(query);
    }

    /**
     * 清空操作日志
     */
    @Override
    public void cleanOperLog() {
        operLogMapper.cleanOperLog();
    }
}
