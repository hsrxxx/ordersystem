package com.hsrxxx.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hsrxxx.common.core.text.Convert;
import com.hsrxxx.common.core.utils.StringUtils;
import com.hsrxxx.common.redis.service.RedisService;
import com.hsrxxx.entity.SysConfig;
import com.hsrxxx.mapper.SysConfigMapper;
import com.hsrxxx.service.SysConfigService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.hsrxxx.utils.DictUtils.getCacheKey;

/**
 * <p>
 * 参数配置表 服务实现类
 * </p>
 *
 * @author 松仁
 * @since 2021-03-27
 */
@Service
public class SysConfigServiceImpl extends ServiceImpl<SysConfigMapper, SysConfig> implements SysConfigService {

    @Autowired
    private SysConfigMapper configMapper;

    @Autowired
    private RedisService redisService;

    @Override
    public String selectConfigByKey(String configKey) {
        String configValue = Convert.toStr(redisService.getCacheObject(getCacheKey(configKey)));
        if (StringUtils.isNotEmpty(configValue))
        {
            return configValue;
        }
        QueryWrapper<SysConfig> query = new QueryWrapper<>();
        query.eq("config_key", configKey);
        SysConfig retConfig = configMapper.selectOne(query);
        if (StringUtils.isNotNull(retConfig))
        {
            redisService.setCacheObject(getCacheKey(configKey), retConfig.getConfigValue());
            return retConfig.getConfigValue();
        }
        return StringUtils.EMPTY;
    }
}
