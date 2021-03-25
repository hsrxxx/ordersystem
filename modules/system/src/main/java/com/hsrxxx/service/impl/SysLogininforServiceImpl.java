package com.hsrxxx.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hsrxxx.common.core.utils.StringUtils;
import com.hsrxxx.entity.SysDictType;
import com.hsrxxx.entity.SysLogininfor;
import com.hsrxxx.mapper.SysLogininforMapper;
import com.hsrxxx.service.SysLogininforService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 系统访问记录 服务实现类
 * </p>
 *
 * @author 松仁
 * @since 2021-03-21
 */
@Service
public class SysLogininforServiceImpl extends ServiceImpl<SysLogininforMapper, SysLogininfor> implements SysLogininforService {

    @Autowired
    private SysLogininforMapper logininforMapper;

    /**
     * 根据条件分页查询登录日志
     *
     * @param logininfor 登录日志查询信息
     * @return 登录日志集合信息
     */
    @Override
    public List<SysLogininfor> selectLogininforList(SysLogininfor logininfor) {
        QueryWrapper<SysLogininfor> query = new QueryWrapper<>();
        query.like(StringUtils.isNotNull(logininfor.getIpaddr()), "ip_addr", logininfor.getIpaddr());
        query.like(StringUtils.isNotNull(logininfor.getUserName()), "user_name", logininfor.getUserName());
        query.like(StringUtils.isNotNull(logininfor.getStatus()), "status", logininfor.getStatus());
        if (!logininfor.getParams().isEmpty()){
            query.between("login_time", logininfor.getParams().get("beginTime"), logininfor.getParams().get("endTime"));
        }
        return logininforMapper.selectList(query);
    }

    /**
     * 清空系统登录日志
     */
    @Override
    public void cleanLogininfor() {
        logininforMapper.cleanLogininfor();
    }
}
